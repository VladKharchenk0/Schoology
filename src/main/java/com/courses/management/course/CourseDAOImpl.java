package com.courses.management.course;

import com.courses.management.common.exceptions.SQLCourseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAOImpl implements CourseDAO {
    private static final Logger LOGGER = LogManager.getLogger(CourseDAOImpl.class);
    private DataSource dataSource;
    private final static String INSERT = "INSERT INTO course(title, status) " +
            "VALUES(?, ?);";
    private final static String FIND_COURSE_BY_TITLE =
            "SELECT id, title, status FROM course " +
                    "WHERE title = ?;";
    private final static String UPDATE_COURSE = "UPDATE course SET " +
            "title=?, status=? WHERE id=?";
    private static final String FIND_COURSE_BY_ID = "SELECT id, title, status FROM course " +
            "WHERE id = ?;";
    private static final String FIND_ALL_COURSES = "SELECT id, title, status FROM course";


    public CourseDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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
        LOGGER.debug(String.format("update: course.title=%s, course.status=%s", course.getTitle(),
                course.getCourseStatus()));
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_COURSE)) {
            statement.setString(1, course.getTitle());
            statement.setString(2, course.getCourseStatus().getStatus());
            statement.setInt(3, course.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(String.format("update: course.title=%s", course.getTitle()), e);
            throw new SQLCourseException("Error occurred when update a course");
        }
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Course can't be deleted");
    }

    @Override
    public Course get(int id) {
        LOGGER.debug(String.format("get: course.id=%s", id));
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_COURSE_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return getCourse(resultSet);
        } catch (SQLException e) {
            LOGGER.error(String.format("get: course.id=%s", id), e);
            throw new SQLCourseException("Error occurred when find a course");
        }
    }

    @Override
    public List<Course> getAll() {
        LOGGER.debug("getAll");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_COURSES)) {
            ResultSet resultSet = statement.executeQuery();
            return getCourseList(resultSet);
        } catch (SQLException e) {
            LOGGER.error("getAll", e);
            throw new SQLCourseException("Error occurred when get all courses");
        }
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
            course.setId(rs.getInt("id"));
            course.setTitle(rs.getString("title"));
            course.setCourseStatus(CourseStatus.getCourseStatus(rs.getString("status")).get());
            return course;
        }
        return null;
    }

    private Course getCourse(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return mapCourseFromRS(rs);
        }
        return null;
    }

    private Course mapCourseFromRS(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setId(rs.getInt("id"));
        course.setTitle(rs.getString("title"));
        course.setCourseStatus(CourseStatus.getCourseStatus(rs.getString("status")).get());
        return course;
    }

    private List<Course> getCourseList(ResultSet rs) throws SQLException {
        List<Course> courses = new ArrayList<>();
        while (rs.next()) {
            courses.add(mapCourseFromRS(rs));
        }
        return courses;
    }
}
