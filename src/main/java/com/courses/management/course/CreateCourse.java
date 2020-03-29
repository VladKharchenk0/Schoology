package com.courses.management.course;

import com.courses.management.common.Command;
import com.courses.management.common.DataAccessObject;
import com.courses.management.common.View;
import com.courses.management.common.commands.util.InputString;

public class CreateCourse implements Command {

    private final View view;
    private DataAccessObject<Course> courseDAO;

    public CreateCourse(View view) {
        this.view = view;
        courseDAO = new CourseDAOImpl();
    }

    @Override
    public String command() {
        return "create_course|title";
    }

    @Override
    public void process(InputString input) {
        Course course = mapCourse(input);
        courseDAO.create(course);
        view.write("Course created with title -> %s" + course.getTitle());
    }

    private Course mapCourse(InputString input) {
        String[] parameters = input.getParameters();
        String courseTitle = parameters[1];
        Course course = new Course();
        course.setTitle(courseTitle);
        course.setCourseStatus(CourseStatus.NOT_STARTED);
        return course;
    }


}

