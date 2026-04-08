package com.example.lujuria.config;

import java.util.Arrays;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jpa.autoconfigure.EntityManagerFactoryDependsOnPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ConditionalOnClass(Flyway.class)
@ConditionalOnProperty(prefix = "spring.flyway", name = "enabled", havingValue = "true", matchIfMissing = true)
public class FlywayConfig {

    @Bean(initMethod = "migrate")
    @ConditionalOnMissingBean
    public Flyway flyway(DataSource dataSource, Environment environment) {
        String configuredLocations = environment.getProperty("spring.flyway.locations", "classpath:db/migration");
        String[] locations = Arrays.stream(configuredLocations.split(","))
            .map(String::trim)
            .filter(location -> !location.isEmpty())
            .toArray(String[]::new);

        return Flyway.configure()
            .dataSource(dataSource)
            .locations(locations)
            .cleanDisabled(true)
            .load();
    }

    @Bean
    public static EntityManagerFactoryDependsOnPostProcessor entityManagerFactoryDependsOnFlywayPostProcessor() {
        return new EntityManagerFactoryDependsOnPostProcessor("flyway");
    }
}
