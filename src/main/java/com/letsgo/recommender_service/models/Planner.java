package com.letsgo.recommender_service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.List;


@Node
public class Planner {

    @Id
    private String id;
    private List<String> carId;
    private String favDest;
    private String Localite;
    private double rating;
    private List<String> keywords;

    public Planner(String id, String favDest, String localite, double rating, String ...cardID) {
        this.carId = List.of(cardID);
        this.id = id;
        this.favDest = favDest;
        this.Localite = localite;
        this.rating = rating;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public double getRating() {
        return rating;
    }

    public String getId() {
        return id;
    }

    public String getFavDest() {
        return favDest;
    }

    public String getLocalite() {
        return Localite;
    }

    public List<String> getCarId() {
        return carId;
    }

    public List<String> getKeywords() {
        return keywords;
    }}
