package com.example.ProductAndSupplyService.Services;


import com.example.ProductAndSupplyService.Entity.Images;
import com.example.ProductAndSupplyService.Entity.Products;
import com.example.ProductAndSupplyService.Exceptions.ImageNotFoundException;
import com.example.ProductAndSupplyService.Exceptions.InternalServerErrorException;
import com.example.ProductAndSupplyService.Exceptions.ProductNotFoundException;
import com.example.ProductAndSupplyService.Repository.ProductRepository;
import com.example.ProductAndSupplyService.RequestDTO.ProductCreationRequestDto;
import com.example.ProductAndSupplyService.RequestDTO.ProductUpdationRequestDto;
import com.example.ProductAndSupplyService.ResponseDTO.ProductResponseDto;
import com.example.ProductAndSupplyService.Enums.Category;

import com.example.ProductAndSupplyService.SuccessResponse.SuccessResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {


    @Autowired
    private ProductRepository productRepository;

    @Lazy
    @Autowired
    private ImageService imageService;   // this is to be changed

    ////////////////////////////////// used to create the product from dto ////////////////////////////////////////////////////////

    private Products createProductDtoToProduct(ProductCreationRequestDto productCreationRequestDto){

        return Products.builder()
                .productName(productCreationRequestDto.getProductName())
                .description(productCreationRequestDto.getDescription())
                .isActive(true)
                .brandName(productCreationRequestDto.getBrandName())
                .createdAt(LocalDateTime.now())
                .createdBy("raghav")
                .updatedAt(LocalDateTime.now())
                .updatedBy("raghav")
                .category(productCreationRequestDto.getCategory())
                .build();
    }

    private ProductResponseDto convertProductToResponseDto(Products product) {

        return ProductResponseDto.builder()
                .productName(product.getProductName())
                .description(product.getDescription())
                .brandName(product.getBrandName())
                .images(product.getImages()) // Ensure this is correctly mapped
                .price(85.00)
//                .message("Product retrieved successfully") // Optional message
                .build();
    }

    private List<Images> createImageDtoToImage(List<String> imageList, Products product){

        List<Images> list = new ArrayList<>();

        for (String imgdetail : imageList) {
            Images image = Images.builder()
                    .imageUrl(imgdetail)
                    .products(product) // Now the product is already saved, so no transient exception
                    .createdBy("vimlesh_raghav")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .updatedBy("vimlesh_raghav")
                    .build();

            imageService.save(image);

            list.add(image);
        }
        return list;
    }

    private void validateImages(List<String> list){

        if (CollectionUtils.isEmpty(list)) {
            throw new ImageNotFoundException("Please provide at least one image of the product");
        }

        if (list.stream()
                .anyMatch(img -> img == null || img.trim().isEmpty())) {
            throw new ImageNotFoundException("Image URL cannot be blank");
        }
    }

    public ResponseEntity<ProductResponseDto> create(ProductCreationRequestDto productCreationRequestDto){

        try {
//             Check if images are provided
            validateImages(productCreationRequestDto.getImages());

            // Creating the product (without images)
            Products products = createProductDtoToProduct(productCreationRequestDto);

            // Save the product first in order to tell Hibernate which product the images belong to.
            products = productRepository.save(products);

            // creating images from imageDto
            List<Images> savedImages = createImageDtoToImage(productCreationRequestDto.getImages(), products);
            products.setImages(savedImages);

            // Convert to Response DTO
            ProductResponseDto responseDto = convertProductToResponseDto(products);

            // Return ResponseEntity with the built response
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        }
        catch (Exception e) {
            throw new InternalServerErrorException("Cannot create the product due to an Internal Server Error");
        }
    }


/////////////////////////////////updating the existing products on the basis of the productId./////////////////////////////////////////////////////////////

    public ResponseEntity<ProductResponseDto> update(Long id, ProductUpdationRequestDto productUpdationRequestDto) {
        // Fetch existing product
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found for the given productId"));

        // Update fields only if they're not null
        if (productUpdationRequestDto.getProductName() != null) {
            product.setProductName(productUpdationRequestDto.getProductName());
        }
        if (productUpdationRequestDto.getDescription() != null) {
            product.setDescription(productUpdationRequestDto.getDescription());
        }
        if (productUpdationRequestDto.getBrandName() != null) {
            product.setBrandName(productUpdationRequestDto.getBrandName());
        }
        if (productUpdationRequestDto.getCategory() != null) {
            product.setCategory(productUpdationRequestDto.getCategory());
        }
        if (productUpdationRequestDto.getIsActive() != null) {
            product.setIsActive(productUpdationRequestDto.getIsActive());
        }

        // Always update timestamps
        product.setUpdatedAt(LocalDateTime.now());
        product.setUpdatedBy("Garima Raghav");

        // Save updated product first
        productRepository.save(product);

        // Handle Image Updates (Append New Images)
        if (productUpdationRequestDto.getImages() != null && !productUpdationRequestDto.getImages().isEmpty()) {
            validateImages(productUpdationRequestDto.getImages());

            // Save each image and associate with the product
            List<Images> newImages = productUpdationRequestDto.getImages().stream()
                    .map(imgDetail -> Images.builder()
                            .imageUrl(imgDetail)
                            .products(product)
                            .createdAt(LocalDateTime.now())
                            .createdBy("MR. Yogendra Singh Raghav")
                            .updatedAt(LocalDateTime.now())
                            .updatedBy("user")
                            .build())
                    .toList();

            // Save new images in bulk
            imageService.saveAll(newImages);

            // Initialize if null, (if someone deleted the only single image of product then it may cause Null pointer exception
            if (product.getImages() == null) {
                product.setImages(new ArrayList<>()); // Initialize if null
            }
            // Append new images to existing ones
            product.getImages().addAll(newImages);
        }
        ProductResponseDto responseDto = convertProductToResponseDto(product);
        // Return success response with updated product (including images)
        return ResponseEntity.ok(responseDto);
    }

///////////////////////////////////////// getting all products.(will try to apply the limit also)//////////////////////////////////////////////////////

    public ResponseEntity<List<Products>> getAllProducts(){
        List<Products> list = productRepository.findAll();
        return ResponseEntity.ok(list);
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

/////////////////////////////////////////// get products by category ////////////////////////////////////////////////////////////

public ResponseEntity<List<Products>> getByCategory(String category) {

    Category categoryEnum = Category.valueOf(category.toUpperCase());

    List<Products> products = productRepository.findByCategory(categoryEnum);

    if (products.isEmpty()) {
        throw new ProductNotFoundException("No products found for category: " + category);
    }

    return ResponseEntity.ok(products);
}

//    /////////////////////////////////////////////// get product by productName. //////////////////////////////////////////////////////////////////////////

    public ResponseEntity<List<Products>> getProductByName(String productName) {

        List<Products> products = productRepository.findByProductName(productName);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("Invalid product_Name: Product not found");
        }
        return ResponseEntity.ok(products);
    }

////////////////////////////////// Get products by brand name //////////////////////////////////////////////////////////

    public ResponseEntity<List<ProductResponseDto>> getByBrand(String brandName){
        List<Products> list = productRepository.findByBrandName(brandName);

        if(list.isEmpty()){
            throw new ProductNotFoundException("Product not found for brand name :- " + brandName);
        }

        List<ProductResponseDto> res = new ArrayList<>();

        for(Products product : list){
            ProductResponseDto responses = convertProductToResponseDto(product);
            res.add(responses);
        }

        return ResponseEntity.ok(res);
    }

//    /////////////////////////////////////////////// get product by productId ////////////////////////////////////////////////////////

   public ResponseEntity<ProductResponseDto> getProductById(Long id){

       Products product = productRepository.findById(id)
               .orElseThrow(() -> new ProductNotFoundException("Invalid product id: Product not found"));
      
               return ResponseEntity.ok(convertProductToResponseDto(product));
   }

//    ///////////////////////////////// Delete the product means soft deleting the products. /////////////////////////////////////////////
//
    public ResponseEntity<SuccessResponseDto> deleteProduct(Long id){

        Optional<Products> product = productRepository.findById(id);

        if (product.isEmpty()) {
            throw new ProductNotFoundException("Invalid product id: Product not found");
        }
        product.get().setIsActive(false);
        productRepository.save(product.get());


        SuccessResponseDto response = new SuccessResponseDto("Product deleted successfully");

        return ResponseEntity.ok(response);
    }

    public Optional<Products> findById(Long id) {
        return productRepository.findById(id);
    }
}

