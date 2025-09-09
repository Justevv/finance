package com.manager.user.infrastructure.adapter.in.rest.controller.user;


import com.manager.user.domain.model.UserModel;
import com.manager.user.infrastructure.adapter.in.rest.dto.request.UserRequestDTO;
import com.manager.user.infrastructure.adapter.in.rest.dto.UserUpdateDTO;
import com.manager.user.infrastructure.adapter.in.rest.dto.request.UserRequestUpdateDTO;
import com.manager.user.infrastructure.adapter.in.rest.dto.response.UserResponseDTO;
import com.manager.finance.infrastructure.adapter.in.rest.error.ErrorHelper;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.domain.service.UserService;
import com.manager.user.infrastructure.adapter.in.rest.mapper.DtoMapper;
import com.manager.user.infrastructure.adapter.in.rest.mapper.UserDtoMapper;
import com.manager.user.infrastructure.adapter.out.persistence.mapper.UserEntityMapper;
import com.manager.user.infrastructure.adapter.out.persistence.mapper.UserPrincipalMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/v1/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ErrorHelper errorHelper;
    private final DtoMapper<UserRequestDTO, UserResponseDTO, UserModel> mapper;
    private final DtoMapper<UserRequestUpdateDTO, UserResponseDTO, UserModel> mapper2;
    private final UserPrincipalMapper principalMapper;

    @GetMapping
    @TrackExecutionTime
    public UserResponseDTO getUser(Principal principal) {
        return userService.getUser(principal);
    }

    @PostMapping
    @TrackExecutionTime
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserRequestDTO userRequestDto, BindingResult bindingResult) {
        ResponseEntity<Object> responseEntity = errorHelper.checkErrors(bindingResult);
        if (responseEntity == null) {
            var responseDTO = userService.create(mapper.toModel(userRequestDto));
            responseEntity = ResponseEntity.ok(responseDTO);
        }
        return responseEntity;
    }

    @PutMapping
    @TrackExecutionTime
    public ResponseEntity<Object> updateUser(Principal principal, @Valid UserRequestUpdateDTO crudDTO, BindingResult bindingResult) {
        var responseEntity = errorHelper.checkErrors(bindingResult);
        if (responseEntity == null) {
            responseEntity = ResponseEntity.ok(userService.update(principalMapper.toModel(principal), mapper2.toModel(crudDTO)));
        }
        return responseEntity;
    }

    @DeleteMapping
    @TrackExecutionTime
    public ResponseEntity<Void> deleteUser(Principal principal) {
        return ResponseEntity.ok(userService.delete(principalMapper.toModel(principal)));
    }

}