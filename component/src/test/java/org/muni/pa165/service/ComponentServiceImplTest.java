package org.muni.pa165.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.muni.pa165.data.domain.Component;
import org.muni.pa165.data.enums.ComponentType;
import org.muni.pa165.data.repository.ComponentRepository;
import org.muni.pa165.exceptions.ComponentNotFoundException;
import org.muni.pa165.exceptions.ComponentValidationException;
import org.muni.pa165.utils.TestComponentFactory;

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
class ComponentServiceImplTest {

    @Mock
    private ComponentRepository componentRepository;

    @InjectMocks
    private ComponentServiceImpl componentServiceImpl;

    private Component component;

    @BeforeEach
    void setUp() {
        component = TestComponentFactory.getComponentEntity();
    }

    @Test
    void findComponentById_whenFound_returns() {
        when(componentRepository.findById(anyLong())).thenReturn(Optional.of(component));

        Component found = componentServiceImpl.findById(1L);

        assertThat(found).as("Check that the component returned matches the expected component when found").isEqualTo(component);
    }

    @Test
    void findById_whenNotFound_throwsException() {
        when(componentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> componentServiceImpl.findById(1L))
                .as("Expect ComponentNotFoundException when the component ID does not exist")
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessageContaining("Component not found with id: 1");
    }

    @Test
    void findAllComponents_whenFound_returns() {
        when(componentRepository.findAll()).thenReturn(Collections.singletonList(component));

        List<Component> components = componentServiceImpl.findAll();

        assertThat(components).as("Check that the list of components returned is not empty and contains the expected component").containsExactly(component);
    }

    @Test
    void findAllComponents_whenNotFound_returnsEmptyList() {
        when(componentRepository.findAll()).thenReturn(Collections.emptyList());

        List<Component> components = componentServiceImpl.findAll();

        assertThat(components).as("Check that the list of components returned is empty when no components are found").isEmpty();
    }

    @Test
    void saveComponent_valid_savesSuccessfully() {
        when(componentRepository.save(any(Component.class))).thenReturn(component);

        Component saved = componentServiceImpl.save(component);

        assertThat(saved).as("Check that the component saved matches the expected component").isEqualTo(component);
    }

    @Test
    void saveComponent_invalidWeight_throwsValidationException() {
        component.setWeight(-1); // Invalid weight

        assertThatThrownBy(() -> componentServiceImpl.save(component))
                .as("Expect ComponentValidationException when component data is invalid")
                .isInstanceOf(ComponentValidationException.class)
                .hasMessageContaining("Component weight must be greater than 0");
    }

    @Test
    void saveComponent_invalidPrice_throwsValidationException() {
        component.setPrice(-1); // Invalid price

        assertThatThrownBy(() -> componentServiceImpl.save(component))
                .as("Expect ComponentValidationException when component data is invalid")
                .isInstanceOf(ComponentValidationException.class)
                .hasMessageContaining("Component price must be greater than 0");
    }

    @Test
    void saveComponent_invalidManufacturer_throwsValidationException() {
        component.setManufacturer(""); // Invalid manufacturer

        assertThatThrownBy(() -> componentServiceImpl.save(component))
                .as("Expect ComponentValidationException when component data is invalid")
                .isInstanceOf(ComponentValidationException.class)
                .hasMessageContaining("Component manufacturer must not be empty");
    }

    @Test
    void saveComponent_invalidType_throwsValidationException() {
        component.setType(null); // Invalid type

        assertThatThrownBy(() -> componentServiceImpl.save(component))
                .as("Expect ComponentValidationException when component data is invalid")
                .isInstanceOf(ComponentValidationException.class)
                .hasMessageContaining("Component type must not be empty");
    }

