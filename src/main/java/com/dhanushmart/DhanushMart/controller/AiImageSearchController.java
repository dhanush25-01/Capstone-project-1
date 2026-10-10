package com.dhanushmart.DhanushMart.controller;

import com.dhanushmart.DhanushMart.service.AiImageSearchService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiImageSearchController {

    private final AiImageSearchService aiImageSearchService;

    public AiImageSearchController(AiImageSearchService aiImageSearchService) {
        this.aiImageSearchService = aiImageSearchService;
    }

    @PostMapping(value = "/image-search", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> imageSearch(@RequestPart("image") MultipartFile image) {
        try {
            return ResponseEntity.ok(aiImageSearchService.searchByImage(image));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "AI image search failed. Check your API key and try again."));
        }
    }
}
