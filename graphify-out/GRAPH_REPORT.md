# Graph Report - DriveDenBack  (2026-09-16)

## Corpus Check
- Corpus is ~42,135 words - fits in a single context window. You may not need a graph.

## Summary
- 1384 nodes · 4621 edges · 62 communities (45 shown, 17 thin omitted)
- Extraction: 92% EXTRACTED · 8% INFERRED · 0% AMBIGUOUS · INFERRED: 359 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- Vehicle Reference Data
- External Vehicle Specs
- Authentication Core
- Vehicle Dashboard
- Parts Repository
- Odometer Repository
- AI Client Config
- Mileage Persistence
- Vehicle Controllers
- Shared Services
- Fuel Repository
- Voice Classification
- Subscription Plans
- Device Tokens
- OpenAI Parsing
- Voice Repair AI
- Voice Input Flow
- Repair Post Processing
- Subscription Status Mapping
- Subscription Granting
- Auth Controller
- Fuel Persistence
- Repair Repository
- Vehicle Details Mapping
- Deployment Docs
- Fuel Tank History
- Subscription Usage
- Vehicle History
- Maintenance AI
- Fuel Service
- Application Tests
- Part Categories
- Payment Methods
- Push Notifications
- Admin Controllers
- Vehicle Registration
- Motorcycle APIs
- Error Handling
- Subscription Limits
- Current Subscription
- Vehicle Odometer Logic
- Tank History DTOs
- Maven Wrapper
- Fuel Statistics
- Payment Method Service
- Car APIs
- Subscription Exceptions
- Repair Extraction Tests
- Subscription Activation
- Vehicle History DTOs
- Repair History DTOs
- Fuel Efficiency DTOs
- Graphify Pipeline
- Notification Dispatch
- Repair Statistics
- Graph Query Docs
- Spring Boot Entry
- Latest Repairs
- Extraction Rules
- Graphify Updates
- Graph Exports
- Maven Project Metadata

## God Nodes (most connected - your core abstractions)
1. `CustomException` - 72 edges
2. `CustomResponse` - 72 edges
3. `SubscriptionService` - 35 edges
4. `FuelLogsDomain` - 34 edges
5. `UserVehicleRepository` - 34 edges
6. `RepairService` - 31 edges
7. `FuelService` - 29 edges
8. `VehicleNotificationDomain` - 29 edges
9. `Subscription` - 28 edges
10. `CarsController` - 28 edges

## Surprising Connections (you probably didn't know these)
- `Graphify Update After Code Changes` --semantically_similar_to--> `Incremental Update`  [INFERRED] [semantically similar]
  AGENTS.md → .codex/skills/graphify/references/update.md
- `Graphify Project Instructions` --references--> `Query Workflow`  [EXTRACTED]
  AGENTS.md → .codex/skills/graphify/references/query.md
- `Docker Deployment` --references--> `Docker Compose`  [EXTRACTED]
  README.md → docker-compose.yml
- `VehicleDashboardService` --references--> `FuelLogRepositoryPort`  [EXTRACTED]
  app/src/main/java/com/driveden/app/application/services/VehicleDashboardService.java → app/src/main/java/com/driveden/app/application/ports/out/FuelLogRepositoryPort.java
- `FuelLogsRepository` --implements--> `FuelLogRepositoryPort`  [EXTRACTED]
  app/src/main/java/com/driveden/app/infrastructure/out/persistence/repositories/implement/FuelLogsRepository.java → app/src/main/java/com/driveden/app/application/ports/out/FuelLogRepositoryPort.java

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **DriveDen Operational Vehicle Features** — readme_vehicle_management, readme_fuel_and_mileage, readme_repairs_and_parts, readme_maintenance_notifications [EXTRACTED 1.00]
- **Graphify Default Pipeline** — _codex_skills_graphify_skill_detect_files, _codex_skills_graphify_skill_structural_extraction, _codex_skills_graphify_skill_semantic_extraction, _codex_skills_graphify_skill_build_cluster_analyze [EXTRACTED 1.00]

## Communities (62 total, 17 thin omitted)

### Community 0 - "Vehicle Reference Data"
Cohesion: 0.05
Nodes (45): GoogleLoginDTO, VehicleType, CAR, MOTORCYCLE, transmissionTypeDomain, LoginDTO, UserVehicleDomain, MaintenanceCategoryDomain (+37 more)

