package com.customer.rewards.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.customer.rewards.model.Transaction;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RewardsRepositoryTest {

	@Autowired
	private RewardsRepository rewardsRepository;

	@BeforeEach
	public void setup() {
	}

	@AfterEach
	public void cleanup() {
		rewardsRepository.deleteAll();
	}

	@Test
	public void testFindByTransactionDateGE() {
		saveTransactions();

		LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(2).withDayOfMonth(1).toLocalDate()
				.atStartOfDay();
		List<Transaction> transactions = rewardsRepository.findByTransactionDateBetween(threeMonthsAgo, LocalDateTime.now());

		// Verify the retrieved transactions
		assertNotNull(transactions);
	}

	public void saveTransactions() {
		Transaction transaction1 = new Transaction();
		Transaction transaction2 = new Transaction();
		Transaction transaction3 = new Transaction();

		transaction1.setCustomerId(11L);
		transaction1.setTransactionDate(LocalDateTime.now().minusMonths(1));
		transaction1.setAmount(120.0);

		transaction2.setCustomerId(1L);
		transaction2.setTransactionDate(LocalDateTime.now().minusMonths(2));
		transaction2.setAmount(75.0);

		transaction3.setCustomerId(2L);
		transaction3.setTransactionDate(LocalDateTime.now().minusMonths(3));
		transaction3.setAmount(45.0);

		rewardsRepository.save(transaction1);
		rewardsRepository.save(transaction2);
		rewardsRepository.save(transaction3);
	}
}
