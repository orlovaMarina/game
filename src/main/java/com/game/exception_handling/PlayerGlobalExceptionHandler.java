package com.game.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import javax.xml.bind.ValidationException;

@ControllerAdvice
public class PlayerGlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<PlayerIncorrectData> handleException(NoSuchPlayerException exc){
        PlayerIncorrectData data = new PlayerIncorrectData();
        data.setInfo(exc.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<PlayerIncorrectData> handleException(Exception exc){
        PlayerIncorrectData data = new PlayerIncorrectData();
        data.setInfo(exc.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<PlayerIncorrectData> handleException(ValidationException exc){
        PlayerIncorrectData data = new PlayerIncorrectData();
        data.setInfo(exc.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<PlayerIncorrectData> handleException(NoSuchIDException exc){
        PlayerIncorrectData data = new PlayerIncorrectData();
        data.setInfo(exc.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
