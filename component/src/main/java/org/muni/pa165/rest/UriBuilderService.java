package org.muni.pa165.rest;

import lombok.NonNull;
import org.muni.pa165.api.ComponentViewDto;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Component
public class UriBuilderService {

    @NonNull
    public URI getLocationOfCreatedResource(ComponentViewDto componentViewDto) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(componentViewDto.getId())
                .toUri();
    }
}
