package org.muni.pa165.rest.exceptionhandling;

import jakarta.servlet.http.HttpServletRequest;
import org.muni.pa165.exceptions.CarNotFoundException;
import org.muni.pa165.exceptions.DataStorageException;
import org.muni.pa165.exceptions.ExternalCallException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.UrlPathHelper;

import java.time.Clock;
import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomRestGlobalExceptionHandling {

    private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();

    @ExceptionHandler({CarNotFoundException.class})
    public ResponseEntity<ApiError> handleResourceNotFound(final CarNotFoundException ex, final HttpServletRequest request) {
        final ApiError apiError = new ApiError(
                LocalDateTime.now(Clock.systemUTC()),
                HttpStatus.NOT_FOUND,
                ex.getLocalizedMessage(),
                URL_PATH_HELPER.getRequestUri(request));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({DataStorageException.class})
    public ResponseEntity<ApiError> handleDataStorageException(DataStorageException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                LocalDateTime.now(Clock.systemUTC()),
                HttpStatus.BAD_GATEWAY,
                ex.getLocalizedMessage(),
                URL_PATH_HELPER.getRequestUri(request)
        );
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ExternalCallException.class})
    public ResponseEntity<ApiError> handleExternalCallException(ExternalCallException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                LocalDateTime.now(Clock.systemUTC()),
                HttpStatus.SERVICE_UNAVAILABLE,
                ex.getLocalizedMessage(),
                URL_PATH_HELPER.getRequestUri(request)
        );
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle all the exceptions not matched by above-mentioned definitions.
     *
     * @param ex      the ex
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiError> handleAll(final Exception ex, HttpServletRequest request) {
        final ApiError apiError = new ApiError(
                LocalDateTime.now(Clock.systemUTC()),
                HttpStatus.INTERNAL_SERVER_ERROR,
                getInitialException(ex).getLocalizedMessage(),
                URL_PATH_HELPER.getRequestUri(request));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    private Exception getInitialException(Exception ex) {
        while (ex.getCause() != null) {
            ex = (Exception) ex.getCause();
        }
        return ex;
    }
}
