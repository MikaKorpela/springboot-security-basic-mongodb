# springboot-security-basic-mongodb
Spring Boot basic authentication with custom configuration; using Mongo DB storage.

## Add Dependencies

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

## Enable Web Security

Web security is enabled in custom configuration.

## Create User Entity and Repository

```java
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
```
```java
@Repository
@RequiredArgsConstructor
public class UserRepository {
  private final MongoTemplate mongoTemplate;

  public UserEntity findByUsername(String username) {
    return mongoTemplate.findOne(new Query(Criteria.where("username").is(username)), UserEntity.class);
  }
}
```

## Create User Accounts

User accounts are ```user4/password4``` and ```user5/password5```.

```json
{
    "username" : "user4",
    "password" : "$2a$10$gjqjl6d0IJo/.XQyBLIOCu4.yMlT43Ly03FCPQzBnJb7knR2i2aam",
    "role" : "USER"
}
```

```json
{
    "username" : "user5",
    "password" : "$2a$10$gz4Xksy8ST.28DR3BU/niOq4B3gtT9xfTLNzqjqdR7zwnurJdELCq",
    "role" : "ADMIN"
}
```

## Create Custom User Details and User Details Service

```java
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomUserDetails implements UserDetails {
  UserEntity userEntity;

  // overridden methods not listed.
}
```

```java
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity userEntity = userRepository.findByUsername(username);
    CustomUserDetails customUserDetails;

    if (userEntity != null) {
      customUserDetails = new CustomUserDetails();
      customUserDetails.setUserEntity(userEntity);
    } else {
      throw new UsernameNotFoundException("User " + username + " not found");
    }

    return customUserDetails;
  }
}
```

## Create Custom Configuration

This custom configuration does not yet contain authorization.

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {
  private final UserDetailsService userDetailsService;

  @Bean
  public AuthenticationManager customAuthenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.userDetailsService(userDetailsService)
      .passwordEncoder(bCryptPasswordEncoder());
    return authenticationManagerBuilder.build();
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http.csrf().disable();

    http.authorizeRequests()
      .antMatchers("/api/hello/**")
      .authenticated()
      .and().formLogin();

    return  http.build();
  }
}
```
