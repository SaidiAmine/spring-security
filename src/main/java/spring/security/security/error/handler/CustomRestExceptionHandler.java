package spring.security.security.error.handler;

import spring.security.exceptions.AuthenticationException;
import spring.security.exceptions.BusinessException;
import spring.security.exceptions.EmailExistsException;
import spring.security.exceptions.UsernameExistsException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(
                ex, apiError, headers, apiError.getStatus(), request);
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<Object> unAuthorizedUserException(HttpServletRequest request, Exception ex) {
       ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, "You are not authenticated, you can not reach this resource.", "You're not authorized to reach this page.", "You're not authorized to reach this page." );
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<Object> forbiddenUserException(HttpServletRequest request, Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, "You don't have enough power to reach this page.", "You're not authorized to reach this page.", "You're not authorized to reach this page." );
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    // Send error code 400 Bad Request - for Disabled account / bad credentials
    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException e) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getMessage(), "Bad credentials or account disabled.");
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    // Handles the UsernameExistsExceton when registering, return a 400 Bad request status
    // with appropriate message.
    @ExceptionHandler(UsernameExistsException.class)
    public ResponseEntity<Object> handleUsernameExists(UsernameExistsException e) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getMessage(), "Username already exists.");
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<Object> handleEmailExists(EmailExistsException e) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getMessage(), "Email already exists.");
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException e) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage(), e.getMessage(), "Oops, something went wrong.");
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
}
