package uk.gov.dvsa.mot.security;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.PredefinedClientConfigurations;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;

import uk.gov.dvsa.mot.app.ConfigKeys;
import uk.gov.dvsa.mot.app.ConfigManager;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Base64;

public class Decrypt {
    private final AWSKMS kms;

    public Decrypt() throws IOException {

        kms = getClient();
    }

    public String decrypt(String cipherText) {

        ByteBuffer cipherTextBlob64 = strToBb(cipherText, Charset.defaultCharset());
        ByteBuffer cipherTextBlob = Base64.getDecoder().decode(cipherTextBlob64);
        ByteBuffer plainTextBlob = decrypt(cipherTextBlob);

        return bbToStr(plainTextBlob, Charset.defaultCharset());
    }

    private ByteBuffer decrypt(ByteBuffer ciphertextblob) {

        DecryptRequest req = new DecryptRequest().withCiphertextBlob(ciphertextblob);
        return kms.decrypt(req).getPlaintext();
    }

    private AWSKMS getClient() throws IOException {

        AWSKMSClientBuilder clientBuilder = AWSKMSClientBuilder.standard();

        ClientConfiguration clientConfig = PredefinedClientConfigurations.defaultConfig();

        String proxyHost = ConfigManager.getEnvironmentVariable(ConfigKeys.ProxyHost);
        if (proxyHost != null) {
            clientConfig.setProxyHost(proxyHost);
            clientConfig.setProxyPort(Integer.parseInt(ConfigManager.getEnvironmentVariable(ConfigKeys.ProxyPort)));
        }
        clientBuilder.setClientConfiguration(clientConfig);

        return clientBuilder.build();
    }

    private static ByteBuffer strToBb(String msg, Charset charset) {

        return ByteBuffer.wrap(msg.getBytes(charset));
    }

    private static String bbToStr(ByteBuffer buffer, Charset charset) {

        byte[] bytes;

        if (buffer.hasArray()) {
            bytes = buffer.array();
        } else {
            bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
        }

        return new String(bytes, charset);
    }
}