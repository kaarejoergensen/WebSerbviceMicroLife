import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import tokens.TokenProvider;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;

public class BarcodeTest {
    private String userName = "core";
    private String userId = "1234";

    @Test
    public void issueTokens() {
        TokenProvider tokenProvider = new TokenProvider();
        List<String> tokens = tokenProvider.issueTokens(this.userName, this.userId, 5);
        assertThat(tokens, is(notNullValue()));
        assertThat(tokens.size(), is(5));
        for (String token : tokens) {
            assertThat(tokenProvider.checkToken(token), is(true));
        }
    }
}
