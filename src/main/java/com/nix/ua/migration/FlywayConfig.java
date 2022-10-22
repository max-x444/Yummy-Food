package com.nix.ua.migration;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

public class FlywayConfig {
//    @Bean
//    @Profile("local")
//    public FlywayMigrationStrategy cleanMigrateStrategy() {
//        FlywayMigrationStrategy strategy = flyway -> {
//            flyway.clean();
//            flyway.migrate();
//        };
//
//        return strategy;
//    }
}