package org.muni.pa165.rest;

import lombok.NonNull;
import org.muni.pa165.api.CarViewDto;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Component
public class UriBuilderService {

    @NonNull
    public URI getLocationOfCreatedResource(CarViewDto carViewDto) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(carViewDto.getId())
                .toUri();
    }
}
