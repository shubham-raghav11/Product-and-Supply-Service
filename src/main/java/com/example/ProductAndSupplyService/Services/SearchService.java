package com.example.ProductAndSupplyService.Services;

import com.example.ProductAndSupplyService.Entity.Images;
import com.example.ProductAndSupplyService.Entity.Products;
import com.example.ProductAndSupplyService.Enums.Category;
import com.example.ProductAndSupplyService.Exceptions.ImageNotFoundException;
import com.example.ProductAndSupplyService.Exceptions.ProductNotFoundException;
import com.example.ProductAndSupplyService.ResponseDTO.ProductResponseDto;
import com.example.ProductAndSupplyService.SuccessResponse.SuccessResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SearchService {

    @Autowired
    private ProductService productService;

    @Autowired
    private ImageService imageService;

    private ProductResponseDto getProductResponseDto(Products product) {

        return ProductResponseDto.builder()
                .productName(product.getProductName())
                .description(product.getDescription())
                .brandName(product.getBrandName())
                .images(product.getImages()) // Ensure this is correctly mapped
                .price(85.00)
                .build();
    }

    //////////////////////////// get product by productId or productName or by both (productId and productName). ///////////////////////////////////////////////////////

//    public ResponseEntity<?> getProducts(Long id, String product_Name) {
//        if (id == null && product_Name == null) {
//            throw new ProductNotFoundException("Provide at least one filter criteria either :- (id or product_Name)");
//        }
//
////        if(id!=null && product_Name==null){
////            list.add(getProductById(id));
////        }
////        else{
////            list = productRepository.findByProductName(productName);
////        }
//        return ResponseEntity.ok(new ArrayList<>());
//  }


//////////////////////////////////// getting all products.(will try to apply the limit also)//////////////////////////////////////////////////////

    public ResponseEntity<List<Products>> getAllProducts(Integer pageNumber, Integer size){

        if (pageNumber < 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid pageNumber or size");
        }

        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<Products> productPage = productService.getAllProducts(pageable);

        List<Products> productsList = productPage.getContent();

        return ResponseEntity.ok(productsList);
    }

/////////////////////////////////// get products by category ////////////////////////////////////////////////////////////

    public ResponseEntity<List<Products>> getProductByCategory(String category) {

        Category categoryEnum = Category.valueOf(category.toUpperCase());

        List<Products> products = productService.getProductByCategory(categoryEnum);

        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found for category: " + category);
        }

        return ResponseEntity.ok(products);
    }

////////////////////////////////// get product by productName. //////////////////////////////////////////////////////////////////////////

    public ResponseEntity<List<Products>> getProductByName(String productName) {

        List<Products> products = productService.getProductByName(productName);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("Invalid product_Name: Product not found");
        }
        return ResponseEntity.ok(products);
    }

////////////////////////////////// Get products by brand name //////////////////////////////////////////////////////////

    public ResponseEntity<List<ProductResponseDto>> getProductByBrand(String brandName){
        List<Products> list = productService.getProductByBrand(brandName);

        if(list.isEmpty()){
            throw new ProductNotFoundException("Product not found for brand name :- " + brandName);
        }

        List<ProductResponseDto> res = new ArrayList<>();

        for(Products product : list){
            ProductResponseDto responses = getProductResponseDto(product);
            res.add(responses);
        }

        return ResponseEntity.ok(res);
    }

///////////////////////////////// get product by productId ////////////////////////////////////////////////////////

    public ResponseEntity<ProductResponseDto> getProductById(Integer id){

        Products product = productService.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Invalid product id: Product not found"));

        return ResponseEntity.ok(getProductResponseDto(product));
    }

////////////////////////////////////////// Image searching ///////////////////////////////////////////////////////

    /////////////////////////////////////// get image by image id //////////////////////////////////////////

    public ResponseEntity<SuccessResponseDto> getImageByImageId(Integer imageId){
        Optional<Images> opt = imageService.findById(imageId);

        if (opt.isEmpty()) {
            throw new ImageNotFoundException("Image with ID " + imageId + " not found.");
        }

        SuccessResponseDto response = new SuccessResponseDto("Image found successfully",opt.get());

        return ResponseEntity.ok(response);
    }

    ////////////////////////////////////// Get all image of a specific product ////////////////////////////////////

    public ResponseEntity<List<String>> getImagesById(Integer id){

        List<Images> images = imageService.findByProducts_Id(id);

        if (images.isEmpty()) {
            throw new ImageNotFoundException("Image not found for product id :- " + id);
        }
        List<String> imageUrls = new ArrayList<>();

        for (Images image : images) {
            imageUrls.add(image.getImageUrl()); // Extracting image URL
        }
        return ResponseEntity.ok(imageUrls);
    }
}
