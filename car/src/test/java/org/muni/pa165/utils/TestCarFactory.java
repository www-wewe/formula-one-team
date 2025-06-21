package org.muni.pa165.utils;

import org.muni.pa165.api.CarCreateViewDto;
import org.muni.pa165.api.CarViewDto;
import org.muni.pa165.data.domain.Car;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestCarFactory {

    public static CarViewDto getCarViewDto() {
        Set<Long> testDrivers = new HashSet<>();
        testDrivers.add(1L);
        testDrivers.add(2L);

        Set<Long> components = new HashSet<>();
        components.add(100L);
        components.add(101L);

        return CarViewDto.builder()
                .id(1L)
                .mainDriver(1L)
                .carMake("Toyota")
                .testDrivers(testDrivers)
                .components(components)
                .build();
    }

    public static Car getCarEntity() {
        Set<Long> testDrivers = new HashSet<>();
        testDrivers.add(1L);
        testDrivers.add(2L);

        Set<Long> components = new HashSet<>();
        components.add(100L);
        components.add(101L);

        return Car.builder()
                .mainDriver(1L)
                .carMake("Toyota")
                .testDrivers(testDrivers)
                .components(components)
                .build();
    }

    public static List<CarViewDto> getListOfCarViewDto() {
        return Stream.generate(TestCarFactory::getCarViewDto)
                .limit(20)
                .collect(Collectors.toList());
    }

    public static List<Car> getListOfCarEntities() {
        return Stream.generate(TestCarFactory::getCarEntity)
                .limit(20)
                .collect(Collectors.toList());
    }

    public static CarCreateViewDto getCarCreateViewDto() {
        Set<Long> testDrivers = new HashSet<>();
        testDrivers.add(1L);
        testDrivers.add(2L);

        Set<Long> components = new HashSet<>();
        components.add(100L);
        components.add(101L);

        return CarCreateViewDto.builder()
                .mainDriver(1L)
                .carMake("Toyota")
                .testDrivers(testDrivers)
                .components(components)
                .build();
    }
}
