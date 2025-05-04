package com.example.backend.controller;

import com.example.backend.models.SizeName;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/sizes")
public class SizeController {

    @GetMapping
    public ResponseEntity<List<String>> getAllSizes() {
        List<String> sizes = Arrays.stream(SizeName.values())
                .map(SizeName::getValue)
                .toList();
        return ResponseEntity.ok(sizes);
    }
}
