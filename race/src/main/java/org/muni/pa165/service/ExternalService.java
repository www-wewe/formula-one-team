package org.muni.pa165.service;

import org.muni.pa165.api.Car;
import org.muni.pa165.api.Component;
import org.muni.pa165.api.Driver;
import org.springframework.http.ResponseEntity;

public interface ExternalService {

    boolean carExists(Long id);

    ResponseEntity<Car> getCar(Long id);

    ResponseEntity<Driver> getDriver(Long id);

    ResponseEntity<Component> getComponent(Long id);
}
