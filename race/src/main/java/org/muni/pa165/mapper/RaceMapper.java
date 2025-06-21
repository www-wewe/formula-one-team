package org.muni.pa165.mapper;

import org.mapstruct.Mapper;
import org.muni.pa165.api.RaceCreateViewDto;
import org.muni.pa165.api.RaceViewDto;
import org.muni.pa165.data.domain.Race;

@Mapper(componentModel = "spring")
public interface RaceMapper {

    RaceViewDto toRaceViewDto(Race entity);

    Race fromRaceViewDto(RaceViewDto dto);

    RaceCreateViewDto toRaceCreateViewDto(Race entity);

    Race fromRaceCreateViewDto(RaceCreateViewDto dto);
}
