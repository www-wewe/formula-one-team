package org.muni.pa165.utils;

import org.muni.pa165.api.ComponentCreateViewDto;
import org.muni.pa165.api.ComponentViewDto;
import org.muni.pa165.data.domain.Component;
import org.muni.pa165.data.enums.ComponentType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestComponentFactory {

    public static ComponentViewDto getComponentViewDto() {
        return ComponentViewDto.builder()
                .id(1L)
                .weight(20)
                .price(1000)
                .manufacturer("BMW")
                .version("1.0.0")
                .type(ComponentType.SPOILER)
                .build();
    }

    public static Component getComponentEntity() {
        return Component.builder()
                .id(1L)
                .weight(20)
                .price(1000)
                .manufacturer("BMW")
                .version("1.0.0")
                .type(ComponentType.SPOILER)
                .build();
    }

    public static List<Component> getListOfComponentEntities() {
        return Stream.generate(TestComponentFactory::getComponentEntity)
                .limit(20)
                .collect(Collectors.toList());
    }

    public static List<ComponentViewDto> getListOfComponentViewDto() {
        return Stream.generate(TestComponentFactory::getComponentViewDto)
                .limit(20)
                .collect(Collectors.toList());
    }

    public static ComponentCreateViewDto getComponentCreateViewDto() {
        return ComponentCreateViewDto.builder()
                .weight(20)
                .price(1000)
                .manufacturer("BMW")
                .version("1.0.0")
                .type(ComponentType.SPOILER)
                .build();
    }
}
