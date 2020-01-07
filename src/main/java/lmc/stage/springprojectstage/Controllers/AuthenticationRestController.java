package lmc.stage.springprojectstage.Controllers;

import io.jsonwebtoken.Clock;
import io.jsonwebtoken.impl.DefaultClock;
import lmc.stage.springprojectstage.entities.User;
import lmc.stage.springprojectstage.security.JwtAuthenticationRequest;
import lmc.stage.springprojectstage.security.JwtTokenUtil;
import lmc.stage.springprojectstage.security.JwtUser;
import lmc.stage.springprojectstage.security.service.JwtAuthenticationResponse;
import lmc.stage.springprojectstage.services.UserService;
import lmc.stage.springprojectstage.utils.AuthenticationException;
import lmc.stage.springprojectstage.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/currentLoggedUser", method = RequestMethod.GET)
    public ResponseEntity<?> getCurrentLoggedUser(){
        logger.info("Inside getCurrentLoggedUser controller");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            JwtUser currentUser = (JwtUser) authentication.getPrincipal();
            logger.info("JwtUser: "+currentUser.getUsername() +" - "+ currentUser.getEmail());
            return ResponseEntity.ok(currentUser);
        }
        return ResponseEntity.badRequest().body("No user logged in");
    }

    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
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

    @RequestMapping(value = "/me", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<?> getAuthenticatedUser(HttpServletRequest request) {
        logger.info("Inside /me controller");
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        System.out.println("DEBUG: "+authToken);
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

    ////

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@RequestBody User user) {

        userService.addUser(user);
        return ok().body("User successfuly used.");

    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseEntity<String> logoutUser(HttpServletRequest request){
        logger.info("/logout controller.");
        String authToken = request.getHeader(tokenHeader).substring(7);
        if(redisUtil.sismember(authToken,"blacklisted")){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Already logged out.");//406
        } else {
            Date date = jwtTokenUtil.getExpirationDateFromToken(authToken);
            redisUtil.sadd(authToken, "blacklisted", date.getTime() / 1000);
            logger.info("Redis expiration time of blacklisted token: "+date.getTime()/1000);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Successfully logged out");
        }
    }

    ////
    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    /**
     * Authenticates the user. If something is wrong, an {@link AuthenticationException} will be thrown
     */
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
