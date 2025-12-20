package com.example.fintar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Boolean isActive;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<User> users;

    /*
      id uuid pk DONE
      name string DONE
      is_active DONE
      created_by uuid
      created_at timestamp
      updated_by uuid
      updated_at timestamp
    */

}
