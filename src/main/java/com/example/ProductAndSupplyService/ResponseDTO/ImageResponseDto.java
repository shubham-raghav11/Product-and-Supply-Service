package com.example.ProductAndSupplyService.ResponseDTO;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageResponseDto {
    
    private List<String> images;
}
