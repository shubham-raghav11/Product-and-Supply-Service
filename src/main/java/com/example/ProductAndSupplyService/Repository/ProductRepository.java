package com.example.ProductAndSupplyService.Repository;

import com.example.ProductAndSupplyService.Enums.Category;
import com.example.ProductAndSupplyService.Entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Products,Integer> {


    List<Products> findByProductName(String productName);

    List<Products> findProductByCategory(Category category);

    List<Products> findProductByBrandName(String brandName);

    // Optional<Products> findByProductIdAndProductName(Long id, String product_Name);

     Page<Products> findAll(Pageable pageable);

    Optional<Products> findProductById(Integer id);

    // Optional<Products findById(Long id);
}

