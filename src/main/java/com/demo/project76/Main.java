package com.demo.project76;

import java.util.Arrays;

import com.demo.project76.domain.Customer;
import com.demo.project76.domain.MySecrets;
import com.demo.project76.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.core.VaultSysOperations;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.core.VaultTransitOperations;
import org.springframework.vault.support.VaultMount;
import org.springframework.vault.support.VaultResponse;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(MySecrets.class)
public class Main {

    private final VaultTemplate vaultTemplate;
    private final MySecrets mySecrets;
    private final VaultOperations operations;

    @Value("${my-group.username}")
    private String userName;

    @Value("${my-group.appType}")
    private String appType;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner onStart(CustomerRepository customerRepository, ConfigurableEnvironment environment) {
        return args -> {
            log.info("Value injected via @Value userName : {}", userName);
            log.info("Value injected via @Value environment : {}", environment);
            log.info("Value injected via @Value appType : {}", appType);
            log.info("Value injected via class mySecrets: {}", mySecrets);

            //Reading directly.
            if (Arrays.stream(environment.getActiveProfiles()).anyMatch(t -> t.equals("dev"))) {
                VaultResponse response = vaultTemplate.opsForKeyValue("secret",
                        VaultKeyValueOperationsSupport.KeyValueBackend.KV_2).get("myapp/dev");
                log.info("Value of myKey: {} ", response.getData().get("myKey"));
            } else {
                VaultResponse response = vaultTemplate.opsForKeyValue("secret",
                        VaultKeyValueOperationsSupport.KeyValueBackend.KV_2).get("myapp/prod");
                log.info("Value of myKey: {} ", response.getData().get("myKey"));
            }

            //Writing new values to different path.
            VaultTransitOperations transitOperations = vaultTemplate.opsForTransit();
            VaultSysOperations sysOperations = vaultTemplate.opsForSys();
            if (!sysOperations.getMounts().containsKey("transit/")) {
                sysOperations.mount("transit", VaultMount.create("transit"));
                transitOperations.createKey("foo-key");
            }

            // Encrypt a plain-text value
            String ciphertext = transitOperations.encrypt("foo-key", "Secure message");
            log.info("Encrypted value: {}", ciphertext);

            // Decrypt
            String plaintext = transitOperations.decrypt("foo-key", ciphertext);
            log.info("Decrypted value: {}", plaintext);

            //Save to db, connection established via vault credentials
            Customer customer = customerRepository.save(Customer.builder().firstName("John").lastName("Rambo").build());
            log.info("Customer: {}", customer);

        };
    }
}
