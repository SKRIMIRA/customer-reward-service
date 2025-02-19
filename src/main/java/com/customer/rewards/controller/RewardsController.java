package com.customer.rewards.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.customer.rewards.constants.RewardsConstant;
import com.customer.rewards.exception.RewardsException;
import com.customer.rewards.model.BaseResponse;
import com.customer.rewards.model.RewardsResponse;
import com.customer.rewards.model.Transaction;
import com.customer.rewards.service.RewardsService;

/**
 * RewardsController is a REST controller that handles requests related to
 * customer rewards.
 */
@RestController
@RequestMapping("/rewards")
public class RewardsController {
	@Autowired
	private RewardsService rewardsService;

	/**
	 * Endpoint to calculate customer rewards.
	 * 
	 * @return ResponseEntity containing a list of RewardsResponse with calculated
	 *         rewards.
	 * @throws RewardsException
	 */
	@GetMapping(value = "/calculate", produces = "application/json")
	public ResponseEntity<BaseResponse<List<RewardsResponse>>> calculateCustomerRewards(
			@RequestParam(required = false) String transactionDate) throws RewardsException {

		// Calculate monthly and total rewards per customer
		List<RewardsResponse> rewardsResponseList = rewardsService.calculateRewards(transactionDate);

		return new ResponseEntity<>(new BaseResponse<List<RewardsResponse>>(RewardsConstant.RESULT_CODE_SUCCESS,
				RewardsConstant.RESULT_MESSAGE_SUCCESS, rewardsResponseList), HttpStatus.OK);
	}

	/**
	 * Endpoint to insert a list of transactions.
	 *
	 * @param transactions The list of transactions to be inserted.
	 * @return ResponseEntity containing a BaseResponse with the result code, result
	 *         message, and a success message.
	 * @throws RewardsException if an error occurs while inserting the transactions.
	 */
	@PostMapping(value = "/insertTransactions")
	public ResponseEntity<BaseResponse<String>> insertTransactions(@RequestBody List<Transaction> transactions)
			throws RewardsException {

		// Insert transactions
		rewardsService.insertTransactions(transactions);

		return new ResponseEntity<>(new BaseResponse<String>(RewardsConstant.RESULT_CODE_SUCCESS,
				RewardsConstant.RESULT_MESSAGE_SUCCESS, "data saved successfully."), HttpStatus.OK);
	}
}