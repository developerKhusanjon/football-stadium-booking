package org.stadium.adminapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stadium.adminapi.controller.error.BadRequestAlertException;
import org.stadium.adminapi.service.AdminService;
import org.stadium.adminapi.service.dto.AdminDto;
import org.stadium.adminapi.service.dto.AlertResponseDto;
import org.stadium.adminapi.service.dto.JWTTokenDto;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public ResponseEntity<Page<AdminDto>> getAllAdmin(Pageable pageable) {
        Page<AdminDto> adminDtoPage = adminService.findAll(pageable);
        return ResponseEntity.ok(adminDtoPage);
    }

    @GetMapping("/{adminId}")
    public ResponseEntity<AdminDto> getOneAdmin(@PathVariable Long adminId) throws BadRequestAlertException {
        if(adminId == null) {
            throw new BadRequestAlertException("id must be null","admin","id");
        }
        AdminDto adminDto = adminService.findOne(adminId);
        return ResponseEntity.ok(adminDto);
    }
    @Operation(description = "Register user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "if user is created successfully, return JWT and send sms to phone number",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JWTTokenDto.class))}),
            @ApiResponse(responseCode = "409", description = "required fields",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class),
                            examples = {@ExampleObject(value = "{username:'username is required'...}")})),
            @ApiResponse(responseCode = "400", description = "Other situations throw  BadRequestAlert exception",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestAlertException.class)))})
    @PostMapping
    public ResponseEntity<JWTTokenDto> createAdmin(@RequestBody @Valid AdminDto adminDto) throws Exception {
        if(adminDto.getId() != null){
            throw new BadRequestAlertException("Id is coming", "Admin","adminId");
        }
        return ResponseEntity.ok(adminService.save(adminDto));
    }

    @PutMapping
    public ResponseEntity<JWTTokenDto> updateAdmin(@RequestBody @Valid AdminDto adminDto) throws Exception {
        if(adminDto.getId() == null){
            throw new BadRequestAlertException("Id is coming null", "Admin","adminId");
        }
        return ResponseEntity.ok(adminService.save(adminDto));
    }

    @DeleteMapping("/{adminId}")
    public ResponseEntity<AlertResponseDto> deleteAdmin(@PathVariable Long adminId) throws Exception {
        return ResponseEntity.ok(adminService.delete(adminId));
    }

}
