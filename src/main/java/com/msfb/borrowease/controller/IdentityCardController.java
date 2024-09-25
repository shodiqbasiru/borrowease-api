package com.msfb.borrowease.controller;

import com.msfb.borrowease.constant.ApiRoute;
import com.msfb.borrowease.entity.IdentityCard;
import com.msfb.borrowease.model.response.CommonResponse;
import com.msfb.borrowease.service.IdentityCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiRoute.IDENTITY_CARD_API)
@Tag(name = "Identity Card", description = "Identity Card API")
public class IdentityCardController {
    private final IdentityCardService identityCardService;

    @Autowired
    public IdentityCardController(IdentityCardService identityCardService) {
        this.identityCardService = identityCardService;
    }

    @Operation(
            summary = "Create new Identity Card",
            description = "API to create new Identity Card"
    )
    @SecurityRequirement(name = "Authorization")
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<IdentityCard>> createIdentityCard(@RequestBody IdentityCard identityCard) {
        IdentityCard identityCardResponse = identityCardService.createIdentityCard(identityCard);
        CommonResponse<IdentityCard> response = CommonResponse.<IdentityCard>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Identity Card created successfully")
                .data(identityCardResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get all Identity Cards",
            description = "API to get all Identity Cards"
    )
    @SecurityRequirement(name = "Authorization")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<List<IdentityCard>>> getAllIdentityCards() {
        List<IdentityCard> identityCardResponses = identityCardService.getAllIdentityCards();
        CommonResponse<List<IdentityCard>> responses = CommonResponse.<List<IdentityCard>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("All Identity Cards retrieved successfully")
                .data(identityCardResponses)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @Operation(
            summary = "Get Identity Card by id",
            description = "API to get Identity Card by id"
    )
    @SecurityRequirement(name = "Authorization")
    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<IdentityCard>> getIdentityCardById(@PathVariable String id) {
        IdentityCard identityCardResponse = identityCardService.getById(id);
        CommonResponse<IdentityCard> response = CommonResponse.<IdentityCard>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Identity Card retrieved successfully")
                .data(identityCardResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Update Identity Card",
            description = "API to update Identity Card"
    )
    @SecurityRequirement(name = "Authorization")
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<IdentityCard>> updateIdentityCard(@RequestBody IdentityCard identityCard) {
        IdentityCard identityCardResponse = identityCardService.updateIdentityCard(identityCard);
        CommonResponse<IdentityCard> response = CommonResponse.<IdentityCard>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Identity Card updated successfully")
                .data(identityCardResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Delete Identity Card",
            description = "API to delete Identity Card"
    )
    @SecurityRequirement(name = "Authorization")
    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>> deleteIdentityCard(@PathVariable String id) {
        identityCardService.deleteIdentityCard(id);
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Identity Card deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
