package com.project.logistic_management_2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attached_images")
public class AttachedImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "type")
    private Integer type;

    @Column(name = "img_path")
    private String imgPath;

    @Column(name = "uploaded_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date uploadedAt;
}
