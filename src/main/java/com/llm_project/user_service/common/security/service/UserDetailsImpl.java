package com.llm_project.user_service.common.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.llm_project.user_service.user.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

  private String userId;

  private String username;

  private String email;

  @JsonIgnore
  private String password;

  private Collection<? extends GrantedAuthority> authorities;

  public UserDetailsImpl(String userId, String username, String email, String password,
                         Collection<? extends GrantedAuthority> authorities) {
    this.userId = userId;
    this.username = username;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
  }

  public static UserDetailsImpl build(String username, String password) {
    UserDetailsImpl userDetails = new UserDetailsImpl();
    userDetails.username = username;
    userDetails.password = password;
    return userDetails;
  }

  public static UserDetailsImpl build(User user){
    List<GrantedAuthority> authorities = user.getRoles().stream()
        .map(role -> (GrantedAuthority) role::getRoleName)
        .toList();
    return new UserDetailsImpl(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getPasswordHash(),
        authorities);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(username, user.username);
  }
}
