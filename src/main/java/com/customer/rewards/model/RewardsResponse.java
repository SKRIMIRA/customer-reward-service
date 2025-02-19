package com.customer.rewards.model;

import java.util.List;

import lombok.Data;

/**
 * RewardsResponse represents the rewards information for a customer.
 */
@Data
public class RewardsResponse {
	private Long customerId;
	private List<RewardsPerMonth> rewardsPerMonth;
	private Integer totalRewards;
	
	// Getters and Setters
}
