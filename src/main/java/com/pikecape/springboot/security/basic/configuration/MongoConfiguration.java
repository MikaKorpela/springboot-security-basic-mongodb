package com.pikecape.springboot.security.basic.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.lang.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
@Profile("!test")
public class MongoConfiguration extends AbstractMongoClientConfiguration {

  @Value("${spring.data.mongodb.uri}")
  private String mongoDbUri;

  @Value("${spring.data.mongodb.database}")
  private String mongoDbName;

  @Override
  @NonNull
  protected String getDatabaseName() {
    return mongoDbName;
  }

  @Override
  @Bean
  @NonNull
  public MongoClient mongoClient() {
    ConnectionString connectionString = new ConnectionString(mongoDbUri);
    MongoClientSettings settings = MongoClientSettings.builder()
        .applyConnectionString(connectionString).build();
    return MongoClients.create(settings);
  }
}
