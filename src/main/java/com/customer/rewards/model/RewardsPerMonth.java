package com.customer.rewards.model;

import lombok.Data;

/**
 * RewardsPerMonth represents the rewards earned by a customer in a specific
 * month.
 */
@Data
public class RewardsPerMonth {
	private String month;
	private Integer rewards;
	
	// Getters and Setters
}
