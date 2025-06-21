package org.muni.pa165.service;

import jakarta.transaction.Transactional;
import org.muni.pa165.data.domain.Component;
import org.muni.pa165.data.enums.ComponentType;
import org.muni.pa165.data.repository.ComponentRepository;
import org.muni.pa165.exceptions.ComponentNotFoundException;
import org.muni.pa165.exceptions.ComponentValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ComponentServiceImpl implements ComponentService {

    private final ComponentRepository componentRepository;

    @Autowired
    public ComponentServiceImpl(ComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    @Override
    public Component findById(Long id) {
        return componentRepository.findById(id)
                .orElseThrow(() -> new ComponentNotFoundException("Component not found with id: " + id));
    }

    @Override
    public List<Component> findAll() {
        return componentRepository.findAll();
    }

    @Override
    public Component save(Component component) {
        validateComponent(component);
        return componentRepository.save(component);
    }

    @Override
    public void deleteById(Long id) {
        if (!componentRepository.existsById(id)) {
            throw new ComponentNotFoundException("Cannot delete, component not found with id: " + id);
        }
        componentRepository.deleteById(id);
    }

    @Override
    public void update(Component component) {
        validateComponent(component);
        if (!componentRepository.existsById(component.getId())) {
            throw new ComponentNotFoundException("Cannot update, component not found with id: " + component.getId());
        }
        componentRepository.save(component);
    }

    @Override
    public void deleteAll() {
        componentRepository.deleteAll();
    }

    @Override
    public List<Component> findByType(ComponentType type) {
        return componentRepository.findByType(type);
    }

    @Override
    public List<Component> findByManufacturer(String manufacturer) {
        return componentRepository.findByManufacturer(manufacturer);
    }

    private void validateComponent(Component component) {
        if (component.getWeight() <= 0) {
            throw new ComponentValidationException("Component weight must be greater than 0");
        }
        if (component.getPrice() <= 0) {
            throw new ComponentValidationException("Component price must be greater than 0");
        }
        if (component.getManufacturer() == null || component.getManufacturer().isEmpty()) {
            throw new ComponentValidationException("Component manufacturer must not be empty");
        }
        if (component.getType() == null) {
            throw new ComponentValidationException("Component type must not be empty");
        }
    }
}
