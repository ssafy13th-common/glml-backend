package com.ssafy.a705.domain.member._auth.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssetLinkController {

    @GetMapping(value = "/.well-known/assetlinks.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAssetLinks() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/deeplink/assetlinks.json");
        String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        return ResponseEntity.ok(content);
    }
}
