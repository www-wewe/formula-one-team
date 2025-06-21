package org.muni.pa165.rest;

import lombok.NonNull;
import org.muni.pa165.api.RaceViewDto;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Component
public class UriBuilderService {

    @NonNull
    public URI getLocationOfCreatedResource(RaceViewDto raceViewDto) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(raceViewDto.getId())
                .toUri();
    }
}
