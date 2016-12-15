package ru.poliscam.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ru.poliscam.processing.database.model.Account;
import ru.poliscam.processing.service.AccountService;
import ru.poliscam.processing.service.exceptions.AccountNameRequiredException;
import ru.poliscam.processing.service.exceptions.AccountNotFoundException;
import ru.poliscam.web.model.AccountAddRequest;
import ru.poliscam.web.model.AccountRemoveRequest;
import ru.poliscam.web.model.status.ErrorStatus;
import ru.poliscam.web.model.status.OkStatus;

@RestController
public class AccountController {

	private final AccountService service;

	@Autowired
	public AccountController(AccountService service)	{
		this.service = service;
	}

	/**
	 * Создать новый счет
	 *
	 * @param request запрос
	 * @return аккаунт в JSON представлении
	 */
	@RequestMapping(value = "/api/account/add", method = RequestMethod.POST)
	public Object addAccount(@RequestBody AccountAddRequest request) {
		long start = System.currentTimeMillis();
		Account account;

		try {
			account = service.addAccount(request.getName());
		} catch (AccountNameRequiredException e) {
			return new ErrorStatus("Account name required");
		}

		return new OkStatus(account.toString(), System.currentTimeMillis()-start);
	}

	/**
	 * Удалить счет
	 *
	 * @param request запрос
	 * @return JSON ответ успех либо ошибка
	 */
	@RequestMapping(value = "/api/account/remove", method = RequestMethod.POST)
	public Object removeAccount(@RequestBody AccountRemoveRequest request) {
		long start = System.currentTimeMillis();

		try {
			service.removeAccount(request.getNumber());
		} catch (AccountNotFoundException e) {
			return new ErrorStatus("Account not found");
		}

		return new OkStatus("deleted", System.currentTimeMillis()-start);
	}
}
