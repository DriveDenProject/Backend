package com.driveden.app.application.ports.out;

import java.util.Set;

public interface PartCategoryRepositoryPort {

    Set<Long> findExistingIds(Set<Long> categoryIds);
}
