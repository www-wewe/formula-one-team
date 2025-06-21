package org.muni.pa165.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.muni.pa165.api.RaceCreateViewDto;
import org.muni.pa165.api.RaceView;
import org.muni.pa165.api.RaceViewDto;
import org.muni.pa165.data.domain.Race;
import org.muni.pa165.exceptions.DataStorageException;
import org.muni.pa165.exceptions.ExternalCallException;
import org.muni.pa165.exceptions.RaceNotFoundException;
import org.muni.pa165.exceptions.RaceValidationException;
import org.muni.pa165.facade.RaceFacade;
import org.muni.pa165.rest.exceptionhandling.ApiError;
import org.muni.pa165.rest.exceptionhandling.CustomRestGlobalExceptionHandling;
import org.muni.pa165.utils.TestRaceFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RaceRestControllerTest {

    @Mock
    UriBuilderService uriBuilderService;
    @Mock
    private RaceFacade raceFacade;
    @InjectMocks
    private RaceRestController raceRestController;
    private RaceViewDto raceViewDto;
    private RaceCreateViewDto raceCreateViewDto;
    private Race raceEntity;

    private CustomRestGlobalExceptionHandling exceptionHandler;
    @Mock
    private HttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        raceViewDto = TestRaceFactory.getRaceViewDto();
        raceCreateViewDto = TestRaceFactory.getRaceCreateViewDto();
        raceEntity = TestRaceFactory.getRaceEntity();
        exceptionHandler = new CustomRestGlobalExceptionHandling();
    }

    @Test
    void saveRace_validData_createsRace() throws URISyntaxException {
        when(raceFacade.save(any(RaceCreateViewDto.class))).thenReturn(raceViewDto);
        when(uriBuilderService.getLocationOfCreatedResource(raceViewDto)).thenReturn(new URI("http://localhost:8084/races/1"));

        ResponseEntity<?> response = raceRestController.saveRace(raceCreateViewDto);

        assertThat(response.getStatusCode()).as("Verify status code is CREATED when race is successfully created").isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).as("Ensure the body matches the expected race after successful creation").isEqualTo(raceViewDto);
    }

    @Test
    void findById_whenFound_returnsRace() {
        when(raceFacade.findById(anyLong())).thenReturn(raceViewDto);

        ResponseEntity<RaceViewDto> response = raceRestController.findById(1L);

        assertThat(response.getStatusCode()).as("Verify status code is OK when race is found by ID").isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).as("Ensure the body matches the expected race when found by ID").isEqualTo(raceViewDto);
    }

    @Test
    void findAllRaces_returnsAllRaces() {
        List<RaceViewDto> races = Collections.singletonList(raceViewDto);
        when(raceFacade.findAll()).thenReturn(races);

        ResponseEntity<?> response = raceRestController.findAllRaces();

        assertThat(response.getStatusCode()).as("Verify status code is OK when retrieving all races").isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).as("Ensure the body matches the expected list of races when all are found").isEqualTo(races);
    }

    @Test
    void deleteRaceById_whenFound_deletesRace() {
        doNothing().when(raceFacade).deleteById(anyLong());

        ResponseEntity<?> response = raceRestController.deleteRaceById(1L);

        assertThat(response.getStatusCode()).as("Verify status code is OK when race is found and deleted by ID").isEqualTo(HttpStatus.OK);
    }

    @Test
    void updateRace_whenFound_updatesRace() {
        doNothing().when(raceFacade).update(any(Race.class));

        ResponseEntity<?> response = raceRestController.updateRace(raceEntity);

        assertThat(response.getStatusCode()).as("Verify status code is OK when a race is found and updated").isEqualTo(HttpStatus.OK);
    }

    @Test
    void assignCarOne_whenRaceExists_assignsCar() {
        doNothing().when(raceFacade).assignCarOne(anyLong(), anyLong());

        ResponseEntity<?> response = raceRestController.assignCarOne(1L, 1L);

        assertThat(response.getStatusCode()).as("Verify status code is OK when first car is assigned to the race").isEqualTo(HttpStatus.OK);
    }

    @Test
    void assignCarTwo_whenRaceExists_assignsCar() {
        doNothing().when(raceFacade).assignCarTwo(anyLong(), anyLong());

        ResponseEntity<?> response = raceRestController.assignCarTwo(1L, 2L);

        assertThat(response.getStatusCode()).as("Verify status code is OK when second car is assigned to the race").isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteAllRaces_deletesAll() {
        doNothing().when(raceFacade).deleteAll();

        ResponseEntity<?> response = raceRestController.deleteAllRaces();

        assertThat(response.getStatusCode()).as("Verify status code is OK when all races are deleted").isEqualTo(HttpStatus.OK);
    }

    @Test
    void findByLocation_whenFound_returnsRaces() {
        List<RaceViewDto> races = Collections.singletonList(raceViewDto);
        when(raceFacade.findByLocation_CountryOrLocation_CityOrLocation_Street(any(), any(), any())).thenReturn(races);

        ResponseEntity<?> response = raceRestController.findByLocation("Brno", "Czech Republic", "Masaryk Circuit");

        assertThat(response.getStatusCode()).as("Verify status code is OK when races are found by location").isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).as("Ensure the body matches the expected list of races found by location").isEqualTo(races);
    }

    @Test
    void findByCarId_whenFound_returnsRaces() {
        List<RaceViewDto> races = Collections.singletonList(raceViewDto);
        when(raceFacade.findByCarId(anyLong())).thenReturn(races);

        ResponseEntity<?> response = raceRestController.findByCarId(1L);

        assertThat(response.getStatusCode()).as("Verify status code is OK when races are found by car ID").isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).as("Ensure the body matches the expected list of races found by car ID").isEqualTo(races);
    }

    @Test
    void handleRaceNotFoundException() {
        RaceNotFoundException exception = new RaceNotFoundException("Race not found");
        when(mockRequest.getRequestURI()).thenReturn("/races/-1");

        ResponseEntity<ApiError> response = exceptionHandler.handleResourceNotFound(exception, mockRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Race not found", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals("/races/-1", response.getBody().getPath());
    }

    @Test
    void handleRaceValidationException() {
        RaceValidationException exception = new RaceValidationException("Validation failed");
        when(mockRequest.getRequestURI()).thenReturn("/races/-1");

        ResponseEntity<ApiError> response = exceptionHandler.handleValidation(exception, mockRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals("/races/-1", response.getBody().getPath());
    }

    @Test
    void handleExternalCallException() {
        ExternalCallException exception = new ExternalCallException("External call error");
        when(mockRequest.getRequestURI()).thenReturn("/races/-1");

        ResponseEntity<ApiError> response = exceptionHandler.handleExternalCallException(exception, mockRequest);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("External call error", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals("/races/-1", response.getBody().getPath());
    }

    @Test
    void handleDataStorageException() {
        DataStorageException exception = new DataStorageException("Data storage error");
        when(mockRequest.getRequestURI()).thenReturn("/races/-1");

        ResponseEntity<ApiError> response = exceptionHandler.handleDataStorageException(exception, mockRequest);

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        assertEquals("Data storage error", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals("/races/-1", response.getBody().getPath());
    }

    @Test
    void findAllWIthCars_returnsAllRacesWithCars() {
        List<RaceView> races = TestRaceFactory.getListOfRaceViews();
        when(raceFacade.findAllWithCars()).thenReturn(races);

        ResponseEntity<?> response = raceRestController.findAllWithCars();

        assertThat(response.getStatusCode()).as("Verify status code is OK when all races with cars are retrieved").isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).as("Ensure the body matches the expected list of races with cars").isEqualTo(races);
    }
}
