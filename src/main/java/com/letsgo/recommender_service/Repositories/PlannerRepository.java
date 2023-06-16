package com.letsgo.recommender_service.Repositories;

import com.letsgo.recommender_service.models.Planner;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlannerRepository extends Neo4jRepository<Planner, String>{
    Optional<Planner> findById(String id);

    public List<Planner> findAll();

    public List<Planner> plannerByLocalite(String Localite);

    List<Planner> plannerByfavDest(String favDest);
}
