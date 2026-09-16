package com.driveden.app.application.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.driveden.app.domain.AiTesting.DTO.maintenanceAi;
import com.driveden.app.application.tools.vehicleTools;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiService {

    private final ChatClient chatClient;
    private final vehicleTools vehicleTools;

    public maintenanceAi ask(String message) {
        return chatClient
                .prompt()
                .system(SYSTEM_PROMPT)
                .user(message)
                .call()
                .entity(maintenanceAi.class);
    }


        public String askMCPString(String message) {
            return chatClient
                    .prompt()
                    .system("""
                            Eres un asistente especializado en mantenimiento de vehículos.

                                        Puedes utilizar las herramientas disponibles
                                            cuando necesites información sobre los vehículos
                                            registrados del usuario.

                            Reglas:
                            - Responde siempre en español.
                            - Sé claro y conciso.
                            - No inventes información.
                            - Si no tienes suficiente información, dilo.
                            - No proporciones información personal ni confidencial.
                            - SOLO responde a preguntas relacionadas con vehículos y su mantenimiento. No respondas a preguntas fuera de este ámbito.""")
                    .user(message)
                    .tools(vehicleTools)
                    .call()
                    .content();
        }

        private static final String SYSTEM_PROMPT = """
        Eres un asistente especializado en mantenimiento de vehículos.

                    Debes analizar la solicitud del usuario y determinar:
                    - La intención.
                    - El vehículo mencionado.
                    - El año del vehículo si está disponible.
                    - La acción solicitada.

                    Si un dato no está disponible, utiliza null.

        Reglas:
        - Responde siempre en español.
        - Sé claro y conciso.
        - No inventes información.
        - Si no tienes suficiente información, dilo.
        - No proporciones información personal ni confidencial.
        - SOLO responde a preguntas relacionadas con vehículos y su mantenimiento. No respondas a preguntas fuera de este ámbito.
        """;
}