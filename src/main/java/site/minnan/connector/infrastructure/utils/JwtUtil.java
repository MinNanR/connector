package site.minnan.connector.infrastructure.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import site.minnan.connector.domain.aggregrate.AuthUser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Jwt相关操作
 * @author Minnan on 2021/09/14
 */
@Component
public class JwtUtil {

    @Value("${jwt.expiration}")
    private long JWT_TOKEN_VALIDITY;

    @Value("${jwt.secret}")
    private String secret;

    public String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public String generateToken(AuthUser user){
        Map<String,Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("stamp", user.getPasswordStamp());
        return doGenerateToken(claims, user.getUsername());
    }

    private Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(expiration);
    }

    //从token中解析过期时间
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //解析token
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public Boolean validateToken(String token, AuthUser userDetails) {
        String username = getClaimFromToken(token, Claims::getSubject);
        String stamp = getClaimFromToken(token, e -> e.get("stamp", String.class));
        return (Objects.equals(username, userDetails.getUsername()) && Objects.equals(stamp,
                userDetails.getPasswordStamp()) && !isTokenExpired(token));
    }

    //从token中解析用户名
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
}
