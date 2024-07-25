package com.chomsy.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Project {
    private String title;
    private List<ProjectColumn> columns;
}