### Community 1 - "External Vehicle Specs"
Cohesion: 0.06
Nodes (35): MotorcycleClient, AuthRefreshRequestDTO, carRegisterRequestDTO, makesDTO, modelByGenerationDTO, modelsDTO, RegisterDeviceTokenDTO, UpdateFuelLogDTO (+27 more)

### Community 2 - "Authentication Core"
Cohesion: 0.05
Nodes (34): GoogleAuthPort, AuthService, EmailService, Override, SecurityFilter, AuthResponseDTO, ChangePasswordDTO, AuthProvider (+26 more)

### Community 3 - "Vehicle Dashboard"
Cohesion: 0.06
Nodes (26): VehicleDashboardService, VehicleNotificationSchedulerService, VehicleNotificationService, CustomException, LastDashboardFuelLogResponseDTO, NextServiceResponseDTO, VehicleDashboardResponseDTO, NotificationDispatchResponseDTO (+18 more)

### Community 4 - "Parts Repository"
Cohesion: 0.06
Nodes (22): PartCategoryRepositoryPort, PartRepositoryPort, RepairPartRepositoryPort, RepairService, LatestRepairByCategoryResponseDTO, RegisterRepairDTO, RegisterRepairPartDTO, RepairHistoryResponseDTO (+14 more)

### Community 5 - "Odometer Repository"
Cohesion: 0.08
Nodes (14): OdometerLogRepositoryPort, OdometerLogService, OdometerMileageStatsService, CurrentMonthMileageStatsResponseDTO, MonthlyMileageStatsResponseDTO, OdometerLogDomain, OdometerLogSource, FUEL (+6 more)

### Community 6 - "AI Client Config"
Cohesion: 0.08
Nodes (24): AiConfig, Builder, CarSpecsClientConfig, Builder, FirebaseConfig, Builder, MotorcycleClientConfig, Builder (+16 more)

### Community 7 - "Mileage Persistence"
Cohesion: 0.12
Nodes (7): VehicleNotificationEntity, MonthlyMileageStatsProjection, OdometerLogJpa, RepairJpa, VehicleNotificationJpa, org.springframework.data.jpa.repository.JpaRepository, org.springframework.data.jpa.repository.Query

### Community 8 - "Vehicle Controllers"
Cohesion: 0.16
Nodes (7): PageResponseDTO, CarsController, RepairsController, org.springframework.security.core.Authentication, org.springframework.web.bind.annotation.DeleteMapping, org.springframework.web.bind.annotation.GetMapping, org.springframework.web.bind.annotation.PutMapping

### Community 9 - "Shared Services"
Cohesion: 0.15
Nodes (13): DeviceTokenService, PartCategoryService, SecurityContextService, UsersService, vehicleTools, AuthenticatedUser, PartCategoryResponseDTO, UserDetailsDTO (+5 more)

### Community 10 - "Fuel Repository"
Cohesion: 0.15
Nodes (3): FuelLogRepositoryPort, FuelService, FuelLogResponseDTO

### Community 11 - "Voice Classification"
Cohesion: 0.17
Nodes (12): VoiceClassificationResponseDTO, VoiceClassificationException, VoiceClassificationResult, VoiceClassificationType, FUEL_LOG, INVALID_AUDIO, REMINDER, REPAIR (+4 more)

### Community 12 - "Subscription Plans"
Cohesion: 0.13
Nodes (8): SubscriptionPlanRepositoryPort, SubscriptionPlanResponseDTO, SubscriptionPlan, SubscriptionPlanMapper, Override, SubscriptionPlanRepository, SubscriptionPlanJpa, org.springframework.data.jpa.repository.EntityGraph

### Community 13 - "Device Tokens"
Cohesion: 0.17
Nodes (8): DeviceTokenResponseDTO, DevicePlatform, ANDROID, IOS, UserDeviceTokenDomain, UserDeviceTokenMapper, UserDeviceTokenRepository, UserDeviceTokenJpa

### Community 14 - "OpenAI Parsing"
Cohesion: 0.26
Nodes (4): OpenAIResponseParser, SuppressWarnings, OpenAIResponseParserTest, com.fasterxml.jackson.databind.JsonNode

### Community 15 - "Voice Repair AI"
Cohesion: 0.15
Nodes (8): OpenAIPromptBuilder, Override, OpenAIVoiceInputClassifier, VoiceInputPrefilter, VehicleNotificationScheduler, java.util.regex.Pattern, org.springframework.scheduling.annotation.Scheduled, org.springframework.stereotype.Component

