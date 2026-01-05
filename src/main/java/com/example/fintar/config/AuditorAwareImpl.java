package com.example.fintar.config;

import com.example.fintar.entity.UserPrincipal;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("auditorProvider")
public class AuditorAwareImpl implements AuditorAware<UUID> {

  @Override
  public Optional<UUID> getCurrentAuditor() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth == null || !auth.isAuthenticated()) {
      return Optional.of(UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }

    UserPrincipal user = (UserPrincipal) auth.getPrincipal();
    return Optional.of(user.getUser().getId());
  }
}
