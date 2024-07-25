package com.chomsy.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.chomsy.models.Project;
import com.chomsy.models.ProjectColumn;

public class YamlProjectRepositoryTest {

    private static final String TEST_SAVE_DIRECTORY = "src/test/resources/test_project_dir";
    
    @Test
    public void givenProject_whenProjectIsSavedFirstTime_thenCreateANewYamlFile() {
        // Arrange
        String saveDir = "src/test/resources/projects";
        File file = new File(saveDir + "/Real Playing Game.yaml");
        YamlProjectRepository repository = new YamlProjectRepository(saveDir);
        final Project sampleProject = new Project(
        "Real Playing Game",
            List.of(
                new ProjectColumn("Backlog", new ArrayList<>(List.of("Fuck off", "Implement Habits", "Implement Pets"))),
                new ProjectColumn("In Progress", new ArrayList<>(List.of("Implement the alarm"))),
                new ProjectColumn("Completed", new ArrayList<>(List.of("Dumb shit", "Create Unit Tescascascascascascascsacascascascsacascsaccsats")))
            )
        );
        final String expected = "title: Real Playing Gamecolumns:- title: Backlog  tasks: [Fuck off, Implement Habits, Implement Pets]- title: In Progress  tasks: [Implement the alarm]- title: Completed  tasks: [Dumb shit, Create Unit Tescascascascascascascsacascascascsacascsaccsats]";

        // Act
        repository.saveProject(sampleProject);

        // Assert
        assertTrue(file.exists());
        assertEquals(expected, readFile(file));

        // Cleanup
        File saveFolder = new File(saveDir);
        deleteDirectory(saveFolder);
    }

    @Test
    public void givenFilePath_whenFileExists_thenReturnProject() {
        // Arrange
        YamlProjectRepository repository = new YamlProjectRepository(TEST_SAVE_DIRECTORY);
        final Project expected = new Project(
        "Real Playing Game",
            List.of(
                new ProjectColumn("Backlog", new ArrayList<>(List.of("Fuck off", "Implement Habits", "Implement Pets"))),
                new ProjectColumn("In Progress", new ArrayList<>(List.of("Implement the alarm"))),
                new ProjectColumn("Completed", new ArrayList<>(List.of("Dumb shit", "Create Unit Tescascascascascascascsacascascascsacascsaccsats")))
            )
        );
        final String filePath = TEST_SAVE_DIRECTORY + "/Real Playing Game.yaml";

        // Act
        Optional<Project> actual = repository.getProject(filePath);

        // Assert
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    public void givenSaveDirectory_whenThereAreValidProjectFiles_thenReturnAllProjects() {
        // Arrange
        YamlProjectRepository repository = new YamlProjectRepository(TEST_SAVE_DIRECTORY);
        final int PROJECT_COUNT = 3;
        final List<String> expectedTitles = List.of("Real Playing Game", "Test Project Alpha", "Test Project");

        // Act
        List<Project> actual = repository.getAllProjects();
        List<String> actualTitles = actual.stream()
            .map(project -> project.getTitle())
            .collect(Collectors.toList());

        // Assert
        assertEquals(PROJECT_COUNT, actual.size());
        assertEquals(expectedTitles, actualTitles);
    }

    public static void deleteDirectory(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File subfile : files) {
                    deleteDirectory(subfile);
                }
            }
        }
        file.delete();
    }

    private String readFile(File file) {
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            return String.join("", lines);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error Occurred";
        }
    }
}
