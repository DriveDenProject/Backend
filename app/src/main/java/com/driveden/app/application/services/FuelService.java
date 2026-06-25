package com.driveden.app.application.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.driveden.app.application.ports.out.FuelLogRepositoryPort;
import com.driveden.app.common.exception.CustomException;
import com.driveden.app.domain.common.dto.PageResponseDTO;
import com.driveden.app.domain.fuelLogs.dto.CurrentMonthFuelStatsResponseDTO;
import com.driveden.app.domain.fuelLogs.dto.FuelLogHistoryResponseDTO;
import com.driveden.app.domain.fuelLogs.dto.FuelLogResponseDTO;
import com.driveden.app.domain.fuelLogs.dto.FuelLogTankHistoryResponseDTO;
import com.driveden.app.domain.fuelLogs.dto.LastFuelLogResponseDTO;
import com.driveden.app.domain.fuelLogs.dto.RegisterFuelLogDTO;
import com.driveden.app.domain.fuelLogs.dto.UpdateFuelLogDTO;
import com.driveden.app.domain.fuelLogs.model.FuelLogEfficiencyDomain;
import com.driveden.app.domain.fuelLogs.model.FuelLogsDomain;
import com.driveden.app.domain.odometerLogs.model.OdometerLogSource;
import com.driveden.app.infrastructure.out.persistence.mappers.FuelLogsMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.PaymentMethodRepository;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.UserVehicleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FuelService {

    private final FuelLogRepositoryPort fuelLogsRepository;
    private final UserVehicleRepository userVehicleRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final VehicleOdometerService vehicleOdometerService;
    private final OdometerLogService odometerLogService;
    private final UsersService usersService;

    @Transactional
    public FuelLogResponseDTO registerFuelLog(RegisterFuelLogDTO registerFuelLogDTO, Long userId) {
        validateVehicleOwnership(userId, registerFuelLogDTO.getVehicleId());
        validatePaymentMethod(registerFuelLogDTO.getPaymentMethodId());
        vehicleOdometerService.lockVehicleOdometer(registerFuelLogDTO.getVehicleId());
        vehicleOdometerService.validateOdometer(registerFuelLogDTO.getVehicleId(), registerFuelLogDTO.getKmAtFill());

        FuelLogsDomain fuelLogsDomain = FuelLogsMapper.fromDTOtoDomain(registerFuelLogDTO);
        FuelLogsDomain savedFuelLog = fuelLogsRepository.save(fuelLogsDomain);
        vehicleOdometerService.updateCurrentKmIfLatestFuelLog(savedFuelLog);
        registerOdometerLog(savedFuelLog);

        return FuelLogsMapper.toResponseDTO(savedFuelLog);
    }

    @Transactional
    public FuelLogResponseDTO updateFuelLog(Long fuelLogId, UpdateFuelLogDTO updateFuelLogDTO, Long userId) {
        FuelLogsDomain currentFuelLog = findFuelLogById(fuelLogId);
        validateVehicleOwnership(userId, currentFuelLog.getVehicleId());
        validatePaymentMethod(updateFuelLogDTO.getPaymentMethodId());
        vehicleOdometerService.lockVehicleOdometer(currentFuelLog.getVehicleId());
        vehicleOdometerService.validateOdometerForUpdate(
                currentFuelLog.getVehicleId(),
                updateFuelLogDTO.getKmAtFill(),
                currentFuelLog
        );

        FuelLogsDomain updatedFuelLog = new FuelLogsDomain(
                currentFuelLog.getId(),
                updateFuelLogDTO.getPriceTotal(),
                updateFuelLogDTO.getPricePerGallon(),
                updateFuelLogDTO.getGallons(),
                updateFuelLogDTO.getKmAtFill(),
                updateFuelLogDTO.getFilledAt(),
                updateFuelLogDTO.getGasStation(),
                currentFuelLog.getVehicleId(),
                updateFuelLogDTO.getPaymentMethodId(),
                updateFuelLogDTO.getNotes()
        );

        FuelLogsDomain savedFuelLog = fuelLogsRepository.save(updatedFuelLog);
        updateOdometerLog(savedFuelLog);
        vehicleOdometerService.recalculateCurrentKm(savedFuelLog.getVehicleId());

        return FuelLogsMapper.toResponseDTO(savedFuelLog);
    }

    @Transactional
    public String deleteFuelLog(Long fuelLogId, Long userId) {
        FuelLogsDomain fuelLog = findFuelLogById(fuelLogId);
        validateVehicleOwnership(userId, fuelLog.getVehicleId());
        vehicleOdometerService.lockVehicleOdometer(fuelLog.getVehicleId());

        fuelLogsRepository.deleteById(fuelLog.getId());
        odometerLogService.deleteOdometerLogFromSource(OdometerLogSource.FUEL, fuelLog.getId());
        vehicleOdometerService.recalculateCurrentKm(fuelLog.getVehicleId());

        return "Fuel log deleted successfully";
    }

    public List<FuelLogResponseDTO> getFuelLogs(Long vehicleId, Long userId) {
        validateVehicleOwnership(userId, vehicleId);

        return fuelLogsRepository.findByVehicleId(vehicleId).stream()
                .map(FuelLogsMapper::toResponseDTO)
                .toList();
    }

    public List<FuelLogHistoryResponseDTO> getFuelLogsHistory(
            Long vehicleId,
            LocalDate startDate,
            LocalDate endDate,
            Long userId
    ) {
        validateVehicleOwnership(userId, vehicleId);
        validateDateRange(startDate, endDate);

        return fuelLogsRepository
                .findByVehicleIdAndFilledAtBetween(
                        vehicleId,
                        startDate.atStartOfDay(),
                        endDate.atTime(LocalTime.MAX)
                )
                .stream()
                .map(FuelLogsMapper::toHistoryResponseDTO)
                .toList();
    }

    public PageResponseDTO<FuelLogTankHistoryResponseDTO> getFuelLogsTankHistory(
            Long vehicleId,
            Pageable pageable,
            Long userId
    ) {
        validateVehicleOwnership(userId, vehicleId);

        Page<FuelLogTankHistoryResponseDTO> history = fuelLogsRepository
                .findTankHistoryByVehicleId(vehicleId, pageable)
                .map(FuelLogsMapper::toTankHistoryResponseDTO);

        return PageResponseDTO.from(history);
    }

    public CurrentMonthFuelStatsResponseDTO getCurrentMonthFuelStats(Long vehicleId, Long userId) {
        validateVehicleOwnership(userId, vehicleId);

        YearMonth currentMonth = YearMonth.now();
        LocalDate startOfMonth = currentMonth.atDay(1);
        LocalDate startOfNextMonth = currentMonth.plusMonths(1).atDay(1);

        BigDecimal monthlyExpense = fuelLogsRepository.sumPriceTotalByVehicleIdAndFilledAtBetween(
                vehicleId,
                startOfMonth.atStartOfDay(),
                startOfNextMonth.atStartOfDay()
        );
        List<FuelLogEfficiencyDomain> latestFuelLogs = fuelLogsRepository.findLatestTwoByVehicleId(vehicleId);

        return new CurrentMonthFuelStatsResponseDTO(
                safeBigDecimal(monthlyExpense),
                getLastFuelingDate(latestFuelLogs),
                calculateFuelEfficiency(latestFuelLogs)
        );
    }

    public List<LastFuelLogResponseDTO> getLastFourFuelLogs(Long vehicleId, Long userId) {
        validateVehicleOwnership(userId, vehicleId);

        return fuelLogsRepository.findLastFourByVehicleId(vehicleId).stream()
                .map(FuelLogsMapper::toLastFuelLogResponseDTO)
                .toList();
    }

    private void validateVehicleOwnership(Long userId, Long vehicleId) {
        usersService.findUserById(userId);

        if (!userVehicleRepository.existsByUserIdAndVehicleId(userId, vehicleId)) {
            throw new CustomException("Vehicle not found for user", HttpStatus.NOT_FOUND);
        }
    }

    private void validatePaymentMethod(Long paymentMethodId) {
        if (paymentMethodId == null) {
            return;
        }

        if (!paymentMethodRepository.existsAvailableById(paymentMethodId)) {
            throw new CustomException("Payment method not found or inactive", HttpStatus.NOT_FOUND);
        }
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new CustomException("startDate must be before or equal to endDate", HttpStatus.BAD_REQUEST);
        }
    }

    private FuelLogsDomain findFuelLogById(Long fuelLogId) {
        return fuelLogsRepository.findById(fuelLogId)
                .orElseThrow(() -> new CustomException("Fuel log not found", HttpStatus.NOT_FOUND));
    }

    private LocalDateTime getLastFuelingDate(List<FuelLogEfficiencyDomain> latestFuelLogs) {
        if (latestFuelLogs == null || latestFuelLogs.isEmpty()) {
            return null;
        }

        return latestFuelLogs.get(0).filledAt();
    }

    private BigDecimal calculateFuelEfficiency(List<FuelLogEfficiencyDomain> latestFuelLogs) {
        if (latestFuelLogs == null || latestFuelLogs.size() < 2) {
            return BigDecimal.ZERO;
        }

        FuelLogEfficiencyDomain latestFuelLog = latestFuelLogs.get(0);
        FuelLogEfficiencyDomain previousFuelLog = latestFuelLogs.get(1);

        if (latestFuelLog.kmAtFill() == null
                || previousFuelLog.kmAtFill() == null
                || latestFuelLog.gallons() == null
                || latestFuelLog.gallons().compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        int kilometersTraveled = latestFuelLog.kmAtFill() - previousFuelLog.kmAtFill();
        if (kilometersTraveled <= 0) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(kilometersTraveled)
                .divide(latestFuelLog.gallons(), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal safeBigDecimal(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }

        return value;
    }

    private void registerOdometerLog(FuelLogsDomain fuelLog) {
        odometerLogService.registerOdometerLog(
                fuelLog.getVehicleId(),
                fuelLog.getKmAtFill(),
                fuelLog.getFilledAt(),
                OdometerLogSource.FUEL,
                fuelLog.getId(),
                getFuelLogOdometerNote(fuelLog)
        );
    }

    private void updateOdometerLog(FuelLogsDomain fuelLog) {
        odometerLogService.updateOdometerLogFromSource(
                OdometerLogSource.FUEL,
                fuelLog.getId(),
                fuelLog.getVehicleId(),
                fuelLog.getKmAtFill(),
                fuelLog.getFilledAt(),
                getFuelLogOdometerNote(fuelLog)
        );
    }

    private String getFuelLogOdometerNote(FuelLogsDomain fuelLog) {
        if (fuelLog.getNotes() == null || fuelLog.getNotes().isBlank()) {
            return "Fuel log registration";
        }

        return fuelLog.getNotes();
    }
}
