package com.letsgo.recommender_service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.Date;
import java.util.List;

@Node
public class Trip {

    @Id
    private String id;
    private String source;
    private String destination;
    private Double price;
    private String driverId;
    private Date Start;
    private Date End;
    private String plannerId;
    private List<String> trajetId;

    public Trip(String id, String source, String destination, Double price, Date Start, Date End, String plannerId, String trajetId) {
        this.id=id;
        this.source=source;
        this.destination=destination;
        this.price=price;
        this.Start=Start;
        this.End=End;
        this.plannerId=plannerId;
        this.trajetId= List.of(trajetId);
    }

    public Trip() {

    }

    public Double getPrice() {
        return price;
    }

    public Date getEnd() {
        return End;
    }

    public Date getStart() {
        return Start;
    }

    public List<String> getTrajetId() {
        return trajetId;
    }

    public String getDestination() {
        return destination;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getId() {
        return id;
    }

    public String getPlannerId() {
        return plannerId;
    }

    public String getSource() {
        return source;
    }
}
