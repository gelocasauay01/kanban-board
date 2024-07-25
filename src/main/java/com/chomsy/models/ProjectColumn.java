package com.chomsy.models;

import java.util.ArrayList;
import java.util.List;

import com.chomsy.interfaces.Observable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class ProjectColumn extends Observable<ProjectColumn>{
    private String title;
    private List<String> tasks;

    public ProjectColumn(String title, List<String> tasks) {
        this.title = title;
        this.tasks = tasks;
    }

    public ProjectColumn(String title) {
        this.title = title;
        this.tasks = new ArrayList<>();
    }

    public void addTask(String task) {
        this.tasks.add(task);
        notifyListeners(this);
    }

    public void removeTask(String task) {
        this.tasks.remove(task);
        notifyListeners(this);
    }
}
