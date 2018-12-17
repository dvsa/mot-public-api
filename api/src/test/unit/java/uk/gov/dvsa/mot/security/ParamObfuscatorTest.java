package uk.gov.dvsa.mot.security;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.gov.dvsa.mot.app.ConfigKeys;

import java.io.IOException;

@RunWith(DataProviderRunner.class)
public class ParamObfuscatorTest {

    @Before
    public void beforeTest() throws IOException {

        // Set a key to use for unit tests
        System.setProperty(ConfigKeys.ObfuscationSecret, "BbV[`8d7zQnc:?}\"CSz$L0t+(3r:_uT$");
    }


    @Test
    @UseDataProvider("dataProviderVariableParameters")
    public void testObfuscatedParameterReturnsBase64EncodedStringAndCanDeobfuscateToOriginalPlaintext(
            String value, String expected, int expectedLength) throws ParamObfuscator.ObfuscationException {

        // Given I obfuscate a parameter
        String obfuscatedString = ParamObfuscator.obfuscate(value);

        // Ensure encoded result is valid Base64
        Assert.assertTrue(Base64.isBase64(obfuscatedString));

        // Encoded length should be {expectedLength}
        Assert.assertEquals(expectedLength, obfuscatedString.length());

        // When I deobfuscate the encoded result
        String result = ParamObfuscator.deobfuscate(obfuscatedString);

        // Then I should end up with the initial plaintext
        Assert.assertEquals(expected, result);
    }

    @Test(expected = ParamObfuscator.ObfuscationException.class)
    public void testObfuscatedParameterThrowsExceptionWhenDeobfuscatedWithInvalidKey() throws ParamObfuscator.ObfuscationException {
        // Given I obfuscate a parameter
        String obfuscatedString = ParamObfuscator.obfuscate("Hello World!");

        // Ensure encoded result is valid Base64
        Assert.assertTrue(Base64.isBase64(obfuscatedString));

        // When I use an invalid secret to decrypt
        System.setProperty(ConfigKeys.ObfuscationSecret, "some random and invalid secret..");

        // When I deobfuscate the encoded result
        String result = ParamObfuscator.deobfuscate(obfuscatedString);

        // Then an ParamObfuscator.ObfuscationException exception should be thrown
    }

    @Test
    @UseDataProvider("dataProviderVariableVehicleIdLengths")
    public void testObfuscatedVehicleIdParameterReturnsBase64EncodedStringWithLength24AndCanDeobfuscate(String value, String expected)
            throws ParamObfuscator.ObfuscationException {

        // Given I obfuscate a vehicle ID
        String obfuscatedString = ParamObfuscator.obfuscate(value);

        // Ensure encoded result is valid Base64
        Assert.assertTrue(Base64.isBase64(obfuscatedString));

        // And Encoded length should be exactly 24 characters:
        // For vehicle IDs less than 16 digits, we expect NO vehicle ID to surpass 15 characters
        Assert.assertEquals(24,  obfuscatedString.length());

        // When I deobfuscate the encoded result
        String result = ParamObfuscator.deobfuscate(obfuscatedString);

        // Then I should end up with the initial plaintext
        Assert.assertEquals(expected, result);
    }

    @DataProvider
    public static Object[][] dataProviderVariableVehicleIdLengths() {
        return new Object[][] {
                { "1", "1"},
                { "12", "12" },
                { "123", "123" },
                { "1234", "1234" },
                { "12345", "12345" },
                { "123456", "123456" },
                { "1234567", "1234567" },
                { "12345678", "12345678" },
                { "123456789", "123456789" },
                { "1234567890", "1234567890" },
                { "12345678901", "12345678901" },
                { "123456789012", "123456789012" },
                { "1234567890123", "1234567890123" },
                { "12345678901234", "12345678901234" },
                { "123456789012345", "123456789012345" },
        };
    }

    @DataProvider
    public static Object[][] dataProviderVariableParameters() {
        return new Object[][] {
                {
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                        88
                },
                {
                    "http://example.com",
                        "http://example.com",
                        44
                },
                {
                    "@z2j!_rNf0%qsE%A{R~&8t@o)&BxsAqz5n`Dj=#E0jR5~I7\\ya(}qsWX\"#uCLJq",
                        "@z2j!_rNf0%qsE%A{R~&8t@o)&BxsAqz5n`Dj=#E0jR5~I7\\ya(}qsWX\"#uCLJq",
                        88
                },
                {
                    "¥·£·€·$·¢·₡·₢·₣·₤·₥·₦·₧·₨·₩·₪·₫·₭·₮·₯·₹",
                        "¥·£·€·$·¢·₡·₢·₣·₤·₥·₦·₧·₨·₩·₪·₫·₭·₮·₯·₹",
                        128
                },
        };
    }


}
