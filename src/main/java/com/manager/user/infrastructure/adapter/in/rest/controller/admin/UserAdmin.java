package com.manager.user.infrastructure.adapter.in.rest.controller.admin;


import com.manager.user.infrastructure.adapter.in.rest.dto.request.UserAdminDTO;
import com.manager.user.infrastructure.adapter.in.rest.dto.response.UserAdminResponseDTO;
import com.manager.user.infrastructure.adapter.in.rest.dto.request.UserAdminUpdateDTO;
import com.manager.user.domain.service.admin.UserAdminService;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.finance.infrastructure.adapter.in.rest.error.ErrorHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin/user")
@Slf4j
@RequiredArgsConstructor
public class UserAdmin {
    private final UserAdminService userAdminService;
    private final ErrorHelper errorHelper;

    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    public List<UserAdminResponseDTO> getUsers() {
        return userAdminService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:read')")
    public UserAdminResponseDTO getUser(@PathVariable("id") UserEntity user) {
        return userAdminService.get(user);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<Object> createUser(@Valid UserAdminDTO userAdminDTO, BindingResult bindingResult) {
        var responseEntity = errorHelper.checkErrors(bindingResult);
        if (responseEntity == null) {
            responseEntity = ResponseEntity.ok(userAdminService.createAndGetDTO(userAdminDTO));
        }
        return responseEntity;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<Object> updateUser(@PathVariable("id") UserEntity user, @Valid UserAdminUpdateDTO userUpdateDTO,
                                             BindingResult bindingResult) {
        var responseEntity = errorHelper.checkErrors(bindingResult);
        if (responseEntity == null) {
            responseEntity = ResponseEntity.ok(userAdminService.update(user, userUpdateDTO));
        }
        return responseEntity;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") UserEntity user) {
        return ResponseEntity.ok(userAdminService.delete(user));
    }

}

