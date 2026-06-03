package com.driveden.app.application.services;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.driveden.app.application.ports.out.OdometerLogRepositoryPort;
import com.driveden.app.common.exception.CustomException;
import com.driveden.app.domain.odometerLogs.model.OdometerLogDomain;
import com.driveden.app.domain.odometerLogs.model.OdometerLogSource;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OdometerLogService {

    private static final int NOTE_MAX_LENGTH = 200;

    private final OdometerLogRepositoryPort odometerLogRepositoryPort;

    public OdometerLogDomain registerOdometerLog(
            Long vehicleId,
            Integer km,
            LocalDateTime recordedAt,
            OdometerLogSource source,
            Long sourceId,
            String note
    ) {
        validateOdometerLog(vehicleId, km, source);

        OdometerLogDomain odometerLogDomain = new OdometerLogDomain(
                null,
                vehicleId,
                km,
                recordedAt,
                normalizeNote(note),
                source,
                sourceId,
                null
        );

        return odometerLogRepositoryPort.save(odometerLogDomain);
    }

    private void validateOdometerLog(Long vehicleId, Integer km, OdometerLogSource source) {
        if (vehicleId == null) {
            throw new CustomException("vehicleId is required", HttpStatus.BAD_REQUEST);
        }

        if (km == null || km < 0) {
            throw new CustomException("km must be zero or positive", HttpStatus.BAD_REQUEST);
        }

        if (source == null) {
            throw new CustomException("source is required", HttpStatus.BAD_REQUEST);
        }
    }

    private String normalizeNote(String note) {
        if (note == null || note.length() <= NOTE_MAX_LENGTH) {
            return note;
        }

        return note.substring(0, NOTE_MAX_LENGTH);
    }
}
