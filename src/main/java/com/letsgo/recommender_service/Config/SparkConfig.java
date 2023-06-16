package com.letsgo.recommender_service.Config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

public class SparkConfig {
    private static final String SPARK_MASTER_URL = "spark://localhost:7077";
    private static final String APP_NAME = "RecommenderService";

    public static SparkConf getSparkConf() {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setMaster(SPARK_MASTER_URL);
        sparkConf.setAppName(APP_NAME);
        return sparkConf;
    }

}
