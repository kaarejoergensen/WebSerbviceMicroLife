package models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BarcodeTokenPair {
    private String token;
    private String barcodePath;
}
