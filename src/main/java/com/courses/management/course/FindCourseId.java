package com.courses.management.course;

import com.courses.management.common.Command;
import com.courses.management.common.DataAccessObject;
import com.courses.management.common.View;

import java.sql.SQLException;

public class FindCourseId implements Command {

    private final View view;
    private DataAccessObject<Course> courseDAO;


    public FindCourseId(View view) {
        this.view = view;
        courseDAO = new CourseDAOImpl();
    }

    @Override
    public String command() {
        return "find_course";
    }

    @Override
    public void process()  {
        view.write("Enter a course id");
        int courseId = Integer.parseInt(view.read());
        view.write(courseDAO.get(courseId).toString());
    }
}
