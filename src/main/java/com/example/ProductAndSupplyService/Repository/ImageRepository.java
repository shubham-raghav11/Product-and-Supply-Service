package com.example.ProductAndSupplyService.Repository;

import com.example.ProductAndSupplyService.Entity.Images;
import com.example.ProductAndSupplyService.Entity.Products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository  extends JpaRepository<Images,Long> {

    List<Images> findByProducts(Products products);

    void deleteById(Long imageId);

    List<Images> findByProducts_Id(Long id);

    @Modifying
    @Query("DELETE FROM Images i WHERE i.id IN :imageIds")
    void deleteImagesByIds(@Param("imageIds") List<Long> imageIds);

}
