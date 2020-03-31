package com.courses.management.user;

public class UsersTest {
    public static User getTestUser() {
        User user = new User();
        user.setFirstName("Vlad");
        user.setLastName("Kharchenko");
        user.setEmail("vlad@email.com");
        user.setStatus(UserStatus.NOT_ACTIVE);
        user.setUserRole(UserRole.NEWCOMER);
        return user;
    }
}
