package spring.security.controllers;

import io.jsonwebtoken.Clock;
import io.jsonwebtoken.impl.DefaultClock;
import spring.security.entities.http.models.JwtAuthenticationRequest;
import spring.security.utils.JwtTokenUtil;
import spring.security.entities.JwtUser;
import spring.security.entities.http.models.JwtAuthenticationResponse;
import spring.security.exceptions.AuthenticationException;
import spring.security.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final RedisUtil redisUtil;
    private final SessionRegistry sessionRegistry;

    @Autowired
    public AuthenticationRestController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
        UserDetailsService userDetailsService, RedisUtil redisUtil, SessionRegistry sessionRegistry) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.redisUtil = redisUtil;
        this.sessionRegistry = sessionRegistry;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/currentLoggedUser")
    public ResponseEntity<Object> getCurrentLoggedUser(){
        logger.info("Inside getCurrentLoggedUser controller");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            JwtUser currentUser = (JwtUser) authentication.getPrincipal();
            logger.info(String.format("JwtUser: %s - %s", currentUser.getUsername(), currentUser.getEmail()));
            return ResponseEntity.ok(currentUser);
        }
        return ResponseEntity.badRequest().body("No user logged in");
    }

    @PostMapping(value = "/auth")
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
        logger.info(" *** In /auth Controller");
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        // Reload password post-security so we can generate the token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Returning expiration date with the token.
        Clock clock = DefaultClock.INSTANCE;
        Date date = clock.now();
        Date exp = new Date(date.getTime() + 60400 * 1000);

        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @GetMapping(value = "${jwt.route.authentication.refresh}")
    public ResponseEntity<Object> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ok(new JwtAuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping(value = "/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {
        logger.info("/logout controller.");
        String authToken = request.getHeader(tokenHeader).substring(7);
        if (redisUtil.sismember(authToken, "blacklisted")) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Already logged out.");//406
        } else {
            Date date = jwtTokenUtil.getExpirationDateFromToken(authToken);
            redisUtil.sadd(authToken, "blacklisted", date.getTime() / 1000);
            logger.info(String.format("Redis expiration time of blacklisted token: %d", date.getTime() / 1000));
            return new ResponseEntity<>("{}", HttpStatus.OK);
        }
    }

    @GetMapping(value = "/connectedUsers")
    public ResponseEntity<Object> getAllConnectedUsers() {
        List<Object> principals = sessionRegistry.getAllPrincipals();
        List<JwtUser> connectUserNames = new ArrayList<>();
        for(Object principal : principals) {
            if(principal instanceof JwtUser) {
                connectUserNames.add((JwtUser) principal);
            }
        }
        return new ResponseEntity<>(connectUserNames, HttpStatus.OK);
    }

    private void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials!", e);
        }
    }
}
