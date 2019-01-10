package com.dtu.webserver.restserver.endpoints;

import barcode.BarcodeProvider;
import com.dtu.webserver.restserver.models.TokenBarcodePathPair;
import com.dtu.webserver.restserver.utils.QRMapper;
import exceptions.QRException;
import models.TokenBarcodePair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(path = "v1/tokens")
public class TokenEndpoint {
    private BarcodeProvider barcodeProvider = new BarcodeProvider();

    @GetMapping("")
    public ResponseEntity getToken(@RequestParam(value = "username") String username,
                                   @RequestParam(value = "userId") String userId,
                                   @RequestParam(value = "numberOfTokens") Integer numberOfTokens) {
        try {
            Set<TokenBarcodePair> tokens = this.barcodeProvider.getTokens(username, userId, numberOfTokens);
            Set<TokenBarcodePathPair> finalTokens = new HashSet<>();
            for (TokenBarcodePair token : tokens) {
                finalTokens.add(new TokenBarcodePathPair(token.getToken(), QRMapper.saveQRToDisk(token.getBarcode())));
            }
            return ResponseEntity.status(HttpStatus.OK).body(finalTokens);
        } catch (QRException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity useToken(@RequestHeader(value = "Authorization") String token) {
        boolean success;
        try {
            success = this.barcodeProvider.useToken(token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        if (success)
            return ResponseEntity.status(HttpStatus.OK).build();
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/barcode/{fileName}")
    public ResponseEntity getBarcode(@PathVariable("fileName") String fileName) {
        String path = System.getProperty("user.dir") + QRMapper.IMAGE_DIR + fileName;
        Path filePath = FileSystems.getDefault().getPath(path);
        String type = this.getFileType(fileName);

        if(Files.exists(filePath) && type != null){
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("content-disposition", "attachment; filename=" + fileName);
            responseHeaders.add("Content-Type", "image/" + type);

            try {
                return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(Files.readAllBytes(filePath));
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File not found");
        }
    }

    private String getFileType(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index > 0)
            return fileName.substring(index + 1);
        else
            return null;
    }
}
