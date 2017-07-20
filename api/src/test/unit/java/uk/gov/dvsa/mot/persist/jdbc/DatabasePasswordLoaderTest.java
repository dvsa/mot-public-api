package uk.gov.dvsa.mot.persist.jdbc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import uk.gov.dvsa.mot.app.ConfigKeys;

import java.io.IOException;

import static org.mockito.Mockito.doReturn;

public class DatabasePasswordLoaderTest {

    @Spy
    DatabasePasswordLoader holder = new DatabasePasswordLoader();

    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getDbPassword_NoEncryptedPassword() throws IOException {

        doReturn(null).when(holder).getEnvironmentVariable(ConfigKeys.DatabaseEncryptedPassword, false);
        doReturn("Password01").when(holder).getEnvironmentVariable(ConfigKeys.DatabasePassword);

        String password = holder.getDbPassword();

        Assert.assertEquals("Password01", password);
    }

    @Test
    public void getDbPassword_EncryptedPasswordDecrypted() throws IOException {

        doReturn("qwerty").when(holder).getEnvironmentVariable(ConfigKeys.DatabaseEncryptedPassword, false);
        doReturn("Password01").when(holder).getEnvironmentVariable(ConfigKeys.DatabaseEncryptedPassword);

        String password = holder.getDbPassword();

        Assert.assertEquals("Password01", password);
    }

    @Test
    public void getDbPassword_EncryptedPasswordCachedNotChanged() throws IOException {

        doReturn("qwerty").when(holder).getEnvironmentVariable(ConfigKeys.DatabaseEncryptedPassword, false);
        doReturn("Password01").when(holder).getEnvironmentVariable(ConfigKeys.DatabaseEncryptedPassword);

        String password = holder.getDbPassword();

        Assert.assertEquals("Password01", password);

        password = holder.getDbPassword();

        Assert.assertEquals("Password01", password);
    }

    @Test
    public void getDbPassword_EncryptedPasswordCachedChanged() throws IOException {

        doReturn("qwerty1", "qwerty2").when(holder).getEnvironmentVariable(ConfigKeys.DatabaseEncryptedPassword, false);
        doReturn("Password01", "Password02").when(holder).getEnvironmentVariable(ConfigKeys.DatabaseEncryptedPassword);

        String password = holder.getDbPassword();

        Assert.assertEquals("Password01", password);

        password = holder.getDbPassword();

        Assert.assertEquals("Password02", password);
    }
}
