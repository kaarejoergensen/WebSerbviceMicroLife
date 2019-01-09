package persistence;

import tokens.TokenProvider;

import java.util.*;

public class MemoryDataStore implements Datastore {
    private TokenProvider tokenProvider;

    public MemoryDataStore(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    private Map<String, Integer> numberOfUnusedTokenMap = new HashMap<>();
    private Set<String> usedTokens = new HashSet<>();

    @Override
    public void useToken(String tokenString) {
        this.usedTokens.add(tokenString);
        String userName = this.tokenProvider.getUserName(tokenString);
        Integer numberOfUnusedTokens = this.numberOfUnusedTokenMap.get(userName);
        if (numberOfUnusedTokens != null) {
            this.numberOfUnusedTokenMap.put(userName, numberOfUnusedTokens - 1);
        }
    }

    @Override
    public boolean isTokenUsed(String tokenString) {
        return this.usedTokens.contains(tokenString);
    }

    @Override
    public void addTokens(int tokens, String userName) {
        Integer numberOfUnusedTokens = this.numberOfUnusedTokenMap.get(userName);
        if (numberOfUnusedTokens == null)
            numberOfUnusedTokens = 0;
        numberOfUnusedTokens += tokens;
        this.numberOfUnusedTokenMap.put(userName, numberOfUnusedTokens);
    }

    @Override
    public int getNumberOfUnusedTokens(String userName) {
        Integer numberOfUnusedTokens = this.numberOfUnusedTokenMap.get(userName);
        return numberOfUnusedTokens != null ? numberOfUnusedTokens : 0;
    }
}
