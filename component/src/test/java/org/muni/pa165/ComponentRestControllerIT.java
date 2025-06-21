package org.muni.pa165;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.muni.pa165.api.ComponentViewDto;
import org.muni.pa165.facade.ComponentFacade;
import org.muni.pa165.utils.TestComponentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ComponentMain.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ComponentRestControllerIT {

    @LocalServerPort
    private int port;

    @MockBean
    private ComponentFacade componentFacade;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ComponentViewDto newComponent;

    private String getUrl(String path) {
        return "http://localhost:" + port + path;
    }

    @BeforeEach
    public void setUp() {
        newComponent = TestComponentFactory.getComponentViewDto();
    }

    @Test
    void findComponentById_whenFound_returnsComponent() throws Exception {
        when(componentFacade.findById(newComponent.getId())).thenReturn(newComponent);

        String expectedJson = objectMapper.writeValueAsString(newComponent);
        mockMvc.perform(get("/components/{id}", newComponent.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void findAllComponents_whenFound_returnsComponents() throws Exception {
        List<ComponentViewDto> componentViewDtos = Collections.singletonList(newComponent);
        when(componentFacade.findAll()).thenReturn(componentViewDtos);

        String expectedJson = objectMapper.writeValueAsString(componentViewDtos);
        mockMvc.perform(get("/components")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));
    }
}
