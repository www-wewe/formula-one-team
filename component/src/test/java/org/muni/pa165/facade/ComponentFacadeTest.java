package org.muni.pa165.facade;

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
import org.muni.pa165.mapper.ComponentMapper;
import org.muni.pa165.service.ComponentServiceImpl;
import org.muni.pa165.utils.TestComponentFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComponentFacadeTest {

    @Mock
    private ComponentServiceImpl componentServiceImpl;
    @Mock
    private ComponentMapper componentMapper;
    @InjectMocks
    private ComponentFacade componentFacade;
    private Component testComponent;
    private ComponentViewDto testComponentDto;
    private ComponentCreateViewDto testComponentCreateDto;

    @BeforeEach
    void setUp() {
        testComponent = TestComponentFactory.getComponentEntity();
        testComponentDto = TestComponentFactory.getComponentViewDto();
        testComponentCreateDto = TestComponentFactory.getComponentCreateViewDto();
    }

    @Test
    void findById_componentFound_returnsComponent() {
        when(componentServiceImpl.findById(1L)).thenReturn(testComponent);
        when(componentMapper.toComponentViewDto(any())).thenReturn(testComponentDto);

        ComponentViewDto foundEntity = componentFacade.findById(1L);

        assertThat(foundEntity).as("Ensure the found component's DTO matches the expected DTO for a given ID.").isEqualTo(testComponentDto);
    }

    @Test
    void saveComponent_componentSaved_returnsSaved() {
        when(componentMapper.fromComponentCreateViewDto(any(ComponentCreateViewDto.class))).thenReturn(testComponent);
        when(componentServiceImpl.save(any(Component.class))).thenReturn(testComponent);
        when(componentMapper.toComponentViewDto(any(Component.class))).thenReturn(testComponentDto);

        ComponentViewDto savedDto = componentFacade.save(testComponentCreateDto);

        assertThat(savedDto).as("Verify the saved component's DTO is equivalent to the expected DTO after saving.").isEqualTo(testComponentDto);
        verify(componentServiceImpl, times(1)).save(testComponent);
    }

    @Test
    void deleteComponentById_Deleted_invokesDeletion() {
        componentFacade.deleteById(1L);

        verify(componentServiceImpl, times(1)).deleteById(1L);
    }

    @Test
    void deleteAllComponents_Deleted_invokesDeletion() {
        componentFacade.deleteAll();

        verify(componentServiceImpl, times(1)).deleteAll();
    }

    @Test
    void findAllComponents_componentsFound_returnsList() {
        when(componentServiceImpl.findAll()).thenReturn(TestComponentFactory.getListOfComponentEntities());
        when(componentMapper.toComponentViewDto(any())).thenReturn(testComponentDto);

        List<ComponentViewDto> foundDtos = componentFacade.findAll();

        assertThat(foundDtos).as("Verify that the list of found component DTOs matches the expected list from TestDataFactory").isEqualTo(TestComponentFactory.getListOfComponentViewDto());
    }

    @Test
    void updateComponent_componentUpdated_returnsUpdated() {
        componentFacade.update(testComponent);

        verify(componentServiceImpl, times(1)).update(testComponent);
    }

    @Test
    void findComponentsByType_componentsFound_returnsList() {
        when(componentServiceImpl.findByType(any(ComponentType.class))).thenReturn(TestComponentFactory.getListOfComponentEntities());
        when(componentMapper.toComponentViewDto(testComponent)).thenReturn(testComponentDto);

        List<ComponentViewDto> foundDtos = componentFacade.findByType(ComponentType.SPOILER);

        assertThat(foundDtos).as("Validate that the list of component DTOs filtered by type aligns with the anticipated list.").isEqualTo(TestComponentFactory.getListOfComponentViewDto());
    }

    @Test
    void findComponentsByManufacturer_componentsFound_returnsList() {
        when(componentServiceImpl.findByManufacturer(any(String.class))).thenReturn(TestComponentFactory.getListOfComponentEntities());
        when(componentMapper.toComponentViewDto(testComponent)).thenReturn(testComponentDto);

        List<ComponentViewDto> foundDtos = componentFacade.findByManufacturer("Manufacturer Name");

        assertThat(foundDtos).as("Validate that the list of component DTOs filtered by type aligns with the anticipated list.").isEqualTo(TestComponentFactory.getListOfComponentViewDto());
    }

    @Test
    void findComponentsByManufacturer_whenNotFound_returnsEmptyList() {
        when(componentServiceImpl.findByManufacturer(any(String.class))).thenReturn(List.of());
        List<ComponentViewDto> foundDtos = componentFacade.findByManufacturer("Manufacturer Name");
        assertThat(foundDtos).as("Validate that the list of component DTOs filtered by type aligns with the anticipated list.").isEmpty();
    }

    @Test
    void findComponentsByType_whenNotFound_returnsEmptyList() {
        when(componentServiceImpl.findByType(any(ComponentType.class))).thenReturn(List.of());
        List<ComponentViewDto> foundDtos = componentFacade.findByType(ComponentType.SPOILER);
        assertThat(foundDtos).as("Validate that the list of component DTOs filtered by type aligns with the anticipated list.").isEmpty();
    }
}
