package org.muni.pa165.service;

import jakarta.transaction.Transactional;
import org.muni.pa165.api.Car;
import org.muni.pa165.api.CarView;
import org.muni.pa165.api.Component;
import org.muni.pa165.api.ComponentView;
import org.muni.pa165.api.Driver;
import org.muni.pa165.api.DriverView;
import org.muni.pa165.api.RaceView;
import org.muni.pa165.data.domain.Location;
import org.muni.pa165.data.domain.Race;
import org.muni.pa165.data.repository.RaceRepository;
import org.muni.pa165.exceptions.DataStorageException;
import org.muni.pa165.exceptions.RaceNotFoundException;
import org.muni.pa165.exceptions.RaceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RaceServiceImpl implements RaceService {

    private final RaceRepository raceRepository;
    private final ExternalService externalService;

    @Autowired
    public RaceServiceImpl(RaceRepository raceRepository, ExternalService externalService) {
        this.raceRepository = raceRepository;
        this.externalService = externalService;
    }

    @Override
    public Race save(Race race) {
        validateRace(race);
        return raceRepository.save(race);
    }

    @Override
    public Race findById(Long id) {
        return raceRepository.findById(id)
                .orElseThrow(() -> new RaceNotFoundException("Race not found with id: " + id));
    }

    @Override
    public List<Race> findAll() {
        return raceRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        if (!raceRepository.existsById(id)) {
            throw new RaceNotFoundException("Cannot delete, race not found with id: " + id);
        }
        raceRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        raceRepository.deleteAll();
    }

    @Override
    public void update(Race race) {
        validateRace(race);
        if (!raceRepository.existsById(race.getId())) {
            throw new RaceNotFoundException("Cannot update, race not found with id: " + race.getId());
        }
        raceRepository.save(race);
    }

    @Override
    public void assignCarOne(Long raceId, Long carId) {
        Race race = findById(raceId);
        if (!externalService.carExists(carId)) {
            throw new DataStorageException("Car does not exist");
        }
        race.setCar1Id(carId);
        raceRepository.save(race);
    }

    @Override
    public void assignCarTwo(Long raceId, Long carId) {
        Race race = findById(raceId);
        if (!externalService.carExists(carId)) {
            throw new DataStorageException("Car does not exist");
        }
        race.setCar2Id(carId);
        raceRepository.save(race);
    }

    @Override
    public List<Race> findByLocation_CountryOrLocation_CityOrLocation_Street(String country, String city, String street) {
        return raceRepository.findByLocation_CountryOrLocation_CityOrLocation_Street(country, city, street);
    }

    @Override
    public List<Race> findByCarId(Long carId) {
        return raceRepository.findCarById(carId);
    }

    public CarView getCarView(Long id) {
        Set<DriverView> testDrivers = new HashSet<>();
        Set<ComponentView> components = new HashSet<>();
        DriverView mainDriverView = null;
        CarView carView;
        Car car;
        Driver driver;

        car = externalService.getCar(id).getBody();
        if (car.getMainDriver() != null) {
            driver = externalService.getDriver(car.getMainDriver()).getBody();
            mainDriverView = new DriverView(driver.getName(), driver.getSurname(),
                    driver.getNationality(), driver.getPerk());
        }

        if (car.getTestDrivers() != null) {
            car.getTestDrivers().forEach(testDriverId -> {
                Driver testDriver = externalService.getDriver(testDriverId).getBody();
                DriverView driverView = new DriverView(testDriver.getName(), testDriver.getSurname(),
                        testDriver.getNationality(), testDriver.getPerk());
                testDrivers.add(driverView);
            });
        }

        car.getComponents().forEach(componentId -> {
            Component component = externalService.getComponent(componentId).getBody();
            ComponentView componentView = new ComponentView(component.getWeight(), component.getPrice(),
                    component.getManufacturer(), component.getVersion(), component.getType());
            components.add(componentView);
        });

        carView = new CarView(mainDriverView, car.getCarMake(), testDrivers, components);
        return carView;
    }

    @Override
    public List<RaceView> findAllWithCars() {
        List<Race> races = findAll();
        List<RaceView> raceViews = new ArrayList<>();
        for (Race race : races) {
            RaceView raceView = new RaceView(race.getName(), race.getLocation(), race.getDate(), null, null);
            if (race.getCar1Id() != null) {
                raceView.setCar1(getCarView(race.getCar1Id()));
            }
            if (race.getCar2Id() != null) {
                raceView.setCar2(getCarView(race.getCar2Id()));
            }
            raceViews.add(raceView);
        }
        return raceViews;
    }

    private void validateRace(Race race) {
        if (race.getCar1Id() != null) {
            if (!externalService.carExists(race.getCar1Id())) {
                throw new DataStorageException("Car with id " + race.getCar1Id() + " does not exist");
            }
        }

        if (race.getCar2Id() != null) {
            if (!externalService.carExists(race.getCar2Id())) {
                throw new DataStorageException("Car with id " + race.getCar2Id() + " does not exist");
            }
        }

        if (race.getDate() == null) {
            throw new RaceValidationException("Race date cannot be empty");
        }

        if (race.getName() == null || race.getName().isEmpty()) {
            throw new RaceValidationException("Race name cannot be empty");
        }

        if (race.getLocation() == null) {
            throw new RaceValidationException("Race location cannot be empty");
        } else {
            Location location = race.getLocation();
            if (location.getCity() == null || location.getCity().isEmpty() ||
                    location.getCountry() == null || location.getCountry().isEmpty() ||
                    location.getStreet() == null || location.getStreet().isEmpty()) {
                throw new RaceValidationException("Race location data cannot be empty");
            }
        }
    }
}
