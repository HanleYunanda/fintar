package com.example.fintar.entity;

import com.example.fintar.enums.DocType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import tools.jackson.core.ObjectReadContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "documents")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE documents SET is_deleted = 1 WHERE id = ?")
@SQLRestriction("is_deleted = 0")
public class Document extends BaseEntity {
    private String fileName;
    private String fileUri;
    private String contentType;
    private Long size;
    @Enumerated(EnumType.STRING)
    private DocType docType;

    @ManyToOne
    private CustomerDetail customerDetail;

//    @ManyToMany
//    @JoinTable(
//            name = "loan_documents",
//            joinColumns = @JoinColumn(name = "document_id"),
//            inverseJoinColumns = @JoinColumn(name = "loan_id")
//    )
//    private List<Loan> loans;
}
