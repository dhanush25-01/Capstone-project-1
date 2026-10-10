package com.dhanushmart.DhanushMart.service;

import com.dhanushmart.DhanushMart.model.Product;
import com.dhanushmart.DhanushMart.repository.ProductRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.Base64;

@Service
public class AiImageSearchService {

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${openai.api.key:}")
    private String apiKey;

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    public AiImageSearchService(ProductRepository productRepository, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    public Map<String, Object> searchByImage(MultipartFile image) throws Exception {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Please select a product image.");
        }
        if (image.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("Image must be 10 MB or smaller.");
        }

        String detectedDescription;
        List<String> terms;

        if (apiKey == null || apiKey.isBlank()) {
            detectedDescription = "AI image search is not configured yet. Set OPENAI_API_KEY and try again.";
            terms = List.of();
        } else {
            String result = analyzeImage(image);
            JsonNode root = objectMapper.readTree(result);
            String content = root.path("choices").path(0).path("message").path("content").asText("");

            // The model is asked for JSON, but this fallback also handles a fenced JSON response.
            content = content.replace("```json", "").replace("```", "").trim();
            JsonNode ai = objectMapper.readTree(content);
            detectedDescription = ai.path("description").asText("Product image");
            terms = new ArrayList<>();
            ai.path("keywords").forEach(n -> {
                String term = n.asText("").trim();
                if (!term.isBlank() && term.length() <= 60) terms.add(term);
            });
        }

        // Search the DhanushMart catalogue using AI-generated keywords.
        LinkedHashMap<Long, Product> unique = new LinkedHashMap<>();
        for (String term : terms) {
            for (Product p : productRepository.findByAiSearchTerm(term)) {
                unique.putIfAbsent(p.getId(), p);
            }
        }

        // If AI returns no useful keyword, return an empty result rather than guessing.
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("description", detectedDescription);
        response.put("keywords", terms);
        response.put("products", new ArrayList<>(unique.values()));
        response.put("configured", apiKey != null && !apiKey.isBlank());
        return response;
    }

    private String analyzeImage(MultipartFile image) throws Exception {
        byte[] bytes = StreamUtils.copyToByteArray(image.getInputStream());
        String mime = image.getContentType();
        if (mime == null || !mime.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are supported.");
        }

        String dataUrl = "data:" + mime + ";base64," + Base64.getEncoder().encodeToString(bytes);

        Map<String, Object> textPart = Map.of(
                "type", "text",
                "text", """
                        Identify the men's clothing product in this image for an online shop.
                        Return ONLY valid JSON in this exact shape:
                        {"description":"short description","keywords":["product type","color","style","material","pattern"]}
                        Use simple shopping keywords that are likely to appear in a product catalogue.
                        Do not invent a brand, size, price, or product ID.
                        """);

        Map<String, Object> imagePart = Map.of(
                "type", "image_url",
                "image_url", Map.of("url", dataUrl, "detail", "low"));

        Map<String, Object> message = Map.of(
                "role", "user",
                "content", List.of(textPart, imagePart));

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", model);
        body.put("messages", List.of(message));
        body.put("temperature", 0);
        body.put("max_tokens", 250);
        body.put("response_format", Map.of("type", "json_object"));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body)))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException("AI service returned HTTP " + response.statusCode() + ".");
        }
        return response.body();
    }
}
