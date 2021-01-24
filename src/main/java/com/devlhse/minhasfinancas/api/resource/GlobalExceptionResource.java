package com.devlhse.minhasfinancas.api.resource;

import com.devlhse.minhasfinancas.exception.AutenticacaoException;
import com.devlhse.minhasfinancas.exception.AutorizacaoException;
import com.devlhse.minhasfinancas.exception.NotFoundException;
import com.devlhse.minhasfinancas.exception.RegraNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionResource {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleNotFoundException(Exception e) {
        return new ResponseEntity<>(getErrorMessageResponse(e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AutorizacaoException.class, AutenticacaoException.class})
    public ResponseEntity<ErrorMessageResponse> handleAutorizacaoException(Exception e) {
        return new ResponseEntity<>(getErrorMessageResponse(e), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErrorMessageResponse> handleRegraNegocioException(Exception e) {
        return new ResponseEntity<>(getErrorMessageResponse(e), HttpStatus.BAD_REQUEST);
    }

    private ErrorMessageResponse getErrorMessageResponse(Exception e) {
        ErrorMessageResponse errorMessage = new ErrorMessageResponse();
        errorMessage.setMessage(e.getMessage());
        return errorMessage;
    }
}

class ErrorMessageResponse {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}