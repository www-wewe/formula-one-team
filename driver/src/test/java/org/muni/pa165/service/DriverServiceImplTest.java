package org.muni.pa165.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.muni.pa165.data.domain.Driver;
import org.muni.pa165.data.domain.DriverPerk;
import org.muni.pa165.data.repository.DriverRepository;
import org.muni.pa165.exceptions.DriverNotFoundException;
import org.muni.pa165.exceptions.DriverValidationException;
import org.muni.pa165.utils.TestDriverFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DriverServiceImplTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverServiceImpl driverServiceImpl;
    private Driver testDriver;

    @BeforeEach
    void setUp() {
        testDriver = TestDriverFactory.getDriverEntity();
    }

    @Test
    void findDriverById_whenFound_returnsDriver() {
        Long driverId = testDriver.getId();
        when(driverRepository.findById(driverId)).thenReturn(Optional.of(testDriver));

        Driver result = driverServiceImpl.findById(driverId);

        verify(driverRepository).findById(driverId);
        assertThat(result).as("Verify the driver returned by getDriverById matches the expected driver").isSameAs(testDriver);
    }

    @Test
    void findDriverById_whenNotFound_throwsException() {
        when(driverRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> driverServiceImpl.findById(1L))
                .as("Expect DriverNotFoundException when the driver ID does not exist")
                .isInstanceOf(DriverNotFoundException.class)
                .hasMessageContaining("Driver not found with id: 1");
    }

    @Test
    void saveDriver_savesAndReturnsDriver() {
        when(driverRepository.save(any(Driver.class))).thenReturn(testDriver);

        Driver result = driverServiceImpl.save(testDriver);

        verify(driverRepository).save(testDriver);
        assertThat(result).as("Ensure the driver saved is the same as the driver returned").isSameAs(testDriver);
    }

    @Test
    void saveDriverName_invalidManufacturer_throwsValidationException() {
        testDriver.setName("");

        assertThatThrownBy(() -> driverServiceImpl.save(testDriver))
                .as("Expect DriverValidationException when driver data is invalid")
                .isInstanceOf(DriverValidationException.class)
                .hasMessageContaining("Driver name cannot be empty");
    }

    @Test
    void saveDriverSurname_invalidManufacturer_throwsValidationException() {
        testDriver.setSurname("");

        assertThatThrownBy(() -> driverServiceImpl.save(testDriver))
                .as("Expect DriverValidationException when driver data is invalid")
                .isInstanceOf(DriverValidationException.class)
                .hasMessageContaining("Driver Surname cannot be empty");
    }

    @Test
    void saveDriverCountry_invalidManufacturer_throwsValidationException() {
        testDriver.setNationality("");

        assertThatThrownBy(() -> driverServiceImpl.save(testDriver))
                .as("Expect DriverValidationException when driver data is invalid")
                .isInstanceOf(DriverValidationException.class)
                .hasMessageContaining("Driver Country cannot be empty");
    }

    @Test
    void saveDriverPerk_invalidManufacturer_throwsValidationException() {
        testDriver.setPerk(null);

        assertThatThrownBy(() -> driverServiceImpl.save(testDriver))
                .as("Expect DriverValidationException when driver data is invalid")
                .isInstanceOf(DriverValidationException.class)
                .hasMessageContaining("Driver perk cannot be empty");
    }

    @Test
    void deleteDriverById_deletesDriver() {
        Long driverId = 1L;
        when(driverRepository.existsById(driverId)).thenReturn(true);
        doNothing().when(driverRepository).deleteById(driverId);

        driverServiceImpl.deleteById(driverId);

        verify(driverRepository).deleteById(driverId);
    }

    @Test
    void deleteDriverById_whenNotExists_throwsException() {
        when(driverRepository.existsById(anyLong())).thenReturn(false);

        assertThatThrownBy(() -> driverServiceImpl.deleteById(1L))
                .as("Expect DriverNotFoundException when trying to delete a driver that does not exist")
                .isInstanceOf(DriverNotFoundException.class)
                .hasMessageContaining("Driver not found with id: 1 for deletion");
    }

    @Test
    void deleteAllDrivers_deletesAllDrivers() {
        driverServiceImpl.deleteAll();

        verify(driverRepository).deleteAll();
    }

    @Test
    void findAllDrivers_returnsAllDrivers() {
        List<Driver> expectedDrivers = Arrays.asList(testDriver, TestDriverFactory.getDriverEntity());
        when(driverRepository.findAll()).thenReturn(expectedDrivers);

        List<Driver> result = driverServiceImpl.findAll();

        verify(driverRepository).findAll();
        assertThat(result).as("Check if the list of all drivers matches the expected list").isEqualTo(expectedDrivers);
    }

    @Test
    void findAllDriver_whenNotFound_returnsEmptyList() {
        when(driverRepository.findAll()).thenReturn(Collections.emptyList());

        List<Driver> driver = driverServiceImpl.findAll();

        assertThat(driver).as("Check that the list of drivers returned is empty when no drivers are found").isEmpty();
    }

    @Test
    void updateDriver_updatesAndSavesDriver() {
        when(driverRepository.existsById(testDriver.getId())).thenReturn(true);
        when(driverRepository.save(any(Driver.class))).thenReturn(testDriver);

        driverServiceImpl.update(testDriver);

        verify(driverRepository).save(testDriver);
    }

    @Test
    void updateDriver_whenDriverDoesNotExist_throwsException() {
        when(driverRepository.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> driverServiceImpl.update(testDriver))
                .as("Expect DriverNotFoundException when trying to update a driver that does not exist")
                .isInstanceOf(DriverNotFoundException.class)
                .hasMessageContaining("Driver not found with id: " + testDriver.getId() + " for update");
    }

    @Test
    void updateDriver_invalidName_throwsValidationException() {
        testDriver.setName("");

        assertThatThrownBy(() -> driverServiceImpl.update(testDriver))
                .as("Expect DriverValidationException when driver data is invalid")
                .isInstanceOf(DriverValidationException.class)
                .hasMessageContaining("Driver name cannot be empty");
    }

    @Test
    void updateDriver_invalidSurname_throwsValidationException() {
        testDriver.setSurname("");

        assertThatThrownBy(() -> driverServiceImpl.update(testDriver))
                .as("Expect DriverValidationException when driver data is invalid")
                .isInstanceOf(DriverValidationException.class)
                .hasMessageContaining("Driver Surname cannot be empty");
    }

    @Test
    void updateDriver_invalidNationality_throwsValidationException() {
        testDriver.setNationality("");

        assertThatThrownBy(() -> driverServiceImpl.update(testDriver))
                .as("Expect DriverValidationException when driver data is invalid")
                .isInstanceOf(DriverValidationException.class)
                .hasMessageContaining("Driver Country cannot be empty");
    }

    @Test
    void updateDriver_invalidPerk_throwsValidationException() {
        testDriver.setPerk(null);

        assertThatThrownBy(() -> driverServiceImpl.update(testDriver))
                .as("Expect DriverValidationException when driver data is invalid")
                .isInstanceOf(DriverValidationException.class)
                .hasMessageContaining("Driver perk cannot be empty");
    }

    @Test
    void findDriversByPerk_returnsDriversWithPerk() {
        List<Driver> expectedDrivers = Collections.singletonList(testDriver);
        DriverPerk perk = testDriver.getPerk();
        when(driverRepository.findByPerk(perk)).thenReturn(expectedDrivers);

        List<Driver> result = driverServiceImpl.findByPerk(perk);

        verify(driverRepository).findByPerk(perk);
        assertThat(result).as("Ensure the drivers returned by getDriversByPerk match the expected drivers").isEqualTo(expectedDrivers);
    }

    @Test
    void findByPerk_whenNotFound_returnsEmptyList() {
        when(driverRepository.findByPerk(any(DriverPerk.class))).thenReturn(Collections.emptyList());

        List<Driver> driver = driverServiceImpl.findByPerk(DriverPerk.OVERTAKER);

        verify(driverRepository).findByPerk(DriverPerk.OVERTAKER);
        assertThat(driver).as("Check that the driver returned by type are empty when no driver are found").isEmpty();
    }

    @Test
    void findDriversByDriverNationality_whenDriversFound_returnsDriverList() {
        List<Driver> expectedDrivers = List.of(testDriver);
        when(driverRepository.findByNationality("Netherlands")).thenReturn(expectedDrivers);

        List<Driver> result = driverServiceImpl.findByNationality("Netherlands");

        verify(driverRepository).findByNationality("Netherlands");
        assertThat(result).as("Verify that the list of drivers found matches the expected list for drivers from 'Netherlands'").isEqualTo(expectedDrivers);
    }

    @Test
    void findDriversByDriverNationality_whenNoDriversFound_returnsEmptyList() {
        when(driverRepository.findByNationality("Netherlands")).thenReturn(Collections.emptyList());

        List<Driver> result = driverServiceImpl.findByNationality("Netherlands");

        verify(driverRepository).findByNationality("Netherlands");
        assertThat(result).as("Verify that an empty list is returned when no drivers are found by nationality").isEmpty();
    }
}
