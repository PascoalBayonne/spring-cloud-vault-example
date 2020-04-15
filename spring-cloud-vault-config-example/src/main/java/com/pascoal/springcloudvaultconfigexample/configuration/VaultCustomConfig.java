package com.pascoal.springcloudvaultconfigexample.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("example")
public class VaultCustomConfig {

    /* $ vault kv put secret/spring-vault-config-example example.username=pbayona example.username=qwerty
    * */
    private String username;
    private String password;
}
