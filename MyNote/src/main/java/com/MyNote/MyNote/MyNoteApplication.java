package com.MyNote.MyNote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class MyNoteApplication {
	public static void main(String[] args) {
		SpringApplication.run(MyNoteApplication.class, args);
	}

}





