package com.driveden.app.application.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.driveden.app.application.ports.out.OdometerLogRepositoryPort;
import com.driveden.app.common.exception.CustomException;
import com.driveden.app.domain.odometerLogs.dto.CurrentMonthMileageStatsResponseDTO;
import com.driveden.app.domain.odometerLogs.dto.MonthlyMileageStatsResponseDTO;
import com.driveden.app.infrastructure.out.persistence.mappers.OdometerLogMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.UserVehicleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OdometerMileageStatsService {

    private final OdometerLogRepositoryPort odometerLogRepositoryPort;
    private final UserVehicleRepository userVehicleRepository;
    private final UsersService usersService;

    public List<MonthlyMileageStatsResponseDTO> getMonthlyMileageStats(Long vehicleId, Long userId) {
        validateVehicleOwnership(userId, vehicleId);

        return odometerLogRepositoryPort.findMonthlyMileageStats(vehicleId).stream()
                .map(OdometerLogMapper::toMonthlyMileageStatsResponseDTO)
                .toList();
    }

    public CurrentMonthMileageStatsResponseDTO getCurrentMonthMileageStats(Long vehicleId, Long userId) {
        validateVehicleOwnership(userId, vehicleId);

        LocalDate today = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(today);
        LocalDateTime startDate = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = currentMonth.plusMonths(1).atDay(1).atStartOfDay();
        Integer kilometersThisMonth = odometerLogRepositoryPort.calculateKmTraveledByVehicleIdAndRecordedAtBetween(
                vehicleId,
                startDate,
                endDate
        );

        if (kilometersThisMonth == null) {
            kilometersThisMonth = 0;
        }

        BigDecimal averageKilometersPerDay = calculateDailyAverage(kilometersThisMonth, today.getDayOfMonth());

        return OdometerLogMapper.toCurrentMonthMileageStatsResponseDTO(
                kilometersThisMonth,
                averageKilometersPerDay
        );
    }

    private void validateVehicleOwnership(Long userId, Long vehicleId) {
        usersService.findUserById(userId);

        if (!userVehicleRepository.existsByUserIdAndVehicleId(userId, vehicleId)) {
            throw new CustomException("Vehicle not found for user", HttpStatus.NOT_FOUND);
        }
    }

    private BigDecimal calculateDailyAverage(Integer currentMonthKm, Integer elapsedDays) {
        int safeElapsedDays = Math.max(elapsedDays, 1);

        return BigDecimal.valueOf(currentMonthKm)
                .divide(BigDecimal.valueOf(safeElapsedDays), 2, RoundingMode.HALF_UP);
    }
}
