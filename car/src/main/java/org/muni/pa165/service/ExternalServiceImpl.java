package org.muni.pa165.service;

import org.muni.pa165.api.Component;
import org.muni.pa165.api.Driver;
import org.muni.pa165.exceptions.ExternalCallException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalServiceImpl implements ExternalService {

    private static final String DRIVER_URL = "http://driver-service:8082/drivers/";
    private static final String COMPONENT_URL = "http://component-service:8083/components/";

    private final RestTemplate restTemplate;

    public ExternalServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean componentExists(Long id) {
        try {
            ResponseEntity<Component> response = restTemplate.getForEntity(
                    COMPONENT_URL + id, Component.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (ResourceAccessException e) {
            throw new ExternalCallException("Error while calling external service: " + e.toString());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean driverExists(Long id) {
        try {
            ResponseEntity<Driver> response = restTemplate.getForEntity(
                    DRIVER_URL + id, Driver.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (ResourceAccessException e) {
            throw new ExternalCallException("Error while calling external service: " + e.toString());
        } catch (Exception e) {
            return false;
        }
    }
}