package lmc.stage.springprojectstage.security;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_CREATED = "iat";
    private static final long serialVersionUID = -3301605591108950415L;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @SuppressFBWarnings(value = "SE_BAD_FIELD", justification = "It's okay here")
    private Clock clock = DefaultClock.INSTANCE;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(clock.now());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private Boolean ignoreTokenExpiration(String token) {
        // here you specify tokens, for that the expiration is ignored
        return false;
    }

    public String generateToken(UserDetails userDetails) {
        logger.info(" *** in generateToken()");
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        logger.info(" *** in doGenerateToken()");
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getIssuedAtDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public String refreshToken(String token) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            JwtUser user = (JwtUser) userDetails;
            final String username = getUsernameFromToken(token);
            final Date created = getIssuedAtDateFromToken(token);
            //final Date expiration = getExpirationDateFromToken(token);
            return (
                    username.equals(user.getUsername())
                            && !isTokenExpired(token)
                            && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate())
            );
        } catch (
                SignatureException ex) {
            logger.info("Invalid JWT Signature");
        } catch (MalformedJwtException ex) {
            logger.info("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.info("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.info("Unsupported JWT exception");
        } catch (IllegalArgumentException ex) {
            logger.info("Jwt claims string is empty");
        }
        return false;
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 1000);
    }

    ////

    public String getTokenHeader(String token) {
        int indx = 0;
        for (int i = 0; i < token.length(); i++) {
            if (token.charAt(i) == '.') {
                indx = i;
                return token.substring(0, indx);
            }
        }
        return token.substring(0, indx);
    }

    public String getTokenPayload(String token) {
        int indx1 = 0, indx2 = 0;
        for (int i = 0; i < token.length(); i++) {
            if (token.charAt(i) == '.') {
                if (indx1 != 0)
                    indx2 = i;
                else
                    indx1 = i;
            }
        }
        return token.substring(indx1 + 1, indx2);
    }

    public String getTokenSignature(String token) {
        int indx = 0;
        for (int i = 0; i < token.length(); i++) {
            if (token.charAt(i) == '.')
                indx = i;
        }
        return token.substring(indx + 1);
    }

    private Cookie getHeaderPayloadCookie(String token) {
        Cookie cookie = new Cookie("token", getTokenHeader(token) + "." + getTokenPayload(token));
        return cookie;
    }
}
