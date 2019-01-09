import barcode.BarcodeProvider;
import org.junit.Before;
import org.junit.Test;
import persistence.Datastore;
import persistence.MemoryDataStore;
import tokens.TokenProvider;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class BarcodeTest {
    private String userName = "core";
    private String userId = "1234";

    private TokenProvider tokenProvider;
    private BarcodeProvider barcodeProvider;

    @Before
    public void init() {
        this.tokenProvider = new TokenProvider();
        Datastore datastore = new MemoryDataStore(tokenProvider);
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
        assertThat(barcodeProvider.useToken(tokens.get(0)), is(true));
        assertThat(barcodeProvider.useToken(tokens.get(0)), is(false));
    }

}
