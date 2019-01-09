import barcode.BarcodeProvider;
import org.junit.Before;
import org.junit.Test;
import persistence.Datastore;
import persistence.MemoryDataStore;
import tokens.TokenProvider;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
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
    public void issueTokens() {
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
    public void tamperedToken() {
        List<String> tokens = barcodeProvider.getTokens(this.userName, this.userId, 1);
        assertThat(tokens, is(notNullValue()));
        assertThat(tokens.size(), is(1));
        assertThat(tokenProvider.checkToken(tokens.get(0) + "1234"), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void maxFiveTokens() {
        barcodeProvider.getTokens(this.userName, this.userId, 6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void atLeastOneToken() {
        barcodeProvider.getTokens(this.userName, this.userId, 0);
    }

    @Test
    public void useToken() {
        List<String> tokens = barcodeProvider.getTokens(this.userName, this.userId, 1);
        assertThat(tokens, is(notNullValue()));
        assertThat(tokens.size(), is(1));
        assertThat(datastore.getNumberOfUnusedTokens(this.userName), is(1));
        assertThat(barcodeProvider.useToken(tokens.get(0)), is(true));
        assertThat(datastore.getNumberOfUnusedTokens(this.userName), is(0));
        assertThat(barcodeProvider.useToken(tokens.get(0)), is(false));
    }

    @Test
    public void getTokenIfOnlyOneUnused() {
        List<String> tokens = barcodeProvider.getTokens(this.userName, this.userId, 1);
        tokens.addAll(barcodeProvider.getTokens(this.userName, this.userId, 1));
        assertThat(tokens, is(notNullValue()));
        assertThat(tokens.size(), is(2));
        barcodeProvider.useToken(tokens.get(0));
        barcodeProvider.getTokens(this.userName, this.userId, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void maxOneUnusedToken() {
        barcodeProvider.getTokens(this.userName, this.userId, 2);
        assertThat(datastore.getNumberOfUnusedTokens(this.userName), is(2));
        barcodeProvider.getTokens(this.userName, this.userId, 1);
    }

}
