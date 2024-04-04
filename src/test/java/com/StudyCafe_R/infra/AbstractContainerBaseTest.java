package com.StudyCafe_R.infra;

import org.testcontainers.containers.PostgreSQLContainer;

public abstract class AbstractContainerBaseTest {

    static final PostgreSQLContainer POSTGRE_SQL_CONTAINER;
    static  {
        POSTGRE_SQL_CONTAINER = new PostgreSQLContainer(PostgreSQLContainer.IMAGE);
        POSTGRE_SQL_CONTAINER.start();
    }
}
