package com.letsgo.recommender_service.Controllers;

import com.letsgo.recommender_service.Services.RecommemderService;
import com.letsgo.recommender_service.models.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/recommender")
public class RecommenderController {
    private final RecommemderService recommemderService;

    @Autowired
    public RecommenderController(RecommemderService recommemderService) {
        this.recommemderService = recommemderService;
    }

    @GetMapping("/trips/{userId}")
    public ResponseEntity<List<Trip>> getSortedTrips(@PathVariable Long userId,
                                                     @PathVariable List<Trip> trips) {
        try {
            List<Trip> sortedTrips = recommemderService.sortSearch(trips,userId.toString());
            return new ResponseEntity<>(sortedTrips, HttpStatus.OK);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/recommend/{userId}")
    public ResponseEntity<Trip> getRecommendedTrip(@PathVariable Long userId) {
        Trip recommendedTrip = recommemderService.printRecommendTrip(userId);
        return new ResponseEntity<>(recommendedTrip, HttpStatus.OK);
    }
}
