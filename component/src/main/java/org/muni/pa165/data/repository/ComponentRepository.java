package org.muni.pa165.data.repository;

import org.muni.pa165.data.domain.Component;
import org.muni.pa165.data.enums.ComponentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComponentRepository extends JpaRepository<Component, Long> {

    List<Component> findByType(ComponentType type);

    List<Component> findByManufacturer(String manufacturer);
}
