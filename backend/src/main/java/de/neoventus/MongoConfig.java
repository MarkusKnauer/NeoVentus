package de.neoventus;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

/**
 * Database connection
 * Created by Markus on 07.04.2017.
 */
public class MongoConfig extends AbstractMongoConfiguration {
    @Override
    protected String getDatabaseName() {
        return "admin";
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient("193.174.205.44");
    }
}
