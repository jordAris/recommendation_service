package com.letsgo.recommender_service;

import com.letsgo.recommender_service.Services.RecommemderService;
import com.letsgo.recommender_service.models.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class RecommenderApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(RecommenderApplication.class, args);
	}


	@Autowired
	RecommemderService recommemderService;


	@Override
	public void run(String... args) throws Exception {

		//List<Trip> trips = recommemderService.sortSearch(new ArrayList<>(), "eeee");

		Trip tripList = recommemderService.printRecommendTrip(155L);

		// System.out.println(trips);
		System.out.println(tripList);
	}
}
