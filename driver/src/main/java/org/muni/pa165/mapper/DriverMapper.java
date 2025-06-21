package org.muni.pa165.mapper;

import org.mapstruct.Mapper;
import org.muni.pa165.api.DriverCreateViewDto;
import org.muni.pa165.api.DriverViewDto;
import org.muni.pa165.data.domain.Driver;

@Mapper(componentModel = "spring")
public interface DriverMapper {

    DriverViewDto toDriverViewDto(Driver driver);

    Driver fromDriverViewDto(DriverViewDto driverDto);

    DriverCreateViewDto toDriverCreateViewDto(Driver driver);

    Driver fromDriverCreateViewDto(DriverCreateViewDto driverDto);
}