### Community 16 - "Voice Input Flow"
Cohesion: 0.15
Nodes (7): ProcessVoiceInputUseCase, CacheEntry, VoiceInputDuplicateCache, VoiceInputClassifier, FailureState, OpenAIRateLimiter, org.springframework.beans.factory.annotation.Autowired

### Community 17 - "Repair Post Processing"
Cohesion: 0.19
Nodes (5): SuppressWarnings, VoiceRepairPostProcessor, RepairCostCandidate, SuppressWarnings, VoiceRepairPostProcessorTest

### Community 18 - "Subscription Status Mapping"
Cohesion: 0.13
Nodes (9): SubscriptionStatus, ACTIVE, CANCELLED, EXPIRED, UserSubscriptionMapper, Override, UserSubscriptionRepository, UsersJpa (+1 more)

### Community 19 - "Subscription Granting"
Cohesion: 0.25
Nodes (3): UserSubscriptionRepositoryPort, SubscriptionService, Subscription

### Community 20 - "Auth Controller"
Cohesion: 0.22
Nodes (3): AuthController, CustomResponse, org.springframework.web.bind.annotation.PostMapping

### Community 21 - "Fuel Persistence"
Cohesion: 0.21
Nodes (4): FuelLogsEntity, FuelLogsRepository, Override, FuelLogsJpa

### Community 22 - "Repair Repository"
Cohesion: 0.20
Nodes (6): RepairRepositoryPort, LatestRepairByCategoryDomain, RepairHistoryDomain, RepairStatsDomain, Override, RepairRepository

### Community 23 - "Vehicle Details Mapping"
Cohesion: 0.25
Nodes (6): vehicleDetailsDomain, VehicleDetailsEntity, VehicleDetailsMapper, VehicleDetailsRepository, VehicleDetailsJpa, org.springframework.data.jpa.repository.Lock

### Community 24 - "Deployment Docs"
Cohesion: 0.14
Nodes (18): Backend Service, Docker Compose, DriveDen Network, Postgres Data Volume, Postgres Service, Authentication And Users, Docker Deployment, DriveDen Backend (+10 more)

### Community 25 - "Fuel Tank History"
Cohesion: 0.22
Nodes (7): FuelLogTankHistoryDomain, Override, VehicleHistoryRepository, VehicleHistoryJpa, org.springframework.data.domain.Page, org.springframework.data.domain.Pageable, org.springframework.data.repository.Repository

### Community 26 - "Subscription Usage"
Cohesion: 0.22
Nodes (6): SubscriptionUsageRepositoryPort, SubscriptionUsage, SubscriptionUsageMapper, Override, SubscriptionUsageRepository, SubscriptionUsageJpa

### Community 27 - "Vehicle History"
Cohesion: 0.24
Nodes (5): VehicleHistoryRepositoryPort, VehicleHistoryService, VehicleHistoryItemResponseDTO, VehicleHistoryItemDomain, VehicleHistoryMapper

### Community 28 - "Maintenance AI"
Cohesion: 0.17
Nodes (9): AiService, maintenanceAi, Intent, DIAGNOSTIC, MAINTENANCE, REPAIR, UNKOWN, AiController (+1 more)

### Community 29 - "Fuel Service"
Cohesion: 0.27
Nodes (5): FuelLogHistoryResponseDTO, LastFuelLogResponseDTO, RegisterFuelLogDTO, FuelLogsDomain, FuelLogsMapper

### Community 30 - "Application Tests"
Cohesion: 0.34
Nodes (4): AppApplicationTests, ProcessVoiceInputUseCaseTest, org.junit.jupiter.api.Test, org.springframework.boot.test.context.SpringBootTest

### Community 31 - "Part Categories"
Cohesion: 0.21
Nodes (5): PartCategoryMapper, PartCategoryProjection, Override, PartCategoryRepository, PartCategoryJpa

### Community 32 - "Payment Methods"
Cohesion: 0.20
Nodes (5): PaymentMethodMapper, PaymentMethodProjection, Override, PaymentMethodRepository, PaymentMethodJpa

### Community 33 - "Push Notifications"
Cohesion: 0.24
Nodes (7): PushNotificationPort, PushNotificationResult, FirebasePushNotificationAdapter, Override, com.google.firebase.messaging.FirebaseMessaging, com.google.firebase.messaging.FirebaseMessagingException, org.springframework.beans.factory.ObjectProvider

### Community 34 - "Admin Controllers"
Cohesion: 0.31
Nodes (8): AdminGrantSubscriptionRequestDTO, AdminSubscriptionsController, DeviceTokensController, PaymentMethodsController, VoiceInputController, org.springframework.validation.annotation.Validated, org.springframework.web.bind.annotation.RequestMapping, org.springframework.web.bind.annotation.RestController

