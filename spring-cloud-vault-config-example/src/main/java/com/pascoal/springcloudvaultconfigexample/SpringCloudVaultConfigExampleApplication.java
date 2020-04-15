package com.pascoal.springcloudvaultconfigexample;

import com.pascoal.springcloudvaultconfigexample.configuration.VaultCustomConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
@EnableConfigurationProperties(VaultCustomConfig.class)
public class
SpringCloudVaultConfigExampleApplication implements CommandLineRunner {

	private final VaultCustomConfig vaultCustomConfig;

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudVaultConfigExampleApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		log.info("----------------------------------------");
		log.info("Configuration properties");
		log.info("   example.username is {}", vaultCustomConfig.getUsername());
		log.info("   example.password is {}", vaultCustomConfig.getPassword());
		log.info("----------------------------------------");
	}
}
