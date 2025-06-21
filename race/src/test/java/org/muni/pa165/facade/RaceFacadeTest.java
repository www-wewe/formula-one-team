package org.muni.pa165.facade;

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
import org.muni.pa165.mapper.RaceMapper;
import org.muni.pa165.service.RaceServiceImpl;
import org.muni.pa165.utils.TestRaceFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RaceFacadeTest {

    @Mock
    private RaceServiceImpl raceServiceImpl;
    @Mock
    private RaceMapper raceMapper;
    @InjectMocks
    private RaceFacade raceFacade;
    private Race testRace;
    private RaceViewDto testRaceDto;
    private RaceCreateViewDto testRaceCreateDto;

    @BeforeEach
    void setUp() {
        testRace = TestRaceFactory.getRaceEntity();
        testRaceDto = TestRaceFactory.getRaceViewDto();
        testRaceCreateDto = TestRaceFactory.getRaceCreateViewDto();
    }

    @Test
    void saveRace_savesAndReturnsRaceDto() {
        when(raceMapper.fromRaceCreateViewDto(any(RaceCreateViewDto.class))).thenReturn(testRace);
        when(raceServiceImpl.save(any(Race.class))).thenReturn(testRace);
        when(raceMapper.toRaceViewDto(any(Race.class))).thenReturn(testRaceDto);

        RaceViewDto savedDto = raceFacade.save(testRaceCreateDto);

        assertThat(savedDto).as("Verify the saved race DTO is equivalent to the expected DTO after saving.").isEqualTo(testRaceDto);
        verify(raceServiceImpl, times(1)).save(testRace);
    }

    @Test
    void findRaceById_whenFound_returnsRaceDto() {
        when(raceServiceImpl.findById(testRace.getId())).thenReturn(testRace);
        when(raceMapper.toRaceViewDto(any(Race.class))).thenReturn(testRaceDto);

        RaceViewDto foundDto = raceFacade.findById(testRace.getId());

        assertThat(foundDto).as("Ensure the found race DTO matches the expected DTO for a given ID.").isEqualTo(testRaceDto);
    }

    @Test
    void findAllRaces_returnsAllRaceList() {
        List<Race> races = TestRaceFactory.getListOfRaceEntities();
        List<RaceViewDto> expectedRaces = TestRaceFactory.getListOfRaceViewDto();
        when(raceServiceImpl.findAll()).thenReturn(races);
        when(raceMapper.toRaceViewDto(any(Race.class))).thenReturn(testRaceDto);

        List<RaceViewDto> foundRaces = raceFacade.findAll();

        assertThat(foundRaces).as("Verify that the list of all race DTOs matches the expected list from TestRaceFactory").isEqualTo(expectedRaces);
    }

    @Test
    void deleteRaceById_deletesRace() {
        raceFacade.deleteById(testRace.getId());

        verify(raceServiceImpl, times(1)).deleteById(testRace.getId());
    }

    @Test
    void deleteAllRaces_deletesAllRaces() {
        raceFacade.deleteAll();

        verify(raceServiceImpl, times(1)).deleteAll();
    }

    @Test
    void updateRace_updatesRace() {
        raceFacade.update(testRace);

        verify(raceServiceImpl, times(1)).update(testRace);
    }

    @Test
    void assignCarOne_assignsCarOneToRace() {
        Long carId = 1L;
        raceFacade.assignCarOne(testRace.getId(), carId);

        verify(raceServiceImpl, times(1)).assignCarOne(testRace.getId(), carId);
    }

    @Test
    void assignCarTwo_assignsCarTwoToRace() {
        Long carId = 2L;
        raceFacade.assignCarTwo(testRace.getId(), carId);

        verify(raceServiceImpl, times(1)).assignCarTwo(testRace.getId(), carId);
    }

    @Test
    void findByLocation_whenFound_returnsRaceList() {
        when(raceServiceImpl.findByLocation_CountryOrLocation_CityOrLocation_Street("", "Czech Republic", ""))
                .thenReturn(TestRaceFactory.getListOfRaceEntities());
        when(raceMapper.toRaceViewDto(testRace)).thenReturn(testRaceDto);

        List<RaceViewDto> foundRaces = raceFacade.findByLocation_CountryOrLocation_CityOrLocation_Street("", "Czech Republic", "");

        assertThat(foundRaces).as("Validate that the list of races DTOs matches the expected list from TestRaceFactory.").isEqualTo(TestRaceFactory.getListOfRaceViewDto());
    }

    @Test
    void findByLocation_whenNoRacesFound_returnsEmptyList() {
        when(raceServiceImpl.findByLocation_CountryOrLocation_CityOrLocation_Street("", "Czech Republic", "")).thenReturn(List.of());
        List<RaceViewDto> result = raceFacade.findByLocation_CountryOrLocation_CityOrLocation_Street("", "Czech Republic", "");

        assertThat(result).as("Verify that an empty list is returned when no races are found by location").isEmpty();
    }

    @Test
    void findByCarId_whenFound_returnsRaceList() {
        Long carId = 1L;
        when(raceServiceImpl.findByCarId(carId)).thenReturn(TestRaceFactory.getListOfRaceEntities());
        when(raceMapper.toRaceViewDto(testRace)).thenReturn(testRaceDto);

        List<RaceViewDto> foundRaces = raceFacade.findByCarId(carId);

        assertThat(foundRaces).as("Validate that the list of races DTOs matches the expected list from TestRaceFactory.").isEqualTo(TestRaceFactory.getListOfRaceViewDto());
    }

    @Test
    void findByCarId_whenNoRacesFound_returnsEmptyList() {
        Long carId = 1L;
        when(raceServiceImpl.findByCarId(carId)).thenReturn(List.of());
        List<RaceViewDto> result = raceFacade.findByCarId(carId);

        assertThat(result).as("Verify that an empty list is returned when no races are found by car ID").isEmpty();
    }

    @Test
    void findAllWithCars_returnsAllRaceList() {
        List<RaceView> races = TestRaceFactory.getListOfRaceViews();
        when(raceServiceImpl.findAllWithCars()).thenReturn(races);

        List<RaceView> foundRaces = raceFacade.findAllWithCars();

        assertThat(foundRaces).as("Verify that the list of all races with cars matches the expected list from TestRaceFactory").isEqualTo(races);
    }
}
