package org.muni.pa165.facade;

import org.muni.pa165.api.ComponentCreateViewDto;
import org.muni.pa165.api.ComponentViewDto;
import org.muni.pa165.data.domain.Component;
import org.muni.pa165.data.enums.ComponentType;
import org.muni.pa165.mapper.ComponentMapper;
import org.muni.pa165.service.ComponentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComponentFacade {

    private final ComponentService componentService;
    private final ComponentMapper componentMapper;

    public ComponentFacade(ComponentService componentService, ComponentMapper componentMapper) {
        this.componentService = componentService;
        this.componentMapper = componentMapper;
    }

    public void deleteById(Long id) {
        componentService.deleteById(id);
    }

    public void deleteAll() {
        componentService.deleteAll();
    }

    public void update(Component component) {
        componentService.update(component);
    }

    public List<ComponentViewDto> findByType(ComponentType type) {
        return componentService.findByType(type).stream().map(componentMapper::toComponentViewDto).toList();
    }

    public List<ComponentViewDto> findByManufacturer(String manufacturer) {
        return componentService.findByManufacturer(manufacturer).stream().map(componentMapper::toComponentViewDto).toList();
    }

    public ComponentViewDto findById(Long id) {
        return componentMapper.toComponentViewDto(componentService.findById(id));
    }

    public ComponentViewDto save(ComponentCreateViewDto componentCreateViewDto) {
        return componentMapper.toComponentViewDto(componentService.save(componentMapper.fromComponentCreateViewDto(componentCreateViewDto)));
    }

    public List<ComponentViewDto> findAll() {
        return componentService.findAll().stream().map(componentMapper::toComponentViewDto).toList();
    }
}
