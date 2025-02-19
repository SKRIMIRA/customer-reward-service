package com.customer.rewards.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.customer.rewards.model.Transaction;
import com.customer.rewards.repository.RewardsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
@AutoConfigureMockMvc
public class RewardsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private RewardsRepository rewardsRepository;

	private ObjectMapper objectMapper;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@AfterEach
	public void cleanup() {
		rewardsRepository.deleteAll();
	}

	@Test
	public void testCalculateCustomerRewards() throws Exception {

		// Perform the get request and verify the response
		mockMvc.perform(get("/rewards/calculate").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.data").value("Data Not Found"));
	}

	@Test
	public void testCalculateCustomerRewardsWithNonEmptyResponse() throws Exception {
		List<Transaction> transactions = transactionList();
		rewardsRepository.saveAll(transactions);

		// Perform the get request and verify the response
		mockMvc.perform(get("/rewards/calculate").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.data[0].customerId").value(1L))
				.andExpect(jsonPath("$.data[0].totalRewards").value(25));
	}

	@Test
	void testInsertTransactions() throws Exception {
		// Arrange
		List<Transaction> transactions = transactionList();

		// Convert transactions list to JSON string
		String transactionsJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(transactions);

		// Act
		mockMvc.perform(
				post("/rewards/insertTransactions").contentType(MediaType.APPLICATION_JSON).content(transactionsJson))
				.andExpect(status().isOk()).andExpect(jsonPath("$.data").value("data saved successfully."));

	}

	public List<Transaction> transactionList() {
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

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction1);
		transactions.add(transaction2);
		transactions.add(transaction3);

		return transactions;
	}

}
