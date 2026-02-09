package org.example.bank_rest.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppProperties {
    private String baseUrl;
    private UUID adminUuid;
    private Files files;
    private Email email;
    private Template template;

    public record Files(String uploadPath) { }

    public record Email(String from) { }

    public record Template(
        String emailVerification,
        String notification,
        String remind) {}

}
