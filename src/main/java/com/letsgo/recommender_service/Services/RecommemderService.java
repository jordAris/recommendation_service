package com.letsgo.recommender_service.Services;

import com.letsgo.recommender_service.Repositories.PlannerRepository;
import com.letsgo.recommender_service.Repositories.PoolerRepository;
import com.letsgo.recommender_service.Repositories.TripRepository;
import com.letsgo.recommender_service.models.Trip;
import graphql.schema.GraphQLSchema;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RecommemderService {
    private final PoolerRepository poolerRepository;
    private final TripRepository tripRepository;
    private final PlannerRepository plannerRepository;
    private RestTemplate restTemplate;
    private SparkSession sparkSession;
    private final GraphQLSchema graphQLSchema;
    private final RestClient esClient;
    private static final double LOCALITY_WEIGHT = 0.5;
    private static final double FAVORITE_DESTINATION_WEIGHT = 0.3;
    private static final double TRIP_HISTORY_WEIGHT = 0.2;

    @Autowired
    public RecommemderService(PoolerRepository userRepository,
                              TripRepository tripRepository,
                              PlannerRepository plannerRepository, GraphQLSchema graphQLSchema, RestClient esClient) {
        this.poolerRepository = userRepository;
        this.tripRepository = tripRepository;
        this.plannerRepository = plannerRepository;
        this.graphQLSchema = graphQLSchema;
        this.esClient = esClient;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public Trip printRecommendTrip(String userId){
        Trip trips = new Trip();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String query = "query { user(id: \""+ userId + "\") }";
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(Map.of("query", query), headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange("userMicroserviceURl", HttpMethod.POST, requestEntity, Map.class);
        Map<String, Object> responseBody = responseEntity.getBody();
        Object UserID = responseBody.get("id");
        Object UserLocality = responseBody.get("locality");
        Object UserFavDest = responseBody.get("fav_dest");

        if(UserID != null && UserID.equals(userId)) {
            SparkConf sparkConf = new SparkConf();
            sparkConf.setMaster("spark://localhost:7077");
            sparkConf.setAppName("RecommenderService");

            this.sparkSession = SparkSession
                    .builder()
                    .config(sparkConf)
                    .getOrCreate();

            SparkContext sc = new SparkContext(sparkConf);

            String apiEndpoint = "http://192.168.1.42:8080/api/recommendation";
            JavaRDD<String> data = sc.textFile(apiEndpoint);

            // treatment to get only trips attributes that I need

            this.sparkSession.close();
        }


        return trips;
    }

    public List<Trip> sortSearch(List<Trip> Trips, String userID) throws ParseException {
        List<Trip> trips = new ArrayList<>();
        List<Trip> tripList = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String query = "query { tripsByUsers(id: \""+ userID + "\") }";
        String query1 = "query { user(id: \"" + userID + "\") }";
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(Map.of("query", query), headers);
        HttpEntity<Map<String, Object>> requestEntity1 = new HttpEntity<>(Map.of("query", query1), headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange("TripsServiceSearchEndpoint", HttpMethod.POST, requestEntity, Map.class);
        ResponseEntity<Map> responseEntity1 = restTemplate.exchange("UserServiceGetUserEndpoint", HttpMethod.POST, requestEntity1, Map.class);


        Map<String, Object> responseBody = responseEntity.getBody();
        Map<String, Object> responseBody1 = responseEntity1.getBody();
        Object Locality = responseBody1.get("locality");
        Object poolerId = responseBody1.get("id");
        Object poolerFavDest = responseBody1.get("fav_dest");
        List<Map<String, Object>> UserTrips = (List<Map<String, Object>>) responseBody.get("trips");



        if ((Locality instanceof String) && (poolerFavDest instanceof String)){
            locality = (String) Locality;
            fav_dest = (String) poolerFavDest;

            for (Map<String, Object> trip : UserTrips) {
                Object Id = trip.get("id");
                Object source = trip.get("source");
                Object destination = trip.get("destination");
                Object price = trip.get("price");
                Object plannerId = trip.get("plannerId");
                Object driverId = trip.get("driverId");
                Object start = trip.get("Start");
                Object End = trip.get("End");
                Object trajet = trip.get("trajetId");

                if ((Id instanceof String) && (source instanceof String) && (destination instanceof String) && (price instanceof String) && (plannerId instanceof String) && (driverId instanceof String) && (start instanceof Date) && (End instanceof Date) && (trajet instanceof String)){
                    Trip tripHis = new Trip((String) Id, (String) source, (String) destination, (Double) price, (Date) start, (Date) End, (String) plannerId, (String) trajet);
                    tripList.add(tripHis);
                }
            }
        }


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
