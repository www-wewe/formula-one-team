package org.muni.pa165.mapper;

import org.mapstruct.Mapper;
import org.muni.pa165.api.CarCreateViewDto;
import org.muni.pa165.api.CarViewDto;
import org.muni.pa165.data.domain.Car;

@Mapper(componentModel = "spring")
public interface CarMapper {

    CarViewDto toCarViewDto(Car entity);

    Car fromCarViewDto(CarViewDto dto);

    CarCreateViewDto toCarCreateViewDto(Car entity);

    Car fromCarCreateViewDto(CarCreateViewDto dto);
}
