package com.StudyCafe_R.infra.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;


@Transactional
@Component
public class DatabaseTruncator {

    private JdbcTemplate jdbcTemplate;

    public DatabaseTruncator(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public void truncateAllTables() {
        // Get the list of table names
        String query = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'test' AND table_type = 'BASE TABLE'";
        List<String> tableNames = jdbcTemplate.queryForList(query, String.class);

        // Truncate each table
        for (String tableName : tableNames) {
            try {
                System.out.println("truncating tableName = " + tableName);
                jdbcTemplate.execute("DELETE FROM " + tableName);
            } catch( Exception e ) {
                System.out.println("having exception due to foreign key but will ignore and keep going on");
            }
        }
    }
}
