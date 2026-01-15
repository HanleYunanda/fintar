package com.example.fintar.config;

import com.example.fintar.entity.Permission;
import com.example.fintar.entity.Role;
import com.example.fintar.entity.User;
import com.example.fintar.repository.PermissionRepository;
import com.example.fintar.repository.RoleRepository;
import com.example.fintar.repository.UserRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final PermissionRepository permissionRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  public void run(String... args) throws Exception {
    if (permissionRepository.count() == 0) {
      List<Permission> permissions =
          Arrays.asList(
              Permission.builder().code("CREATE_USER").build(),
              Permission.builder().code("READ_USER").build(),
              Permission.builder().code("UPDATE_USER").build(),
              Permission.builder().code("DELETE_USER").build(),
              Permission.builder().code("CREATE_ROLE").build(),
              Permission.builder().code("READ_ROLE").build(),
              Permission.builder().code("UPDATE_ROLE").build(),
              Permission.builder().code("DELETE_ROLE").build(),
              Permission.builder().code("ASSIGN_PERMISSION").build(),
              Permission.builder().code("CREATE_PERMISSION").build(),
              Permission.builder().code("READ_PERMISSION").build(),
              Permission.builder().code("DELETE_PERMISSION").build(),
              Permission.builder().code("CREATE_CUSTOMER_DETAIL").build(),
              Permission.builder().code("READ_CUSTOMER_DETAIL").build(),
              Permission.builder().code("UPDATE_CUSTOMER_DETAIL").build(),
              Permission.builder().code("CREATE_PLAFOND").build(),
              Permission.builder().code("READ_PLAFOND").build(),
              Permission.builder().code("UPDATE_PLAFOND").build(),
              Permission.builder().code("DELETE_PLAFOND").build(),
              Permission.builder().code("CREATE_PRODUCT").build(),
              Permission.builder().code("READ_PRODUCT").build());
      permissions = permissionRepository.saveAll(permissions);

      permissionRepository.saveAll(Arrays.asList(
              Permission.builder().code("READ_LOAN").build(),
              Permission.builder().code("CREATE_LOAN").build(),
              Permission.builder().code("REVIEW_LOAN").build(),
              Permission.builder().code("APPROVE_LOAN").build(),
              Permission.builder().code("REJECT_LOAN").build()
      ));

      if (roleRepository.count() == 0) {
        Role customer = Role.builder().name("CUSTOMER").build();
        Role marketing = Role.builder().name("MARKETING").build();
        Role bm = Role.builder().name("BRANCH_MANAGER").build();
        Role bo = Role.builder().name("BACK_OFFICE").build();
        Role admin = Role.builder().name("ADMIN").permissions(new HashSet<>(permissions)).build();
        roleRepository.save(customer);
        roleRepository.save(marketing);
        roleRepository.save(bm);
        roleRepository.save(bo);
        roleRepository.save(admin);

        if (userRepository.count() == 0) {
          User adminUser =
              User.builder()
                  .username("ADMIN_USER")
                  .email("admin@gmail.com")
                  .password(bCryptPasswordEncoder.encode("Admin123!"))
                  .isActive(true)
                  .roles(Set.of(admin))
                  .build();

          User boUser =
              User.builder()
                  .username("BACK_OFFICE_USER")
                  .email("backoffice@gmail.com")
                  .password(bCryptPasswordEncoder.encode("BackOffice123!"))
                  .isActive(true)
                  .roles(Set.of(bo))
                  .build();

          User bmUser =
              User.builder()
                  .username("BRANCH_MANAGER_USER")
                  .email("branchmanager@gmail.com")
                  .password(bCryptPasswordEncoder.encode("BranchManager123!"))
                  .isActive(true)
                  .roles(Set.of(bm))
                  .build();

          User marketingUser =
              User.builder()
                  .username("MARKETING_USER")
                  .email("marketing@gmail.com")
                  .password(bCryptPasswordEncoder.encode("Marketing123!"))
                  .isActive(true)
                  .roles(Set.of(marketing))
                  .build();

          User customer1User =
              User.builder()
                  .username("CUSTOMER1_USER")
                  .email("customer1@gmail.com")
                  .password(bCryptPasswordEncoder.encode("Customer1123!"))
                  .isActive(true)
                  .roles(Set.of(customer))
                  .build();

          userRepository.save(adminUser);
          userRepository.save(boUser);
          userRepository.save(bmUser);
          userRepository.save(marketingUser);
          userRepository.save(customer1User);
        }
      }
    }
  }
}
