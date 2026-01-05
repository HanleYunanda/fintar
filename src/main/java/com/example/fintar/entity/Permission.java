package com.example.fintar.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE permissions SET is_deleted = 1 WHERE id = ?")
@SQLRestriction("is_deleted = 0")
public class Permission extends BaseEntity implements Serializable {
  private String code;

  @ManyToMany(mappedBy = "permissions")
  private Set<Role> roles = new HashSet<>();
}
