package org.muni.pa165.utils;

import org.muni.pa165.api.DriverCreateViewDto;
import org.muni.pa165.api.DriverViewDto;
import org.muni.pa165.data.domain.Driver;
import org.muni.pa165.data.domain.DriverPerk;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestDriverFactory {

    public static DriverViewDto getDriverViewDto() {
        return DriverViewDto.builder()
                .id(1L)
                .name("Sebastian")
                .surname("Vettel")
                .nationality("German")
                .perk(DriverPerk.OVERTAKER)
                .build();
    }

    public static Driver getDriverEntity() {
        return Driver.builder()
                .id(1L)
                .name("Sebastian")
                .surname("Vettel")
                .nationality("German")
                .perk(DriverPerk.OVERTAKER)
                .build();
    }

    public static List<DriverViewDto> getListOfDriverViewDto() {
        return Stream.generate(TestDriverFactory::getDriverViewDto)
                .limit(20)
                .collect(Collectors.toList());
    }

    public static List<Driver> getListOfDriverEntities() {
        return Stream.generate(TestDriverFactory::getDriverEntity)
                .limit(20)
                .collect(Collectors.toList());
    }

    public static DriverCreateViewDto getDriverCreateViewDto() {
        return DriverCreateViewDto.builder()
                .name("Sebastian")
                .surname("Vettel")
                .nationality("German")
                .perk(DriverPerk.OVERTAKER)
                .build();
    }
}
