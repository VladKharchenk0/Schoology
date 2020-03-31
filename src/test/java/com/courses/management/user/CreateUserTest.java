package com.courses.management.user;

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

public class CreateUserTest {
    private Command command;
    private UserDAO dao;
    private View view;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup(){
        this.view = mock(View.class);
        this.dao = mock(UserDAO.class);
        this.command = new CreateUser(view, dao);
    }

    @Test
    public void testCanProcessWithCorrectCommand() {
        //given
        InputString inputString = new InputString("create_user|Vlad|Kharchenko|vlad@email.com");
        //when
        boolean result = command.canProcess(inputString);
        //then
        assertTrue(result);
    }

    @Test
    public void testCanProcessWithIncorrectCommand() {
        //given
        InputString inputString = new InputString("create_userS|Vlad|Kharchenko|vlad@email.com");
        //when
        boolean result = command.canProcess(inputString);
        //then
        assertFalse(result);
    }

    @Test
    public void testProcessWithIncorrectEmail() {
        //given
        InputString inputString = new InputString("create_user|Vlad|Kharchenko|vlad@email");
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Wrong email address vlad@email");
        //when
        command.process(inputString);
    }
    @Test
    public void testProcessWithDuplicateEmail() {
        //given
        InputString inputString = new InputString("create_user|Vlad|Kharchenko|vlad@email.com");
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("User with email vlad@email.com already exists");
        //when
        when(dao.get("vlad@email.com")).thenReturn(new User());
        command.process(inputString);
    }

    @Test
    public void testProcessWithCorrectData() {
        //given
        InputString inputString = new InputString("create_user|Vlad|Kharchenko|vlad@email.com");
        User user =  new User();
        //when
        when(dao.get("vlad@email.com")).thenReturn(null);
        command.process(inputString);
        //then
    }

    @Test
    public void testProcessEmptyFirstName() {
        //given
        InputString inputString = new InputString("create_user||Kharchenko|vlad@email.com");
        User user = UsersTest.getTestUser();
        user.setFirstName(null);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("User first name can't be empty");
        //when
        command.process(inputString);
    }

    @Test
    public void testProcessEmptyLastName() {
        //given
        InputString inputString = new InputString("create_user|Vlad||vlad@email.com");
        User user = UsersTest.getTestUser();
        user.setLastName(null);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("User last name can't be empty");
        //when
        command.process(inputString);
    }

    @Test
    public void testProcessEmptyEmail() {
        //given
        InputString inputString = new InputString("create_user|Vlad|Kharchenko| |");
        User user = UsersTest.getTestUser();
        user.setEmail(null);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Wrong email address  ");
        //when
        command.process(inputString);
    }


}
