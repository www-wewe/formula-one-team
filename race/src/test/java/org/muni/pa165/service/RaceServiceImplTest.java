package org.muni.pa165.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.muni.pa165.api.RaceView;
import org.muni.pa165.data.domain.Race;
import org.muni.pa165.data.repository.RaceRepository;
import org.muni.pa165.exceptions.DataStorageException;
import org.muni.pa165.exceptions.RaceNotFoundException;
import org.muni.pa165.exceptions.RaceValidationException;
import org.muni.pa165.utils.TestRaceFactory;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RaceServiceImplTest {

    @Mock
    private RaceRepository raceRepository;

    @Mock
    private ExternalService externalService;

    @InjectMocks
    private RaceServiceImpl raceServiceImpl;

    private Race testRace;

    @BeforeEach
    void setUp() {
        testRace = TestRaceFactory.getRaceEntity();
    }

    @Test
    void saveRace_validRace_savesAndReturnsRace() {
        when(raceRepository.save(any(Race.class))).thenReturn(testRace);
        when(externalService.carExists(anyLong())).thenReturn(true);

        Race result = raceServiceImpl.save(testRace);

        verify(raceRepository).save(testRace);
        assertThat(result).as("Ensure the race saved is the same as the race returned").isSameAs(testRace);
    }

    @Test
    void saveRace_invalidRace_throwsException() {
        testRace.setName(null);
        when(externalService.carExists(anyLong())).thenReturn(true); // Invalid name

        assertThatThrownBy(() -> raceServiceImpl.save(testRace))
                .as("Expect RaceValidationException when race data is invalid")
                .isInstanceOf(RaceValidationException.class)
                .hasMessageContaining("Race name cannot be empty");
    }

    @Test
    void findRaceById_whenFound_returnsRace() {
        when(raceRepository.findById(testRace.getId())).thenReturn(Optional.of(testRace));

        Race result = raceServiceImpl.findById(testRace.getId());

        verify(raceRepository).findById(testRace.getId());
        assertThat(result).as("Verify that the race returned by findRaceById matches the expected race").isSameAs(testRace);
    }

    @Test
    void findRaceById_whenNotFound_throwsException() {
        when(raceRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> raceServiceImpl.findById(testRace.getId()))
                .as("Expect RaceNotFoundException when the race ID does not exist")
                .isInstanceOf(RaceNotFoundException.class)
                .hasMessageContaining("Race not found with id: 1");
    }

    @Test
    void findAllRaces_returnsAllRaces() {
        List<Race> expectedRaces = Arrays.asList(testRace, TestRaceFactory.getRaceEntity());
        when(raceRepository.findAll()).thenReturn(expectedRaces);

        List<Race> result = raceServiceImpl.findAll();

        verify(raceRepository).findAll();
        assertThat(result).as("Check if the list of all races matches the expected list").isEqualTo(expectedRaces);
    }

    @Test
    void findAllRaces_whenNoneFound_throwsException() {
        when(raceRepository.findAll()).thenReturn(Collections.emptyList());

        List<Race> result = raceServiceImpl.findAll();

        verify(raceRepository).findAll();
        assertThat(result).as("Check if the returned list is empty when no races are found").isEmpty();
    }

    @Test
    void deleteRaceById_deletesRace() {
        when(raceRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(raceRepository).deleteById(anyLong());

        raceServiceImpl.deleteById(testRace.getId());

        verify(raceRepository).deleteById(testRace.getId());
        assertThat(true).as("Ensure the method deleteRaceById effectively requests deletion of the race").isTrue();
    }

    @Test
    void deleteRaceById_whenNotFound_throwsException() {
        when(raceRepository.existsById(anyLong())).thenReturn(false);

        assertThatThrownBy(() -> raceServiceImpl.deleteById(testRace.getId()))
                .as("Expect RaceNotFoundException when trying to delete a component that does not exist")
                .isInstanceOf(RaceNotFoundException.class)
                .hasMessageContaining("Cannot delete, race not found with id: 1");
    }

    @Test
    void deleteAllRaces_deletesAllRaces() {
        doNothing().when(raceRepository).deleteAll();

        raceServiceImpl.deleteAll();

        verify(raceRepository).deleteAll();
        assertThat(true).as("Confirm that deleteAllRaces requests deletion of all races").isTrue();
    }

    @Test
    void updateRace_whenRaceExists_updatesRace() {
        when(raceRepository.existsById(testRace.getId())).thenReturn(true);
        when(raceRepository.save(any(Race.class))).thenReturn(testRace);
        when(externalService.carExists(anyLong())).thenReturn(true);

        raceServiceImpl.update(testRace);

        verify(raceRepository).save(testRace);
        assertThat(true).as("Verify that updateRace updates and saves the race").isTrue();
    }

    @Test
    void updateRace_whenRaceDoesNotExist_throwException() {
        when(raceRepository.existsById(testRace.getId())).thenReturn(false);
        when(externalService.carExists(anyLong())).thenReturn(true);

        assertThatThrownBy(() -> raceServiceImpl.update(testRace))
                .as("Expect RaceNotFoundException when trying to update a care that does not exist")
                .isInstanceOf(RaceNotFoundException.class)
                .hasMessageContaining("Cannot update, race not found with id: " + testRace.getId());
    }

    @Test
    void updateRace_whenRaceLocationIsEmpty_throwsException() {
        testRace.setLocation(null); // Empty location
        when(externalService.carExists(anyLong())).thenReturn(true);

        assertThatThrownBy(() -> raceServiceImpl.update(testRace))
                .as("Expect RaceValidationException when trying to update a race with an empty location")
                .isInstanceOf(RaceValidationException.class)
                .hasMessageContaining("Race location cannot be empty");
    }

    @Test
    void updateRace_whenRaceLocationCountryIsEmpty_throwsException() {
        testRace.getLocation().setCountry(""); // Empty country
        when(externalService.carExists(anyLong())).thenReturn(true);

        assertThatThrownBy(() -> raceServiceImpl.update(testRace))
                .as("Expect RaceValidationException when trying to update a race location with an empty country")
                .isInstanceOf(RaceValidationException.class)
                .hasMessageContaining("Race location data cannot be empty");
    }

    @Test
    void updateRace_whenRaceLocationCityIsEmpty_throwsException() {
        testRace.getLocation().setCity(""); // Empty city
        when(externalService.carExists(anyLong())).thenReturn(true);

        assertThatThrownBy(() -> raceServiceImpl.update(testRace))
                .as("Expect RaceValidationException when trying to update a race location with an empty city")
                .isInstanceOf(RaceValidationException.class)
                .hasMessageContaining("Race location data cannot be empty");
    }

    @Test
    void updateRace_whenRaceLocationStreetIsEmpty_throwsException() {
        testRace.getLocation().setStreet(""); // Empty street
        when(externalService.carExists(anyLong())).thenReturn(true);

        assertThatThrownBy(() -> raceServiceImpl.update(testRace))
                .as("Expect RaceValidationException when trying to update a race location with an empty street")
                .isInstanceOf(RaceValidationException.class)
                .hasMessageContaining("Race location data cannot be empty");
    }

    @Test
    void updateRace_whenRaceDateIsEmpty_throwsException() {
        testRace.setDate(null); // Empty date
        when(externalService.carExists(anyLong())).thenReturn(true);

        assertThatThrownBy(() -> raceServiceImpl.update(testRace))
                .as("Expect RaceValidationException when trying to update a race date that is empty")
                .isInstanceOf(RaceValidationException.class)
                .hasMessageContaining("Race date cannot be empty");
    }

    @Test
    void assignCarOne_assignsCarOneToRaceSuccessfully() {
        Long carId = 1L;
        when(raceRepository.findById(testRace.getId())).thenReturn(Optional.of(testRace));
        when(externalService.carExists(anyLong())).thenReturn(true);
        raceServiceImpl.assignCarOne(testRace.getId(), carId);

        verify(raceRepository).save(testRace);
        assertThat(testRace.getCar1Id()).as("Ensure car one is assigned to the race").isEqualTo(carId);
    }

    @Test
    void assignCarOne_whenRaceDoesNotExist_throwsException() {
        Long carId = 1L;

        assertThatThrownBy(() -> raceServiceImpl.assignCarOne(testRace.getId(), carId))
                .as("Expect RaceNotFoundException when trying to assign a car to race that does not exist")
                .isInstanceOf(RaceNotFoundException.class)
                .hasMessageContaining("Race not found with id: " + testRace.getId());
    }

    @Test
    void assignCarTwo_assignsCarTwoToRaceSuccessfully() {
        Long carId = 2L;
        when(raceRepository.findById(testRace.getId())).thenReturn(Optional.of(testRace));
        when(externalService.carExists(anyLong())).thenReturn(true);
        raceServiceImpl.assignCarTwo(testRace.getId(), carId);

        verify(raceRepository).save(testRace);
        assertThat(testRace.getCar2Id()).as("Confirm car two is assigned to the race").isEqualTo(carId);
    }

    @Test
    void assignCarTwo_whenRaceDoesNotExist_throwsException() {
        Long carId = 1L;

        assertThatThrownBy(() -> raceServiceImpl.assignCarTwo(testRace.getId(), carId))
                .as("Expect RaceNotFoundException when trying to assign a car to race that does not exist")
                .isInstanceOf(RaceNotFoundException.class)
                .hasMessageContaining("Race not found with id: " + testRace.getId());
    }

    @Test
    void findByLocation_whenFound_returnsRacesByLocation() {
        List<Race> expectedRaces = Collections.singletonList(testRace);
        when(raceRepository.findByLocation_CountryOrLocation_CityOrLocation_Street("", "Czech Republic", "")).thenReturn(expectedRaces);

        List<Race> result = raceServiceImpl.findByLocation_CountryOrLocation_CityOrLocation_Street("", "Czech Republic", "");

        verify(raceRepository).findByLocation_CountryOrLocation_CityOrLocation_Street("", "Czech Republic", "");
        assertThat(result).as("Ensure the races returned by location match the expected races").isEqualTo(expectedRaces);
    }

    @Test
    void findByLocation_whenNotFound_returnsEmptyList() {
        when(raceRepository.findByLocation_CountryOrLocation_CityOrLocation_Street("", "Czech Republic", "")).thenReturn(Collections.emptyList());

        List<Race> result = raceServiceImpl.findByLocation_CountryOrLocation_CityOrLocation_Street("", "Czech Republic", "");

        verify(raceRepository).findByLocation_CountryOrLocation_CityOrLocation_Street("", "Czech Republic", "");
        assertThat(result).as("Verify that an empty list is returned when no races are found with the specified location").isEmpty();
    }

    @Test
    void findByCarId_whenFound_returnsRacesByCarId() {
        Long carId = 1L;
        List<Race> expectedRaces = Collections.singletonList(testRace);
        when(raceRepository.findCarById(carId)).thenReturn(expectedRaces);

        List<Race> result = raceServiceImpl.findByCarId(carId);

        verify(raceRepository).findCarById(carId);
        assertThat(result).as("Ensure the races returned by car id match the expected races").isEqualTo(expectedRaces);
    }

    @Test
    void findByCarId_whenNotFound_returnsEmptyList() {
        Long carId = 1L;
        when(raceRepository.findCarById(carId)).thenReturn(Collections.emptyList());

        List<Race> result = raceServiceImpl.findByCarId(carId);

        verify(raceRepository).findCarById(carId);
        assertThat(result).as("Verify that an empty list is returned when no cars are found with the specified car ID").isEmpty();
    }

    @Test
    void assignCarOne_whenExternalServiceThrowsException_throwsException() {
        Long carId = 1L;
        when(raceRepository.findById(testRace.getId())).thenReturn(Optional.of(testRace));
        when(externalService.carExists(carId)).thenThrow(new RuntimeException("Service unavailable"));

        assertThatThrownBy(() -> raceServiceImpl.assignCarOne(testRace.getId(), carId))
                .as("Expect an exception when external service fails")
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Service unavailable");
    }

    @Test
    void deleteAllRaces_whenRepositoryThrowsException_throwsException() {
        doThrow(new DataStorageException("Database error")).when(raceRepository).deleteAll();

        assertThatThrownBy(() -> raceServiceImpl.deleteAll())
                .as("Expect DataStorageException when repository operation fails")
                .isInstanceOf(DataStorageException.class)
                .hasMessageContaining("Database error");
    }

    @Test
    void validateRace_WithNonExistentCar1_ThrowsDataStorageException() {
        testRace.setCar1Id(1L);  // Set a car ID that presumably doesn't exist.
        when(externalService.carExists(1L)).thenReturn(false);  // Mock the external service to simulate the car does not exist.

        assertThatThrownBy(() -> raceServiceImpl.save(testRace))
                .as("Expect DataStorageException when car1 does not exist")
                .isInstanceOf(DataStorageException.class)
                .hasMessageContaining("Car with id 1 does not exist");
    }

    @Test
    void validateRace_WithNonExistentCar2_ThrowsDataStorageException() {
        // Prepare the test race
        testRace.setCar1Id(1L);
        testRace.setCar2Id(2L);

        // Stubbing to return false for any long value
        when(externalService.carExists(anyLong())).thenReturn(false);

        // Execute and verify
        assertThatThrownBy(() -> raceServiceImpl.save(testRace))
                .as("Expect DataStorageException when car does not exist")
                .isInstanceOf(DataStorageException.class)
                .hasMessageContaining("Car with id 1 does not exist");
    }

    @Test
    void assignCarOne_NonExistentCar_ThrowsDataStorageException() {
        Long raceId = testRace.getId();
        Long carId = 3L;  // Non-existent car ID.
        when(raceRepository.findById(raceId)).thenReturn(Optional.of(testRace));
        when(externalService.carExists(carId)).thenReturn(false);

        assertThatThrownBy(() -> raceServiceImpl.assignCarOne(raceId, carId))
                .as("Expect DataStorageException when trying to assign a non-existent car to car1")
                .isInstanceOf(DataStorageException.class)
                .hasMessageContaining("Car does not exist");
    }

    @Test
    void assignCarTwo_NonExistentCar_ThrowsDataStorageException() {
        Long raceId = testRace.getId();
        Long carId = 4L;  // Non-existent car ID.
        when(raceRepository.findById(raceId)).thenReturn(Optional.of(testRace));
        when(externalService.carExists(carId)).thenReturn(false);

        assertThatThrownBy(() -> raceServiceImpl.assignCarTwo(raceId, carId))
                .as("Expect DataStorageException when trying to assign a non-existent car to car2")
                .isInstanceOf(DataStorageException.class)
                .hasMessageContaining("Car does not exist");
    }

    @Test
    void validateRace_WithoutCar2_ThrowsDataStorageException() {
        testRace.setCar1Id(1L);
        testRace.setCar2Id(2L);  // Assume 2L is an ID for a car that does not exist

        // Stubbing both potential calls to carExists
        when(externalService.carExists(1L)).thenReturn(true);  // Assuming car1 exists
        when(externalService.carExists(2L)).thenReturn(false);  // car2 does not exist

        // Execute and verify that an exception is thrown for car2
        assertThatThrownBy(() -> raceServiceImpl.save(testRace))
                .as("Expect DataStorageException when car2 does not exist")
                .isInstanceOf(DataStorageException.class)
                .hasMessageContaining("Car with id 2 does not exist");
    }

    @Test
    void findAllWithRaces_returnsAllRaces() {
        when(raceServiceImpl.findAll()).thenReturn(TestRaceFactory.getListOfRaceEntities());
        when(externalService.getCar(anyLong())).thenReturn(ResponseEntity.ok(TestRaceFactory.getCarEntity()));
        when(externalService.getDriver(anyLong())).thenReturn(ResponseEntity.ok(TestRaceFactory.getDriverEntity()));
        when(externalService.getComponent(anyLong())).thenReturn(ResponseEntity.ok(TestRaceFactory.getComponentEntity()));

        List<RaceView> expectedRaces = TestRaceFactory.getListOfRaceViews();

        List<RaceView> foundRaces = raceServiceImpl.findAllWithCars();
        assertThat(foundRaces).as("Ensure returned races match the expected races").isEqualTo(expectedRaces);
    }
}
