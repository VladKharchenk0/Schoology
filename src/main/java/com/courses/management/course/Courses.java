package com.courses.management.course;

import com.courses.management.common.View;
import com.courses.management.common.commands.util.InputString;

public class Courses {
    public static Course mapCourse(InputString input){
            String[] parameters = input.getParameters();
            String courseTitle = parameters[1];
            Course course = new Course();
            course.setTitle(courseTitle);
            course.setCourseStatus(CourseStatus.NOT_STARTED);
            return course;
    }
    public static void printCourse(View view, Course course) {
        view.write("Course:");
        view.write(String.format("\t title = %s", course.getTitle()));
        view.write(String.format("\t status = %s", course.getCourseStatus()));
    }
}