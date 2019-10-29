package com.main.jobs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.main.model.User;
import com.main.model.VerificationToken;
import com.main.model.interfaces.IVerificationToken;
import com.main.repository.VerificationTokenRepository;
import com.main.service.UserService;

import lombok.extern.java.Log;

/**
 * 
 * Klasse zum Erstellen von Cronjobs
 * 
 * @author Markus
 * @since 22.10.2019
 * @version 1.0
 */

@Log
@Component
public class ScheduledTask {
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("DD.MM.yyyy HH:mm:ss");

	private VerificationTokenRepository verificationTokenRepository;
	
	private UserService service;
	
	public ScheduledTask(VerificationTokenRepository verificationTokenRepository, UserService service) {
		this.verificationTokenRepository = verificationTokenRepository;
		this.service = service;
	}
	
	/**
	 * Funktion überprüft jede Stunde ob ein Verificationtoken <br>
	 *  abgelaufen ist
	 * 
	 * @author Markus
	 */
	
	@Scheduled(fixedRate = 1000 * 60 * 60)
	@Transactional
	public void task() {
		List<IVerificationToken> tokens = StreamSupport.stream( verificationTokenRepository.findAll().spliterator(), false).collect(Collectors.toList());
		for (IVerificationToken token : tokens) {
			if(isExpired(token.getExpiryDate())) {
				log.info("Token:" + token.getToken() + " got deleted at" + dateFormat.format(new Date()) );
				User user = token.getUser();
				verificationTokenRepository.delete((VerificationToken) token);	
				service.deleteUser(user);
			}
		}
	}


	
	
	private static boolean isExpired(Date date) {
		return date.before(new Date());
	}
	
}
