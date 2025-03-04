package com.example.ProductAndSupplyService.Controllers;

import com.example.ProductAndSupplyService.Entity.Products;
import com.example.ProductAndSupplyService.RequestDTO.ProductCreationRequestDto;
import com.example.ProductAndSupplyService.RequestDTO.ProductUpdationRequestDto;
import com.example.ProductAndSupplyService.ResponseDTO.ProductResponseDto;
import com.example.ProductAndSupplyService.Services.ProductService;

import com.example.ProductAndSupplyService.Services.SearchService;
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

    @Autowired
    private  SearchService searchService;

///////////////////////////////////////// Creation of Products //////////////////////////////////////////////////
    @PostMapping("/create")
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody ProductCreationRequestDto productCreationRequestDto) {
        return productService.create(productCreationRequestDto);
    }

/////////////////////////////////////////// update the existing product ///////////////////////////////////////////
     @PutMapping("/update/{id}")
     public ResponseEntity<ProductResponseDto> update(@PathVariable Integer id, @RequestBody ProductUpdationRequestDto productUpdationRequestDto){
         return productService.update(id, productUpdationRequestDto);
     }

//////////////////////////////////////////// soft Deletion of product. ////////////////////////////////////////

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SuccessResponseDto> deleteProduct(@PathVariable Integer id){
        return productService.deleteProduct(id);
    }

///////////////////////////////////////////// Searching service Api's ////////////////////////////////////////////////

    // // Flexible searching of products
     // get products by id and name
//    @GetMapping("/")
//    public ResponseEntity<?> getProduct(@RequestParam(required = false) Long id, @RequestParam(required = false) String product_Name){
//        return productService.getProducts(id,product_Name);
//    }


    // get all products
    @GetMapping("/search/products/all")
    public ResponseEntity<List<Products>> getAllProducts(@RequestParam(defaultValue = "0", required = false )Integer pageNumber,
                                                 @RequestParam(defaultValue = "10", required = false) Integer size){
        return searchService.getAllProducts(pageNumber,size);
    }

    //// get products by productName

     @GetMapping("/search/products/{productName}")
     public ResponseEntity<List<Products>> getProductByName(@PathVariable String productName){
         return searchService.getProductByName(productName);
     }

     @GetMapping("/search/products/category/{category}")
     public ResponseEntity<List<Products>> getProductByCategory(@PathVariable String category){

        return searchService.getProductByCategory(category);
     }

    @GetMapping("/search/products/id/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Integer id){
        return searchService.getProductById(id);
    }

    @GetMapping("/search/products/brand/{brandName}")
    public ResponseEntity<List<ProductResponseDto>> getProductByBrand(@PathVariable String brandName){
        return searchService.getProductByBrand(brandName);
    }


}


