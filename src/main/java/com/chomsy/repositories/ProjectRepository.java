package com.chomsy.repositories;

import java.util.List;
import java.util.Optional;

import com.chomsy.models.Project;

public interface ProjectRepository {
    Optional<Project> getProject(String projectId);
    List<Project> getAllProjects();
    void saveProject(Project project);
}
