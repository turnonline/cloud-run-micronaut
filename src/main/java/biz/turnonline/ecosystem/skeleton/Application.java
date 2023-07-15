package biz.turnonline.ecosystem.skeleton;

import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.context.ApplicationContextConfigurer;
import io.micronaut.context.annotation.ContextConfigurer;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.runtime.Micronaut;

public class Application
{
    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    public static void main( String[] args )
    {
        Micronaut.run( Application.class, args );
    }

    @ContextConfigurer
    public static class DeduceCloudEnvironmentConfigurer
            implements ApplicationContextConfigurer
    {
        @Override
        public void configure( @NonNull ApplicationContextBuilder builder )
        {
            // From Micronaut 4 this value is by default false
            builder.deduceCloudEnvironment( true );
        }
    }
}
