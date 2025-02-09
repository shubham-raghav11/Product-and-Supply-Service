package com.example.ProductAndSupplyService.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Images")
public class Images {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @ManyToOne()
    @JoinColumn(name = "product_Id")
    @JsonIgnore
    private Products products;

    @NotBlank(message = "Image URL cannot be blank")
    @Column(name = "image_Url")
    private String imageUrl;

    @Column(name = "created_By", updatable = false)
    private String createdBy;

    @Column(name = "created_At", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_By")
    private String updatedBy;

    @Column(name = "updated_At")
    private LocalDateTime updatedAt;

    @Column(name = "is_Primary")
    private Boolean isPrimary;

    public Boolean getIsPrimary(Boolean b) {
        return b;
    }
}

