package ru.poliscam;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;

import ru.poliscam.processing.database.model.Account;
import ru.poliscam.processing.service.AccountService;
import ru.poliscam.processing.service.exceptions.AccountNameRequiredException;
import ru.poliscam.processing.service.exceptions.AccountNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("tests")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountTests {

	@Autowired
	private AccountService accountService;

	@Test
	public void test1() throws AccountNameRequiredException {
		Account account1 = accountService.addAccount("Test user 1");
		Account account2 = accountService.addAccount("Test user 2");

		Assert.assertNotNull(account1);
		Assert.assertNotNull(account2);
		Assert.assertEquals(account1.getName(), "Test user 1");
		Assert.assertEquals(account2.getName(), "Test user 2");
	}

	@Test
	public void test2() throws AccountNameRequiredException {

		Collection<Account> accounts = accountService.allAccounts();
		Assert.assertEquals(accounts.size(), 2);

		accounts.forEach(
				account -> {
					try {
						accountService.removeAccount(account.getNumber());
					} catch (AccountNotFoundException e) {
						Assert.fail();
					}
				}
		);

		accounts = accountService.allAccounts();
		Assert.assertEquals(accounts.size(), 0);
	}

}