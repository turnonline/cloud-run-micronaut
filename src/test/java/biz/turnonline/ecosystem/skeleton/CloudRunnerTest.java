package biz.turnonline.ecosystem.skeleton;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@MicronautTest
class CloudRunnerTest
{
    @Inject
    @Client( "/" )
    private HttpClient client;

    @Inject
    private EmbeddedApplication<?> application;

    /**
     * Reads the content of the file in the same package as this test and converts it into a string.
     *
     * @param filename the file name to be read
     * @return the string content of the file
     */
    private static String readString( String filename )
    {
        InputStream stream = CloudRunnerTest.class.getResourceAsStream( filename );
        if ( stream == null )
        {
            throw new IllegalArgumentException( filename + " not found" );
        }
        return new BufferedReader( new InputStreamReader( stream ) )
                .lines()
                .collect( Collectors.joining( System.lineSeparator() ) );
    }

    @Test
    public void testItWorks()
    {
        Assertions.assertTrue( application.isRunning() );
    }

    @Test
    public void testPost_BodyPlainText()
    {
        String jsonString = readString( "test.json" );
        MutableHttpRequest<String> post = HttpRequest.POST( "/", jsonString )
                .contentType( MediaType.TEXT_PLAIN );

        System.out.println( "response = " + client.toBlocking().exchange( post ).getStatus() );
    }

    @Test
    public void testPost_BodyBase64()
    {
        String jsonString = readString( "test.json" );
        byte[] encoded = Base64.encodeBase64( jsonString.getBytes( StandardCharsets.UTF_8 ) );
        String body = new String( encoded, StandardCharsets.UTF_8 );

        MutableHttpRequest<String> post = HttpRequest.POST( "/", body )
                .contentType( MediaType.TEXT_PLAIN );

        System.out.println( "response = " + client.toBlocking().exchange( post ).getStatus() );
    }
}
