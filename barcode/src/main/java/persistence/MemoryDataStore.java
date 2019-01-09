package persistence;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MemoryDataStore implements Datastore {
    private Map<String, Integer> numberOfUnusedTokenMap = new HashMap<>();
    private Set<String> usedTokens = new HashSet<>();

    @Override
    public void useToken(String tokenString, String userName) {
        this.usedTokens.add(tokenString);
        Integer numberOfUnusedTokens = this.numberOfUnusedTokenMap.get(userName);
        if (numberOfUnusedTokens != null) {
            this.numberOfUnusedTokenMap.put(tokenString, --numberOfUnusedTokens);
        }
    }

    @Override
    public boolean isTokenUsed(String tokenString) {
        return this.usedTokens.contains(tokenString);
    }

    @Override
    public void addNumberOfUnusedTokens(int numberOfTokens, String userName) {
        Integer numberOfUnusedTokens = this.numberOfUnusedTokenMap.get(userName);
        if (numberOfUnusedTokens == null)
            numberOfUnusedTokens = 0;
        numberOfTokens += numberOfUnusedTokens;
        this.numberOfUnusedTokenMap.put(userName, numberOfTokens);
    }

    @Override
    public int getNumberOfUnusedTokens(String userName) {
        Integer numberOfUnusedTokens = this.numberOfUnusedTokenMap.get(userName);
        return numberOfUnusedTokens != null ? numberOfUnusedTokens : 0;
    }
}
