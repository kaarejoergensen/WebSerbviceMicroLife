package tokens;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TokenProvider {
    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public TokenProvider() {
    }

    public String issueToken(String userName, String userId) {
        LocalDateTime expiration = LocalDateTime.now().plusDays(7);
        Date out = Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(userName)
                .setId(userId)
                .setExpiration(out).signWith(key).compact();
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

    public String getUserName(String tokenString) {
        Jws<Claims> claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(tokenString);
        } catch(SignatureException e ) {
            throw new IllegalArgumentException("Token not valid");
        }
        return claims.getBody().getSubject();
    }
}
