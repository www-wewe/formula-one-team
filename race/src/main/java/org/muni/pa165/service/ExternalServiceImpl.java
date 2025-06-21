package org.muni.pa165.service;

import org.muni.pa165.api.Car;
import org.muni.pa165.api.Component;
import org.muni.pa165.api.Driver;
import org.muni.pa165.exceptions.ExternalCallException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalServiceImpl implements ExternalService {

    private static final String CAR_URL = "http://car-service:8084/cars/";
    private static final String DRIVER_URL = "http://driver-service:8082/drivers/";
    private static final String COMPONENT_URL = "http://component-service:8083/components/";
    private final RestTemplate restTemplate;

    public ExternalServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean carExists(Long id) {
        try {
            ResponseEntity<Car> carResponseEntity = getCar(id);
            return carResponseEntity.getStatusCode().is2xxSuccessful();
        } catch (ResourceAccessException e) {
            throw new ExternalCallException("Error while calling external service: " + e);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public ResponseEntity<Car> getCar(Long id) {
        try {
            return restTemplate.getForEntity(
                    CAR_URL + id, Car.class);
        } catch (ResourceAccessException e) {
            throw new ExternalCallException("Error while calling external service: " + e);
        }
    }

    @Override
    public ResponseEntity<Driver> getDriver(Long id) {
        try {
            return restTemplate.getForEntity(
                    DRIVER_URL + id, Driver.class);
        } catch (ResourceAccessException e) {
            throw new ExternalCallException("Error while calling external service: " + e);
        }
    }

    @Override
    public ResponseEntity<Component> getComponent(Long id) {
        return restTemplate.getForEntity(
                COMPONENT_URL + id, Component.class);
    }
}
