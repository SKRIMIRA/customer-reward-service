package com.customer.rewards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.customer.rewards.constants.RewardsConstant;
import com.customer.rewards.model.BaseResponse;

/**
 * It handles the exceptions globally.
 */
@RestControllerAdvice
public class RewardsExceptionHandler {

	@ExceptionHandler(value = RewardsException.class)
	@ResponseStatus(HttpStatus.OK)
	public BaseResponse<Object> handleDataNotFoundException(RewardsException ex) {
		return new BaseResponse<Object>(RewardsConstant.RESULT_CODE_SUCCESS, RewardsConstant.RESULT_MESSAGE_SUCCESS, ex.getMessage());
	}
	
	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(HttpStatus.OK)
	public BaseResponse<Object> handleException(Exception ex) {
		return new BaseResponse<Object>(RewardsConstant.RESULT_CODE_ERROR, RewardsConstant.RESULT_MESSAGE_ERROR, ex.getMessage());
	}
}
