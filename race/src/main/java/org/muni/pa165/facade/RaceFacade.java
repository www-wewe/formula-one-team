package org.muni.pa165.facade;

import org.muni.pa165.api.RaceCreateViewDto;
import org.muni.pa165.api.RaceView;
import org.muni.pa165.api.RaceViewDto;
import org.muni.pa165.data.domain.Race;
import org.muni.pa165.mapper.RaceMapper;
import org.muni.pa165.service.RaceService;
import org.muni.pa165.service.RaceServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RaceFacade {

    private final RaceService raceService;
    private final RaceMapper raceMapper;

    public RaceFacade(RaceServiceImpl raceServiceImpl, RaceMapper raceMapper) {
        this.raceService = raceServiceImpl;
        this.raceMapper = raceMapper;
    }

    public RaceViewDto save(RaceCreateViewDto raceCreateViewDto) {
        return raceMapper.toRaceViewDto(raceService.save(raceMapper.fromRaceCreateViewDto(raceCreateViewDto)));
    }

    public RaceViewDto findById(Long id) {
        return raceMapper.toRaceViewDto(raceService.findById(id));
    }

    public List<RaceViewDto> findAll() {
        return raceService.findAll().stream()
                .map(raceMapper::toRaceViewDto)
                .toList();
    }

    public void deleteById(Long id) {
        raceService.deleteById(id);
    }

    public void deleteAll() {
        raceService.deleteAll();
    }

    public void update(Race race) {
        raceService.update(race);
    }

    public void assignCarOne(Long raceId, Long carId) {
        raceService.assignCarOne(raceId, carId);
    }

    public void assignCarTwo(Long raceId, Long carId) {
        raceService.assignCarTwo(raceId, carId);
    }

    public List<RaceViewDto> findByLocation_CountryOrLocation_CityOrLocation_Street(String country, String city, String street) {
        return raceService.findByLocation_CountryOrLocation_CityOrLocation_Street(country, city, street).stream()
                .map(raceMapper::toRaceViewDto)
                .toList();
    }

    public List<RaceViewDto> findByCarId(Long carId) {
        return raceService.findByCarId(carId).stream()
                .map(raceMapper::toRaceViewDto)
                .toList();
    }

    public List<RaceView> findAllWithCars() {
        return raceService.findAllWithCars();
    }
}
