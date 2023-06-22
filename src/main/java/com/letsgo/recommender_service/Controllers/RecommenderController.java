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

    @GetMapping("/recommend/registered/{userId}/{userRole}")
    public ResponseEntity<Trip> getRecommendedTrip(@PathVariable Long userId,
                                                   @PathVariable String userRole) {
        Trip recommendedTrip = recommemderService.printRecommendTrip(userId, userRole);
        return new ResponseEntity<>(recommendedTrip, HttpStatus.OK);
    }

    @GetMapping("/recommend/unregistered/{locality}")
    public ResponseEntity<Trip> getRecommendTrip(@PathVariable String locality,
                                                 @PathVariable String userRole) {
        Trip recommendedTrip = recommemderService.printRecommendTrip0(userRole, locality);

        return new ResponseEntity<>(recommendedTrip, HttpStatus.OK);
    }
}
