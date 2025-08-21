package com.nthuy.demo_erm.Exception;


import com.nthuy.demo_erm.DTO.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static com.nthuy.demo_erm.Constant.EnumErrorCode.*;


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
//    @ExceptionHandler(value = IdInvalidException.class)
//    public ResponseEntity<RestResponse<Object>> handleIdInvalidException(IdInvalidException ex) {
//        RestResponse<Object> restResponse = new RestResponse<Object>();
//        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
//        restResponse.setMessage(ex.getMessage());
//        restResponse.setErrorCode(VALIDATION_ERROR);
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
//    }
//
//    @ExceptionHandler(value = {
//            UsernameNotFoundException.class,
//            BadCredentialsException.class
//    })
//    public ResponseEntity<RestResponse<Object>> handleIdException(Exception ex) {
//        RestResponse<Object> restResponse = new RestResponse<Object>();
//        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
//        restResponse.setMessage(ex.getMessage());
//        restResponse.setErrorCode(VALIDATION_ERROR);
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
//    }
//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    public ResponseEntity<RestResponse<Object>> handleValidationException(MethodArgumentNotValidException methodArgumentNotValidException) {
//        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
//        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//
//        RestResponse<Object> res = new RestResponse<Object>();
//        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
//        res.setErrorCode(VALIDATION_ERROR);
//        List<String> errors = fieldErrors.stream().map(f->f.getDefaultMessage()).collect(Collectors.toList());
//        res.setMessage(errors.size() > 1 ? errors : errors.get(0));
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
//    }
//    @ExceptionHandler(value = {
//            NoResourceFoundException.class,
//    })
//    public ResponseEntity<RestResponse<Object>> handleNoResourceFoundException(NoResourceFoundException ex) {
//        RestResponse<Object> restResponse = new RestResponse<Object>();
//        restResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
//        restResponse.setMessage(ex.getMessage());
//        restResponse.setErrorCode(NOT_FOUND);
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(restResponse);
//    }
//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<RestResponse<Object>> handleIllegalStateException(Exception ex) {
//        RestResponse<Object> restResponse = new RestResponse<Object>();
//        restResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
//        restResponse.setMessage("có lỗi xảy ra");
//        restResponse.setErrorCode(INTERNAL_SERVER_ERROR);
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(restResponse);
//    }
//    @ExceptionHandler(value = BadRequestValidationException.class)
//    public ResponseEntity<RestResponse<Object>> handleBadRequestValidationException(BadRequestValidationException ex) {
//        RestResponse<Object> restResponse = new RestResponse<Object>();
//        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
//        restResponse.setMessage(ex.getMessage());
//        restResponse.setErrorCode(NOT_FOUND);
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
//    }


}
