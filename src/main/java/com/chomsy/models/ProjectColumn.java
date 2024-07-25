package com.chomsy.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectColumn {
    private String title;
    private List<String> tasks;
    
    public void addTask(String task) {
        if(!tasks.contains(task)) {
            tasks.add(task);
        }
    }
}
