package com.example.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletApplication.class, args);
	}

}
// между анти-фрауд и конвертером синхронно . к ним черех сервис кошелька обращаться
// clients сделать , кафку сделать . по кафке только в уведомления и аудит события
//