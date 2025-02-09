package com.example.ProductAndSupplyService.Controllers;

import com.example.ProductAndSupplyService.Entity.Products;
import com.example.ProductAndSupplyService.RequestDTO.ProductCreationRequestDto;
import com.example.ProductAndSupplyService.RequestDTO.ProductUpdationRequestDto;
import com.example.ProductAndSupplyService.ResponseDTO.ProductResponseDto;
import com.example.ProductAndSupplyService.Services.ProductService;

import com.example.ProductAndSupplyService.SuccessResponse.SuccessResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Creation of Products
    @PostMapping("/create")
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody ProductCreationRequestDto productCreationRequestDto) {
        return productService.create(productCreationRequestDto);
    }

    // update the existing product
     @PutMapping("/update/{id}")
     public ResponseEntity<ProductResponseDto> update(@PathVariable Long id, @RequestBody ProductUpdationRequestDto productUpdationRequestDto){
         return productService.update(id, productUpdationRequestDto);
     }

    // get all products
     @GetMapping("/all")
     public ResponseEntity<List<Products>> getAll(){
         return productService.getAllProducts();
     }

    // // Flexible searching of products
     // get products by id and name
//    @GetMapping("/")
//    public ResponseEntity<?> getProduct(@RequestParam(required = false) Long id, @RequestParam(required = false) String product_Name){
//        return productService.getProducts(id,product_Name);
//    }

    // // get products by productName

     @GetMapping("/name/{productName}")
     public ResponseEntity<List<Products>> getProductByName(@PathVariable String productName){
         return productService.getProductByName(productName);
     }

     @GetMapping("/category/{category}")
     public ResponseEntity<List<Products>> getByCategory(@PathVariable String category){

        return productService.getByCategory(category);
     }

    @GetMapping("id/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @GetMapping("/{brandName}")
    public ResponseEntity<List<ProductResponseDto>> getByBrand(@PathVariable String brandName){
        return productService.getByBrand(brandName);
    }

    // ////////////////////////////////////////// soft Deletion of product. ////////////////////////////////////////

     @DeleteMapping("/delete/{id}")
     public ResponseEntity<SuccessResponseDto> deleteProduct(@PathVariable Long id){
         return productService.deleteProduct(id);
     }
}


