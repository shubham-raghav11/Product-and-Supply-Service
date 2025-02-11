 package com.example.ProductAndSupplyService.Controllers;

import com.example.ProductAndSupplyService.RequestDTO.ImageUpdateRequestDto;
import com.example.ProductAndSupplyService.RequestDTO.ImageUploadRequestDto;
import com.example.ProductAndSupplyService.Services.ImageService;

import com.example.ProductAndSupplyService.Services.SearchService;
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

    @Autowired
    private SearchService searchService;

    @PostMapping("/{id}")
    public ResponseEntity<SuccessResponseDto> upload(@PathVariable Integer id,@Valid @RequestBody ImageUploadRequestDto imageUploadRequestDto) {
        return imageService.upload(id, imageUploadRequestDto);
    }

    @PutMapping("/update/{imageId}")
    public ResponseEntity<SuccessResponseDto> update(@PathVariable Integer imageId, @Valid @RequestBody ImageUpdateRequestDto imageUpdateRequestDto) {
        return imageService.update(imageId,imageUpdateRequestDto);
    }

   @DeleteMapping("/{id}/{imageId}")
   public ResponseEntity<SuccessResponseDto> deleteProductImage(@PathVariable Integer id, @PathVariable Integer imageId) {
       return imageService.deleteProductImage(id, imageId);
   }

   @DeleteMapping("/bulk")
   public ResponseEntity<SuccessResponseDto> deleteMultipleImages(@RequestBody List<Integer> imageIds) {
       return imageService.deleteMultipleImages(imageIds);
   }

//////////////////////////////////////////// Search api's /////////////////////////////////////////////////////////

   @GetMapping("/search/images/{imageId}")
   public ResponseEntity<SuccessResponseDto> getImageByImageId(@PathVariable Integer imageId){
       return searchService.getImageByImageId(imageId);
   }

   @GetMapping("search/images/product/{id}")
   public ResponseEntity<List<String>> getImagesById(@PathVariable Integer id) {
       return searchService.getImagesById(id);
   }


}
