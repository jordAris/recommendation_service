package com.letsgo.recommender_service.Services;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.letsgo.recommender_service.Repositories.PlannerRepository;
import com.letsgo.recommender_service.Repositories.PoolerRepository;
import com.letsgo.recommender_service.Repositories.TripRepository;
import com.letsgo.recommender_service.models.Trip;
import graphql.schema.GraphQLSchema;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.elasticsearch.client.RestClient;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RecommemderService {
    private final PoolerRepository poolerRepository;
    private final TripRepository tripRepository;
    private final PlannerRepository plannerRepository;
    private RestTemplate restTemplate;
    private CqlSession cqlSession;
    private SparkSession sparkSession;
    private final GraphQLSchema graphQLSchema;
    private final RestClient esClient;
    private static final double LOCALITY_WEIGHT = 0.5;
    private static final double FAVORITE_DESTINATION_WEIGHT = 0.3;
    private static final double TRIP_HISTORY_WEIGHT = 0.2;

    @Autowired
    public RecommemderService(PoolerRepository userRepository,
                              TripRepository tripRepository,
                              PlannerRepository plannerRepository, GraphQLSchema graphQLSchema,RestTemplate restTemplate, CqlSession cqlSession, RestClient esClient) {
        this.poolerRepository = userRepository;
        this.tripRepository = tripRepository;
        this.plannerRepository = plannerRepository;
        this.cqlSession = cqlSession;
        this.restTemplate = restTemplate;
        this.graphQLSchema = graphQLSchema;
        this.esClient = esClient;
    }


    public Trip printRecommendTrip(Long userId, String profileRole){
        Trip trips = new Trip();
        if (profileRole.equals("Planner") ) {
            Long PlannerId= 456L;
            String PlannerLocality = "Yaounde";
            String PlannerFavDest = "Bafia";
            // retrieve these information to UserService and ameliorate the knowledge db for the api
            String tripendpointUrl = "http://192.168.5.38:8000/TripService/TripsByplannerId";

            URI uri1 = UriComponentsBuilder.fromUriString(tripendpointUrl)
                    .build(userId);

            ResponseEntity<String> response1 = restTemplate.exchange(uri1, HttpMethod.GET, null, String.class);
            if (response1.getStatusCode().is2xxSuccessful()) {
                String responseBody = response1.getBody();
                // Process the response data as needed
                System.out.println(responseBody);
            } else {
                // Handle error response
                System.err.println("Error: " + response1.getStatusCode());
            }

            String apiUrl = "http://127.0.0.1:8000/RecommendationsPlanner/{PlannerId}/{locality}/{dest_fav}";

            URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                    .build(PlannerId, PlannerLocality, PlannerFavDest);

            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                // Process the response data as needed
                System.out.println(responseBody);
            } else {
                // Handle error response
                System.err.println("Error: " + response.getStatusCode());
            }
            
        } else if ( profileRole.equals("pooler")) {
            Long poolerId = 223L;
            String UserLocality = "Yaounde";
            String UserFavDest = "Bafia";
            // retrieve these information to UserService and ameliorate the knowledge db for the api
            String tripendpointUrl = "http://192.168.5.38:8000/TripService/TripsforPooler";

            URI uri1 = UriComponentsBuilder.fromUriString(tripendpointUrl)
                    .build(userId);

            ResponseEntity<String> response1 = restTemplate.exchange(uri1, HttpMethod.GET, null, String.class);
            if (response1.getStatusCode().is2xxSuccessful()) {
                String responseBody = response1.getBody();
                // Process the response data as needed
                System.out.println(responseBody);
            } else {
                // Handle error response
                System.err.println("Error: " + response1.getStatusCode());
            }

            String apiUrl = "http://127.0.0.1:8000/RecommendationsTraveler/{poolerID}/{locality}/{fav_dest}";

            URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                    .build(poolerId, UserLocality, UserFavDest);

            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                // Process the response data as needed
                System.out.println(responseBody);
            } else {
                // Handle error response
                System.err.println("Error: " + response.getStatusCode());
            }
        }

        return trips;
    }

    public Trip printRecommendTrip0(String profileRole, String locality){
        Trip trips = new Trip();
        if (profileRole.equals("Planner0")) {
            // retrieve locality from the service responsible for it and ameliorate the knowledge db for the api
            String tripendpointUrl = "http://192.168.5.38:8000/TripService/allTrips";

            URI uri1 = UriComponentsBuilder.fromUriString(tripendpointUrl)
                    .build(locality);

            ResponseEntity<String> response1 = restTemplate.exchange(uri1, HttpMethod.GET, null, String.class);
            if (response1.getStatusCode().is2xxSuccessful()) {
                String responseBody = response1.getBody();
                // Process the response data as needed
                System.out.println(responseBody);
            } else {
                // Handle error response
                System.err.println("Error: " + response1.getStatusCode());
            }

            // store in db

            String apiUrl = "http://127.0.0.1:8000/RecommendationsNewPlanner_0/{locality}";

            URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                    .build(locality);

            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                // Process the response data as needed
                System.out.println(responseBody);
            } else {
                // Handle error response
                System.err.println("Error: " + response.getStatusCode());
            }

        } else if ( profileRole.equals("pooler0")) {
            // retrieve these information to UserService and ameliorate the knowledge db for the api

            String tripendpointUrl = "http://192.168.5.38:8000/TripService/allTrips";

            URI uri1 = UriComponentsBuilder.fromUriString(tripendpointUrl)
                    .build(locality);

            ResponseEntity<String> response1 = restTemplate.exchange(uri1, HttpMethod.GET, null, String.class);
            if (response1.getStatusCode().is2xxSuccessful()) {
                String responseBody = response1.getBody();
                // Process the response data as needed
                System.out.println(responseBody);
            } else {
                // Handle error response
                System.err.println("Error: " + response1.getStatusCode());
            }

            String apiUrl = "http://127.0.0.1:8000/RecommendationsNewTraveler/{locality}";

            URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                    .build(locality);

            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                // Process the response data as needed
                System.out.println(responseBody);
            } else {
                // Handle error response
                System.err.println("Error: " + response.getStatusCode());
            }
        }

        return trips;
    }

    public List<Trip> sortSearch(List<Trip> Trips, String userID) throws ParseException {
        List<Trip> trips = new ArrayList<>();
        List<Trip> tripList = new ArrayList<>();

        String locality = "Yaounde";
        String fav_dest = "Kribi";
        String dateString = "3-9-2006";
        String EntryDateTest = "4-6-2004";
        String GetOutDateTest = "6-6-2004";
        String dateEndString = "4-9-2006";
        String pattern = "dd-MM-yyyy";

        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date start = dateFormat.parse(dateString);
        Date End = dateFormat.parse(dateEndString);
        Date testEnt = dateFormat.parse(EntryDateTest);
        Date testArr = dateFormat.parse(GetOutDateTest);

        Trip triptest = new Trip("986a45dd", "Yaounde, Melen", "Yaounde, Nkozoa", 5200.50, start, End, "1addas186", "fds1212ds3");
        tripList.add(triptest);
        Trip triptest1 = new Trip("986a45dd", "Yaounde, Melen", "Yaounde, Nkozoa", 5100.50, start, End, "1adfsda46", "fds12sdads3");
        tripList.add(triptest1);
        Trip triptest6 = new Trip("986a45dd", "Yaounde, Biteng", "Douala, Deido", 20100.50, start, End, "1adfsda46", "fds12erhges3");
        tripList.add(triptest6);
        Trip triptes45 = new Trip("986a45dd", "Kribi,Mairie", "Kribi, deportuaire", 500.50, start, End, "1adfsda46", "fds1utgfkjds3");
        tripList.add(triptes45);
        Trip triptest11 = new Trip("986a45dd", "Yaounde, Melen", "Yaounde, Nkozoa", 600.50, start, End, "1adfdasda46", "fds12sdads3");
        tripList.add(triptest11);
        Trip triptest10 = new Trip("986a45dd", "Yaounde, Melen", "Yaounde, Nkozoa", 1100.50, start, End, "1adqvsda46", "fdjhgf2sjutds3");
        tripList.add(triptest10);
        Trip triptest12 = new Trip("986a45dd", "Yaounde, Melen", "Yaounde, Nkozoa", 15100.50, start, End, "1afgerfsda46", "fds1jlkjads3");
        tripList.add(triptest12);


        Trip triptest2 = new Trip("986a45dd", "Yaounde, Melen", "Yaounde, Nkozoa", 500.50, start, End, "1addas186", "fds1212ds3");
        Trips.add(triptest2);
        Trip triptest4 = new Trip("986a45dd", "Yaounde, Melen", "Yaounde, Nkozoa", 3600.50, start, End, "1adfsda46", "fds12sdads3");
        Trips.add(triptest4);
        Trip triptest64 = new Trip("986a45dd", "Yaounde, Biteng", "Douala, Deido", 15500.00, start, End, "1adfsda46", "fds12erhges3");
        Trips.add(triptest64);
        Trip triptes9 = new Trip("986a45dd", "Kribi,Mairie", "Kribi, deportuaire", 300.50, start, End, "1adfsda46", "fds1utgfkjds3");
        Trips.add(triptes9);
        Trip triptest22 = new Trip("986a45dd", "Yaounde, Melen", "Yaounde, Nkozoa", 800.50, start, End, "1adfdasda46", "fds12sdads3");
        Trips.add(triptest22);
        Trip triptest13 = new Trip("986a45dd", "Yaounde, Melen", "Yaounde, Nkozoa", 1500.50, start, End, "1adqvsda46", "fdjhgf2sjutds3");
        Trips.add(triptest13);
        Trip triptest16 = new Trip("986a45dd", "Yaounde, Melen", "Yaounde, Nkozoa", 13000.00, start, End, "1afgerfsda46", "fds1jlkjads3");
        Trips.add(triptest16);


        UserData userData = new UserData(locality, fav_dest, tripList);

        List<TripWithScore> tripsWithScore = calculateCompositeScoreForTrips(Trips, userData);

        tripsWithScore.sort((trip1, trip2) -> Double.compare(trip2.getScore(), trip1.getScore()));
        for (TripWithScore tripWithScore : tripsWithScore) {
            trips.add(tripWithScore.getTrip());
        }

        return trips;
    }

    public void UpgradeRecommendation() {

        String tripEndpointUrl = "http://192.168.5.38:8000/TripService/allTrips";

        URI uri = URI.create(tripEndpointUrl);

        ResponseEntity<Trip[]> response = restTemplate.exchange(uri, HttpMethod.GET, null, Trip[].class);
        if (response.getStatusCode().is2xxSuccessful()) {
            Trip[] trips = response.getBody();
            List<Trip> tripList = Arrays.asList(trips);
            // Process the list of trips as needed
            String createQuery = "CREATE KEYSPACE IF NOT EXISTS apiBase WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };" +
                    "USE apiBase;" +
                    "CREATE TABLE trips (\n" +
                    "  id uuid PRIMARY KEY,\n" +
                    "  source text,\n" +
                    "  destination text,\n" +
                    "  price double, \n" +
                    "  driverId text" +
                    "  startDate date,\n" +
                    "  endDate date, \n" +
                    "  plannerId, \n" +
                    "  trajet tuple(id uuid, duration date, source text, end text)" +
                    ");";
            cqlSession.execute(createQuery);
            for (Trip trip : tripList) {
                // Do something with each trip
                String storedquery = "INSERT INTO trips (id, source, destination, price, driverId, startDate, endDate, plannerId, trajet) VALUES (?, ?, ?, ?, ?, ?, ?, ?, tuple(?, ?, ?, ?));";
                PreparedStatement preparedStatement = cqlSession.prepare(storedquery);
                BoundStatement boundStatement = preparedStatement.bind(
                        trip.getId(),
                        trip.getSource(),
                        trip.getDestination(),
                        trip.getPrice(),
                        trip.getDriverId(),
                        trip.getStart(),
                        trip.getEnd(),
                        trip.getPlannerId(),
                        trip.getTrajetId()
                );
                cqlSession.execute(boundStatement);
            }
        } else {
            // Handle error response
            System.err.println("Error: " + response.getStatusCode());
        }

        String reservEndpointUrl = "http://192.168.5.38:8000/ReservationService/allTrips";

        URI uri1 = URI.create(reservEndpointUrl);

        ResponseEntity<Trip[]> response1 = restTemplate.exchange(uri1, HttpMethod.GET, null, Trip[].class);
        if (response.getStatusCode().is2xxSuccessful()) {
            Trip[] trips = response1.getBody();
            List<Trip> tripList = Arrays.asList(trips);
            // Process the list of trips as needed
            for (Trip trip : tripList) {
                // Do something with each trip
                System.out.println(trip);
            }
        } else {
            // Handle error response
            System.err.println("Error: " + response1.getStatusCode());
        }

        String UserEndpointUrl = "http://192.168.5.38:8000/UserService/getLocalityandFav_destForALlUser";

        URI uri2 = URI.create(reservEndpointUrl);

        ResponseEntity<Trip[]> response2 = restTemplate.exchange(uri2, HttpMethod.GET, null, Trip[].class);
        if (response.getStatusCode().is2xxSuccessful()) {
            Trip[] trips = response2.getBody();
            List<Trip> tripList = Arrays.asList(trips);
            // Process the list of trips as needed
            for (Trip trip : tripList) {
                // Do something with each trip
                System.out.println(trip);
            }
        } else {
            // Handle error response
            System.err.println("Error: " + response2.getStatusCode());
        }

    }

    private List<TripWithScore> calculateCompositeScoreForTrips(List<Trip> trips, UserData userData) {
        List<TripWithScore> tripsWithScore = new ArrayList<>();
        for (Trip trip : trips) {
            double score = calculateCompositeScore(trip, userData);
            tripsWithScore.add(new TripWithScore(trip, score));
        }
        return tripsWithScore;
    }

    private double calculateCompositeScore(Trip trip, UserData userData) {
        double localityScore = calculateLocalityScore(trip.getSource(), userData.getLocalite());
        double favoriteDestinationScore = calculateFavoriteDestinationScore(trip.getDestination(), userData.getDestFav());
        double tripHistoryScore = calculateTripHistoryScore(trip, userData.getUserTrips());

        return (localityScore * LOCALITY_WEIGHT) +
                (favoriteDestinationScore * FAVORITE_DESTINATION_WEIGHT) +
                (tripHistoryScore * TRIP_HISTORY_WEIGHT);
    }

    private double calculateLocalityScore(String tripLocality, String userLocality) {
        // Custom logic to calculate the score for locality
        // Implementation of calculating locality score goes here

        return 0;
    }

    private double calculateFavoriteDestinationScore(String tripDestination, String userFavoriteDestination) {
        // Custom logic to calculate the score for favorite destination
        // Implementation of calculating favorite destination score goes here

        return 0;
    }

    private double calculateTripHistoryScore(Trip trip, List<Trip> userTripHistory) {
        // Custom logic to calculate the score for trip history
        // Implementation of calculating trip history score goes here

        return 0;
    }



    public class UserData {
        private String Locality;
        private String dest_fav;
        private List<Trip> tripHistory;


        public UserData(String Locality, String dest_fav, List<Trip> tripHistory) {
            this.Locality = Locality;
            this.dest_fav = dest_fav;
            this.tripHistory = tripHistory;
        }


        public String getLocalite() {
            return Locality;
        }

        public String getDestFav() {
            return dest_fav;
        }

        public List<Trip> getUserTrips() {
            return tripHistory;
        }
    }

    public class TripWithScore {
        private Trip trip;
        private double score;

        public TripWithScore(Trip trip, double score) {
            this.trip = trip;
            this.score = score;
        }
        public Trip getTrip() {
            return trip;
        }
        public double getScore() {
            return score;
        }
    }


}
