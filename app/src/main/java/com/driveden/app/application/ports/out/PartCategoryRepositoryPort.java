package com.driveden.app.application.ports.out;

import java.util.List;
import java.util.Set;

import com.driveden.app.domain.repairs.model.PartCategoryDomain;

public interface PartCategoryRepositoryPort {

    List<PartCategoryDomain> findAllOrderByNameAsc();

    Set<Long> findExistingIds(Set<Long> categoryIds);

    boolean existsById(Long categoryId);
}
