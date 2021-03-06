package com.courses.management;

import com.courses.management.common.Console;
import com.courses.management.common.MainController;
import com.courses.management.common.View;
import com.courses.management.config.DatabaseConnector;
import com.courses.management.config.HibernateDatabaseConnector;

public class Main {
    private static final String APPLICATION_PROPERTIES_FILENAME = "application.properties";

    public static void main(String[] args) {
        View view = new Console();
        //TODO remove main class as this class is not used anymore.
        DatabaseConnector.init(APPLICATION_PROPERTIES_FILENAME);
        HibernateDatabaseConnector.init();
        MainController controller = new MainController(view, DatabaseConnector.getDataSource(), HibernateDatabaseConnector.getSessionFactory());
        controller.read();
    }
}