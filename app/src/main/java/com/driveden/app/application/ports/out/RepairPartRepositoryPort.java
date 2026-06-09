package com.driveden.app.application.ports.out;

import java.util.List;

import com.driveden.app.domain.repairs.model.RepairPartDomain;

public interface RepairPartRepositoryPort {

    List<RepairPartDomain> saveAll(List<RepairPartDomain> repairParts);
}
