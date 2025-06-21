package org.muni.pa165.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.muni.pa165.api.DriverCreateViewDto;
import org.muni.pa165.api.DriverViewDto;
import org.muni.pa165.data.domain.Driver;
import org.muni.pa165.data.domain.DriverPerk;
import org.muni.pa165.exceptions.DriverNotFoundException;
import org.muni.pa165.exceptions.DriverValidationException;
import org.muni.pa165.facade.DriverFacade;
import org.muni.pa165.rest.exceptionhandling.ApiError;
import org.muni.pa165.rest.exceptionhandling.CustomRestGlobalExceptionHandling;
import org.muni.pa165.utils.TestDriverFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DriverRestControllerTest {

    @Mock
    UriBuilderService uriBuilderService;
    @Mock
    private DriverFacade driverFacade;
    @InjectMocks
    private DriverRestController driverRestController;
    private DriverViewDto driverViewDto;
    private DriverCreateViewDto driverCreateViewDto;
    private Driver driverEntity;

    private CustomRestGlobalExceptionHandling exceptionHandling;

    @Mock
    private HttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        driverViewDto = TestDriverFactory.getDriverViewDto();
        driverCreateViewDto = TestDriverFactory.getDriverCreateViewDto();
        driverEntity = TestDriverFactory.getDriverEntity();
        exceptionHandling = new CustomRestGlobalExceptionHandling();
    }

    @Test
    void findDriverById_whenFound_returnsDriver() {
        when(driverFacade.findById(driverEntity.getId())).thenReturn(driverViewDto);

        ResponseEntity<DriverViewDto> response = driverRestController.findDriverById(driverEntity.getId());

        assertThat(response.getStatusCode()).as("Verify status code is OK when driver is found by ID").isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).as("Ensure the body matches the expected driver when found by ID").isEqualTo(driverViewDto);
    }

    @Test
    void saveDriver_validData_createsDriver() throws URISyntaxException {
        when(driverFacade.save(any(DriverCreateViewDto.class))).thenReturn(driverViewDto);
        when(uriBuilderService.getLocationOfCreatedResource(driverViewDto)).thenReturn(new URI("http://localhost:8084/drivers/1"));

        ResponseEntity<?> response = driverRestController.saveDriver(driverCreateViewDto);

        assertThat(response.getStatusCode()).as("Verify status code is CREATED when driver is successfully created").isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).as("Ensure the body matches the expected driver after successful creation").isEqualTo(driverViewDto);
    }

    @Test
    void deleteDriverById_whenFound_deletesDriver() {
        doNothing().when(driverFacade).deleteById(anyLong());

        ResponseEntity<?> response = driverRestController.deleteDriverById(1L);

        assertThat(response.getStatusCode()).as("Verify status code is OK when driver is successfully deleted").isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteAllDrivers_deletesAll() {
        doNothing().when(driverFacade).deleteAll();

        ResponseEntity<?> response = driverRestController.deleteAllDrivers();

        assertThat(response.getStatusCode()).as("Verify status code is OK when all drivers are deleted").isEqualTo(HttpStatus.OK);
    }

    @Test
    void findAllDrivers_returnsAllDrivers() {
        List<DriverViewDto> drivers = Collections.singletonList(driverViewDto);
        when(driverFacade.findAll()).thenReturn(drivers);

        ResponseEntity<?> response = driverRestController.findAllDrivers();

        assertThat(response.getStatusCode()).as("Verify status code is OK when retrieving all drivers").isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).as("Ensure the body matches the expected list of drivers when all are found").isEqualTo(drivers);
    }

    @Test
    void updateDriver_whenFound_updatesDriver() {
        doNothing().when(driverFacade).update(any(Driver.class));

        ResponseEntity<?> response = driverRestController.updateDriver(driverEntity);

        assertThat(response.getStatusCode()).as("Verify status code is OK when the driver is updated").isEqualTo(HttpStatus.OK);
    }

    @Test
    void findDriversByPerk_whenFound_returnsDrivers() {
        List<DriverViewDto> drivers = Collections.singletonList(driverViewDto);
        when(driverFacade.findByPerk(any(DriverPerk.class))).thenReturn(drivers);

        ResponseEntity<List<DriverViewDto>> response = driverRestController.findDriversByPerk(DriverPerk.OVERTAKER);

        assertThat(response.getStatusCode()).as("Verify status code is OK when drivers are found for a given perk").isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).as("Ensure the body matches the expected list of drivers found by perk").isEqualTo(drivers);
    }

    @Test
    void findDriversByDriverNationality_whenDriversFound_returnsDrivers() {
        List<DriverViewDto> drivers = Collections.singletonList(driverViewDto);
        when(driverFacade.findByNationality("Netherlands")).thenReturn(drivers);

        ResponseEntity<List<DriverViewDto>> response = driverRestController.findDriversByNationality("Netherlands");

        assertThat(response.getStatusCode()).as("Verify status code is OK when drivers are found by nationality").isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).as("Ensure the body matches the expected list of drivers").isEqualTo(drivers);
    }

    @Test
    void handleDriverNotFoundException() {
        DriverNotFoundException exception = new DriverNotFoundException("Driver not found");
        when(mockRequest.getRequestURI()).thenReturn("/drivers/1");

        ResponseEntity<ApiError> response = exceptionHandling.handleResourceNotFound(exception, mockRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Driver not found", response.getBody().getMessage());
        assertEquals("/drivers/1", response.getBody().getPath());
    }

    @Test
    void handleDriverValidationException() {
        DriverValidationException exception = new DriverValidationException("Invalid driver data");
        when(mockRequest.getRequestURI()).thenReturn("/drivers");

        ResponseEntity<ApiError> response = exceptionHandling.handleValidation(exception, mockRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid driver data", response.getBody().getMessage());
        assertEquals("/drivers", response.getBody().getPath());
    }

    @Test
    void handleGeneralException() {
        Exception exception = new Exception("Internal server error");
        when(mockRequest.getRequestURI()).thenReturn("/drivers");

        ResponseEntity<ApiError> response = exceptionHandling.handleAll(exception, mockRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody().getMessage());
        assertEquals("/drivers", response.getBody().getPath());
    }
}
