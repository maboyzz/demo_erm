package com.nthuy.demo_erm.exception;


import com.nthuy.demo_erm.dto.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static com.nthuy.demo_erm.constant.EnumErrorCode.*;


@RestControllerAdvice
public class GlobalException {


    @ExceptionHandler(value = NameExisted.class)
    public ResponseEntity<RestResponse<Object>> handleUserNameInvalidEx(NameExisted ex) {
        RestResponse<Object> restResponse = new RestResponse<Object>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setMessage(ex.getMessage());
        restResponse.setErrorCode(VALIDATION_ERROR);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }
    @ExceptionHandler(value = BadRequestValidationException.class)
    public ResponseEntity<RestResponse<Object>> handleBadRequestValidationException(BadRequestValidationException ex) {
        RestResponse<Object> restResponse = new RestResponse<Object>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setMessage(ex.getMessage());
        restResponse.setErrorCode(NOT_FOUND);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }
    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<RestResponse<Object>> handleIdInvalidException(IdInvalidException ex) {
        RestResponse<Object> restResponse = new RestResponse<Object>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setMessage(ex.getMessage());
        restResponse.setErrorCode(VALIDATION_ERROR);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }
    @ExceptionHandler(value = TypeAttributeGroupValidException.class)
    public ResponseEntity<RestResponse<Object>> handleTypeAttributeGroupValid(TypeAttributeGroupValidException ex) {
        RestResponse<Object> restResponse = new RestResponse<Object>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setMessage(ex.getMessage());
        restResponse.setErrorCode(VALIDATION_ERROR);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }



}
