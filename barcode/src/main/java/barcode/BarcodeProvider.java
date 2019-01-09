package barcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import exceptions.QRException;
import org.apache.commons.lang3.RandomStringUtils;
import persistence.Datastore;
import tokens.TokenProvider;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BarcodeProvider {
    private static final int QR_SIZE = 300;

    private TokenProvider tokenProvider;
    private Datastore datastore;

    public BarcodeProvider(TokenProvider tokenProvider, Datastore datastore) {
        this.tokenProvider = tokenProvider;
        this.datastore = datastore;
    }

    public List<String> getTokens(String userName, String userId, int numberOfTokens) throws QRException {
        if (numberOfTokens < 1 || numberOfTokens > 5)
            throw new IllegalArgumentException("The number of tokens must be between 1 and 5");
        int numberOfUnusedTokens = this.datastore.getNumberOfUnusedTokens(userName);
        if (numberOfUnusedTokens > 1)
            throw new IllegalArgumentException("Number of unused tokens is more than 1");
        List<String> tokens = new ArrayList<>();
        for (int i = 0; i < numberOfTokens; i++) {
            String token = this.tokenProvider.issueToken(userName, userId);
            tokens.add(token);
            this.generateQRCode(token);
        }
        this.datastore.addTokens(tokens.size(), userName);
        return tokens;
    }

    public boolean useToken(String tokenString) {
        boolean tokenValid = this.tokenProvider.checkToken(tokenString) && !this.datastore.isTokenUsed(tokenString);
        if (tokenValid)
            this.datastore.useToken(tokenString);
        return tokenValid;
    }

    private void generateQRCode(String tokenString) throws QRException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String directory = System.getProperty("user.dir") + "/images/";
        String randomString = RandomStringUtils.randomAlphabetic(10);
        Path path = FileSystems.getDefault().getPath(directory + randomString+ ".png");
        BitMatrix bitMatrix;
        try {
            bitMatrix = qrCodeWriter.encode(tokenString, BarcodeFormat.QR_CODE, QR_SIZE, QR_SIZE);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        } catch (WriterException | IOException e) {
            throw new QRException(e.getMessage(), e);
        }

    }
}
