package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.clients.KafkaProducerClient;
import com.example.userauthenticationservice.dtos.EmailDto;
import com.example.userauthenticationservice.exceptions.PasswordDoesNotMatchException;
import com.example.userauthenticationservice.exceptions.SessionNotFoundException;
import com.example.userauthenticationservice.exceptions.UserAlreadyExistsException;
import com.example.userauthenticationservice.exceptions.UserDoesNotExistException;
import com.example.userauthenticationservice.models.Role;
import com.example.userauthenticationservice.models.Session;
import com.example.userauthenticationservice.models.Status;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.repositories.SessionRepository;
import com.example.userauthenticationservice.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;

import java.util.*;

@Service
public class AuthenticationService implements IAuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SecretKey secretKey;

    @Autowired
    private KafkaProducerClient kafkaProducerClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${email.id}")
    private String emailId;


    @Override
    public User signUp(String email, String password, List<Role> roles) throws UserAlreadyExistsException {

        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()) throw new UserAlreadyExistsException("User already exists");

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(bCryptPasswordEncoder.encode(password));
        newUser.setCreatedAt(new Date());
        newUser.setUpdatedAt(new Date());
        newUser.setStatus(Status.ACTIVE);

        if( !roles.isEmpty() ){
            for(Role role : roles){
                newUser.getRoles().add(role);
            }
        }

//        setting role
        Role role = new Role();
        role.setValue("USER");
        role.setCreatedAt(new Date());
        role.setUpdatedAt(new Date());
        role.setStatus(Status.ACTIVE);
        newUser.getRoles().add(role);

        userRepository.save(newUser);

        EmailDto emailDto = new EmailDto();
        emailDto.setTo(email);
        emailDto.setFrom(emailId);
        emailDto.setSubject("Welcome to Chitraveda");
        emailDto.setBody("Have a great shopping experience");

        try {
            kafkaProducerClient.sendMessage("signup", objectMapper.writeValueAsString(emailDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
        return newUser;
    }

    @Override
    public Pair<User, String> login(String email, String password) throws UserDoesNotExistException, PasswordDoesNotMatchException {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()) throw new UserDoesNotExistException("User does not exist. Please sign up first.");

        String storedPassword = user.get().getPassword();
        if(!bCryptPasswordEncoder.matches(password, storedPassword)) throw new PasswordDoesNotMatchException("Password does not match. Try again");

        Map<String, Object> payload = new HashMap<>();
        Long nowInMillis = System.currentTimeMillis();
        payload.put("iat", nowInMillis);
        payload.put("exp", nowInMillis + 1000 * 60 * 60 * 24);
        payload.put("userId", user.get().getId());
        payload.put("iss", "Scaler");
        payload.put("scope", user.get().getRoles());


        String token = Jwts.builder().setClaims(payload).signWith(secretKey).compact();

        Session session = new Session();
        session.setToken(token);
        session.setUser(user.get());
        session.setStatus(Status.ACTIVE);
        sessionRepository.save(session);


        EmailDto emailDto = new EmailDto();
        emailDto.setTo(email);
        emailDto.setFrom(emailId);
        emailDto.setSubject("Welcome Back!");
        emailDto.setBody("Have a great shopping experience");

        try {
            kafkaProducerClient.sendMessage("login", objectMapper.writeValueAsString(emailDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }

        return new org.antlr.v4.runtime.misc.Pair<User, String>(user.get(), token);
    }

    @Override
    public boolean validateToken(String token, Long userId) throws SessionNotFoundException {

        Session session = sessionRepository.findSessionByTokenAndUser_Id(token, userId).orElseThrow(() -> new SessionNotFoundException("Token not found"));
        User user = session.getUser();

        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        Long tokenExpiry = (Long) claims.get("exp");
        Long nowInMillis = System.currentTimeMillis();

        if( nowInMillis > tokenExpiry ){
            session.setStatus(Status.INACTIVE);
            sessionRepository.save(session);
            return false;
        }

        return true;
    }


}
