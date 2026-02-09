package org.example.bank_rest.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;


@ConfigurationProperties(prefix = "cors")
@Getter
public class CorsProperties {
    private final List<String> allowedOrigins;
    private final List<String> allowedMethods;
    private final List<String> allowedHeaders;
    private final boolean allowCredentials;

    @ConstructorBinding
    public CorsProperties(List<String> allowedOrigins, List<String> allowedMethods,
                          List<String> allowedHeaders, boolean allowCredentials) {
        this.allowedOrigins = allowedOrigins;
        this.allowedMethods = allowedMethods;
        this.allowedHeaders = allowedHeaders;
        this.allowCredentials = allowCredentials;
    }

    /**
     * Creates CorsConfiguration object with current properties
     * @return Cors configuration
     */
    public CorsConfiguration getCorsConfiguration() {
        var corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOriginPatterns(getAllowedOrigins());
        corsConfiguration.setAllowedMethods(getAllowedMethods());
        corsConfiguration.setAllowedHeaders(getAllowedHeaders());
        corsConfiguration.setAllowCredentials(isAllowCredentials());

        return corsConfiguration;
    }
}
