package org.muni.pa165.service;

import jakarta.transaction.Transactional;
import org.muni.pa165.data.domain.Driver;
import org.muni.pa165.data.domain.DriverPerk;
import org.muni.pa165.data.repository.DriverRepository;
import org.muni.pa165.exceptions.DriverNotFoundException;
import org.muni.pa165.exceptions.DriverValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    @Autowired
    public DriverServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public Driver findById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found with id: " + id));
    }

    @Override
    public Driver save(Driver driver) {
        validateDriver(driver);
        return driverRepository.save(driver);
    }

    @Override
    public void deleteById(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new DriverNotFoundException("Driver not found with id: " + id + " for deletion");
        }
        driverRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        driverRepository.deleteAll();
    }

    @Override
    public List<Driver> findAll() {
        return driverRepository.findAll();
    }

    @Override
    public void update(Driver driver) {
        validateDriver(driver);
        if (!driverRepository.existsById(driver.getId())) {
            throw new DriverNotFoundException("Driver not found with id: " + driver.getId() + " for update");
        }
        driverRepository.save(driver);
    }

    @Override
    public List<Driver> findByPerk(DriverPerk perk) {
        return driverRepository.findByPerk(perk);
    }

    @Override
    public List<Driver> findByNationality(String nationality) {
        return driverRepository.findByNationality(nationality);
    }

    private void validateDriver(Driver driver) {
        if (driver.getName() == null || driver.getName().isEmpty()) {
            throw new DriverValidationException("Driver name cannot be empty");
        }
        if (driver.getSurname() == null || driver.getSurname().isEmpty()) {
            throw new DriverValidationException("Driver Surname cannot be empty");
        }
        if (driver.getNationality() == null || driver.getNationality().isEmpty()) {
            throw new DriverValidationException("Driver Country cannot be empty");
        }
        if (driver.getPerk() == null) {
            throw new DriverValidationException("Driver perk cannot be empty");
        }
    }
}
