package com.customer.rewards.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customer.rewards.exception.RewardsException;
import com.customer.rewards.model.RewardsPerMonth;
import com.customer.rewards.model.RewardsResponse;
import com.customer.rewards.model.Transaction;
import com.customer.rewards.repository.RewardsRepository;

/**
 * RewardsService is a service class that provides methods to calculate customer
 * rewards and retrieve customer transaction data.
 */
@Service
public class RewardsService {

	@Autowired
	private RewardsRepository rewardsRepository;

	/**
	 * Calculates rewards points based on the transaction amount. - For amounts
	 * greater than $100, 2 points are awarded for every dollar spent over $100. -
	 * For amounts between $50 and $100, 1 point is awarded for every dollar spent
	 * over $50.
	 * 
	 * @param amount The transaction amount for which to calculate points.
	 * @return The calculated rewards points.
	 */
	public int calculatePoints(double amount) {
		int points = 0;

		// amount should be positive
		if (amount >= 0) {
			if (amount > 100) {
				points += (amount - 100) * 2;
				amount = 100;
			}
			if (amount > 50) {
				points += (amount - 50) * 1;
			}
		}
		return points;
	}

	/**
	 * Calculates rewards for customers based on their transactions for last three
	 * months.
	 * 
	 * @param transactionDate
	 *
	 * @return List<RewardsResponse> containing the calculated rewards for each
	 *         customer.
	 * @throws RewardsException
	 */
	public List<RewardsResponse> calculateRewards(String transactionDate) throws RewardsException {
		List<RewardsResponse> rewardsResponseList = new ArrayList<>();

		LocalDateTime dateTime;
		if (null != transactionDate) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate date = LocalDate.parse(transactionDate, formatter);
			dateTime = date.atStartOfDay();
		} else {
			dateTime = LocalDateTime.now();
		}

		// Fetch last three months customer transaction data
		List<Transaction> transactions = getLastThreeMonthsTransactions(dateTime);

		if (transactions.size() > 0) {
			// Group by customer and transaction month
			Map<Long, Map<String, List<Transaction>>> customerTransByMonth = transactions.stream()
					.collect(Collectors.groupingBy(c -> c.getCustomerId(), Collectors.groupingBy(
							d -> d.getTransactionDate().getMonthValue() + "-" + d.getTransactionDate().getYear())));

			customerTransByMonth.forEach((customerId, transactionsPerMonth) -> {

				int totalPoints = 0;
				RewardsResponse rewardsResponse = new RewardsResponse();
				rewardsResponse.setCustomerId(customerId);

				List<RewardsPerMonth> RewardsPerMonthList = new ArrayList<>();
				for (Map.Entry<String, List<Transaction>> entry : transactionsPerMonth.entrySet()) {
					int monthlyPoints = 0;

					for (Transaction transaction : entry.getValue()) {
						// Calculate rewards points per transaction
						monthlyPoints += calculatePoints(transaction.getAmount());
					}

					RewardsPerMonth rewardsPerMonth = new RewardsPerMonth();
					rewardsPerMonth.setMonth(entry.getKey());
					rewardsPerMonth.setRewards(monthlyPoints);
					RewardsPerMonthList.add(rewardsPerMonth);
					totalPoints += monthlyPoints;
				}
				rewardsResponse.setRewardsPerMonth(RewardsPerMonthList);

				rewardsResponse.setTotalRewards(totalPoints);
				rewardsResponseList.add(rewardsResponse);
			});
		} else {
			// If Not found last three months records
			throw new RewardsException("Data Not Found");
		}

		return rewardsResponseList;
	}

	/**
	 * Retrieves customer transactions from the last three months.
	 *
	 * @param dateTime The current date and time.
	 * @return List<Transaction> containing customer transactions from the last
	 *         three months.
	 */
	public List<Transaction> getLastThreeMonthsTransactions(LocalDateTime dateTime) {

		// Calculate local date based on given date minus 2 months to fetch last three
		// months transactions
		LocalDate localDate = dateTime.minusMonths(2).withDayOfMonth(1).toLocalDate();
		LocalDateTime startlocalDateTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT);

		return rewardsRepository.findByTransactionDateBetween(startlocalDateTime, dateTime);
	}

	/**
	 * Inserts a list of transactions into the database.
	 *
	 * @param transactions The list of transactions to be inserted.
	 */
	public void insertTransactions(List<Transaction> transactions) {
		try {
			rewardsRepository.saveAll(transactions);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
