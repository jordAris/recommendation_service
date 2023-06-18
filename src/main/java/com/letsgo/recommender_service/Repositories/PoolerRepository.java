package com.letsgo.recommender_service.Repositories;

import com.letsgo.recommender_service.models.Pooler;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface PoolerRepository extends Neo4jRepository<Pooler, String> {
    Optional<Pooler> findById(String id);

    List<Pooler> findAll();


}
