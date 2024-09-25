package com.msfb.borrowease.controller;

import com.msfb.borrowease.constant.ApiRoute;
import com.msfb.borrowease.model.request.StaffRequest;
import com.msfb.borrowease.model.response.CommonResponse;
import com.msfb.borrowease.model.response.StaffResponse;
import com.msfb.borrowease.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(ApiRoute.STAFF_API)
@Tag(name = "Staff", description = "Staff API")
public class StaffController {
    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @Operation(
            summary = "Get all staffs",
            description = "API to get all staffs"
    )
    @SecurityRequirement(name = "Authorization")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<List<StaffResponse>>> getAllStaffs(){
        List<StaffResponse> staffResponses = staffService.getAllStaffs();
        CommonResponse<List<StaffResponse>> response = CommonResponse.<List<StaffResponse>>builder()
                .message("Staffs retrieved successfully")
                .statusCode(HttpStatus.OK.value())
                .data(staffResponses)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get staff by id",
            description = "API to get staff by id"
    )
    @SecurityRequirement(name = "Authorization")
    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<StaffResponse>> getStaffById(@PathVariable String id){
        StaffResponse staffResponse = staffService.getStaffById(id);
        CommonResponse<StaffResponse> response = CommonResponse.<StaffResponse>builder()
                .message("Staff retrieved successfully")
                .statusCode(HttpStatus.OK.value())
                .data(staffResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update staff",
            description = "API to update staff"
    )
    @SecurityRequirement(name = "Authorization")
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<StaffResponse>> updateStaff(@RequestBody StaffRequest request){
        StaffResponse staffResponse = staffService.updateStaff(request);
        CommonResponse<StaffResponse> response = CommonResponse.<StaffResponse>builder()
                .message("Staff updated successfully")
                .statusCode(HttpStatus.OK.value())
                .data(staffResponse)
                .build();
        return ResponseEntity.ok(response);
    }
}
