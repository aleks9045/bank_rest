package org.example.bank_rest.service.card;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

@Component
public class CardEncryptor {

    @Value("${card_encryption-key}")
    private String encryptionKey;

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final String KEY_ALGORITHM = "CardEncryptor";
    private static final int TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public String encrypt(String value) {
        try {
            var cipher = Cipher.getInstance(ALGORITHM);
            var key = generateKey(encryptionKey.getBytes(StandardCharsets.UTF_8));

            cipher.init(Cipher.ENCRYPT_MODE, key);

            var iv = cipher.getIV();
            var encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));

            var combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

            return Base64.getEncoder().encodeToString(combined);

        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
    public String decrypt(String base64Value) {
        try {
            var combined = Base64.getDecoder().decode(base64Value);

            var iv = Arrays.copyOfRange(combined, 0, IV_LENGTH);
            var ciphertext = Arrays.copyOfRange(combined, IV_LENGTH, combined.length);

            var cipher = Cipher.getInstance(ALGORITHM);
            var key = generateKey(encryptionKey.getBytes(StandardCharsets.UTF_8));

            var spec = new GCMParameterSpec(TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);

            var decrypted = cipher.doFinal(ciphertext);
            return new String(decrypted, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }

    private static Key generateKey(byte[] value) {
        try {
            return new SecretKeySpec(value, KEY_ALGORITHM);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
