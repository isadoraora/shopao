package com.shopao.alura.ecommerce.service;

import com.shopao.alura.ecommerce.entity.Role;
import com.shopao.alura.ecommerce.entity.User;
import com.shopao.alura.ecommerce.model.UserDTO;
import com.shopao.alura.ecommerce.repository.RoleRepository;
import com.shopao.alura.ecommerce.repository.UserRepository;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;

  public User registerNewUserAccount(UserDTO userDto) {
    if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
      throw new UserAlreadyExistException(
          "There is an account with that username: " + userDto.getUsername());
    }

    User user = new User();
    user.setUsername(userDto.getUsername());
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));

    Set<Role> roles = new HashSet<>();
    if (userDto.getRoles() == null || userDto.getRoles().isEmpty()) {
      Role userRole = roleRepository.findByName("ROLE_USER")
          .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));
      roles.add(userRole);
    } else {
      userDto.getRoles().forEach(roleName -> {
        Role role = roleRepository.findByName(roleName)
            .orElseThrow(() -> new RuntimeException("Error: Role " + roleName + " is not found."));
        roles.add(role);
      });
    }
    user.setRoles(roles);

    return userRepository.save(user);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(user));
  }

  private Collection<? extends GrantedAuthority> getAuthorities(User user) {
    return user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toSet());
  }
}
