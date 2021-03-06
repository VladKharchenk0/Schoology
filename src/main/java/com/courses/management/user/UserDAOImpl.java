package com.courses.management.user;

import com.courses.management.common.exceptions.SQLUserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDAOImpl implements UserDAO {

    private final static Logger LOGGER = LogManager.getLogger(UserDAOImpl.class);
    private static final String FIND_USER_BY_ID = "SELECT id, first_name, last_name, email, user_role, status " +
            "FROM users WHERE id=?;";
    private static final String FIND_USER_BY_EMAIL= "SELECT id, first_name, last_name, email, user_role, status " +
            "FROM users WHERE email=?;";
    private static final String FIND_ALL_USERS = "SELECT id, first_name, last_name, email, user_role, status " +
            "FROM users;";
    private static final String UPDATE_REMOVE_COURSE_AND_SET_STATUS = "UPDATE users SET course_id = null, status = ? " +
            "WHERE email = ?";
    private DataSource dataSource;
    private final static String INSERT = "INSERT INTO users(first_name, last_name, email, user_role, status) " +
            "VALUES(?, ?, ?, ?, ?);";
    private final static String UPDATE = "UPDATE users SET first_name=?, last_name=?, email=?, user_role=?," +
            " status=?, course_id=? WHERE id=?;";
    private static final String DELETE = "DELETE FROM users WHERE id=?;";
    private static final String FIND_USERS_BY_COURSE_TITLE = "SELECT u.id, u.first_name, u.last_name, u.email, u.user_role, u.status " +
            "FROM users u " +
            "INNER JOIN course c ON c.id=u.course_id " +
            "WHERE c.title=?;";
    private static final String FIND_ALL_USERS_BY_STATUS = "SELECT id, first_name, last_name, email, user_role, status " +
            "FROM users WHERE status=?;";

    public UserDAOImpl(DataSource ds) {
        this.dataSource = ds;
    }

    @Override
    public void create(User user) {
        LOGGER.debug(String.format("create: user.first_name=%s " +
                "user.last_name=%s" +
                "user.email=%s", user.getFirstName(), user.getLastName(), user.getEmail()));
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getUserRole().name());
            statement.setString(5, user.getStatus().name());
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error(String.format("create: user.email=%s", user.getEmail()), e);
            throw new SQLUserException("Error occurred when creating user");
        }
    }

    @Override
    public void update(User user) {
        LOGGER.debug(String.format("update: user.first_name=%s " +
                "user.last_name=%s" +
                "user.email=%s", user.getFirstName(), user.getLastName(), user.getEmail()));
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getUserRole().name());
            statement.setString(5, user.getStatus().name());

            if (Objects.isNull(user.getCourse())) {
                statement.setNull(6, Types.NULL);
            } else {
                statement.setInt(6, user.getCourse().getId());
            }

            statement.setInt(7, user.getId());
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error(String.format("update: user.email=%s", user.getEmail()), e);
            throw new SQLUserException("Error occurred when updating user");
        }
    }

    @Override
    public void delete(int id) {
        LOGGER.debug(String.format("delete: user.id=%s ", id));
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error(String.format("delete: user.id=%s", id), e);
            throw new SQLUserException("Error occurred when removing user");
        }
    }

    @Override
    public User get(int id) {
        LOGGER.debug(String.format("get: user.id=%s ", id));
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_ID)) {
            statement.setInt(1, id);
            return getUser(statement.executeQuery());
        } catch (SQLException e) {
            LOGGER.error(String.format("get: user.id=%s", id), e);
            throw new SQLUserException("Error occurred when retrieving user");
        }
    }

    private User getUser(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return mapUserFromRS(rs);
        }
        return null;
    }

    private User mapUserFromRS(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        user.setUserRole(UserRole.getUserRole(rs.getString("user_role")).get());
        user.setStatus(UserStatus.getUserStatus(rs.getString("status")).get());
        return user;
    }

    @Override
    public List<User> getAll() {
        LOGGER.debug("getAll: ");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_USERS)) {
            return getUserList(statement.executeQuery());
        } catch (SQLException e) {
            LOGGER.error("getAll: ", e);
            throw new SQLUserException("Error occurred when retrieving all user");
        }
    }

    private List<User> getUserList(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            users.add(mapUserFromRS(rs));
        }
        return users;
    }

    @Override
    public User get(String email) {
        LOGGER.debug(String.format("get: user.email=%s ", email));
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_EMAIL)) {
            statement.setString(1, email);
            return getUser(statement.executeQuery());
        } catch (SQLException e) {
            LOGGER.error(String.format("get: user.email=%s", email), e);
            throw new SQLUserException("Error occurred when retrieving user");
        }
    }

    @Override
    public List<User> getUsersByCourse(String courseTitle) {
        LOGGER.debug(String.format("getUsersByCourse: course.title=%s", courseTitle));
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_USERS_BY_COURSE_TITLE)) {
            statement.setString(1, courseTitle);
            return getUserList(statement.executeQuery());
        } catch (SQLException e) {
            LOGGER.error(String.format("getUsersByCourse: course.title=%s", courseTitle), e);
            throw new SQLUserException("Error occurred when retrieving users by course title");
        }
    }

    @Override
    public List<User> getAllByStatus(UserStatus userStatus) {
        LOGGER.debug(String.format("getAllByStatus: user.status=%s", userStatus.name()));
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_USERS_BY_STATUS)) {
            statement.setString(1, userStatus.name());
            return getUserList(statement.executeQuery());
        } catch (SQLException e) {
            LOGGER.error(String.format("getAllByStatus: user.status=%s", userStatus.name()), e);
            throw new SQLUserException("Error occurred when retrieving users by status");
        }
    }
}
