package ru.poliscam.bank.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ru.poliscam.bank.processing.database.model.Account;
import ru.poliscam.bank.processing.service.AccountService;
import ru.poliscam.bank.processing.service.exceptions.AccountNameRequiredException;
import ru.poliscam.bank.processing.service.exceptions.AccountNotFoundException;
import ru.poliscam.bank.web.model.AccountAddRequest;
import ru.poliscam.bank.web.model.AccountRemoveRequest;
import ru.poliscam.bank.web.model.status.ErrorStatus;
import ru.poliscam.bank.web.model.status.OkStatus;

@RestController
public class AccountController {

	private final AccountService service;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

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

		return new OkStatus(account, System.currentTimeMillis()-start);
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

		if(request.getNumber() == null)
			return new ErrorStatus("Empty account passed");

		try {
			service.removeAccount(request.getNumber());
		} catch (AccountNotFoundException e) {
			return new ErrorStatus("Account not found");
		}

		return new OkStatus("deleted", System.currentTimeMillis()-start);
	}

	/**
	 * Получить все счета
	 *
	 * @return JSON ответ успех либо ошибка
	 */
	@RequestMapping(value = "/api/account/all", method = RequestMethod.GET)
	public Object allAccounts() {
		long start = System.currentTimeMillis();
		return new OkStatus(service.allAccounts(), System.currentTimeMillis()-start);
	}

	@ExceptionHandler({org.springframework.http.converter.HttpMessageNotReadableException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorStatus resolveException() {
		logger.error("Error happens");
		return new ErrorStatus("something goes wrong");
	}
}
