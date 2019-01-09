package persistence;

public interface Datastore {
    void useToken(String tokenString, String userName);

    boolean isTokenUsed(String tokenString);

    void addNumberOfUnusedTokens(int numberOfTokens, String userName);

    int getNumberOfUnusedTokens(String userName);
}
