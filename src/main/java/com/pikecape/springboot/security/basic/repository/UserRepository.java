package com.pikecape.springboot.security.basic.repository;

import com.pikecape.springboot.security.basic.model.UserEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {
  private final MongoTemplate mongoTemplate;

  public List<UserEntity> findAll() {
    return mongoTemplate.findAll(UserEntity.class);
  }

  public UserEntity findByUsername(String username) {
    return mongoTemplate.findOne(new Query(Criteria.where("username").is(username)), UserEntity.class);
  }

  public UserEntity create(UserEntity userEntity) {
    if (findByUsername(userEntity.getUsername()) != null) {
      throw new RuntimeException("User " + userEntity.getUsername() + " already exists");
    }

    return mongoTemplate.insert(userEntity);
  }
}
