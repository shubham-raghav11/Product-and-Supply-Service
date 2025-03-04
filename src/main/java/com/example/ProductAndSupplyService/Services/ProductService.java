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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


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
    private ImageService imageService;

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

    private ProductResponseDto getProductResponseDto(Products product) {

        return ProductResponseDto.builder()
                .productName(product.getProductName())
                .description(product.getDescription())
                .brandName(product.getBrandName())
                .images(product.getImages()) // Ensure this is correctly mapped
                .price(85.00)
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

//     this method checks that the image is valid (eg :- .jpg, .png type)
     private void validateImageLink(List<String> imageUrls){

         boolean hasInvalidUrl = imageUrls.stream()
                 .anyMatch(url -> url == null || url.trim().isEmpty() || !isValidImageFormat(url));

         System.out.println("Validation executed. Invalid URL found: " + hasInvalidUrl);

         if (hasInvalidUrl) {
             throw new ImageNotFoundException("Image URL cannot be blank and must be in .jpg or .png format");
         }

     }
    // Helper method to check if URL has a valid format
    private boolean isValidImageFormat(String imageUrl) {
        return imageUrl.matches("(?i)^https?://.*\\.(jpg|jpeg|png)$");
    }

///////////////////////////////// Creating the product ///////////////////////////////////////////////////////////////

    public ResponseEntity<ProductResponseDto> create(ProductCreationRequestDto productCreationRequestDto){

        try {
//             Check if images are provided
            validateImageLink(productCreationRequestDto.getImages());

            // Creating the product (without images)
            Products products = createProductDtoToProduct(productCreationRequestDto);

            // Save the product first in order to tell Hibernate which product the images belong to.
            products = productRepository.save(products);

            // creating images from imageDto
            List<Images> savedImages = createImageDtoToImage(productCreationRequestDto.getImages(), products);
            products.setImages(savedImages);

            // Convert to Response DTO
            ProductResponseDto responseDto = getProductResponseDto(products);

            // Return ResponseEntity with the built response
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        }
        catch (ImageNotFoundException e){
            throw new ImageNotFoundException("Each image URL must be valid and in .jpg or .png format");
        }
        catch (Exception e) {
            throw new InternalServerErrorException("Cannot create the product due to an Internal Server Error");
        }
    }


/////////////////////////////////updating the existing products on the basis of the productId./////////////////////////////////////////////////////////////

    public ResponseEntity<ProductResponseDto> update(Integer id, ProductUpdationRequestDto productUpdationRequestDto) {
        // Fetch existing product
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found for the given product id"));

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
            validateImageLink(productUpdationRequestDto.getImages());

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
        ProductResponseDto responseDto = getProductResponseDto(product);
        // Return success response with updated product (including images)
        return ResponseEntity.ok(responseDto);
    }

/////////////////////////////////// Delete the product means soft deleting the products. /////////////////////////////////////////////

    public ResponseEntity<SuccessResponseDto> deleteProduct(Integer id){

        Optional<Products> product = productRepository.findById(id);

        if (product.isEmpty()) {
            throw new ProductNotFoundException("Invalid product id: Product not found");
        }
        product.get().setIsActive(false);
        productRepository.save(product.get());


        SuccessResponseDto response = new SuccessResponseDto("Product deleted successfully");

        return ResponseEntity.ok(response);
    }


/////////////////////////////// methods to be called by the search service ///////////////////////////////

    public Optional<Products> findById(Integer id) {
        return productRepository.findById(id);
    }

    public List<Products> getProductByCategory(Category category) {
        return productRepository.findProductByCategory(category);
    }

    public List<Products> getProductByName(String productName) {
        return productRepository.findByProductName(productName);
    }

    public List<Products> getProductByBrand(String brandName) {
        return productRepository.findProductByBrandName(brandName);
    }

    public Page<Products> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}

