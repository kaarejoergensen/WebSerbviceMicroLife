package tokens;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TokenProvider {
    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public TokenProvider() {
    }

    public List<String> issueTokens(String userName, String userId, int numberOfTokens) {
        if (numberOfTokens < 1 || numberOfTokens > 5)
            throw new IllegalArgumentException("The number of tokens must be between 1 and 5");
        List<String> tokens = new ArrayList<>();
        LocalDateTime expiration = LocalDateTime.now().plusDays(7);
        Date out = Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant());

        for (int i = 0; i < numberOfTokens; i++) {
            String token = Jwts.builder()
                    .setSubject(userName)
                    .setId(userId)
                    .setExpiration(out).signWith(key).compact();
            tokens.add(token);
        }

        return tokens;
    }

    public boolean checkToken(String tokenString) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(tokenString);
        } catch(SignatureException e ) {
            return false;
        }
        return true;
    }
}
