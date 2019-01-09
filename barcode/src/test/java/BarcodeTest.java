import barcode.BarcodeProvider;
import exceptions.QRException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import persistence.Datastore;
import persistence.MemoryDataStore;
import tokens.TokenProvider;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class BarcodeTest {
    private String userName = "core";
    private String userId = "1234";

    private TokenProvider tokenProvider;
    private Datastore datastore;
    private BarcodeProvider barcodeProvider;

    @Before
    public void init() {
        this.tokenProvider = new TokenProvider();
        this.datastore = new MemoryDataStore(tokenProvider);
        this.barcodeProvider = new BarcodeProvider(tokenProvider, datastore);
    }

    @Test
    public void issueTokens() throws QRException {
        List<String> tokens = barcodeProvider.getTokens(this.userName, this.userId, 5);
        assertThat(tokens, is(notNullValue()));
        assertThat(tokens.size(), is(5));
        for (String token : tokens) {
            assertThat(tokenProvider.checkToken(token), is(true));
        }
    }

    @Test
    public void invalidToken() {
        assertThat(tokenProvider.checkToken("1234"), is(false));
    }

    @Test
    public void tamperedToken() throws QRException {
        List<String> tokens = barcodeProvider.getTokens(this.userName, this.userId, 1);
        assertThat(tokens, is(notNullValue()));
        assertThat(tokens.size(), is(1));
        assertThat(tokenProvider.checkToken(tokens.get(0) + "1234"), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void maxFiveTokens() throws QRException {
        barcodeProvider.getTokens(this.userName, this.userId, 6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void atLeastOneToken() throws QRException {
        barcodeProvider.getTokens(this.userName, this.userId, 0);
    }

    @Test
    public void useToken() throws QRException {
        List<String> tokens = barcodeProvider.getTokens(this.userName, this.userId, 1);
        assertThat(tokens, is(notNullValue()));
        assertThat(tokens.size(), is(1));
        assertThat(datastore.getNumberOfUnusedTokens(this.userName), is(1));
        assertThat(barcodeProvider.useToken(tokens.get(0)), is(true));
        assertThat(datastore.getNumberOfUnusedTokens(this.userName), is(0));
        assertThat(barcodeProvider.useToken(tokens.get(0)), is(false));
    }

    @Test
    public void getTokenIfOnlyOneUnused() throws QRException {
        List<String> tokens = barcodeProvider.getTokens(this.userName, this.userId, 1);
        tokens.addAll(barcodeProvider.getTokens(this.userName, this.userId, 1));
        assertThat(tokens, is(notNullValue()));
        assertThat(tokens.size(), is(2));
        barcodeProvider.useToken(tokens.get(0));
        barcodeProvider.getTokens(this.userName, this.userId, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void maxOneUnusedToken() throws QRException {
        barcodeProvider.getTokens(this.userName, this.userId, 2);
        assertThat(datastore.getNumberOfUnusedTokens(this.userName), is(2));
        barcodeProvider.getTokens(this.userName, this.userId, 1);
    }

    @AfterClass
    public static void cleanUpQRCodes() throws IOException {
        String directory = System.getProperty("user.dir") + "/images/";
        Path path = FileSystems.getDefault().getPath(directory);
        Files.walk(path).map(Path::toFile).filter(f -> !f.isDirectory()).forEach(System.out::println);
    }
}
