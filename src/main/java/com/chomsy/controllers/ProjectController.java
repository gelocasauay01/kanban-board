package com.chomsy.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.chomsy.interfaces.Observable;
import com.chomsy.models.Project;
import com.chomsy.models.ProjectColumn;
import com.chomsy.models.TransferDirection;
import com.chomsy.repositories.ProjectRepository;
import com.chomsy.repositories.YamlProjectRepository;

import lombok.Getter;
import lombok.Setter;

public class ProjectController extends Observable<ProjectController>{
    private static ProjectController instance;

    @Getter
    @Setter
    private Project project;
    
    private final ProjectRepository repository;

    private ProjectController() {
        final String SAVE_DIRECTORY_PATH = "projects";
        repository = new YamlProjectRepository(SAVE_DIRECTORY_PATH);
    }

    public static ProjectController getInstance() {
        if(instance == null) {
            instance = new ProjectController();
        }
        return instance;
    }

    public void saveProject() {
        repository.saveProject(project);
    }

    public void setProjectFromFilePath(String filePath) {
        Optional<Project> found = repository.getProject(filePath);
        if(found.isPresent()) {
            this.project = found.get();
        } 
        notifyListeners(this);
    }

    public void createNewProject(String title) {
        project = new Project(title, createDefaultColumns());
        notifyListeners(this);
    }

    private List<ProjectColumn> createDefaultColumns() {
        return List.of("Backlog", "In Progress", "Completed")
            .stream()
            .map(ProjectColumn::new)
            .collect(Collectors.toList());
    }

    public void addColumn(String title) {
        project.getColumns().add(new ProjectColumn(title, new ArrayList<>()));
        notifyListeners(this);
    }

    public void transferTask(String task, TransferDirection direction) {
        int columnIndex = findTaskColumnIndex(task);
        if(columnIndex == -1 || !canMove(columnIndex, direction)) {
            return;
        }

        moveTaskToDirection(task, direction, columnIndex);
        removeTaskFromColumn(task, columnIndex);
    }

    private boolean canMove(int columnIndex, TransferDirection direction) {
        return (direction == TransferDirection.LEFT && columnIndex - 1 >= 0) || (direction == TransferDirection.RIGHT && columnIndex + 1 < project.getColumns().size());
    }

    public void addTask(String task, ProjectColumn column) {
        int columnIndex = findTaskColumnIndex(task);
        if(columnIndex == -1) {
            column.addTask(task);
        }
    }

    private void moveTaskToDirection(String task, TransferDirection direction, int columnIndex) {
        if (direction == TransferDirection.LEFT) {
            ProjectColumn targetColumn = project.getColumns().get(columnIndex - 1);
            targetColumn.addTask(task);
        } else if (direction == TransferDirection.RIGHT) {
            ProjectColumn targetColumn = project.getColumns().get(columnIndex + 1);
            targetColumn.addTask(task);
        }
    }

    private int findTaskColumnIndex(String task) {
        int foundIndex = -1;
        for(int index = 0; index < project.getColumns().size(); index++) {
            ProjectColumn column = project.getColumns().get(index);
            boolean isTaskExist = column.getTasks().contains(task);
            if(isTaskExist) {
                foundIndex = index;
                break;
            }
        }
        return foundIndex;
    }

    private void removeTaskFromColumn(String task, int columnIndex) {
        ProjectColumn sourceColumn = project.getColumns().get(columnIndex);
        sourceColumn.removeTask(task);
    }
}
