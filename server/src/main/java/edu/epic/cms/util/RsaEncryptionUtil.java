package edu.epic.cms.util;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@Component
public class RsaEncryptionUtil {

    private PublicKey publicKey;
    private PrivateKey privateKey;

    @PostConstruct
    public void init() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            this.publicKey = keyPair.getPublic();
            this.privateKey = keyPair.getPrivate();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate RSA key pair", e);
        }
    }

    public String getPublicKeyBase64() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public String decrypt(String encryptedBase64) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedBase64));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting RSA token", e);
        }
    }
}
