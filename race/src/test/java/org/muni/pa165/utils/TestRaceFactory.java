package org.muni.pa165.utils;

import org.muni.pa165.api.Car;
import org.muni.pa165.api.CarView;
import org.muni.pa165.api.Component;
import org.muni.pa165.api.ComponentView;
import org.muni.pa165.api.Driver;
import org.muni.pa165.api.DriverView;
import org.muni.pa165.api.RaceCreateViewDto;
import org.muni.pa165.api.RaceView;
import org.muni.pa165.api.RaceViewDto;
import org.muni.pa165.data.domain.Location;
import org.muni.pa165.data.domain.Race;
import org.muni.pa165.data.enums.ComponentType;
import org.muni.pa165.data.enums.DriverPerk;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestRaceFactory {

    public static RaceViewDto getRaceViewDto() {
        return RaceViewDto.builder()
                .id(1L)
                .name("Monaco Grand Prix")
                .location(new Location("Monaco", "Monte Carlo", "Circuit de Monaco"))
                .date(LocalDate.of(2022, 12, 24))
                .car1Id(1L)
                .car2Id(2L)
                .build();
    }

    public static Race getRaceEntity() {
        return Race.builder()
                .id(1L)
                .name("Monaco Grand Prix")
                .location(new Location("Monaco", "Monte Carlo", "Circuit de Monaco"))
                .date(LocalDate.of(2022, 12, 24))
                .car1Id(1L)
                .car2Id(2L)
                .build();
    }

    public static List<Race> getListOfRaceEntities() {
        return Stream.generate(TestRaceFactory::getRaceEntity)
                .limit(5)
                .collect(Collectors.toList());
    }

    public static List<RaceViewDto> getListOfRaceViewDto() {
        return Stream.generate(TestRaceFactory::getRaceViewDto)
                .limit(5)
                .collect(Collectors.toList());
    }

    public static RaceCreateViewDto getRaceCreateViewDto() {
        return RaceCreateViewDto.builder()
                .name("Monaco Grand Prix")
                .location(new Location("Monaco", "Monte Carlo", "Circuit de Monaco"))
                .date(LocalDate.of(2022, 12, 24))
                .car1Id(1L)
                .car2Id(2L)
                .build();
    }

    public static List<RaceView> getListOfRaceViews() {
        return Stream.generate(TestRaceFactory::getRaceView)
                .limit(5)
                .collect(Collectors.toList());
    }

    public static RaceView getRaceView() {
        return RaceView.builder()
                .name("Monaco Grand Prix")
                .location(new Location("Monaco", "Monte Carlo", "Circuit de Monaco"))
                .date(LocalDate.of(2022, 12, 24))
                .car1(CarView.builder()
                        .carMake("Mercedes")
                        .mainDriver(DriverView.builder()
                                .name("Lewis")
                                .surname("Hamilton")
                                .nationality("British")
                                .perk(DriverPerk.FUEL_SAVING)
                                .build())
                        .testDrivers(Set.of(DriverView.builder()
                                .name("Lewis")
                                .surname("Hamilton")
                                .nationality("British")
                                .perk(DriverPerk.FUEL_SAVING)
                                .build()))
                        .components(Set.of(ComponentView.builder()
                                .manufacturer("Mercedes")
                                .price(1000)
                                .type(ComponentType.ENGINE)
                                .version("1.0")
                                .weight(1000)
                                .build()))
                        .build())
                .car2(CarView.builder()
                        .carMake("Mercedes")
                        .mainDriver(DriverView.builder()
                                .name("Lewis")
                                .surname("Hamilton")
                                .nationality("British")
                                .perk(DriverPerk.FUEL_SAVING)
                                .build())
                        .testDrivers(Set.of(DriverView.builder()
                                .name("Lewis")
                                .surname("Hamilton")
                                .nationality("British")
                                .perk(DriverPerk.FUEL_SAVING)
                                .build()))
                        .components(Set.of(ComponentView.builder()
                                .manufacturer("Mercedes")
                                .price(1000)
                                .type(ComponentType.ENGINE)
                                .version("1.0")
                                .weight(1000)
                                .build()))
                        .build())
                .build();
    }

    public static Car getCarEntity() {
        return Car.builder()
                .id(1L)
                .carMake("Mercedes")
                .mainDriver(1L)
                .testDrivers(Set.of(1L))
                .components(Set.of(1L))
                .build();
    }

    public static Driver getDriverEntity() {
        return Driver.builder()
                .id(1L)
                .name("Lewis")
                .surname("Hamilton")
                .nationality("British")
                .perk(DriverPerk.FUEL_SAVING)
                .build();
    }

    public static Component getComponentEntity() {
        return Component.builder()
                .id(1L)
                .manufacturer("Mercedes")
                .price(1000)
                .type(ComponentType.ENGINE)
                .version("1.0")
                .weight(1000)
                .build();
    }
}
