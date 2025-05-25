package com.pm.personnelmanagement.common.handler;

import com.pm.personnelmanagement.common.NotImplementedException;
import com.pm.personnelmanagement.common.dto.ApiException;
import com.pm.personnelmanagement.config.UnauthenticatedException;
import com.pm.personnelmanagement.task.exception.TaskNotFoundException;
import com.pm.personnelmanagement.taskevent.expcetion.TaskEventNotFoundException;
import com.pm.personnelmanagement.user.exception.AddressNotFoundException;
import com.pm.personnelmanagement.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = TaskNotFoundException.class)
    public ResponseEntity<Object> taskNotFoundException(TaskNotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiException exception = new ApiException(
                e.getMessage(),
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(value = TaskEventNotFoundException.class)
    public ResponseEntity<Object> taskEventNotFoundException(TaskEventNotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiException exception = new ApiException(
                e.getMessage(),
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(value = NotImplementedException.class)
    public ResponseEntity<Object> notImplementedException(NotImplementedException e) {
        HttpStatus status = HttpStatus.NOT_IMPLEMENTED;
        ApiException exception = new ApiException(
                e.getMessage(),
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<Object> userNotFoundException(NotImplementedException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiException exception = new ApiException(
                e.getMessage(),
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(value = UnauthenticatedException.class)
    public ResponseEntity<Object> unauthenticatedException(UnauthenticatedException e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ApiException exception = new ApiException(
                e.getMessage(),
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(value = AddressNotFoundException.class)
    public ResponseEntity<Object> addressNotFoundException(AddressNotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiException exception = new ApiException(
                e.getMessage(),
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(exception, status);
    }
}
