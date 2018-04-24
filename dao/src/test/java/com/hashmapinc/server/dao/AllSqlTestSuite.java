package com.hashmapinc.server.dao;

import org.junit.ClassRule;
import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.runner.RunWith;

import java.util.Arrays;

@RunWith(ClasspathSuite.class)
@ClasspathSuite.ClassnameFilters({
        "com.hashmapinc.server.controller.sql.*SqlTest",
        "com.hashmapinc.server.mqtt.rpc.sql.*Test",
        "com.hashmapinc.server.mqtt.telemetry.sql.*Test",
        "com.hashmapinc.server.system.sql.*SqlTest",
        "com.hashmapinc.server.dao.service.*ServiceSqlTest"
})
public class AllSqlTestSuite {

    @ClassRule
    public static CustomSqlUnit sqlUnit = new CustomSqlUnit(
            Arrays.asList("sql/schema.sql", "sql/system-data.sql"),
            "sql/drop-all-tables.sql",
            "sql-test.properties",
            Arrays.asList("sql/upgrade/1.sql", "sql/upgrade/2.sql")
    );
}