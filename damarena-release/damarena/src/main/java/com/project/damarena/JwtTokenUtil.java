package com.project.damarena;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

/**
 * <p>JwtTokenUtil class.</p>
 *
 * @author Andrea Furini
 * @version $Id: $Id
 */
@Component
public class JwtTokenUtil {
    //    @Value("${jwt.secret}")
    private String secret;
    //    @Value("${jwt.expirationMs}")
    private long expirationMs;

    @Autowired
    /**
     * <p>Constructor for JwtTokenUtil.</p>
     */
    public JwtTokenUtil(){
        if(System.getProperty("jwt.secret") == null) {
            SecureRandom random = new SecureRandom();
            byte[] bytes = new byte[32];
            random.nextBytes(bytes);
            String secretKey = Base64.getEncoder().encodeToString(bytes);
            System.setProperty("jwt.secret", secretKey);
        }

        if(System.getProperty("jwt.expirationMs") == null)
            System.setProperty("jwt.expirationMs", "3600000");

        this.secret = System.getProperty("jwt.secret");
        this.expirationMs = Long.parseLong(System.getProperty("jwt.expirationMs"));
    }

    /**
     * <p>generateToken.</p>
     *
     * @param userEmail a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public String generateToken(String userEmail) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        byte[] secretBytes = secret.getBytes();
        Key signingKey = new SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .setSubject(userEmail)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(signingKey)
                .compact();
    }
}

