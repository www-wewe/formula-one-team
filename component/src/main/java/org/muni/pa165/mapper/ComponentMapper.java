package org.muni.pa165.mapper;

import org.mapstruct.Mapper;
import org.muni.pa165.api.ComponentCreateViewDto;
import org.muni.pa165.api.ComponentViewDto;
import org.muni.pa165.data.domain.Component;

@Mapper(componentModel = "spring")
public interface ComponentMapper {

    ComponentViewDto toComponentViewDto(Component component);

    Component fromComponentViewDto(ComponentViewDto componentViewDto);

    ComponentCreateViewDto toComponentCreateViewDto(Component component);

    Component fromComponentCreateViewDto(ComponentCreateViewDto componentViewDto);
}
