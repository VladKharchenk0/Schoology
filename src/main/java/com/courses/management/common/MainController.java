package com.courses.management.common;

import com.courses.management.course.CreateCourse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainController {
    private View view;
    private List<Command> commands;


    public MainController(View view) {
        this.view = view;
        commands = Arrays.asList(new CreateCourse(view), new Help(view));
    }

    public void read() {
        view.write("Welcome");
        while (true) {
            String read = view.read();
            doCommand(read);
        }
    }

    private void doCommand(String input) {
        for (Command command: commands) {
            if(command.canProcess(input)){
                command.process();
                break;
            }
        }

        view.write("Enter a command or use help to list all available commands");
    }

}
