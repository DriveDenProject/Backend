package com.driveden.app.application.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.driveden.app.application.ports.out.FuelLogRepositoryPort;
import com.driveden.app.application.ports.out.RepairRepositoryPort;
import com.driveden.app.common.exception.CustomException;
import com.driveden.app.domain.cars.dto.VehicleDashboardResponseDTO;
import com.driveden.app.domain.cars.dto.VehicleDashboardResponseDTO.LastDashboardFuelLogResponseDTO;
import com.driveden.app.domain.cars.dto.VehicleDashboardResponseDTO.NextServiceResponseDTO;
import com.driveden.app.domain.cars.model.vehicleDetailsDomain;
import com.driveden.app.domain.fuelLogs.model.FuelLogsDomain;
import com.driveden.app.domain.vehicleNotifications.model.VehicleNotificationDomain;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.UserVehicleRepository;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.VehicleDetailsRepository;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.VehicleNotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleDashboardService {

    private final FuelLogRepositoryPort fuelLogRepository;
    private final RepairRepositoryPort repairRepository;
    private final VehicleDetailsRepository vehicleDetailsRepository;
    private final VehicleNotificationRepository vehicleNotificationRepository;
    private final UserVehicleRepository userVehicleRepository;
    private final UsersService usersService;

    public VehicleDashboardResponseDTO getVehicleDashboard(Long vehicleId, Long userId) {
        validateVehicleOwnership(userId, vehicleId);
        vehicleDetailsDomain vehicleDetails = findVehicleDetails(vehicleId);

        return new VehicleDashboardResponseDTO(
                calculateMonthlyExpenses(vehicleId),
                vehicleDetails.getCurrentKm(),
                getNextService(vehicleId),
                getLastFuelLog(vehicleId)
        );
    }

    private void validateVehicleOwnership(Long userId, Long vehicleId) {
        usersService.findUserById(userId);

        if (!userVehicleRepository.existsByUserIdAndVehicleId(userId, vehicleId)) {
            throw new CustomException("Vehicle not found for user", HttpStatus.NOT_FOUND);
        }
    }

    private vehicleDetailsDomain findVehicleDetails(Long vehicleId) {
        return vehicleDetailsRepository.findByVehicleId(vehicleId)
                .orElseThrow(() -> new CustomException("Vehicle details not found", HttpStatus.NOT_FOUND));
    }

    private BigDecimal calculateMonthlyExpenses(Long vehicleId) {
        YearMonth currentMonth = YearMonth.from(LocalDate.now());
        LocalDateTime startDate = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = currentMonth.plusMonths(1).atDay(1).atStartOfDay();

        BigDecimal fuelExpenses = fuelLogRepository.sumPriceTotalByVehicleIdAndFilledAtBetween(
                vehicleId,
                startDate,
                endDate
        );
        BigDecimal repairExpenses = repairRepository.sumTotalCostByVehicleIdAndRepairDateBetween(
                vehicleId,
                startDate,
                endDate
        );

        return safeBigDecimal(fuelExpenses).add(safeBigDecimal(repairExpenses));
    }

    private NextServiceResponseDTO getNextService(Long vehicleId) {
        Optional<VehicleNotificationDomain> nextNotification = vehicleNotificationRepository.findNextPendingByVehicleId(vehicleId);

        return nextNotification
                .map(notification -> new NextServiceResponseDTO(
                        notification.getServiceName(),
                        notification.getPriority()
                ))
                .orElse(null);
    }

    private LastDashboardFuelLogResponseDTO getLastFuelLog(Long vehicleId) {
        Optional<FuelLogsDomain> lastFuelLog = fuelLogRepository.findLatestByVehicleId(vehicleId);

        return lastFuelLog
                .map(fuelLog -> new LastDashboardFuelLogResponseDTO(
                        fuelLog.getPriceTotal(),
                        fuelLog.getFilledAt()
                ))
                .orElse(null);
    }

    private BigDecimal safeBigDecimal(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }

        return value;
    }
}
