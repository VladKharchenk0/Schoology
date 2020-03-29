package com.courses.management.course;

import com.courses.management.common.Command;
import com.courses.management.common.DataAccessObject;
import com.courses.management.common.View;

public class FIndCourseByTitle implements Command {

    private final View view;
    private CourseDAO courseDAO;


    public FIndCourseByTitle(View view) {
        this.view = view;
        courseDAO = new CourseDAOImpl();
    }

    @Override
    public String command() {
        return "find_course_by_title";
    }

    @Override
    public void process() {
        view.write("Enter a course title");
        String courseTitle = (view.read());
        view.write(courseDAO.get(courseTitle).toString());
    }
}
