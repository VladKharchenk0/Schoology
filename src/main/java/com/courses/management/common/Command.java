package com.courses.management.common;

import java.sql.SQLException;

public interface Command {
    String command();

    void process();

    default boolean canProcess(String command){
        return command.trim().equals(command());
    }
}
