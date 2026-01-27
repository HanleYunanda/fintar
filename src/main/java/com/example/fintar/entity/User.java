package com.example.fintar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE users SET is_deleted = 1 WHERE id = ?")
@SQLRestriction("is_deleted = 0")
public class User extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(name = "fcm_token")
  private String fcmToken;

  @Column(nullable = false)
  @JsonIgnore
  private String password;

  @Column(nullable = false)
  private Boolean isActive;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  @OneToOne(mappedBy = "user")
  private CustomerDetail customerDetail;

  @OneToMany(mappedBy = "performedBy")
  private List<LoanStatusHistory> loanStatusHistory;

  /*
   * id uuid pk DONE
   * username string DONE
   * email string DONE
   * password string DONE
   * branch_id uuid
   * isActive boolean DONE
   * created_by uuid
   * created_at timestamp
   * updated_by uuid
   * updated_at timestamp
   */
}
