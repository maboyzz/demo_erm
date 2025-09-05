package com.nthuy.demo_erm.common.exception;


import com.nthuy.demo_erm.common.dto.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static com.nthuy.demo_erm.common.constant.EnumErrorCode.NOT_FOUND;
import static com.nthuy.demo_erm.common.constant.EnumErrorCode.VALIDATION_ERROR;


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
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<RestResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        RestResponse<Object> restResponse = new RestResponse<Object>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setMessage(ex.getMessage());
        restResponse.setErrorCode(VALIDATION_ERROR);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> handleValidationException(MethodArgumentNotValidException methodArgumentNotValidException) {
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setErrorCode(VALIDATION_ERROR);
        List<String> errors = fieldErrors.stream().map(f->f.getDefaultMessage()).collect(Collectors.toList());
        res.setMessage(errors.size() > 1 ? errors : errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }


}
