package com.example.ProductAndSupplyService.Entity;


import com.example.ProductAndSupplyService.Enums.Category;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_Name", nullable = false)
    private String productName;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<Images> images;

    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "is_Active")
    private Boolean isActive;

    @Column(name = "created_At", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_By", updatable = false)
    private String createdBy;

    @Column(name = "updated_At")
    private LocalDateTime updatedAt;

    @Column(name = "updated_By")
    private String updatedBy;

    @Column(name = "brand_Name", nullable = false)
    private String brandName;

}

