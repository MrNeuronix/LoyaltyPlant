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

import java.math.BigDecimal;
import java.util.Collection;

import ru.poliscam.bank.processing.database.model.Payment;
import ru.poliscam.bank.processing.service.ProcessingService;
import ru.poliscam.bank.processing.service.exceptions.AccountNotFoundException;
import ru.poliscam.bank.processing.service.exceptions.DestinationAccountRequiredException;
import ru.poliscam.bank.processing.service.exceptions.InsufficientMoneyException;
import ru.poliscam.bank.processing.service.exceptions.UnknownPaymentTypeException;
import ru.poliscam.bank.web.model.ProcessingRequest;
import ru.poliscam.bank.web.model.status.ErrorStatus;
import ru.poliscam.bank.web.model.BalanceRequest;
import ru.poliscam.bank.web.model.HistoryRequest;
import ru.poliscam.bank.web.model.status.OkStatus;

@RestController
public class ProcessingController {

	private final ProcessingService service;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public ProcessingController(ProcessingService service)	{
		this.service = service;
	}

	/**
	 * Баланс денег на счету
	 *
	 * @param request запрос
	 * @return текущий баланс счета
	 */
	@RequestMapping(value = "/api/processing/balance", method = RequestMethod.POST)
	public Object balance(@RequestBody BalanceRequest request) {
		long start = System.currentTimeMillis();
		BigDecimal balance;

		try {
			balance = service.getBalance(request.getNumber());
		} catch (AccountNotFoundException e) {
			return new ErrorStatus("Account not found");
		}

		// Если приключилась какая то беда, вроде rollback
		if(balance == null) {
			return new ErrorStatus("Something goes wrong");
		}

		return new OkStatus(balance, System.currentTimeMillis()-start);
	}

	/**
	 * Добавить денег на счет
	 *
	 * @param request запрос
	 * @return итоговый баланс счета
	 */
	@RequestMapping(value = "/api/processing/plus", method = RequestMethod.POST)
	public Object plus(@RequestBody ProcessingRequest request) {
		long start = System.currentTimeMillis();
		BigDecimal balance = null;

		try {
			balance = service.addMoney(request.getNumber(), request.getAmount());
		} catch (AccountNotFoundException e) {
			return new ErrorStatus("Account not found");
		} catch (InsufficientMoneyException e) {
			return new ErrorStatus("Insufficient money");
		} catch (DestinationAccountRequiredException | UnknownPaymentTypeException e) {
			// ignored
		}

		// Если приключилась какая то беда, вроде rollback
		if(balance == null) {
			return new ErrorStatus("Something goes wrong");
		}

		return new OkStatus(balance, System.currentTimeMillis()-start);
	}

	/**
	 * Убрать денег со счета
	 *
	 * @param request запрос
	 * @return итоговый баланс счета
	 */
	@RequestMapping(value = "/api/processing/minus", method = RequestMethod.POST)
	public Object minus(@RequestBody ProcessingRequest request) {
		long start = System.currentTimeMillis();
		BigDecimal balance = null;

		try {
			balance = service.spentMoney(request.getNumber(), request.getAmount());
		} catch (AccountNotFoundException e) {
			return new ErrorStatus("Account not found");
		} catch (InsufficientMoneyException e) {
			return new ErrorStatus("Insufficient money");
		} catch (DestinationAccountRequiredException | UnknownPaymentTypeException e) {
			// ignored
		}

		// Если приключилась какая то беда, вроде rollback
		if(balance == null) {
			return new ErrorStatus("Something goes wrong");
		}

		return new OkStatus(balance, System.currentTimeMillis()-start);
	}

	/**
	 * Перевести деньги со счета на счет
	 *
	 * @param request запрос
	 * @return итоговый баланс счета, с которого переводились деньги
	 */
	@RequestMapping(value = "/api/processing/transfer", method = RequestMethod.POST)
	public Object transfer(@RequestBody ProcessingRequest request) {
		long start = System.currentTimeMillis();
		BigDecimal balance;

		try {
			balance = service.transferMoney(request.getNumber(), request.getTo(), request.getAmount());
		} catch (AccountNotFoundException e) {
			return new ErrorStatus("Account not found");
		} catch (InsufficientMoneyException e) {
			return new ErrorStatus("Insufficient money");
		} catch (UnknownPaymentTypeException e) {
			return new ErrorStatus("Something goes wrong");
		} catch (DestinationAccountRequiredException e) {
			return new ErrorStatus("Destination account does not specified");
		}

		// Если приключилась какая то беда, вроде rollback
		if(balance == null) {
			return new ErrorStatus("Something goes wrong");
		}

		return new OkStatus(balance, System.currentTimeMillis()-start);
	}

	/**
	 * Получить историю транзакций по счету
	 *
	 * @param request запрос
	 * @return история транзакций
	 */
	@RequestMapping(value = "/api/processing/history", method = RequestMethod.POST)
	public Object payments(@RequestBody HistoryRequest request) {
		long start = System.currentTimeMillis();
		Collection<Payment> payments;

		try {
			payments = service.getPayments(request.getNumber());
		} catch (AccountNotFoundException e) {
			return new ErrorStatus("Account not found");
		}

		return new OkStatus(payments, System.currentTimeMillis()-start);
	}

	@ExceptionHandler({org.springframework.http.converter.HttpMessageNotReadableException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorStatus resolveException() {
		logger.error("Error happens");
		return new ErrorStatus("something goes wrong");
	}

}
