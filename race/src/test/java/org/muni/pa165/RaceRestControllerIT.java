package org.muni.pa165;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.muni.pa165.api.RaceViewDto;
import org.muni.pa165.exceptions.RaceNotFoundException;
import org.muni.pa165.facade.RaceFacade;
import org.muni.pa165.service.ExternalService;
import org.muni.pa165.utils.TestRaceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RaceMain.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class RaceRestControllerIT {

    @LocalServerPort
    private int port;

    @MockBean
    private RaceFacade raceFacade;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExternalService externalService;
    private RaceViewDto newRaceViewDto;

    @BeforeEach
    void setUp() {
        when(externalService.carExists(ArgumentMatchers.anyLong())).thenReturn(true);
        newRaceViewDto = TestRaceFactory.getRaceViewDto();
    }

    private String getUrl(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void findRaceById_raceFound_returnsRace() throws Exception {
        when(raceFacade.findById(newRaceViewDto.getId())).thenReturn(newRaceViewDto);

        String expectedJson = objectMapper.writeValueAsString(newRaceViewDto);
        mockMvc.perform(get("/races/{id}", newRaceViewDto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void findAllRaces_racesFound_returnsRaces() throws Exception {
        List<RaceViewDto> raceViewDtos = Collections.singletonList(newRaceViewDto);
        when(raceFacade.findAll()).thenReturn(raceViewDtos);

        String expectedJson = objectMapper.writeValueAsString(raceViewDtos);
        mockMvc.perform(get("/races")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void findRaceById_raceNotFound_returnsNotFound() throws Exception {
        Long nonExistentId = -1L;
        doThrow(new RaceNotFoundException("Race with id: " + nonExistentId + " not found."))
                .when(raceFacade).findById(nonExistentId);

        mockMvc.perform(get("/races/{id}", nonExistentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findRacesByCarId_racesNotFound_returnsOk() throws Exception {
        when(raceFacade.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/races")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
