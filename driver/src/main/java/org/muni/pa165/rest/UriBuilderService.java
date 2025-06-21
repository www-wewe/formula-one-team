package org.muni.pa165.rest;

import lombok.NonNull;
import org.muni.pa165.api.DriverViewDto;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Component
public class UriBuilderService {

    @NonNull
    public URI getLocationOfCreatedResource(DriverViewDto driverViewDto) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(driverViewDto.getId())
                .toUri();
    }
}
