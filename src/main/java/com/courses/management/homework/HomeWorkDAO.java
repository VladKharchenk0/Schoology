package com.courses.management.homework;

import com.courses.management.common.DataAccessObject;

import java.util.List;

public interface HomeWorkDAO extends DataAccessObject<Homework> {
    List<Homework> getAll(int courseId);
}
