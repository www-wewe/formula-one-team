package org.muni.pa165.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.muni.pa165.api.DriverCreateViewDto;
import org.muni.pa165.api.DriverViewDto;
import org.muni.pa165.config.AppConfig;
import org.muni.pa165.data.domain.Driver;
import org.muni.pa165.data.domain.DriverPerk;
import org.muni.pa165.facade.DriverFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/drivers")
public class DriverRestController {

    private final DriverFacade driverFacade;
    private final UriBuilderService uriBuilderService;

    @Autowired
    public DriverRestController(DriverFacade driverFacade, UriBuilderService uriBuilderService) {
        this.driverFacade = driverFacade;
        this.uriBuilderService = uriBuilderService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single driver by ID",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_read"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_read"}),
            },
            responses = {
                    @ApiResponse(description = "Successful retrieval",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = DriverViewDto.class))),
                    @ApiResponse(description = "driver not found",
                            responseCode = "404"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<DriverViewDto> findDriverById(@PathVariable Long id) {
        DriverViewDto driverViewDto = driverFacade.findById(id);
        return ResponseEntity.ok(driverViewDto);
    }

    @PostMapping
    @Operation(summary = "Create a new driver",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_write"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_write"}),
            },
            responses = {
                    @ApiResponse(description = "driver created successfully",
                            responseCode = "201",
                            content = @Content(schema = @Schema(implementation = DriverCreateViewDto.class))),
                    @ApiResponse(description = "Invalid driver data",
                            responseCode = "400"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<DriverViewDto> saveDriver(@RequestBody DriverCreateViewDto driverViewDto) {
        DriverViewDto savedDriver = driverFacade.save(driverViewDto);
        return ResponseEntity.created(uriBuilderService.getLocationOfCreatedResource(savedDriver)).body(savedDriver);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a driver by ID",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_1"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_1"}),
            },
            responses = {
                    @ApiResponse(description = "Driver deleted successfully",
                            responseCode = "200"),
                    @ApiResponse(description = "Driver not found",
                            responseCode = "404"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<Void> deleteDriverById(@PathVariable Long id) {
        driverFacade.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Find all drivers",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_read"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_read"}),
            },
            responses = {
                    @ApiResponse(description = "Successful retrieval",
                            responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = DriverViewDto.class)))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<List<DriverViewDto>> findAllDrivers() {
        List<DriverViewDto> drivers = driverFacade.findAll();
        return ResponseEntity.ok(drivers);
    }

    @GetMapping("/perk/{perk}")
    @Operation(summary = "Get drivers by perk",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_read"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_read"}),
            },
            responses = {
                    @ApiResponse(description = "Successful retrieval",
                            responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = DriverViewDto.class)))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<List<DriverViewDto>> findDriversByPerk(@PathVariable("perk") DriverPerk perk) {
        List<DriverViewDto> drivers = driverFacade.findByPerk(perk);
        return ResponseEntity.ok(drivers);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a driver",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_write"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_write"}),
            },
            responses = {
                    @ApiResponse(description = "Driver updated successfully",
                            responseCode = "200"),
                    @ApiResponse(description = "Invalid driver data",
                            responseCode = "400"),
                    @ApiResponse(description = "Driver not found",
                            responseCode = "404"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<Void> updateDriver(@RequestBody Driver driver) {
        driverFacade.update(driver);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/nationality/{nationality}")
    @Operation(summary = "Get drivers by nationality",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_read"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_read"}),
            },
            responses = {
                    @ApiResponse(description = "Successful retrieval",
                            responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = DriverViewDto.class)))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<List<DriverViewDto>> findDriversByNationality(@PathVariable("nationality") String nationality) {
        List<DriverViewDto> drivers = driverFacade.findByNationality(nationality);
        return ResponseEntity.ok(drivers);
    }

    @DeleteMapping
    @Operation(summary = "Delete all drivers",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_1"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_1"}),
            },
            responses = {
                    @ApiResponse(description = "All drivers deleted successfully",
                            responseCode = "200"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<Void> deleteAllDrivers() {
        driverFacade.deleteAll();
        return ResponseEntity.ok().build();
    }
}
