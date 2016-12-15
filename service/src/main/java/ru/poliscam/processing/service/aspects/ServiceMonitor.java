package ru.poliscam.processing.service.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import ru.poliscam.processing.database.enums.PaymentType;

@Aspect
@Component
public class ServiceMonitor {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Before(
			value = "execution(* ru.poliscam.processing.service.ProcessingServiceImpl.*(..)) && args(number,type,to,amount)",
			argNames = "joinPoint,number,type,to,amount")
	public void logServicemethodStart(JoinPoint joinPoint, String number, PaymentType type, String to, BigDecimal amount) {
		logger.info("Started transaction from number: " + number + ", type is " + type.name() + ", destination: " +
		to + ", amount: " + amount.doubleValue());
	}

	@AfterReturning
			(value = "execution(* ru.poliscam.processing.service.ProcessingServiceImpl.*(..)) && args(number,type,to,amount)",
					argNames = "joinPoint,number,type,to,amount")
	public void logServicemethodStop(JoinPoint joinPoint, String number, PaymentType type, String to, BigDecimal amount) {
		logger.info("Completed transaction from number: " + number + ", type is " + type.name() + ", destination: " +
		                   to + ", amount: " + amount.doubleValue());
	}

}
