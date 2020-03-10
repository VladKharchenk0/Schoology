package com.courses.management.homework;

import java.util.List;

public interface HomeWorkDAO {
    List<Homework> getAll(int courseId);
}
