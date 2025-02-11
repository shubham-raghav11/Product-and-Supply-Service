 package com.example.ProductAndSupplyService.Services;


 import com.example.ProductAndSupplyService.Entity.Images;
 import com.example.ProductAndSupplyService.Entity.Products;
import com.example.ProductAndSupplyService.Exceptions.ImageNotFoundException;
import com.example.ProductAndSupplyService.Exceptions.ProductNotFoundException;
 import com.example.ProductAndSupplyService.Repository.ImageRepository;
 import com.example.ProductAndSupplyService.RequestDTO.ImageUpdateRequestDto;
 import com.example.ProductAndSupplyService.RequestDTO.ImageUploadRequestDto;
import com.example.ProductAndSupplyService.ResponseDTO.ImageResponseDto;

 import com.example.ProductAndSupplyService.SuccessResponse.SuccessResponseDto;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.context.annotation.Lazy;
 import org.springframework.http.ResponseEntity;
 import org.springframework.stereotype.Service;
 import org.springframework.util.CollectionUtils;

 import java.time.LocalDateTime;
import java.util.ArrayList;
 import java.util.List;
 import java.util.Optional;
 import java.util.Set;
 import java.util.stream.Collectors;

 @Service
 public class ImageService {


     @Autowired
     private ImageRepository imageRepository;

     @Lazy
     @Autowired
     private ProductService productService;


    // method to create Image Response of images //

    private ImageResponseDto createImageResponseDtoFromImage(List<String> savedImagesUrls){

        return ImageResponseDto.builder()
                                .images(savedImagesUrls)
                            .build();
    }


    //////////////////////////////// to upload the image for the specific product ////////////////////////////////////////////

    public ResponseEntity<SuccessResponseDto> upload(Integer id, ImageUploadRequestDto imageUploadRequestDto){
        
            if (id == null) {
                throw new ProductNotFoundException("Provide the product id which you want to add image in");
            }
        
        Optional<Products> product = productService.findById(id);

        if(product.isEmpty()){
           throw new ProductNotFoundException("Enter the existing product id you want to add image in");
        }

        Products result = product.get();

        // to store images in order to send the response 
        List<String> savedImageUrls = new ArrayList<>();

        for(String image : imageUploadRequestDto.getImages()) {
            Images images = Images.builder()
                    .imageUrl(image)
                    .products(result)
                    .createdBy("garima")
                    .createdAt(LocalDateTime.now())
                    .updatedBy("admin")
                    .updatedAt(LocalDateTime.now())
                    .build();

            imageRepository.save(images);
            savedImageUrls.add(image);
        }

        SuccessResponseDto response = new SuccessResponseDto("Image uploaded successfully in your product with id :- " + id);

        return ResponseEntity.ok(response);
    }

///////////////////////// to update the specific image of the specific product //////////////////////////////////////

     public ResponseEntity<SuccessResponseDto> update(Integer imageId, ImageUpdateRequestDto imageUpdateRequestDto){

         Images img = imageRepository.findById(imageId)
                 .orElseThrow(() -> new ImageNotFoundException("Image with ID " + imageId + " not found."));

         if (imageUpdateRequestDto.getNewImage() == null || imageUpdateRequestDto.getNewImage().trim().isEmpty()) {
             throw new IllegalArgumentException("Please provide a valid image URL.");
         }

         img.setImageUrl(imageUpdateRequestDto.getNewImage());
         img.setUpdatedAt(LocalDateTime.now());
         img.setUpdatedBy("shubham");
         img.setIsPrimary(true);

         // Save and return the updated entity
         imageRepository.save(img);

         SuccessResponseDto response = new SuccessResponseDto("Image updated successfully", img);
         return ResponseEntity.ok(response);
     }

/////////////////////////////////// Delete the image of the product you want //////////////////////////////////////////////

    public ResponseEntity<SuccessResponseDto> deleteProductImage(Integer id, Integer imageId) {
        Optional<Products> product = productService.findById(id);

        if (product.isEmpty()) {
            throw new ProductNotFoundException("Product with ID " + id + " not found.");
        }

        Optional<Images> imageOptional = imageRepository.findById(imageId);

        if (imageOptional.isEmpty()) {
            throw new ImageNotFoundException("Image with ID " + imageId + " not found.");
        }

        Images image = imageOptional.get();

        if (!image.getProducts().getId().equals(id)) {
            throw new ImageNotFoundException("Image does not belong to the given product.");
        }
        imageRepository.deleteById(imageId);

        SuccessResponseDto response = new SuccessResponseDto("Image with ID " + imageId + " deleted successfully.");

        return ResponseEntity.ok(response);
    }

     /////////////////////////////////////// Bulk delete images ///////////////////////////////////////////////////

     public ResponseEntity<SuccessResponseDto> deleteMultipleImages(List<Integer> imageIds) {
         if (CollectionUtils.isEmpty(imageIds)) {
             throw new IllegalArgumentException("Image IDs list cannot be empty.");
         }
         // Convert list to Set for uniqueness
         Set<Integer> uniqueImageIds = Set.copyOf(imageIds);

         // Fetch existing images in a single query
         List<Images> imagesToDelete = imageRepository.findAllById(uniqueImageIds);

         // Identify missing image IDs
         Set<Integer> foundIds = imagesToDelete.stream()
                 .map(Images::getImageId)
                 .collect(Collectors.toSet());

         List<Integer> missingIds = uniqueImageIds.stream()
                 .filter(id -> !foundIds.contains(id))
                 .toList();

         if (!missingIds.isEmpty()) {
             throw new ImageNotFoundException("No images found for the provided IDs: " + missingIds);
         }

         // Delete all images in a batch operation
         imageRepository.deleteAll(imagesToDelete);

         return ResponseEntity.ok(new SuccessResponseDto(imagesToDelete.size() + " images deleted successfully"));
     }

    ////////////////////////////////////// called by product service //////////////////////////////////////////////////////////

     public void save(Images image) {
          imageRepository.save(image);
     }

     public void saveAll(List<Images> newImages) {
         imageRepository.saveAll(newImages);
     }

     public Optional<Images> getImageByImageId(Integer imageId) {
        return imageRepository.findById(imageId);
     }

     public List<Images> findByProducts_Id(Integer id) {
        return imageRepository.findByProducts_Id(id);
     }

     public List<Images> findAllById(List<Integer> imageIds) {
        return imageRepository.findAllById(imageIds);
     }
 }

