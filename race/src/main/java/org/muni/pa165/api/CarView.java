package org.muni.pa165.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class CarView implements Serializable {

    private DriverView mainDriver;

    private String carMake;

    private Set<DriverView> testDrivers;

    private Set<ComponentView> components;
}
