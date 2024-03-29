package com.shopao.alura.ecommerce.controller;

import com.shopao.alura.ecommerce.entity.Role;
import com.shopao.alura.ecommerce.entity.User;
import com.shopao.alura.ecommerce.model.UserDTO;
import com.shopao.alura.ecommerce.model.UserResponseDto;
import com.shopao.alura.ecommerce.service.UserService;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopao/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody UserDTO userDto) {
    try {
      User registeredUser = userService.registerNewUserAccount(userDto);
      UserResponseDto responseDto = new UserResponseDto(
          registeredUser.getId(),
          registeredUser.getUsername(),
          registeredUser.getEmail(),
          registeredUser.getRoles().stream()
              .map(Role::getName)
              .collect(Collectors.toSet())
      );
      return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    } catch (UserAlreadyExistException ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }
}
