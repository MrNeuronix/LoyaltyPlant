package ru.poliscam.bank;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

import ru.poliscam.bank.processing.service.exceptions.AccountNameRequiredException;
import ru.poliscam.bank.processing.database.model.Account;
import ru.poliscam.bank.processing.service.AccountService;
import ru.poliscam.bank.processing.service.ProcessingService;
import ru.poliscam.bank.processing.service.exceptions.AccountNotFoundException;
import ru.poliscam.bank.processing.service.exceptions.DestinationAccountRequiredException;
import ru.poliscam.bank.processing.service.exceptions.InsufficientMoneyException;
import ru.poliscam.bank.processing.service.exceptions.UnknownPaymentTypeException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("tests")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProcessingTests {

	@Autowired
	private AccountService accountService;

	@Autowired
	private ProcessingService processingService;

	private Account account1, account2;

	@Before
	public void setUp() {
		try {
			account1 = accountService.addAccount("Test user 1");
			account2 = accountService.addAccount("Test user 2");
			processingService.addMoney(account1.getNumber(), new BigDecimal("100.00"));
			processingService.addMoney(account2.getNumber(), new BigDecimal("150.00"));
		} catch (AccountNameRequiredException | AccountNotFoundException | DestinationAccountRequiredException |
				UnknownPaymentTypeException | InsufficientMoneyException e) {
			Assert.fail();
		}
	}

	@Test
	public void test1() {
		try {

			BigDecimal balance1 = processingService.getBalance(account1.getNumber());
			BigDecimal balance2 = processingService.getBalance(account2.getNumber());

			Assert.assertEquals(balance1, new BigDecimal("100.00"));
			Assert.assertEquals(balance2, new BigDecimal("150.00"));

		} catch (AccountNotFoundException e) {
			Assert.fail();
		}
	}

	@Test
	public void test2() {
		try {

			processingService.spentMoney(account1.getNumber(), new BigDecimal("30.00"));
			processingService.spentMoney(account2.getNumber(), new BigDecimal("55.00"));

			BigDecimal balance1 = processingService.getBalance(account1.getNumber());
			BigDecimal balance2 = processingService.getBalance(account2.getNumber());

			Assert.assertEquals(balance1, new BigDecimal("70.00"));
			Assert.assertEquals(balance2, new BigDecimal("95.00"));

		} catch (DestinationAccountRequiredException | UnknownPaymentTypeException | InsufficientMoneyException |
				AccountNotFoundException  e) {
			Assert.fail("Error: " + e.getLocalizedMessage());
		}
	}

	@Test
	public void test3() {
		try {
			processingService.transferMoney(account1.getNumber(), account2.getNumber(), new BigDecimal("30.00"));

			BigDecimal balance1 = processingService.getBalance(account1.getNumber());
			BigDecimal balance2 = processingService.getBalance(account2.getNumber());

			Assert.assertEquals(balance1, new BigDecimal("70.00"));
			Assert.assertEquals(balance2, new BigDecimal("180.00"));

		} catch (AccountNotFoundException | DestinationAccountRequiredException | UnknownPaymentTypeException | InsufficientMoneyException e) {
			Assert.fail("Error: " + e.getLocalizedMessage());
		}
	}
}