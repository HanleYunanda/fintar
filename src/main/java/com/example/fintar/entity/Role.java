package com.example.fintar.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    /*
      id uuid pk DONE
      name string DONE
      created_by uuid
      created_at timestamp
      updated_by uuid
      updated_at timestamp
    */

}
