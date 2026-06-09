package com.driveden.app.application.ports.out;

import java.util.List;

import com.driveden.app.domain.repairs.model.PartDomain;

public interface PartRepositoryPort {

    List<PartDomain> findByNamesIgnoreCase(List<String> names);

    List<PartDomain> saveAll(List<PartDomain> parts);
}
