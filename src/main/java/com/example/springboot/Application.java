package com.example.springboot;

import com.example.springboot.services.IEMDBSources;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		try {
			IEMDBSources.getInstance().importDataFromWeb();
		} catch (Exception e) {
			e.printStackTrace();
		}
		SpringApplication.run(Application.class, args);
	}

}
