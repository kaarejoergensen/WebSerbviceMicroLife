package barcode;

import persistence.Datastore;
import tokens.TokenProvider;

import java.util.ArrayList;
import java.util.List;

public class BarcodeProvider {
    private TokenProvider tokenProvider;
    private Datastore datastore;

    public BarcodeProvider(TokenProvider tokenProvider, Datastore datastore) {
        this.tokenProvider = tokenProvider;
        this.datastore = datastore;
    }

    public List<String> getTokens(String userName, String userId, int numberOfTokens) {
        if (numberOfTokens < 1 || numberOfTokens > 5)
            throw new IllegalArgumentException("The number of tokens must be between 1 and 5");
        int numberOfUnusedTokens = this.datastore.getNumberOfUnusedTokens(userName);
        if (numberOfUnusedTokens > 1)
            throw new IllegalArgumentException("Number of unused tokens is more than 1");
        List<String> tokens = new ArrayList<>();
        for (int i = 0; i < numberOfTokens; i++) tokens.add(this.tokenProvider.issueToken(userName, userId));
        this.datastore.addTokens(tokens.size(), userName);
        return tokens;
    }

    public boolean useToken(String tokenString) {
        boolean tokenValid = this.tokenProvider.checkToken(tokenString) && !this.datastore.isTokenUsed(tokenString);
        if (tokenValid)
            this.datastore.useToken(tokenString);
        return tokenValid;
    }
}
