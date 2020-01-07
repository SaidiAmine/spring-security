package lmc.stage.springprojectstage.security.errorHandler;

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

    //@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<Object> unAuthorizedUserException(HttpServletRequest request, Exception ex) {
       ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, "Custom error: You're not authorized to reach this page.", "You're not authorized to reach this page." );
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    //@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<Object> forbiddenUserException(HttpServletRequest request, Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, "Custom error: You're not authorized to reach this page.", "You're not authorized to reach this page." );
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }
}
