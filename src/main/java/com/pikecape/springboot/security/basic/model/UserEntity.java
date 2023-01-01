package com.pikecape.springboot.security.basic.model;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Builder
@Data
public class UserEntity {
  @Id
  ObjectId id;
  String username;
  String password;
  String role;
  String email;
}
