package com.customer.rewards.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.customer.rewards.model.Transaction;

/**
 * RewardsRepository is a repository interface for managing Transaction
 * entities.
 */
public interface RewardsRepository extends JpaRepository<Transaction, Long> {

	/**
	 * Finds transactions that occurred between the dates.
	 *
	 * @param date The date from which to find transactions.
	 * @return A list of Transaction entities that occurred between the dates.
	 */

	List<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

}
