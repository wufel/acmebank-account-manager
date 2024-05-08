package com.wufel.acmebank.exception;

import com.wufel.acmebank.model.ApiError;
import com.wufel.acmebank.model.FundTransferResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({AccountNotFoundException.class})
    ResponseEntity<ApiError> handlesNotFoundException(Exception e) {
        ApiError apiError = new ApiError().error("AccountNotFoundException").errorMessage(e.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(apiError);
    }

    @ExceptionHandler({FundTransferFailureException.class})
    ResponseEntity<FundTransferResponse> handlesInsufficientFUndException(FundTransferFailureException e) {
        FundTransferResponse failedTransferResponse = new FundTransferResponse(e.getSourceAccountId(), e.getDestinationAccountId(), e.getTransferAmount(), FundTransferResponse.StatusEnum.FAILED);
        failedTransferResponse.setErrorMessage(e.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(failedTransferResponse);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    ResponseEntity<ApiError> handlesInsufficientFUndException(MethodArgumentNotValidException e) {
        StringBuffer stringBuffer = new StringBuffer();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        fieldErrors.forEach(fe -> stringBuffer.append(String.format("%s %s. ", fe.getField(), fe.getDefaultMessage())));

        ApiError error = new ApiError().error(e.getStatusCode().toString()).errorMessage(stringBuffer.toString());
        return ResponseEntity.status(BAD_REQUEST).body(error);
    }
}
