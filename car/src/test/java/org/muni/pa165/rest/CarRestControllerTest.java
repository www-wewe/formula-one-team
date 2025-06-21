package org.muni.pa165.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.muni.pa165.api.CarCreateViewDto;
import org.muni.pa165.api.CarViewDto;
import org.muni.pa165.data.domain.Car;
import org.muni.pa165.exceptions.CarNotFoundException;
import org.muni.pa165.exceptions.DataStorageException;
import org.muni.pa165.exceptions.ExternalCallException;
import org.muni.pa165.facade.CarFacade;
import org.muni.pa165.rest.exceptionhandling.ApiError;
import org.muni.pa165.rest.exceptionhandling.CustomRestGlobalExceptionHandling;
import org.muni.pa165.utils.TestCarFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarRestControllerTest {

    @Mock
    UriBuilderService uriBuilderService;
    @Mock
    private CarFacade carFacade;
    @InjectMocks
    private CarRestController carRestController;
    private CarViewDto carViewDto;
    private CarCreateViewDto carCreateViewDto;
    private Car carEntity;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private CustomRestGlobalExceptionHandling exceptionHandler;

    @BeforeEach
    void setUp() {
        carViewDto = TestCarFactory.getCarViewDto();
        carCreateViewDto = TestCarFactory.getCarCreateViewDto();
        carEntity = TestCarFactory.getCarEntity();
    }

    @Test
    void findCarById_whenFound_returnsCar() {
        when(carFacade.findById(anyLong())).thenReturn(carViewDto);

        ResponseEntity<CarViewDto> response = carRestController.findCarById(1L);

        assertThat(response.getStatusCode()).as("Verify status code is OK when car is found by ID").isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).as("Ensure the body matches the expected car when found by ID").isEqualTo(carViewDto);
    }

    @Test
    void saveCar_validData_createsCar() throws URISyntaxException {
        when(carFacade.create(any(CarCreateViewDto.class))).thenReturn(carViewDto);
        when(uriBuilderService.getLocationOfCreatedResource(carViewDto)).thenReturn(new URI("http://localhost:8084/cars/1"));

        ResponseEntity<CarViewDto> response = carRestController.saveCar(carCreateViewDto);

        assertThat(response.getStatusCode()).as("Verify status code is CREATED (201) when car is successfully created").isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).as("Ensure the body matches the expected car after successful creation").isEqualTo(carViewDto);
    }

    @Test
    void deleteCarById_whenFound_deletesCar() {
        ResponseEntity<?> response = carRestController.deleteCarById(1L);

        assertThat(response.getStatusCode()).as("Verify status code is OK when car is found and deleted by ID").isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteAllCars_deletesAll() {
        doNothing().when(carFacade).deleteAll();

        ResponseEntity<?> response = carRestController.deleteAllCars();

        assertThat(response.getStatusCode()).as("Verify status code is OK when all cars are deleted").isEqualTo(HttpStatus.OK);
    }

    @Test
    void findAllCars_returnsAllCars() {
        List<CarViewDto> cars = Collections.singletonList(carViewDto);
        when(carFacade.findAll()).thenReturn(cars);

        ResponseEntity<List<CarViewDto>> response = carRestController.findAllCars();

        assertThat(response.getStatusCode()).as("Verify status code is OK when retrieving all cars").isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).as("Ensure the body matches the expected list of cars when all are found").isEqualTo(cars);
    }

    @Test
    void updateCar_whenFound_updatesCar() {
        doNothing().when(carFacade).update(carEntity);

        ResponseEntity<?> response = carRestController.updateCar(carEntity);

        assertThat(response.getStatusCode()).as("Verify status code is OK when a car is found and updated").isEqualTo(HttpStatus.OK);
    }

    @Test
    void findCarsByCarMake_whenCarsFound_returnsCars() {
        List<CarViewDto> cars = Collections.singletonList(carViewDto);
        when(carFacade.findByCarMake("Toyota")).thenReturn(cars);

        ResponseEntity<List<CarViewDto>> response = carRestController.findCarsByCarMake("Toyota");

        assertThat(response.getStatusCode()).as("Verify status code is OK when cars are found by make").isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).as("Ensure the body matches the expected list of cars").isEqualTo(cars);
    }

    @Test
    void findCarsMainDriver_whenCarsFound_returnsCars() {
        List<CarViewDto> cars = Collections.singletonList(carViewDto);
        when(carFacade.findByMainDriver(1L)).thenReturn(cars);

        ResponseEntity<List<CarViewDto>> response = carRestController.findCarsMainDriver(1L);

        assertThat(response.getStatusCode()).as("Verify status code is OK when cars are found by main driver ID").isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).as("Ensure the body matches the expected list of cars").isEqualTo(cars);
    }

    @Test
    public void handleResourceNotFound_returnsNotFound() {
        CarNotFoundException exception = new CarNotFoundException("Car not found");
        when(request.getRequestURI()).thenReturn("/cars/1");

        ResponseEntity<ApiError> response = exceptionHandler.handleResourceNotFound(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Car not found", response.getBody().getMessage());
    }

    @Test
    public void handleExternalCallException_returnsServiceUnavailable() {
        ExternalCallException exception = new ExternalCallException("External call error");
        when(request.getRequestURI()).thenReturn("/cars");

        ResponseEntity<ApiError> response = exceptionHandler.handleExternalCallException(exception, request);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("External call error", response.getBody().getMessage());
    }

    @Test
    void handleDataStorageException() {
        DataStorageException exception = new DataStorageException("Data storage error");
        when(request.getRequestURI()).thenReturn("/car/-1");

        ResponseEntity<ApiError> response = exceptionHandler.handleDataStorageException(exception, request);

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        assertEquals("Data storage error", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals("/car/-1", response.getBody().getPath());
    }

    @Test
    public void handleAll_returnsInternalServerError() {
        Exception exception = new Exception("General error");
        when(request.getRequestURI()).thenReturn("/cars");

        ResponseEntity<ApiError> response = exceptionHandler.handleAll(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("General error", response.getBody().getMessage());
    }
}
