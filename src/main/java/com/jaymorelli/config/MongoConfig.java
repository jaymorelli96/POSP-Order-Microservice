package com.jaymorelli.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;


@Configuration
@EnableMongoRepositories(basePackages = "com.jaymorelli.repository")
public class MongoConfig {
    
    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener(LocalValidatorFactoryBean validatorFactoryBean) {
        return new ValidatingMongoEventListener(validatorFactoryBean);
    }
}
