package com.courses.management.common;

import com.courses.management.common.commands.Exit;
import com.courses.management.common.commands.Help;
import com.courses.management.common.commands.util.InputString;
import com.courses.management.course.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

public class MainController {

    private static final Logger LOGGER = LogManager.getLogger(MainController.class);
    private View view;
    private List<Command> commands;


    public MainController(View view, DataSource dataSource) {


        this.view = view;
        commands = Arrays.asList(new CreateCourse(view, new CourseDAOImpl(dataSource)),
                new Help(view),
                new Exit(view),
                new FindCourse(view, new CourseDAOImpl(dataSource)),
                new UpdateCourseStatus(view, new CourseDAOImpl(dataSource)),
                new UpdateCourseTitle(view, new CourseDAOImpl(dataSource)),
                new DeleteCourse(view, new CourseDAOImpl(dataSource)),
                new ShowCourses(view, new CourseDAOImpl(dataSource))
        );
    }

    public void read() {
        view.write("Welcome");
        while (true) {
            view.write("Enter a command or use help to list all available commands:");
            String read = view.read();
            doCommand(read);
        }
    }

    private void doCommand(String input) {
        LOGGER.debug(String.format("doCommand: input=%s", input));
        InputString entry = new InputString(input);
        for (Command command : commands) {
            try {
                if (command.canProcess(entry)) {
                    entry.validateParameters(command.command());
                    command.process(entry);
                    break;
                }
            } catch (Exception e) {
                LOGGER.warn("doCommand. Warning " + e.getMessage());
                printError(e);
                break;

            }
        }
    }

    private void printError(Exception e) {
        String message = e.getMessage();
        view.write("Error because of " + message);
        view.write("Please try again");
    }

}
