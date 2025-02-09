package com.example.ProductAndSupplyService.Repository;

import com.example.ProductAndSupplyService.Enums.Category;
import com.example.ProductAndSupplyService.Entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products,Long> {


    List<Products> findByProductName(String productName);

    List<Products> findByCategory(Category category);

    List<Products> findByBrandName(String brandName);

    // Optional<Products> findByProductIdAndProductName(Long id, String product_Name);

//     List<Products> findAll();

    // Optional<Products findById(Long id);
}

