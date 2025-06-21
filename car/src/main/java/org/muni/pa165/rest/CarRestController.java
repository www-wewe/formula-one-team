package org.muni.pa165.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.muni.pa165.api.CarCreateViewDto;
import org.muni.pa165.api.CarViewDto;
import org.muni.pa165.config.AppConfig;
import org.muni.pa165.data.domain.Car;
import org.muni.pa165.facade.CarFacade;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cars")
@Validated
@Tag(name = "Car Controller",
        description = "Controller for car operations")
public class CarRestController {

    private final CarFacade carFacade;
    private final UriBuilderService uriBuilderService;

    public CarRestController(CarFacade carFacade, UriBuilderService uriBuilderService) {
        this.carFacade = carFacade;
        this.uriBuilderService = uriBuilderService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a car by ID",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_read"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_read"}),
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Car found",
                            content = @Content(schema = @Schema(implementation = CarViewDto.class))),
                    @ApiResponse(responseCode = "404",
                            description = "Car not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<CarViewDto> findCarById(@PathVariable("id") Long id) {
        CarViewDto car = carFacade.findById(id);
        return ResponseEntity.ok(car);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new car",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_write"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_write"}),
            },
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Car created successfully",
                            content = @Content(schema = @Schema(implementation = CarCreateViewDto.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid car data"),
                    @ApiResponse(responseCode = "500",
                            description = "Called external service failed, see message for more details"),
                    @ApiResponse(responseCode = "502",
                            description = "Called external service wrong response, see message for more details"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")

            })
    public ResponseEntity<CarViewDto> saveCar(@RequestBody @Valid CarCreateViewDto carCreateViewDto) {
        CarViewDto carViewDto = carFacade.create(carCreateViewDto);
        return ResponseEntity.created(uriBuilderService.getLocationOfCreatedResource(carViewDto)).body(carViewDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a car by ID",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_1"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_1"}),
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Car deleted successfully"),
                    @ApiResponse(responseCode = "404",
                            description = "Car not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<Void> deleteCarById(@PathVariable("id") Long id) {
        carFacade.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @Operation(summary = "Delete all cars",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_1"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_1"}),
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "All cars deleted successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<Void> deleteAllCars() {
        carFacade.deleteAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Find all cars",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_read"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_read"}),
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Cars retrieved successfully",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CarViewDto.class)))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<List<CarViewDto>> findAllCars() {
        List<CarViewDto> cars = carFacade.findAll();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/carMake/{carMake}")
    @Operation(summary = "Get cars by their car make",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_read"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_read"}),
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Cars retrieved successfully",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CarViewDto.class)))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<List<CarViewDto>> findCarsByCarMake(@PathVariable("carMake") String carMake) {
        List<CarViewDto> cars = carFacade.findByCarMake(carMake);
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/mainDriver/{mainDriverId}")
    @Operation(summary = "Get cars by their main driver ID",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_read"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_read"}),
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Cars retrieved successfully",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CarViewDto.class)))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<List<CarViewDto>> findCarsMainDriver(@PathVariable("mainDriverId") Long mainDriverId) {
        List<CarViewDto> cars = carFacade.findByMainDriver(mainDriverId);
        return ResponseEntity.ok(cars);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a car",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_read"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_read"}),
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Car updated successfully"),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid car data"),
                    @ApiResponse(responseCode = "404",
                            description = "Car not found"),
                    @ApiResponse(responseCode = "500",
                            description = "Called external service failed, see message for more details"),
                    @ApiResponse(responseCode = "502",
                            description = "Called external service wrong response, see message for more details"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<Void> updateCar(@RequestBody @Valid Car car) {
        carFacade.update(car);
        return ResponseEntity.ok().build();
    }
}
