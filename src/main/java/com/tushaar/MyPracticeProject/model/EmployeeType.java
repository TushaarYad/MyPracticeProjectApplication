package com.tushaar.MyPracticeProject.model;

import jakarta.validation.constraints.NotNull;

public enum EmployeeType {
    @NotNull(message = "Type is required")
    FULL_TIME, PART_TIME
}
