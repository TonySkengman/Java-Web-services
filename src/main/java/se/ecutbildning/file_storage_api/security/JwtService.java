package se.ecutbildning.file_storage_api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class JwtService {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    /**
     * Skapar en ny JwtService med angiven hemlighet för signering.
     *
     * @param secret hemlig nyckel som används för att signera och verifiera JWT
     */

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT
                .require(algorithm)
                .withIssuer("file-storage-api")
                .build();
    }

    /**
     * Genererar en JWT-token för en specifik användare.
     *
     * Token innehåller:
     * - issuer (file-storage-api)
     * - utfärdandedatum
     * - utgångsdatum (30 minuter)
     * - användarens UUID som subject
     *
     * @param userId användarens UUID
     * @return en signerad JWT-token som String
     */

    public String generateToken(UUID userId) {
        return JWT.create()
                .withIssuer("file-storage-api")
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(30, ChronoUnit.MINUTES))
                .withSubject(userId.toString())
                .sign(algorithm);
    }

    /**
     * Validerar en JWT-token och extraherar användarens UUID.
     *
     * @param token JWT-token som ska verifieras
     * @return användarens UUID om token är giltig
     * @throws com.auth0.jwt.exceptions.JWTVerificationException
     *         om token är ogiltig eller har gått ut
     */

    public UUID validateToken(String token) {
        DecodedJWT jwt = verifier.verify(token);
        return UUID.fromString(jwt.getSubject());
    }
}
