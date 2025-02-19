package com.customer.rewards.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.customer.rewards.exception.RewardsException;
import com.customer.rewards.model.RewardsResponse;
import com.customer.rewards.model.Transaction;
import com.customer.rewards.repository.RewardsRepository;

public class RewardsServiceTest {
	@Mock
	private RewardsRepository rewardsRepository;

	@InjectMocks
	private RewardsService rewardsService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCalculatePointsAmtGT100() {
		// Test case: Amount greater than 100
		double amount1 = 120;
		int expectedPoints1 = 90;
		int actualPoints1 = rewardsService.calculatePoints(amount1);
		assertEquals(expectedPoints1, actualPoints1);
	}

	@Test
	public void testCalculatePointsAmtLT100() {
		// Test case: Amount between 50 and 100
		double amount2 = 75;
		int expectedPoints2 = 25;
		int actualPoints2 = rewardsService.calculatePoints(amount2);
		assertEquals(expectedPoints2, actualPoints2);
	}

	@Test
	public void testCalculatePointsAmtLT50() {
		// Test case: Amount less than 50
		double amount3 = 45;
		int expectedPoints3 = 0;
		int actualPoints3 = rewardsService.calculatePoints(amount3);
		assertEquals(expectedPoints3, actualPoints3);
	}

	@Test
	public void testCalculatePointsAmtLT0() {
		// Test case: Amount less than 0
		double amount3 = -45;
		int expectedPoints3 = 0;
		int actualPoints3 = rewardsService.calculatePoints(amount3);
		assertEquals(expectedPoints3, actualPoints3);
	}

	@Test
	public void testCalculateRewardsWithTransactions() throws RewardsException {
		List<Transaction> transactions = new ArrayList<>();
		// Add test transactions to the list
		transactions.add(new Transaction(1L, 1L, LocalDateTime.now().minusMonths(1), 120.0));
		transactions.add(new Transaction(2L, 1L, LocalDateTime.now().minusMonths(2), 75.0));

		// Mocking the repository method
		when(rewardsRepository.findByTransactionDateBetween(
				LocalDateTime.now().minusMonths(2).withDayOfMonth(1).toLocalDate().atStartOfDay(), LocalDateTime.now()))
				.thenReturn(transactions);

		List<RewardsResponse> rewardsResponseList = rewardsService.calculateRewards(null);

		// Verify the calculated rewards
		assertEquals(1, rewardsResponseList.size());

		RewardsResponse response = rewardsResponseList.get(0);
		assertEquals(1L, response.getCustomerId());
		assertEquals(115, response.getTotalRewards());
	}

	@Test
	public void testCalculateRewardsNoTransactions() {
		List<Transaction> transactions = new ArrayList<>();

		// Mocking the repository method
		when(rewardsRepository.findByTransactionDateBetween(
				LocalDateTime.now().minusMonths(2).withDayOfMonth(1).toLocalDate().atStartOfDay(), LocalDateTime.now()))
				.thenReturn(transactions);

		RewardsException exception = assertThrows(RewardsException.class, () -> {
			rewardsService.calculateRewards(null);
		});

		// Verify the exception message
		assertEquals("Data Not Found", exception.getMessage());
	}

	@Test
	public void testGetLastThreeMonthsTransactions() {
		List<Transaction> transactions = new ArrayList<>();
		// Add test transactions to the list
		transactions.add(new Transaction(1L, 1L, LocalDateTime.now().minusMonths(1), 120.0));
		transactions.add(new Transaction(2L, 1L, LocalDateTime.now().minusMonths(2), 75.0));
		transactions.add(new Transaction(3L, 2L, LocalDateTime.now().minusMonths(1), 45.0));

		// Mocking the repository method
		when(rewardsRepository.findByTransactionDateBetween(
				LocalDateTime.now().minusMonths(2).withDayOfMonth(1).toLocalDate().atStartOfDay(), LocalDateTime.now()))
				.thenReturn(transactions);

		List<Transaction> result = rewardsService.getLastThreeMonthsTransactions(LocalDateTime.now());

		// Verify the retrieved transactions
		assertEquals(3, result.size());
		assertEquals(transactions, result);
	}
}
