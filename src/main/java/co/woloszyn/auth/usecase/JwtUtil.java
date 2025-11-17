package co.woloszyn.auth.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@ApplicationScoped
public class JwtUtil {

    @ConfigProperty(name = "jwt.secret")
    String secret;

    @ConfigProperty(name = "jwt.expirationSeconds")
    long expirationSeconds;

    private String base64UrlEncode(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }

    private byte[] base64UrlDecode(String s) {
        return Base64.getUrlDecoder().decode(s);
    }

    private byte[] hmacSha256Bytes(String signingInput) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(keySpec);
            return mac.doFinal(signingInput.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Failed to compute HMAC", e);
        }
    }

    private String sign(String signingInput) {
        byte[] sig = hmacSha256Bytes(signingInput);
        return base64UrlEncode(sig);
    }

    private String jsonEscape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public String generateToken(String subject) {
        long nowSec = System.currentTimeMillis() / 1000L;
        long expSec = nowSec + expirationSeconds;

        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payload = "{\"" +
                "sub\":\"" + jsonEscape(subject) + "\"," +
                "\"iat\":" + nowSec +
                ",\"exp\":" + expSec +
                "}";

        String encodedHeader = base64UrlEncode(header.getBytes(StandardCharsets.UTF_8));
        String encodedPayload = base64UrlEncode(payload.getBytes(StandardCharsets.UTF_8));

        String signingInput = encodedHeader + "." + encodedPayload;
        String signature = sign(signingInput);

        return signingInput + "." + signature;
    }

    public String generateRefreshToken(String subject) {
        long nowSec = System.currentTimeMillis() / 1000L;
        long expSec = nowSec + (expirationSeconds * 24L);

        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payload = "{" +
                "\"sub\":\"" + jsonEscape(subject) + "\"," +
                "\"iat\":" + nowSec +
                ",\"exp\":" + expSec +
                "}";

        String encodedHeader = base64UrlEncode(header.getBytes(StandardCharsets.UTF_8));
        String encodedPayload = base64UrlEncode(payload.getBytes(StandardCharsets.UTF_8));

        String signingInput = encodedHeader + "." + encodedPayload;
        String signature = sign(signingInput);

        return signingInput + "." + signature;
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }

    /**
     * Validate a token signature and expiration and return the subject if valid, otherwise null.
     */
    public String validateTokenAndGetSubject(String token) {
        if (token == null) return null;
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;
            String headerB64 = parts[0];
            String payloadB64 = parts[1];
            String signatureB64 = parts[2];

            String signingInput = headerB64 + "." + payloadB64;
            String expectedSig = sign(signingInput);
            // constant-time compare
            if (!constantTimeEquals(expectedSig, signatureB64)) return null;

            byte[] payloadBytes = base64UrlDecode(payloadB64);
            String payload = new String(payloadBytes, StandardCharsets.UTF_8);

            // extract "sub" value
            String sub = extractJsonStringValue(payload, "sub");
            String expStr = extractJsonNumberValue(payload, "exp");
            if (expStr == null) return null;
            long exp = Long.parseLong(expStr);
            long now = System.currentTimeMillis() / 1000L;
            if (exp < now) return null;

            return sub;
        } catch (Exception e) {
            return null;
        }
    }

    private boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return false;
        byte[] ab = a.getBytes(StandardCharsets.UTF_8);
        byte[] bb = b.getBytes(StandardCharsets.UTF_8);
        int diff = ab.length ^ bb.length;
        for (int i = 0; i < Math.min(ab.length, bb.length); i++) {
            diff |= ab[i] ^ bb[i];
        }
        return diff == 0;
    }

    private String extractJsonStringValue(String json, String key) {
        String keyToken = "\"" + key + "\"";
        int idx = json.indexOf(keyToken);
        if (idx == -1) return null;
        int colon = json.indexOf(':', idx + keyToken.length());
        if (colon == -1) return null;
        int i = colon + 1;
        // skip whitespace
        while (i < json.length() && Character.isWhitespace(json.charAt(i))) i++;
        if (i >= json.length() || json.charAt(i) != '"') return null;
        int start = i + 1;
        int end = json.indexOf('"', start);
        if (end == -1) return null;
        return json.substring(start, end);
    }

    private String extractJsonNumberValue(String json, String key) {
        String keyToken = "\"" + key + "\"";
        int idx = json.indexOf(keyToken);
        if (idx == -1) return null;
        int colon = json.indexOf(':', idx + keyToken.length());
        if (colon == -1) return null;
        int i = colon + 1;
        // skip whitespace
        while (i < json.length() && Character.isWhitespace(json.charAt(i))) i++;
        StringBuilder sb = new StringBuilder();
        // optional sign
        if (i < json.length() && (json.charAt(i) == '-' || json.charAt(i) == '+')) {
            sb.append(json.charAt(i));
            i++;
        }
        while (i < json.length()) {
            char c = json.charAt(i);
            if (c >= '0' && c <= '9') {
                sb.append(c);
                i++;
            } else break;
        }
        if (sb.length() == 0) return null;
        return sb.toString();
    }
}
