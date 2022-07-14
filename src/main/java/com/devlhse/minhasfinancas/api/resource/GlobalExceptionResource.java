package com.devlhse.minhasfinancas.api.resource;

import com.devlhse.minhasfinancas.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorMessageResponse> handleConflictException(Exception e) {
        return new ResponseEntity<>(getErrorMessageResponse(e), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ValidacaoUsuarioException.class)
    public ResponseEntity<String> handleValidacaoUsuarioException(Exception e) {
        String content =
                "<header>"
                        + "<h1><span>"+e.getMessage()+"</span></h1>"
                        + "</header>";
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<>(content, responseHeaders, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PinNotFoundException.class)
    public ResponseEntity<String> handlePinNotFoundException(Exception e) {
        String content =
                "<header>"
                        + "<h1><span>"+e.getMessage()+"</span></h1>"
                        + "</header>";
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<>(content, responseHeaders, HttpStatus.NOT_FOUND);
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