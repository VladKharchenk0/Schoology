package com.courses.management.course;

import com.courses.management.common.DatabaseConnector;
import com.courses.management.common.exceptions.SQLCourseException;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CourseDAOImpl implements CourseDAO {
    private static final Logger LOGGER = LogManager.getLogger(CourseDAOImpl.class);
    private final static String INSERT = "INSERT INTO course(title, status) " +
            "VALUES(?, ?);";


    private final static String GET = "SELECT * FROM course WHERE id = ?;";

    private final static String FIND_COURSE_BY_TITLE = "SELECT c.id, c.title, c.status FROM course c " +

            " WHERE c.title = ?;";

    private HikariDataSource dataSource = DatabaseConnector.getConnector();

    @Override
    public void create(Course course) {
        LOGGER.debug(String.format("create: course.title=%s", course.getTitle()));
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            statement.setString(1, course.getTitle());
            statement.setString(2, course.getCourseStatus().getStatus());
            statement.execute();
        } catch (SQLException e) {
            LOGGER.debug(String.format("create: course.title=%s", course.getTitle()), e);
        }
    }

    @Override
    public void update(Course course) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Course get(int id) {
        ResultSet resultSet = null;
        Course course = new Course();

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(GET)) {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                course.setTitle(resultSet.getString("title"));

                String s = resultSet.getString("status");
                course.setCourseStatus(CourseStatus.valueOf(s));

            }
        } catch (SQLException e) {
        }

        return course;
    }

    @Override
    public List<Course> getAll() {
        return null;
    }

    @Override
    public Course get(String title) {
        LOGGER.debug(String.format("create: course.title=%s", title));
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_COURSE_BY_TITLE)) {
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            return mapCourse(resultSet);
        } catch (SQLException e) {
            LOGGER.debug(String.format("get: course.title=%s", title), e);
            throw new SQLCourseException("Error occurred when saving a course");
        }
    }

    private Course mapCourse(ResultSet rs) throws SQLException {
        String title = "";
        if (rs.next()) {
            Course course = new Course();
            course.setId(rs.getInt("c.id"));
            course.setTitle(rs.getString("c.title"));
            course.setCourseStatus(CourseStatus.getCourseStatus(rs.getString("c.status")).get());
            return course;
        }
        return null;
    }
}
