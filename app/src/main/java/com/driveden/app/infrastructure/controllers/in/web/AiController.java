package com.driveden.app.infrastructure.controllers.in.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.driveden.app.application.services.AiService;
import com.driveden.app.domain.AiTesting.DTO.maintenanceAi;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {


    private final AiService aiService;

    @GetMapping
    public maintenanceAi ask(@RequestParam String message) {
        return aiService.ask(message);
    }

    @GetMapping("/mcp")
    public String askMCP(@RequestParam String message) {
        return aiService.askMCPString(message);
    } 

}
