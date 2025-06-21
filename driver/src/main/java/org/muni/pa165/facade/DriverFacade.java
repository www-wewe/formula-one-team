package org.muni.pa165.facade;

import org.muni.pa165.api.DriverCreateViewDto;
import org.muni.pa165.api.DriverViewDto;
import org.muni.pa165.data.domain.Driver;
import org.muni.pa165.data.domain.DriverPerk;
import org.muni.pa165.mapper.DriverMapper;
import org.muni.pa165.service.DriverService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverFacade {

    private final DriverService driverService;
    private final DriverMapper driverMapper;

    public DriverFacade(DriverService driverService, DriverMapper driverMapper) {
        this.driverService = driverService;
        this.driverMapper = driverMapper;
    }

    public DriverViewDto findById(Long id) {
        return driverMapper.toDriverViewDto(driverService.findById(id));
    }

    public DriverViewDto save(DriverCreateViewDto driverCreateViewDto) {
        return driverMapper.toDriverViewDto(driverService.save(driverMapper.fromDriverCreateViewDto(driverCreateViewDto)));
    }

    public void deleteById(Long id) {
        driverService.deleteById(id);
    }

    public void deleteAll() {
        driverService.deleteAll();
    }

    public void update(Driver driver) {
        driverService.update(driver);
    }

    public List<DriverViewDto> findAll() {
        return driverService.findAll().stream()
                .map(driverMapper::toDriverViewDto)
                .toList();
    }

    public List<DriverViewDto> findByPerk(DriverPerk perk) {
        return driverService.findByPerk(perk).stream()
                .map(driverMapper::toDriverViewDto)
                .toList();
    }

    public List<DriverViewDto> findByNationality(String nationality) {
        return driverService.findByNationality(nationality).stream()
                .map(driverMapper::toDriverViewDto)
                .toList();
    }
}
