package com.driveden.app.infrastructure.ai;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OpenAIPromptBuilder {

    private final Clock clock;

    public String build(String text) {
        LocalDate today = LocalDate.now(clock);
        LocalDateTime now = LocalDateTime.now(clock).withNano(0);

        return """
                Return JSON only. Classify vehicle note as FUEL_LOG, REMINDER, REPAIR, or INVALID_AUDIO. Preserve input language (English/Spanish). Today=%s Now=%s.
                Shape:
                {"type":"FUEL_LOG","data":{"gallons":number|null,"priceTotal":number|null,"pricePerGallon":number|null,"kmAtFill":number|null,"filledAt":"ISO_LOCAL_DATE_TIME"|null,"gasStation":string|null}}
                {"type":"REMINDER","data":{"categoryId":number|null,"serviceName":string|null,"description":string|null,"startDate":"ISO_LOCAL_DATE"|null,"dueDate":"ISO_LOCAL_DATE"|null,"reminderFrequencyDays":number|null,"notifyBeforeDays":number|null,"priority":"LOW|MEDIUM|HIGH"|null,"isRecurring":boolean|null,"recurrenceIntervalDays":number|null}}
                {"type":"REPAIR","data":{"repairDate":"ISO_LOCAL_DATE"|null,"description":string|null,"workshop":string|null,"laborCost":number|null,"totalCost":number|null,"parts":[{"name":string|null,"categoryId":number|null,"brand":string|null,"quantity":number|null,"unitPrice":number|null}]}}
                If fuel pricePerGallon missing and gallons+priceTotal exist, compute it. Text: %s
                """.formatted(today, now, text);
    }
}
