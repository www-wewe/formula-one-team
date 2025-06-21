package org.muni.pa165.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.muni.pa165.api.ComponentCreateViewDto;
import org.muni.pa165.api.ComponentViewDto;
import org.muni.pa165.config.AppConfig;
import org.muni.pa165.data.domain.Component;
import org.muni.pa165.data.enums.ComponentType;
import org.muni.pa165.facade.ComponentFacade;
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
@RequestMapping("/components")
public class ComponentRestController {

    private final ComponentFacade componentFacade;
    private final UriBuilderService uriBuilderService;

    @Autowired
    public ComponentRestController(ComponentFacade componentFacade, UriBuilderService uriBuilderService) {
        this.componentFacade = componentFacade;
        this.uriBuilderService = uriBuilderService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single component by ID",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_read"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_read"}),
            },
            responses = {
                    @ApiResponse(description = "Successful retrieval",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ComponentViewDto.class))),
                    @ApiResponse(description = "Component not found",
                            responseCode = "404"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<ComponentViewDto> findComponentById(@PathVariable Long id) {
        ComponentViewDto component = componentFacade.findById(id);
        return ResponseEntity.ok(component);
    }

    @PostMapping
    @Operation(summary = "Create a new component",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_write"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_write"}),
            },
            responses = {
                    @ApiResponse(description = "Component created successfully",
                            responseCode = "201",
                            content = @Content(schema = @Schema(implementation = ComponentCreateViewDto.class))),
                    @ApiResponse(description = "Invalid component data",
                            responseCode = "400"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<ComponentViewDto> saveComponent(@RequestBody ComponentCreateViewDto componentDto) {
        ComponentViewDto createdComponent = componentFacade.save(componentDto);
        return ResponseEntity.created(uriBuilderService.getLocationOfCreatedResource(createdComponent)).body(createdComponent);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a component by ID",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_1"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_1"}),
            },
            responses = {
                    @ApiResponse(description = "Component deleted successfully",
                            responseCode = "200"),
                    @ApiResponse(description = "Component not found",
                            responseCode = "404"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<Void> deleteComponentById(@PathVariable Long id) {
        componentFacade.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Find all components",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_read"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_read"}),
            },
            responses = {
                    @ApiResponse(description = "Successful retrieval",
                            responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ComponentViewDto.class)))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<List<ComponentViewDto>> findAllComponents() {
        List<ComponentViewDto> components = componentFacade.findAll();
        return ResponseEntity.ok(components);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a component",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_write"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_write"}),
            },
            responses = {
                    @ApiResponse(description = "Component updated successfully",
                            responseCode = "200"),
                    @ApiResponse(description = "Component not found",
                            responseCode = "404"),
                    @ApiResponse(description = "Invalid component data",
                            responseCode = "400"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<Void> updateComponent(@RequestBody Component component) {
        componentFacade.update(component);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping
    @Operation(summary = "Delete all components",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_1"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_1"}),
            },
            responses = {
                    @ApiResponse(description = "All components deleted successfully",
                            responseCode = "200"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<Void> deleteAllComponents() {
        componentFacade.deleteAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get components by type",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_read"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_read"}),
            },
            responses = {
                    @ApiResponse(description = "Successful retrieval",
                            responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ComponentViewDto.class)))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<List<ComponentViewDto>> findComponentsByType(@PathVariable ComponentType type) {
        List<ComponentViewDto> components = componentFacade.findByType(type);
        return ResponseEntity.ok(components);
    }

    @GetMapping("/manufacturer/{manufacturer}")
    @Operation(summary = "Get components by manufacturer",
            security = {
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_BEARER,
                            scopes = {"SCOPE_test_read"}),
                    @SecurityRequirement(name = AppConfig.SECURITY_SCHEME_OAUTH2,
                            scopes = {"SCOPE_test_read"}),
            },
            responses = {
                    @ApiResponse(description = "Successful retrieval",
                            responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ComponentViewDto.class)))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            })
    public ResponseEntity<List<ComponentViewDto>> findComponentsByManufacturer(@PathVariable String manufacturer) {
        List<ComponentViewDto> components = componentFacade.findByManufacturer(manufacturer);
        return ResponseEntity.ok(components);
    }
}
