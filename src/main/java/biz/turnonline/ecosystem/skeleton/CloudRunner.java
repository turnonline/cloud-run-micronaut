package biz.turnonline.ecosystem.skeleton;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.ServiceOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

import static biz.turnonline.ecosystem.skeleton.Application.DATE_FORMAT_PATTERN;

@Controller
public class CloudRunner
{
    private static final Logger LOGGER = LoggerFactory.getLogger( CloudRunner.class );

    private final FirebaseAuth auth;

    private final ObjectMapper mapper;

    public CloudRunner() throws IOException
    {
        this( initFirebase() );
    }

    CloudRunner( FirebaseAuth auth )
    {
        this.auth = auth;

        this.mapper = new ObjectMapper();
        this.mapper.setDateFormat( new SimpleDateFormat( DATE_FORMAT_PATTERN ) );
        this.mapper.registerModule( new JavaTimeModule() );
        this.mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
    }

    private static FirebaseAuth initFirebase() throws IOException
    {
        FirebaseApp.initializeApp( FirebaseOptions.builder()
                .setProjectId( ServiceOptions.getDefaultProjectId() )
                .setCredentials( GoogleCredentials.getApplicationDefault() )
                .build() );
        return FirebaseAuth.getInstance();
    }

    @Get( produces = MediaType.TEXT_PLAIN )
    public String get()
    {
        return "Hello World!";
    }

    @Post( consumes = MediaType.TEXT_PLAIN )
    public HttpResponse<?> run( @Body String body )
    {
        String plainText = decode( body );
        LOGGER.info( "Decoded text = " + plainText );

        return HttpResponse.ok( plainText );
    }

    private String decode( @Nonnull String data )
    {
        String decoded;
        if ( Base64.isBase64( data.getBytes() ) )
        {
            decoded = new String( Base64.decodeBase64( data ), StandardCharsets.UTF_8 );
        }
        else
        {
            decoded = data;
        }
        return decoded;
    }
}