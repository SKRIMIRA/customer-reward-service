package com.customer.rewards.exception;

/**
 * The RewardsException class is a custom exception used to handle
 * errors related to the customer rewards system. This exception
 * should be thrown when a specific error condition occurs within
 * the rewards logic, providing a detailed message for easier debugging
 * and troubleshooting.
 */
public class RewardsException extends Exception {
	
	 public RewardsException(String message) { super(message); }
	
}
