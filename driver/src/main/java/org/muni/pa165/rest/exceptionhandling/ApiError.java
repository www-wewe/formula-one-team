package org.muni.pa165.rest.exceptionhandling;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Setter
@Getter
public class ApiError {

    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private String path;

    public ApiError(LocalDateTime timestamp, HttpStatus status, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.path = path;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
