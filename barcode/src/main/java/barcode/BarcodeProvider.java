package barcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import exceptions.QRException;
import models.BarcodeTokenPair;
import org.apache.commons.lang3.RandomStringUtils;
import persistence.Datastore;
import persistence.MemoryDataStore;
import tokens.TokenProvider;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class BarcodeProvider {
    private static final int QR_SIZE = 300;

    private TokenProvider tokenProvider;
    private Datastore datastore;

    public BarcodeProvider() {
        this.tokenProvider = new TokenProvider();
        this.datastore = new MemoryDataStore(tokenProvider);
    }

    public BarcodeProvider(TokenProvider tokenProvider, Datastore datastore) {
        this.tokenProvider = tokenProvider;
        this.datastore = datastore;
    }

    public Set<BarcodeTokenPair> getTokens(String userName, String userId, int numberOfTokens) throws QRException {
        if (numberOfTokens < 1 || numberOfTokens > 5)
            throw new IllegalArgumentException("The number of tokens must be between 1 and 5");
        int numberOfUnusedTokens = this.datastore.getNumberOfUnusedTokens(userName);
        if (numberOfUnusedTokens > 1)
            throw new IllegalArgumentException("Number of unused tokens is more than 1");
        Set<BarcodeTokenPair> tokens = new HashSet<>();
        int count = datastore.getTotalNumberOfTokensIssued();
        for (int i = 0; i < numberOfTokens; i++) {
            String token = this.tokenProvider.issueToken(userName, userId, count++);
            String barcodePath = this.generateQRCode(token);
            tokens.add(new BarcodeTokenPair(token, barcodePath));
        }
        datastore.setTotalNumberOfTokensIssued(count);
        this.datastore.addTokens(tokens.size(), userName);
        return tokens;
    }

    public boolean useToken(String tokenString) {
        boolean tokenValid = this.tokenProvider.checkToken(tokenString) && !this.datastore.isTokenUsed(tokenString);
        if (tokenValid)
            this.datastore.useToken(tokenString);
        return tokenValid;
    }

    private String generateQRCode(String tokenString) throws QRException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String directory = System.getProperty("user.dir") + "/images/";
        String randomString = RandomStringUtils.randomAlphabetic(10);
        Path directoryPath = FileSystems.getDefault().getPath(directory);
        try {
            if (Files.notExists(directoryPath)) Files.createDirectory(directoryPath);
            Path path = FileSystems.getDefault().getPath(directory + randomString + ".png");

            BitMatrix bitMatrix = qrCodeWriter.encode(tokenString, BarcodeFormat.QR_CODE, QR_SIZE, QR_SIZE);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
            return path.toString();
        } catch (WriterException | IOException e) {
            throw new QRException(e.getMessage(), e);
        }

    }
}
