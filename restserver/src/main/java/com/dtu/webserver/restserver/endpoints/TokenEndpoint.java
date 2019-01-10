package com.dtu.webserver.restserver.endpoints;

import barcode.BarcodeProvider;
import exceptions.QRException;
import models.BarcodeTokenPair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            Set<BarcodeTokenPair> tokens = this.barcodeProvider.getTokens(username, userId, numberOfTokens);
            return ResponseEntity.status(HttpStatus.OK).body(tokens);
        } catch (QRException e) {
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
}
