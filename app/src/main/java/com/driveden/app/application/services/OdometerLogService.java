package com.driveden.app.application.services;

import java.time.LocalDateTime;
import java.util.Optional;

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
        validateRecordedAt(recordedAt);

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

        OdometerLogDomain savedOdometerLog = odometerLogRepositoryPort.save(odometerLogDomain);
        validateOdometerTimeline(savedOdometerLog.getId(), vehicleId, recordedAt, km);

        return savedOdometerLog;
    }

    public OdometerLogDomain updateOdometerLogFromSource(
            OdometerLogSource source,
            Long sourceId,
            Long vehicleId,
            Integer km,
            LocalDateTime recordedAt,
            String note
    ) {
        validateSourceReference(source, sourceId);
        validateOdometerLog(vehicleId, km, source);
        validateRecordedAt(recordedAt);

        OdometerLogDomain currentOdometerLog = findBySourceAndSourceId(source, sourceId);
        validateOdometerTimeline(currentOdometerLog.getId(), vehicleId, recordedAt, km);

        OdometerLogDomain updatedOdometerLog = new OdometerLogDomain(
                currentOdometerLog.getId(),
                vehicleId,
                km,
                recordedAt,
                normalizeNote(note),
                source,
                sourceId,
                currentOdometerLog.getCreatedAt()
        );

        return odometerLogRepositoryPort.save(updatedOdometerLog);
    }

    public void deleteOdometerLogFromSource(OdometerLogSource source, Long sourceId) {
        validateSourceReference(source, sourceId);

        OdometerLogDomain odometerLogDomain = findBySourceAndSourceId(source, sourceId);
        odometerLogRepositoryPort.deleteById(odometerLogDomain.getId());
    }

    public Optional<Integer> findLatestKmByVehicleId(Long vehicleId) {
        return odometerLogRepositoryPort.findLatestByVehicleId(vehicleId)
                .map(OdometerLogDomain::getKm);
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

    private void validateSourceReference(OdometerLogSource source, Long sourceId) {
        if (source == null) {
            throw new CustomException("source is required", HttpStatus.BAD_REQUEST);
        }

        if (sourceId == null) {
            throw new CustomException("sourceId is required", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateRecordedAt(LocalDateTime recordedAt) {
        if (recordedAt == null) {
            throw new CustomException("recordedAt is required", HttpStatus.BAD_REQUEST);
        }
    }

    private OdometerLogDomain findBySourceAndSourceId(OdometerLogSource source, Long sourceId) {
        return odometerLogRepositoryPort.findBySourceAndSourceId(source, sourceId)
                .orElseThrow(() -> new CustomException("Odometer log not found", HttpStatus.NOT_FOUND));
    }

    private void validateOdometerTimeline(
            Long currentLogId,
            Long vehicleId,
            LocalDateTime recordedAt,
            Integer km
    ) {
        OdometerLogDomain previousLog = odometerLogRepositoryPort
                .findPreviousLog(vehicleId, recordedAt, currentLogId)
                .orElse(null);
        OdometerLogDomain nextLog = odometerLogRepositoryPort
                .findNextLog(vehicleId, recordedAt, currentLogId)
                .orElse(null);

        if (previousLog != null && km < previousLog.getKm()) {
            throw new CustomException(
                    "The odometer must be consistent with previous records",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (nextLog != null && km > nextLog.getKm()) {
            throw new CustomException(
                    "The odometer must be consistent with next records",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private String normalizeNote(String note) {
        if (note == null || note.length() <= NOTE_MAX_LENGTH) {
            return note;
        }

        return note.substring(0, NOTE_MAX_LENGTH);
    }
}
