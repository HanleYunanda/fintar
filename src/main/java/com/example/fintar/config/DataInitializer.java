package com.example.fintar.config;

import com.example.fintar.entity.Role;
import com.example.fintar.entity.User;
import com.example.fintar.repository.RoleRepository;
import com.example.fintar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            Role customer = Role.builder().name("CUSTOMER").isActive(true).build();
            Role marketing = Role.builder().name("MARKETING").isActive(true).build();
            Role bm = Role.builder().name("BRANCH MANAGER").isActive(true).build();
            Role bo = Role.builder().name("BACK OFFICE").isActive(true).build();
            Role admin = Role.builder().name("ADMIN").isActive(true).build();
            roleRepository.save(customer);
            roleRepository.save(marketing);
            roleRepository.save(bm);
            roleRepository.save(bo);
            roleRepository.save(admin);

            if(userRepository.count() == 0) {
                User user1 = User.builder()
                        .username("ADMIN USER")
                        .email("admin@gmail.com")
                        .password("Admin123!")
                        .isActive(true)
                        .roles(Set.of(admin))
                        .build();
                userRepository.save(user1);
            }
        }
    }
}
