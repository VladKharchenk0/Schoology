package com.courses.management.common.commands.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InputString {
    private static final Logger LOGGER = LogManager.getLogger(InputString.class);
    private String command;

    public InputString(String command) {
        this.command = command;
    }

    public void validateParameters(String inputString){
        int formatLength= getParameterSize(inputString);
        if(formatLength!=getLength()){
            LOGGER.error(String.format("\"Invalid number of parameters separated by |, expected %s, but was %s",
                    getLength(), formatLength));
            throw new IllegalArgumentException("Invalid number of parameters separated by |");
        }

    }

    public int getLength(){
        return getParameterSize(command);
    }

    public String [] getParameters(){
        return  command.split("\\|");
    }

    private int getParameterSize(String command) {
        return command.split("\\|").length;
    }
}
