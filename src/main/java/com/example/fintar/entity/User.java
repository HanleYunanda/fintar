package com.example.fintar.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean isActive;

    /*
      id uuid pk DONE
      username string DONE
      email string DONE
      password string DONE
      branch_id uuid
      isActive boolean DONE
      created_by uuid
      created_at timestamp
      updated_by uuid
      updated_at timestamp
    */
}
