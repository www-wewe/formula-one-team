package org.muni.pa165.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import org.muni.pa165.api.RaceCreateViewDto;
import org.muni.pa165.api.RaceView;
import org.muni.pa165.api.RaceViewDto;
import org.muni.pa165.config.AppConfig;
import org.muni.pa165.data.domain.Race;
import org.muni.pa165.facade.RaceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/races")
public class RaceRestController {

    private final RaceFacade raceFacade;
    private final UriBuilderService uriBuilderService;

    @Autowired
    public RaceRestController(RaceFacade raceFacade, UriBuilderService uriBuilderService) {
        this.raceFacade = raceFacade;
        this.uriBuilderService = uriBuilderService;
    }

    @PostMapping
    @Operation(summary = "Create a new race",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_write"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_write"}),
            },
            responses = {
                    @ApiResponse(description = "Race created successfully",
                            responseCode = "201",
                            content = @Content(schema = @Schema(implementation = RaceCreateViewDto.class))),
                    @ApiResponse(description = "Invalid race data",
                            responseCode = "400"),
                    @ApiResponse(responseCode = "500",
                            description = "Called external service failed, see message for more details"),
                    @ApiResponse(responseCode = "502",
                            description = "Called external service wrong response, see message for more details"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<RaceViewDto> saveRace(@RequestBody RaceCreateViewDto raceViewDto) {
        RaceViewDto createdRaceDto = raceFacade.save(raceViewDto);
        return ResponseEntity.created(uriBuilderService.getLocationOfCreatedResource(createdRaceDto)).body(createdRaceDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single race by ID",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_read"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_read"}),
            },
            responses = {
                    @ApiResponse(description = "Successful retrieval",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = RaceViewDto.class))),
                    @ApiResponse(description = "Race not found",
                            responseCode = "404"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<RaceViewDto> findById(@PathVariable Long id) {
        RaceViewDto race = raceFacade.findById(id);
        return ResponseEntity.ok(race);
    }

    @GetMapping
    @Operation(summary = "Find all races",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_read"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_read"}),
            },
            responses = {
                    @ApiResponse(description = "Successful retrieval",
                            responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RaceViewDto.class)))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<List<RaceViewDto>> findAllRaces() {
        List<RaceViewDto> races = raceFacade.findAll();
        return ResponseEntity.ok(races);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a race by ID",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_1"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_1"}),
            },
            responses = {
                    @ApiResponse(description = "Race deleted successfully",
                            responseCode = "200"),
                    @ApiResponse(description = "Race not found",
                            responseCode = "404"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<Void> deleteRaceById(@PathVariable Long id) {
        raceFacade.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @Operation(summary = "Delete all races",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_1"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_1"}),
            },
            responses = {
                    @ApiResponse(description = "All races deleted successfully",
                            responseCode = "200"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<Void> deleteAllRaces() {
        raceFacade.deleteAll();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a race",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_write"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_write"}),
            },
            responses = {
                    @ApiResponse(description = "Component updated successfully",
                            responseCode = "200"),
                    @ApiResponse(description = "Race not found",
                            responseCode = "404"),
                    @ApiResponse(description = "Invalid race data",
                            responseCode = "400"),
                    @ApiResponse(responseCode = "500",
                            description = "Called external service failed, see message for more details"),
                    @ApiResponse(responseCode = "502",
                            description = "Called external service wrong response, see message for more details"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<Void> updateRace(@RequestBody Race race) {
        raceFacade.update(race);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/assignCarOne")
    @Operation(summary = "Assign first car for race",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_write"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_write"}),
            },
            responses = {
                    @ApiResponse(description = "Car assigned successfully",
                            responseCode = "200"),
                    @ApiResponse(description = "Invalid data",
                            responseCode = "400"),
                    @ApiResponse(description = "Car not found",
                            responseCode = "404"),
                    @ApiResponse(responseCode = "500",
                            description = "Called external service failed, see message for more details"),
                    @ApiResponse(responseCode = "502",
                            description = "Called external service wrong response, see message for more details"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<Void> assignCarOne(@PathVariable Long id, @RequestParam("carId") Long carId) {
        raceFacade.assignCarOne(id, carId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/assignCarTwo")
    @Operation(summary = "Assign second car for race",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_write"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_write"}),
            },
            responses = {
                    @ApiResponse(description = "Car assigned successfully",
                            responseCode = "200"),
                    @ApiResponse(description = "Invalid race data",
                            responseCode = "400"),
                    @ApiResponse(description = "Race not found",
                            responseCode = "404"),
                    @ApiResponse(responseCode = "500",
                            description = "Called external service failed, see message for more details"),
                    @ApiResponse(responseCode = "502",
                            description = "Called external service wrong response, see message for more details"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<?> assignCarTwo(@PathVariable Long id, @RequestParam("carId") Long carId) {
        raceFacade.assignCarTwo(id, carId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/location")
    @Operation(summary = "Get races by location",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_read"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_read"}),
            },
            responses = {
                    @ApiResponse(description = "Successful retrieval",
                            responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RaceViewDto.class)))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<List<RaceViewDto>> findByLocation(@RequestParam @Nullable String country, @RequestParam @Nullable String city, @RequestParam @Nullable String street) {
        List<RaceViewDto> races = raceFacade.findByLocation_CountryOrLocation_CityOrLocation_Street(country, city, street);
        return ResponseEntity.ok(races);
    }

    @GetMapping("/car/{carId}")
    @Operation(summary = "Get races by car ID",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_read"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_read"}),
            },
            responses = {
                    @ApiResponse(description = "Successful retrieval",
                            responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RaceViewDto.class)))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<List<RaceViewDto>> findByCarId(@PathVariable Long carId) {
        List<RaceViewDto> races = raceFacade.findByCarId(carId);
        return ResponseEntity.ok(races);
    }

    @GetMapping("/withCars")
    @Operation(summary = "Get all races with main drivers and cars",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_read"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_read"}),
            },
            responses = {
                    @ApiResponse(description = "Successful retrieval",
                            responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RaceView.class)))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<List<RaceView>> findAllWithCars() {
        List<RaceView> races = raceFacade.findAllWithCars();
        return ResponseEntity.ok(races);
    }
}
