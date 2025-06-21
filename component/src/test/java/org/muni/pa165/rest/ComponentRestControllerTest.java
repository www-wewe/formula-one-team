package org.muni.pa165.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.muni.pa165.api.ComponentCreateViewDto;
import org.muni.pa165.api.ComponentViewDto;
import org.muni.pa165.data.domain.Component;
import org.muni.pa165.data.enums.ComponentType;
import org.muni.pa165.facade.ComponentFacade;
import org.muni.pa165.utils.TestComponentFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComponentRestControllerTest {

    @Mock
    UriBuilderService uriBuilderService;
    @Mock
    private ComponentFacade componentFacade;
    @InjectMocks
    private ComponentRestController componentRestController;

    private ComponentViewDto componentViewDto;
    private ComponentCreateViewDto componentCreateViewDto;
    private Component component;

    @BeforeEach
    void setUp() {
        componentViewDto = TestComponentFactory.getComponentViewDto();
        componentCreateViewDto = TestComponentFactory.getComponentCreateViewDto();
        component = TestComponentFactory.getComponentEntity();
    }

    @Test
    void findComponentById_whenFound_returnsComponent() {
        when(componentFacade.findById(anyLong())).thenReturn(componentViewDto);

        ResponseEntity<ComponentViewDto> response = componentRestController.findComponentById(1L);

        assertThat(response.getStatusCode()).as("Verify status code is OK when component is found by ID").isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).as("Ensure the body matches the expected component when found by ID").isEqualTo(componentViewDto);
    }

    @Test
    void saveComponent_validData_createsComponent() throws URISyntaxException {
        when(componentFacade.save(any(ComponentCreateViewDto.class))).thenReturn(componentViewDto);
        when(uriBuilderService.getLocationOfCreatedResource(componentViewDto)).thenReturn(new URI("http://localhost:8084/components/1"));

        ResponseEntity<?> response = componentRestController.saveComponent(componentCreateViewDto);

        assertThat(response.getStatusCode()).as("Verify status code is CREATED when component is successfully created").isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).as("Ensure the body matches the expected component after successful creation").isEqualTo(componentViewDto);
    }

    @Test
    void deleteComponentById_whenFound_deletesComponent() {
        doNothing().when(componentFacade).deleteById(anyLong());

        ResponseEntity<?> response = componentRestController.deleteComponentById(1L);

        assertThat(response.getStatusCode()).as("Verify status code is OK when component is successfully deleted").isEqualTo(HttpStatus.OK);
    }

    @Test
    void findAllComponents_returnsAllComponents() {
        List<ComponentViewDto> components = Collections.singletonList(componentViewDto);
        when(componentFacade.findAll()).thenReturn(components);

        ResponseEntity<?> response = componentRestController.findAllComponents();

        assertThat(response.getStatusCode()).as("Verify status code is OK when components are found").isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).as("Ensure the body contains the expected list of components").isEqualTo(components);
    }

    @Test
    void updateComponent_whenFound_updatesComponent() {
        doNothing().when(componentFacade).update(any(Component.class));

        ResponseEntity<?> response = componentRestController.updateComponent(component);

        assertThat(response.getStatusCode()).as("Verify status code is OK when the component is updated").isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteAllComponents_deletesAll() {
        doNothing().when(componentFacade).deleteAll();

        ResponseEntity<?> response = componentRestController.deleteAllComponents();

        assertThat(response.getStatusCode()).as("Verify status code is OK when all components are deleted").isEqualTo(HttpStatus.OK);
    }

    @Test
    void findComponentsByType_whenFound_returnsComponents() {
        List<ComponentViewDto> components = TestComponentFactory.getListOfComponentViewDto();
        when(componentFacade.findByType(any(ComponentType.class))).thenReturn(components);

        ResponseEntity<List<ComponentViewDto>> response = componentRestController.findComponentsByType(componentViewDto.getType());

        assertThat(response.getStatusCode()).as("Verify status code is OK when components are found for a given type").isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).as("Ensure the body matches the expected list of components found by type").isEqualTo(components);
    }

    @Test
    void findComponentsByManufacturer_whenFound_returnsComponents() {
        List<ComponentViewDto> components = TestComponentFactory.getListOfComponentViewDto();
        when(componentFacade.findByManufacturer(any(String.class))).thenReturn(components);

        ResponseEntity<List<ComponentViewDto>> response = componentRestController.findComponentsByManufacturer(componentViewDto.getManufacturer());

        assertThat(response.getStatusCode()).as("Verify status code is OK when components are found for a given manufacturer").isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).as("Ensure the body matches the expected list of components found by manufacturer").isEqualTo(components);
    }
}
