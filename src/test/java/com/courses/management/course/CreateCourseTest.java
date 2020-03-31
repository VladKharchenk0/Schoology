package com.courses.management.course;


import com.courses.management.common.Command;
import com.courses.management.common.View;
import com.courses.management.common.commands.util.InputString;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CreateCourseTest {
    private Command command;
    private CourseDAO dao;
    private View view;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        this.view = mock(View.class);
        this.dao = mock(CourseDAO.class);
        this.command = new CreateCourse(view, dao);
    }

    @Test
    public void testCanProcessWithCorrectCommand() {
        //given
        InputString inputString = new InputString("create_course|JAVA");
        //when
        boolean result = command.canProcess(inputString);
        //then
        assertTrue(result);
    }

    @Test
    public void testCanNotProcessWithWrongCommand() {
        //given
        InputString inputString = new InputString("create|JAVA");
        //when
        boolean result = command.canProcess(inputString);
        //then
        assertFalse(result);
    }


    @Test
    public void testProcessWithAlreadyExistTitle() {
        //given
        Course course = new Course();
        course.setTitle("JAVA");
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Course with this title already exists");
        //when
        InputString inputString = new InputString("create_course|JAVA");

        when(dao.get("JAVA")).thenReturn(course);
        command.process(inputString);
    }

    @Test
    public void testProcessWithCorrectParameters() {
        //given
        InputString inputString = new InputString("create_course|JAVA");
        //when
        when(dao.get("JAVA")).thenReturn(null);
        command.process(inputString);

    }


    @Test
    public void testCanNotProcessWithEmptyTitle() {
        //given
        InputString inputString = new InputString("create_course| |");
        //when
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Course title can't be empty");
        when(dao.get("JAVA")).thenReturn(null);
        command.process(inputString);
    }
}