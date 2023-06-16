package com.letsgo.recommender_service.models;

import java.util.List;

public class Pooler {
    private String id;
    private List<String> keywords;
    private String Localite;

    public Pooler(String id, String localite, String ...keywords){
        this.keywords = List.of(keywords);
        this.id = id;
        this.Localite=localite;
    }

    public String getId(){
        return id;
    }

    public String getLocalite(){
        return Localite;
    }

    public List<String> getKeywords() {
        return keywords;
    }
}
