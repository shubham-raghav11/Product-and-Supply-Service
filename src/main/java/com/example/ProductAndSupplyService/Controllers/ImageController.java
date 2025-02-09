 package com.example.ProductAndSupplyService.Controllers;

 import com.example.ProductAndSupplyService.RequestDTO.ImageUpdateRequestDto;
 import com.example.ProductAndSupplyService.RequestDTO.ImageUploadRequestDto;
import com.example.ProductAndSupplyService.Services.ImageService;

 import com.example.ProductAndSupplyService.SuccessResponse.SuccessResponseDto;
 import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.*;

 import java.util.List;

 @RestController
 @RequestMapping("/api/v1/images")
 public class ImageController {

     @Autowired
     private ImageService imageService;

    @PostMapping("/{id}")
    public ResponseEntity<SuccessResponseDto> upload(@PathVariable Long id,@Valid @RequestBody ImageUploadRequestDto imageUploadRequestDto) {
        return imageService.upload(id, imageUploadRequestDto);
    }

     @PutMapping("/update/{imageId}")
     public ResponseEntity<SuccessResponseDto> update(@PathVariable Long imageId, @Valid @RequestBody ImageUpdateRequestDto imageUpdateRequestDto) {
         return imageService.update(imageId,imageUpdateRequestDto);
     }

    @DeleteMapping("/{id}/{imageId}")
    public ResponseEntity<SuccessResponseDto> deleteProductImage(@PathVariable Long id, @PathVariable Long imageId) {
        return imageService.deleteProductImage(id, imageId);
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<SuccessResponseDto> getImageByImageId(@PathVariable Long imageId){
        return imageService.getImageByImageId(imageId);
   }

     @GetMapping("/product/{id}")
     public ResponseEntity<List<String>> getImagesById(@PathVariable Long id) {
         return imageService.getImagesById(id);
     }

     @DeleteMapping("/bulk")
     public ResponseEntity<SuccessResponseDto> deleteMultipleImages(@RequestBody List<Long> imageIds) {
         return imageService.deleteMultipleImages(imageIds);
     }
 }
