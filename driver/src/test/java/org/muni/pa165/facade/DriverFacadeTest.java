package org.muni.pa165.facade;

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
import org.muni.pa165.mapper.DriverMapper;
import org.muni.pa165.service.DriverServiceImpl;
import org.muni.pa165.utils.TestDriverFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DriverFacadeTest {

    @Mock
    private DriverServiceImpl driverServiceImpl;
    @Mock
    private DriverMapper driverMapper;
    @InjectMocks
    private DriverFacade driverFacade;
    private Driver testDriver;
    private DriverViewDto testDriverViewDto;
    private DriverCreateViewDto testDriverCreateViewDto;

    @BeforeEach
    void setUp() {
        testDriver = TestDriverFactory.getDriverEntity();
        testDriverViewDto = TestDriverFactory.getDriverViewDto();
        testDriverCreateViewDto = TestDriverFactory.getDriverCreateViewDto();
    }

    @Test
    void findDriverById_whenFound_returnsDriverDto() {
        when(driverServiceImpl.findById(1L)).thenReturn(testDriver);
        when(driverMapper.toDriverViewDto(any())).thenReturn(testDriverViewDto);

        DriverViewDto foundDto = driverFacade.findById(1L);

        assertThat(foundDto).as("Ensure the found driver DTO matches the expected DTO for a given ID.").isEqualTo(testDriverViewDto);
    }

    @Test
    void saveDriver_savesAndReturnsDriverDto() {
        when(driverMapper.fromDriverCreateViewDto(any(DriverCreateViewDto.class))).thenReturn(testDriver);
        when(driverServiceImpl.save(any(Driver.class))).thenReturn(testDriver);
        when(driverMapper.toDriverViewDto(any(Driver.class))).thenReturn(testDriverViewDto);

        DriverViewDto savedDto = driverFacade.save(testDriverCreateViewDto);

        assertThat(savedDto).as("Verify the saved driver DTO is equivalent to the expected DTO after saving.").isEqualTo(testDriverViewDto);
        verify(driverServiceImpl, times(1)).save(testDriver);
    }

    @Test
    void deleteDriverById_driverDeleted_invokesDeletion() {
        driverFacade.deleteById(1L);

        verify(driverServiceImpl, times(1)).deleteById(1L);
    }

    @Test
    void deleteAllDrivers_driversDeleted_invokesDeletion() {
        driverFacade.deleteAll();

        verify(driverServiceImpl, times(1)).deleteAll();
    }

    @Test
    void findAllDrivers_driversFound_returnsDriversList() {
        when(driverServiceImpl.findAll()).thenReturn(TestDriverFactory.getListOfDriverEntities());
        when(driverMapper.toDriverViewDto(any())).thenReturn(testDriverViewDto);

        List<DriverViewDto> foundDtos = driverFacade.findAll();

        assertThat(foundDtos).as("Verify that the list of found driver DTOs matches the expected list from TestDataFactory").isEqualTo(TestDriverFactory.getListOfDriverViewDto());
    }

    @Test
    void updateDriver_driverUpdated_invokesUpdate() {
        driverFacade.update(testDriver);

        verify(driverServiceImpl, times(1)).update(testDriver);
    }

    @Test
    void findDriversByPerk_driversFound_returnsDriversList() {
        when(driverServiceImpl.findByPerk(any(DriverPerk.class))).thenReturn(TestDriverFactory.getListOfDriverEntities());
        when(driverMapper.toDriverViewDto(testDriver)).thenReturn(testDriverViewDto);

        List<DriverViewDto> foundDrivers = driverFacade.findByPerk(DriverPerk.OVERTAKER);

        assertThat(foundDrivers).as("Verify that the list of drivers found by perk matches the expected list").isEqualTo(TestDriverFactory.getListOfDriverViewDto());
    }

    @Test
    void findDriversByPerk_whenNotFound_returnsEmptyList() {
        when(driverServiceImpl.findByPerk(any(DriverPerk.class))).thenReturn(List.of());
        List<DriverViewDto> foundDtos = driverFacade.findByPerk(DriverPerk.OVERTAKER);
        assertThat(foundDtos).as("Validate that the list of driver DTOs filtered by type aligns with the anticipated list.").isEmpty();
    }

    @Test
    void findDriversByDriverNationality_whenDriversFound_returnsDriverList() {
        List<Driver> driverList = List.of(testDriver);
        when(driverServiceImpl.findByNationality("Netherlands")).thenReturn(driverList);
        when(driverMapper.toDriverViewDto(any(Driver.class))).thenReturn(testDriverViewDto);

        List<DriverViewDto> result = driverFacade.findByNationality("Netherlands");

        assertThat(result).as("Ensure the result contains the expected driver DTOs when drivers are found by nationality").contains(testDriverViewDto);
        assertEquals(1, result.size(), "Check that the correct number of driver DTOs is returned.");
    }

    @Test
    void findDriversByDriverNationality_whenNoDriversFound_returnsEmptyList() {
        when(driverServiceImpl.findByNationality("Netherlands")).thenReturn(List.of());
        List<DriverViewDto> result = driverFacade.findByNationality("Netherlands");

        assertThat(result).as("Verify that an empty list is returned when no drivers are found by nationality").isEmpty();
    }
}
