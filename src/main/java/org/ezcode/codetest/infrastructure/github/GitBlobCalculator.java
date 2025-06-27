package org.ezcode.codetest.infrastructure.github;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

@Component
public class GitBlobCalculator {
    protected String calculateBlobSha(String content) {
        try {
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            String header = "blob " + contentBytes.length + "\0";
            byte[] headerBytes = header.getBytes(StandardCharsets.UTF_8);

            byte[] store = new byte[headerBytes.length + contentBytes.length];
            System.arraycopy(headerBytes, 0, store, 0, headerBytes.length);
            System.arraycopy(contentBytes, 0, store, headerBytes.length, contentBytes.length);

            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] shaBytes = md.digest(store);

            StringBuilder sb = new StringBuilder();
            for (byte b : shaBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm not available", e);
        }
    }
}
