package uk.gov.dvsa.mot.security;

import com.google.common.base.Strings;

import uk.gov.dvsa.mot.app.ConfigKeys;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import static uk.gov.dvsa.mot.app.ConfigManager.getEnvironmentVariable;

public class ParamObfuscator {

    private static final String ALGORITHM = "AES/ECB/PKCS5PADDING";

    /**
     * @param str Value to be obfuscated
     * @return String
     * @throws ObfuscationException
     */
    public static String obfuscate(String str) throws ObfuscationException {

        try {
            // Initialise Secret Key
            SecretKeySpec secretKeySpec = getSecretKeySpecification();

            // Initialise Cipher for encryption
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            // Encrypt..
            byte[] ciphertext = cipher.doFinal(str.getBytes());

            // Return the payload, encoded as a Base64 string.
            return Base64.getUrlEncoder().encodeToString(ciphertext);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException
                | IllegalBlockSizeException e) {
            throw new ObfuscationException("An error occurred during obfuscation", e);
        }
    }

    /**
     * @param str Value to be deobfuscated
     * @return String
     * @throws ObfuscationException
     */
    public static String deobfuscate(String str) throws ObfuscationException {

        try {
            // Decode our Base64 payload to Byte array
            byte[] ciphertext = Base64.getUrlDecoder().decode(str);

            // Initialise Secret Key
            SecretKeySpec secretKeySpec = getSecretKeySpecification();

            // Initialise Cipher for decryption
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            // Decrypt..
            byte[] original = cipher.doFinal(ciphertext);

            // Return plaintext as String
            return new String(original);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException
                | IllegalBlockSizeException e) {
            throw new ObfuscationException("Something went wrong with the deobfuscation", e);
        }


    }

    private static SecretKeySpec getSecretKeySpecification() throws IOException, ObfuscationException {

        // Get the secret from ConfigManager
        String secret = getObfuscationKey();

        if (secret == null || secret.isEmpty()) {
            throw new ObfuscationException(String.format("%s has not been set or is empty!", ConfigKeys.ObfuscationSecret));
        }

        return new SecretKeySpec(secret.getBytes(), "AES");
    }

    private static String getObfuscationKey() throws IOException {

        String configObfuscationKeyEncrypted = getEnvironmentVariable(ConfigKeys.ObfuscationEncryptedSecret, false);

        if (Strings.isNullOrEmpty(configObfuscationKeyEncrypted)) {
            return getEnvironmentVariable(ConfigKeys.ObfuscationSecret);
        } else {
            return getEnvironmentVariable(ConfigKeys.ObfuscationEncryptedSecret);
        }
    }

    public static class ObfuscationException extends Exception {

        public ObfuscationException(String message) {
            super(message);
        }

        public ObfuscationException(String message, Exception ex) {
            super(message, ex);
        }
    }
}
