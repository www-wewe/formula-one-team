package org.muni.pa165.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.muni.pa165.api.Component;
import org.muni.pa165.api.Driver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
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

        when(mockRestClient.get()).thenReturn(mockRequestHeadersUriSpec);
        when(mockRequestHeadersSpec.header(eq(HttpHeaders.AUTHORIZATION), any())).thenReturn(mockRequestHeadersSpec);
        when(mockRequestHeadersUriSpec.uri(anyString())).thenReturn(mockRequestHeadersSpec);
        when(mockRequestHeadersSpec.retrieve()).thenReturn(mockResponseSpec);

        mockRestTemplate = mock(RestTemplate.class);
        externalService = new ExternalServiceImpl(mockRestTemplate);
    }

    @AfterEach
    void tearDown() {
        mockedStatic.close();
    }

    @Test
    void componentExists_WhenComponentExists_ReturnsTrue() {
        when(mockResponseSpec.toEntity(Component.class)).thenReturn(ResponseEntity.ok(new Component()));
        when(mockRestTemplate.getForEntity(anyString(), eq(Component.class)))
                .thenReturn(new ResponseEntity<>(new Component(), HttpStatus.OK));
        assertTrue(externalService.componentExists(1L));
    }

    @Test
    void componentExists_WhenComponentDoesNotExist_ReturnsFalse() {
        when(mockResponseSpec.toEntity(Component.class)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        when(mockRestTemplate.getForEntity(anyString(), eq(Component.class)))
                .thenReturn(new ResponseEntity<>(new Component(), HttpStatus.NOT_FOUND));
        assertFalse(externalService.componentExists(99L));
    }

    @Test
    void driverExists_WhenDriverExists_ReturnsTrue() {
        when(mockResponseSpec.toEntity(Driver.class)).thenReturn(ResponseEntity.ok(new Driver()));
        when(mockRestTemplate.getForEntity(anyString(), eq(Driver.class)))
                .thenReturn(new ResponseEntity<>(new Driver(), HttpStatus.OK));
        assertTrue(externalService.driverExists(1L));
    }

    @Test
    void driverExists_WhenDriverDoesNotExist_ReturnsFalse() {
        when(mockResponseSpec.toEntity(Driver.class)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        when(mockRestTemplate.getForEntity(anyString(), eq(Driver.class)))
                .thenReturn(new ResponseEntity<>(new Driver(), HttpStatus.NOT_FOUND));
        assertFalse(externalService.driverExists(99L));
    }
}
