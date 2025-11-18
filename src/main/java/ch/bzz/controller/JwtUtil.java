package ch.bzz.controller;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKeySpec keySpec;

    @PostConstruct
    public void init() {
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        keySpec = new SecretKeySpec(secretBytes, "HmacSHA256");
    }

    public String generateToken(String projectName) {
        Date currentTime = new Date();
        Date expirationTime = new Date(currentTime.getTime() + 3_600_000);

        String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payloadJson = String.format(
                "{\"sub\":\"%s\",\"iss\":\"AccountingApp\",\"iat\":%d,\"exp\":%d}",
                projectName,
                currentTime.getTime() / 1000,
                expirationTime.getTime() / 1000);

        String header = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));
        String payload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));

        String signature = sign(header + "." + payload);

        return header + "." + payload + "." + signature;
    }

    public boolean validateToken(String token) {
        if (token == null) {
            return false;
        }
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return false;
        }

        String signature = sign(parts[0] + "." + parts[1]);
        if (!signature.equals(parts[2])) {
            return false;
        }

        String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        int expIndex = payloadJson.indexOf("\"exp\":");
        if (expIndex == -1) {
            return false;
        }
        int expEnd = payloadJson.indexOf('}', expIndex);
        String expValue = payloadJson.substring(expIndex + 7, expEnd >= 0 ? expEnd : payloadJson.length());
        long expEpochSeconds = Long.parseLong(expValue.replaceAll("[^0-9]", ""));
        return Instant.ofEpochSecond(expEpochSeconds).isAfter(Instant.now());
    }

    private String sign(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(keySpec);
            byte[] signatureBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("Unable to sign JWT", e);
        }
    }
}
