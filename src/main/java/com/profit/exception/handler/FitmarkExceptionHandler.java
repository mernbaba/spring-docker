package com.profit.exception.handler;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.profit.dto.ResponseObject;
import com.profit.enumeration.ResponseCode;
import com.profit.exceptions.CloudBaseException;

@ControllerAdvice
public class FitmarkExceptionHandler {

	@ExceptionHandler(CloudBaseException.class)
	public ResponseEntity<ResponseObject<String>> processCustomException(CloudBaseException exception) {
		// Create a response object, you can create a custom response class if needed
		ResponseObject<String> response = new ResponseObject<>();
		response.setStatusFromEnum(ResponseCode.valueOf(exception.getMessage()));
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseObject<String>> handleSQLIntegrityConstraintViolationException(
    		ConstraintViolationException ex) {
        ResponseObject<String> response = new ResponseObject<>();
        response.setStatusFromEnum(ResponseCode.DUPLICATE_ENTRY);
        return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseObject<String>> handleDataIntegrityViolationException(
    		DataIntegrityViolationException ex) {
        ResponseObject<String> response = new ResponseObject<>();
        response.setStatusFromEnum(ResponseCode.DUPLICATE_ENTRY);
        return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
    }

}
