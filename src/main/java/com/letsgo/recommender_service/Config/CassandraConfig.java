package com.letsgo.recommender_service.Config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collection;


@Configuration
@EnableCassandraRepositories(basePackages = {"com.letsgo.recommender_service.Repositories"})
public class CassandraConfig {
    @Value("${cassandra.contactPoints}")
    private String contactPoints;

    @Value("${cassandra.port}")
    private int port;

    @Value("${cassandra.keyspace}")
    private String keyspace;

    @Bean
    public CqlSession cqlSession() {
        Collection<InetSocketAddress> contactPointsList = Arrays.asList(
                new InetSocketAddress(contactPoints, port)
        );
        return CqlSession.builder()
                .addContactPoints(contactPointsList)
                .withLocalDatacenter("datacenter1")
                .withKeyspace(keyspace)
                .build();
    }
}
