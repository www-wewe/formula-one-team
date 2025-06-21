package org.muni.pa165;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.muni.pa165.api.DriverViewDto;
import org.muni.pa165.facade.DriverFacade;
import org.muni.pa165.utils.TestDriverFactory;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DriverMain.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class DriverRestControllerIT {

    @LocalServerPort
    private int port;

    @MockBean
    private DriverFacade driverFacade;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    private DriverViewDto newDriver;

    private String getUrl(String path) {
        return "http://localhost:" + port + path;
    }

    @BeforeEach
    void setUp() {
        newDriver = TestDriverFactory.getDriverViewDto();
    }

    @Test
    void findDriverById_whenFound_returnsDriver() throws Exception {
        when(driverFacade.findById(newDriver.getId())).thenReturn(newDriver);

        String expectedJson = objectMapper.writeValueAsString(newDriver);
        mockMvc.perform(get("/drivers/{id}", newDriver.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void findAllDrivers_whenFound_returnsDrivers() throws Exception {
        List<DriverViewDto> driverViewDtos = Collections.singletonList(newDriver);
        when(driverFacade.findAll()).thenReturn(driverViewDtos);

        String expectedJson = objectMapper.writeValueAsString(driverViewDtos);
        mockMvc.perform(get("/drivers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));
    }
}
