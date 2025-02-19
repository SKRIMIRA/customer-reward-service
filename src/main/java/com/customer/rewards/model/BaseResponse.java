package com.customer.rewards.model;

import lombok.Getter;
import lombok.Setter;

/**
 * The BaseResponse class is a generic class used to standardize
 * the structure of responses in the application. It encapsulates
 * the result code, result message, and the actual data being returned.
 *
 * @param <T> the type of the data being returned in the response
 */
@Getter
@Setter
public class BaseResponse<T> {
	private Integer resultCode;
	private String resultMessage;
	private T data;
	
	public BaseResponse(Integer resultCode, String resultMessage, T data) {
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
		this.data = data;
	}
}
