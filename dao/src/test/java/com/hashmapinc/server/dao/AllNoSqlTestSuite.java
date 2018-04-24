package com.hashmapinc.server.dao;

import org.cassandraunit.dataset.CQLDataSet;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.junit.ClassRule;
import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(ClasspathSuite.class)
@ClasspathSuite.ClassnameFilters({
        "com.hashmapinc.server.controller.nosql.*Test",
        "com.hashmapinc.server.mqtt.*.nosql.*Test",
        "com.hashmapinc.server.system.*NoSqlTest",
        "com.hashmapinc.server.dao.service.*ServiceNoSqlTest"

})
public class AllNoSqlTestSuite {
    @ClassRule
    public static CustomCassandraCQLUnit cassandraUnit =
            new CustomCassandraCQLUnit(
                    getDataSetLists(),
                    "cassandra-test.yaml", 30000l);

    private static List<CQLDataSet> getDataSetLists(){
        List<CQLDataSet> dataSets = new ArrayList<>();
        dataSets.add(new ClassPathCQLDataSet("cassandra/schema.cql", false, false));
        dataSets.add(new ClassPathCQLDataSet("cassandra/system-data.cql", false, false));
        dataSets.add(new ClassPathCQLDataSet("cassandra/system-test.cql", false, false));
        dataSets.addAll(Arrays.asList(
                new ClassPathCQLDataSet("cassandra/upgrade/1.cql", false, false)

        ));
        return dataSets;
    }
}
