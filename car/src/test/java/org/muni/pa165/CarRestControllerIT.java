package org.muni.pa165;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.muni.pa165.api.CarViewDto;
import org.muni.pa165.exceptions.CarNotFoundException;
import org.muni.pa165.facade.CarFacade;
import org.muni.pa165.utils.TestCarFactory;
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

@SpringBootTest(classes = CarMain.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class CarRestControllerIT {

    @LocalServerPort
    private int port;

    @MockBean
    private CarFacade carFacade;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CarViewDto newCarViewDto;

    @BeforeEach
    void setUp() {
        newCarViewDto = TestCarFactory.getCarViewDto();
    }

    private String getUrl(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void findCarById_carFound_returnsCar() throws Exception {
        when(carFacade.findById(newCarViewDto.getId())).thenReturn(newCarViewDto);

        String expectedJson = objectMapper.writeValueAsString(newCarViewDto);
        mockMvc.perform(get("/cars/{id}", newCarViewDto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void findAllCars_carsFound_returnsCars() throws Exception {
        List<CarViewDto> carViewDtos = Collections.singletonList(newCarViewDto);
        when(carFacade.findAll()).thenReturn(carViewDtos);

        String expectedJson = objectMapper.writeValueAsString(carViewDtos);
        mockMvc.perform(get("/cars")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void findCarById_carNotFound_returnsNotFound() throws Exception {
        Long nonExistentId = -1L;
        doThrow(new CarNotFoundException("Car with id: " + nonExistentId + " not found."))
                .when(carFacade).findById(nonExistentId);

        mockMvc.perform(get("/cars/{id}", nonExistentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllCars_carNotFound_returnsOk() throws Exception {
        when(carFacade.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/cars")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

