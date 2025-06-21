package org.muni.pa165.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.muni.pa165.api.Car;
import org.muni.pa165.api.Component;
import org.muni.pa165.api.Driver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class ExternalServiceImplTest {

    private RestClient mockRestClient;
    private RestClient.RequestHeadersUriSpec mockRequestHeadersUriSpec;
    private RestClient.RequestHeadersSpec mockRequestHeadersSpec;
    private RestClient.ResponseSpec mockResponseSpec;
    private MockedStatic<RestClient> mockedStatic;

    private RestTemplate mockRestTemplate;
    private ExternalServiceImpl externalService;

    @BeforeEach
    void setUp() {
        mockRestClient = mock(RestClient.class);
        mockRequestHeadersUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        mockRequestHeadersSpec = mock(RestClient.RequestHeadersSpec.class);
        mockResponseSpec = mock(RestClient.ResponseSpec.class);

        mockedStatic = mockStatic(RestClient.class);
        mockedStatic.when(RestClient::create).thenReturn(mockRestClient);

        mockRestTemplate = mock(RestTemplate.class);
        externalService = new ExternalServiceImpl(mockRestTemplate);

        when(mockRestClient.get()).thenReturn(mockRequestHeadersUriSpec);
        when(mockRequestHeadersUriSpec.uri(anyString())).thenReturn(mockRequestHeadersSpec);
        when(mockRequestHeadersSpec.retrieve()).thenReturn(mockResponseSpec);
    }

    @AfterEach
    void tearDown() {
        mockedStatic.close();
    }

    @Test
    void carExists_WhenCarExists_ReturnsTrue() {
        when(mockResponseSpec.toEntity(Car.class)).thenReturn(ResponseEntity.ok(new Car()));

        when(mockRestTemplate.getForEntity(anyString(), eq(Car.class)))
                .thenReturn(new ResponseEntity<>(new Car(), HttpStatus.OK));
        assertTrue(externalService.carExists(1L));
    }

    @Test
    void carExists_WhenCarDoesNotExist_ReturnsFalse() {
        when(mockResponseSpec.toEntity(Car.class)).thenReturn(ResponseEntity.notFound().build());
        when(mockRestTemplate.getForEntity(anyString(), eq(Car.class)))
                .thenReturn(new ResponseEntity<>(new Car(), HttpStatus.NOT_FOUND));

        assertFalse(externalService.carExists(99L));
    }

    @Test
    void getCar_WhenCarExists_ReturnsCar() {
        when(mockResponseSpec.toEntity(Car.class)).thenReturn(ResponseEntity.ok(new Car()));

        when(mockRestTemplate.getForEntity(anyString(), eq(Car.class)))
                .thenReturn(new ResponseEntity<>(new Car(), HttpStatus.OK));
        ResponseEntity<Car> actualCar = externalService.getCar(1L);
        assertTrue(actualCar.getStatusCode().is2xxSuccessful());
    }

    @Test
    void getCar_WhenCarDoesNotExist_ReturnsNotFound() {
        when(mockResponseSpec.toEntity(Car.class)).thenReturn(ResponseEntity.notFound().build());

        when(mockRestTemplate.getForEntity(anyString(), eq(Car.class)))
                .thenReturn(new ResponseEntity<>(new Car(), HttpStatus.NOT_FOUND));
        ResponseEntity<Car> actualCar = externalService.getCar(99L);
        assertTrue(actualCar.getStatusCode().is4xxClientError());
    }

    @Test
    void getDriver_WhenDriverExists_ReturnsDriver() {
        when(mockResponseSpec.toEntity(Driver.class)).thenReturn(ResponseEntity.ok(new Driver()));

        when(mockRestTemplate.getForEntity(anyString(), eq(Driver.class)))
                .thenReturn(new ResponseEntity<>(new Driver(), HttpStatus.OK));
        ResponseEntity<Driver> actualDriver = externalService.getDriver(1L);
        assertTrue(actualDriver.getStatusCode().is2xxSuccessful());
    }

    @Test
    void getDriver_WhenDriverDoesNotExist_ReturnsNotFound() {
        when(mockResponseSpec.toEntity(Driver.class)).thenReturn(ResponseEntity.notFound().build());

        when(mockRestTemplate.getForEntity(anyString(), eq(Driver.class)))
                .thenReturn(new ResponseEntity<>(new Driver(), HttpStatus.NOT_FOUND));
        ResponseEntity<Driver> actualDriver = externalService.getDriver(99L);
        assertTrue(actualDriver.getStatusCode().is4xxClientError());
    }

    @Test
    void getComponent_WhenComponentExists_ReturnsComponent() {
        when(mockResponseSpec.toEntity(Component.class)).thenReturn(ResponseEntity.ok(new Component()));

        when(mockRestTemplate.getForEntity(anyString(), eq(Component.class)))
                .thenReturn(new ResponseEntity<>(new Component(), HttpStatus.OK));
        ResponseEntity<Component> actualComponent = externalService.getComponent(1L);
        assertTrue(actualComponent.getStatusCode().is2xxSuccessful());
    }

    @Test
    void getComponent_WhenComponentDoesNotExist_ReturnsNotFound() {
        when(mockResponseSpec.toEntity(Component.class)).thenReturn(ResponseEntity.notFound().build());

        when(mockRestTemplate.getForEntity(anyString(), eq(Component.class)))
                .thenReturn(new ResponseEntity<>(new Component(), HttpStatus.NOT_FOUND));
        ResponseEntity<Component> actualComponent = externalService.getComponent(99L);
        assertTrue(actualComponent.getStatusCode().is4xxClientError());
    }
}
