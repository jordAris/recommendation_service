package com.letsgo.recommender_service.Repositories;

import com.letsgo.recommender_service.models.Planner;
import com.letsgo.recommender_service.models.Trip;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface TripRepository extends Neo4jRepository<Trip, String> {
    Trip findById(Long id);
    List<Trip> findAll();
    List<Trip> findByPlannerOrderByPriceDesc(Planner planner);
    List<Trip> findByUsers(String id);

}
