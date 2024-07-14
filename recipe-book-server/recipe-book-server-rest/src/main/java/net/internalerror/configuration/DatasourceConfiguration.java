package net.internalerror.configuration;

import lombok.SneakyThrows;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

@Configuration
public class DatasourceConfiguration {

    @Value("${datasource.url}")
    private String datasourceUrl;

    @Value("${datasource.username}")
    private String datasourceUsername;

    @Value("${datasource.password}")
    private String datasourcePassword;

    @Bean
    @SneakyThrows
    public DSLContext dslContext() {
        Connection connection = DriverManager.getConnection(datasourceUrl, datasourceUsername, datasourcePassword);
        return DSL.using(connection);
    }

}
