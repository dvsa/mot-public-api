package uk.gov.dvsa.mot.security;

import java.io.IOException ;
import java.nio.ByteBuffer ;
import java.nio.charset.Charset ;
import java.util.Base64 ;

import com.amazonaws.ClientConfiguration ;
import com.amazonaws.PredefinedClientConfigurations ;
//import com.amazonaws.auth.DefaultAWSCredentialsProviderChain ;
//import com.amazonaws.client.builder.AwsClientBuilder ;
import com.amazonaws.services.kms.AWSKMS ;
//import com.amazonaws.services.kms.AWSKMSClient;
import com.amazonaws.services.kms.AWSKMSClientBuilder ;
import com.amazonaws.services.kms.model.DecryptRequest ;

import uk.gov.dvsa.mot.app.ConfigKeys ;
import uk.gov.dvsa.mot.app.ConfigManager ;

public class Decrypt
{
  private final AWSKMS kms ;

  public Decrypt() throws IOException
  {
    kms = getClient();
  }
  
  public String decrypt( String ciphertext )
  {
    ByteBuffer ciphertextblob64 = str_to_bb( ciphertext, Charset.defaultCharset() ) ;
    ByteBuffer ciphertextblob = Base64.getDecoder().decode( ciphertextblob64 ) ;
    
    ByteBuffer plaintextblob = decrypt( ciphertextblob ) ;
    
    return bb_to_str( plaintextblob, Charset.defaultCharset() ) ;
  }

  public ByteBuffer decrypt( ByteBuffer ciphertextblob )
  {
    DecryptRequest req = new DecryptRequest().withCiphertextBlob( ciphertextblob );
    return kms.decrypt(req).getPlaintext();
  }
  
  private AWSKMS getClient() throws IOException
  {
    AWSKMSClientBuilder clientBuilder = AWSKMSClientBuilder.standard() ;

    ClientConfiguration clientConfig = PredefinedClientConfigurations.defaultConfig() ;
    
    String proxyHost = ConfigManager.getEnvironmentVariable( ConfigKeys.ProxyHost ) ;
    if ( proxyHost != null )
    {
      clientConfig.setProxyHost( proxyHost ) ;
      clientConfig.setProxyPort( Integer.parseInt( ConfigManager.getEnvironmentVariable( ConfigKeys.ProxyPort ) ) ) ;
    }
    clientBuilder.setClientConfiguration( clientConfig );

//    DefaultAWSCredentialsProviderChain defaultCredentials = new DefaultAWSCredentialsProviderChain() ;
//    clientBuilder.setCredentials( defaultCredentials );
    
    AWSKMS kmsclient = clientBuilder.build() ;

//    kmsclient.setEndpoint( "https://kms.eu-west-1.amazonaws.com" ) ;

    return kmsclient ;
  }
 
  public static ByteBuffer str_to_bb( String msg, Charset charset )
  {
    return ByteBuffer.wrap( msg.getBytes( charset ) ) ;
  }

  public static String bb_to_str( ByteBuffer buffer, Charset charset )
  {
    byte[] bytes ;
    
    if ( buffer.hasArray() ) 
    {
      bytes = buffer.array() ;
    } 
    else 
    {
      bytes = new byte[buffer.remaining()] ;
      buffer.get( bytes ) ;
    }
    
    return new String( bytes, charset ) ;
  }
}