    @Test
    void deleteById_whenExists_deletesSuccessfully() {
        when(componentRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(componentRepository).deleteById(anyLong());

        componentServiceImpl.deleteById(1L);

        verify(componentRepository).deleteById(1L);
        assertThat(true).as("Verify that the delete operation was called on the repository").isTrue();
    }

    @Test
    void deleteById_whenNotExists_throwsException() {
        when(componentRepository.existsById(anyLong())).thenReturn(false);

        assertThatThrownBy(() -> componentServiceImpl.deleteById(1L))
                .as("Expect ComponentNotFoundException when trying to delete a component that does not exist")
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessageContaining("Cannot delete, component not found with id: 1");
    }

    @Test
    void updateComponent_whenExists_updatesSuccessfully() {
        when(componentRepository.existsById(component.getId())).thenReturn(true);
        when(componentRepository.save(any(Component.class))).thenReturn(component);

        componentServiceImpl.update(component);

        verify(componentRepository).save(component);
    }

    @Test
    void updateComponent_whenDoesNotExist_throwsException() {
        when(componentRepository.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> componentServiceImpl.update(component))
                .as("Expect ComponentNotFoundException when trying to update a component that does not exist")
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessageContaining("Cannot update, component not found with id: " + component.getId());
    }

    @Test
    void updateComponent_invalidWeight_throwsValidationException() {
        component.setWeight(-1); // Invalid weight

        assertThatThrownBy(() -> componentServiceImpl.update(component))
                .as("Expect ComponentValidationException when component data is invalid")
                .isInstanceOf(ComponentValidationException.class)
                .hasMessageContaining("Component weight must be greater than 0");
    }

    @Test
    void updateComponent_invalidPrice_throwsValidationException() {
        component.setPrice(-1); // Invalid price

        assertThatThrownBy(() -> componentServiceImpl.update(component))
                .as("Expect ComponentValidationException when component data is invalid")
                .isInstanceOf(ComponentValidationException.class)
                .hasMessageContaining("Component price must be greater than 0");
    }

    @Test
    void updateComponent_invalidManufacturer_throwsValidationException() {
        component.setManufacturer(""); // Invalid manufacturer

        assertThatThrownBy(() -> componentServiceImpl.update(component))
                .as("Expect ComponentValidationException when component data is invalid")
                .isInstanceOf(ComponentValidationException.class)
                .hasMessageContaining("Component manufacturer must not be empty");
    }

    @Test
    void updateComponent_invalidType_throwsValidationException() {
        component.setType(null); // Invalid type

        assertThatThrownBy(() -> componentServiceImpl.update(component))
                .as("Expect ComponentValidationException when component data is invalid")
                .isInstanceOf(ComponentValidationException.class)
                .hasMessageContaining("Component type must not be empty");
    }

    @Test
    void deleteAllComponents_deletesAllSuccessfully() {
        doNothing().when(componentRepository).deleteAll();

        componentServiceImpl.deleteAll();

        verify(componentRepository).deleteAll();
        assertThat(true).as("Verify that the delete all operation was called on the repository").isTrue();
    }

    @Test
    void findByType_whenFound_returns() {
        when(componentRepository.findByType(ComponentType.ENGINE)).thenReturn(Collections.singletonList(component));

        List<Component> components = componentServiceImpl.findByType(ComponentType.ENGINE);

        assertThat(components).as("Check that the components returned by type match the expected components").containsExactly(component);
    }

    @Test
    void findByManufacturer_whenFound_returns() {
        when(componentRepository.findByManufacturer("NVIDIA")).thenReturn(Collections.singletonList(component));

        List<Component> components = componentServiceImpl.findByManufacturer("NVIDIA");

        assertThat(components).as("Check that the components returned by manufacturer match the expected components").containsExactly(component);
    }

    @Test
    void findByManufacturer_whenNotFound_returnsEmptyList() {
        when(componentRepository.findByManufacturer(any(String.class))).thenReturn(Collections.emptyList());

        List<Component> components = componentServiceImpl.findByManufacturer("NVIDIA");

        verify(componentRepository).findByManufacturer("NVIDIA");
        assertThat(components).as("Check that the components returned by manufacturer are empty when no components are found").isEmpty();
    }

    @Test
    void findByType_whenNotFound_returnsEmptyList() {
        when(componentRepository.findByType(any(ComponentType.class))).thenReturn(Collections.emptyList());

        List<Component> components = componentServiceImpl.findByType(ComponentType.ENGINE);

        verify(componentRepository).findByType(ComponentType.ENGINE);
        assertThat(components).as("Check that the components returned by type are empty when no components are found").isEmpty();
    }
}
