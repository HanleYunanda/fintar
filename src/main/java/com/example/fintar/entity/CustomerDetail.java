package com.example.fintar.entity;

import com.example.fintar.enums.HouseStatus;
import com.example.fintar.enums.MaritalStatus;
import com.example.fintar.enums.Religion;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "customer_details")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE customer_details SET is_deleted = 1 WHERE id = ?")
@SQLRestriction("is_deleted = 0")
public class CustomerDetail extends BaseEntity implements Serializable {

  @OneToOne private User user;

  private String fullName;

  private String nationalId;

  private String citizenship;

  private String placeOfBirth;

  private LocalDateTime dateOfBirth;

  private Boolean isMale;

  @Enumerated(EnumType.STRING)
  private Religion religion;

  @Enumerated(EnumType.STRING)
  private MaritalStatus maritalStatus;

  private String phoneNumber;

  private String address;

  private String zipCode;

  @Enumerated(EnumType.STRING)
  private HouseStatus houseStatus;

  private String job;

  private String workplace;

  private Double salary;

  private String accountNumber;

  @ManyToOne
  private Plafond plafond;

  private Double remainPlafond;

  @OneToMany(mappedBy = "customerDetail")
  private List<Document> documents;
}
