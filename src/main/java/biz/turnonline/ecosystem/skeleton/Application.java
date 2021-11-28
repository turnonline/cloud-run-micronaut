package biz.turnonline.ecosystem.skeleton;

import io.micronaut.runtime.Micronaut;

public class Application
{
    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    public static void main( String[] args )
    {
        Micronaut.run( Application.class, args );
    }
}
