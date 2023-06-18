package com.letsgo.recommender_service.Config;

import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class GraphQLConfig {

    @Bean
    public GraphQLSchema graphQLSchema() throws IOException {
        Resource schemaResource = new ClassPathResource("graphql/schema.graphql");
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaResource.getFile());
        RuntimeWiring runtimeWiring = buildRuntimeWiring();
        return new SchemaGenerator().makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildRuntimeWiring() {

        return RuntimeWiring.newRuntimeWiring().build();
    }
}