### Community 35 - "Vehicle Registration"
Cohesion: 0.27
Nodes (4): vehicleDomain, VehicleMapper, VehicleRepository, VehicleJpa

### Community 37 - "Error Handling"
Cohesion: 0.33
Nodes (8): ErrorResponse, GlobalExceptionHandler, jakarta.servlet.http.HttpServletRequest, lombok.extern.slf4j.Slf4j, org.springframework.http.ResponseEntity, org.springframework.web.bind.annotation.ControllerAdvice, org.springframework.web.bind.annotation.ExceptionHandler, org.springframework.web.bind.MethodArgumentNotValidException

### Community 39 - "Current Subscription"
Cohesion: 0.21
Nodes (6): CurrentSubscriptionResponseDTO, SubscriptionProvider, ADMIN_GRANT, GOOGLE_PLAY, SYSTEM_FREE, org.junit.jupiter.api.BeforeEach

### Community 42 - "Maven Wrapper"
Cohesion: 0.38
Nodes (8): mvnw script, clean(), die(), exec_maven(), hash_string(), set_java_home(), trim(), verbose()

### Community 44 - "Payment Method Service"
Cohesion: 0.29
Nodes (3): PaymentMethodRepositoryPort, PaymentMethodService, PaymentMethodResponseDTO

### Community 46 - "Subscription Exceptions"
Cohesion: 0.27
Nodes (3): InvalidSubscriptionStateException, SubscriptionLimitExceededException, SubscriptionNotFoundException

### Community 48 - "Subscription Activation"
Cohesion: 0.33
Nodes (3): ActivateSubscriptionRequestDTO, ActivateSubscriptionResponseDTO, SubscriptionsController

### Community 52 - "Graphify Pipeline"
Cohesion: 0.40
Nodes (6): Build Cluster Analyze, Detect Files, Graph Health Check, Graphify Skill, Semantic Extraction, Structural Extraction

### Community 55 - "Graph Query Docs"
Cohesion: 0.40
Nodes (5): Explain Node, Path Query, Query Workflow, Existing Knowledge Graph, Graphify Project Instructions

### Community 56 - "Spring Boot Entry"
Cohesion: 0.60
Nodes (3): AppApplication, org.springframework.boot.autoconfigure.SpringBootApplication, org.springframework.scheduling.annotation.EnableScheduling

## Knowledge Gaps
- **49 isolated node(s):** `com.driveden:app`, `MAINTENANCE`, `REPAIR`, `DIAGNOSTIC`, `UNKOWN` (+44 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 134 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **17 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `CustomException` connect `Vehicle Dashboard` to `Vehicle Reference Data`, `External Vehicle Specs`, `Authentication Core`, `Parts Repository`, `Odometer Repository`, `Error Handling`, `Vehicle Odometer Logic`, `Shared Services`, `Fuel Repository`, `Voice Classification`, `Subscription Exceptions`, `Voice Repair AI`, `Subscription Activation`, `Voice Input Flow`, `Auth Controller`, `Vehicle Details Mapping`, `Vehicle History`, `Fuel Service`?**
  _High betweenness centrality (0.095) - this node is a cross-community bridge._
- **Why does `CustomResponse` connect `Auth Controller` to `Vehicle Reference Data`, `Admin Controllers`, `Vehicle Dashboard`, `Motorcycle APIs`, `Error Handling`, `Vehicle Controllers`, `Shared Services`, `Payment Method Service`, `Subscription Activation`?**
  _High betweenness centrality (0.038) - this node is a cross-community bridge._
- **Why does `OpenAIResponseParser` connect `OpenAI Parsing` to `Shared Services`, `Voice Classification`, `Voice Repair AI`?**
  _High betweenness centrality (0.036) - this node is a cross-community bridge._
- **What connects `com.driveden:app`, `MAINTENANCE`, `REPAIR` to the rest of the system?**
  _49 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Vehicle Reference Data` be split into smaller, more focused modules?**
  _Cohesion score 0.0533988533988534 - nodes in this community are weakly interconnected._
- **Should `External Vehicle Specs` be split into smaller, more focused modules?**
  _Cohesion score 0.05806572068707991 - nodes in this community are weakly interconnected._
- **Should `Authentication Core` be split into smaller, more focused modules?**
  _Cohesion score 0.05318491032776747 - nodes in this community are weakly interconnected._