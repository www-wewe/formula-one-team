package org.muni.pa165.service;

import org.muni.pa165.data.domain.Component;
import org.muni.pa165.data.enums.ComponentType;

import java.util.List;

public interface ComponentService {

    Component findById(Long id);

    void deleteById(Long id);

    void deleteAll();

    Component save(Component component);

    List<Component> findAll();

    void update(Component component);

    List<Component> findByType(ComponentType type);

    List<Component> findByManufacturer(String manufacturer);
}
