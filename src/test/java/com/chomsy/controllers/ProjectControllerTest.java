package com.chomsy.controllers;


import com.chomsy.models.Project;
import com.chomsy.models.ProjectColumn;
import com.chomsy.models.TransferDirection;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProjectControllerTest {
    public static final String EXISTING_TASK = "Create Prototype";
    public static final String LEFTMOST_TASK = "Implement Database";
    public static final String NON_EXISTING_TASK = "Implement Crying";
    final Project sampleProject = new Project(
        "Real Playing Game",
        List.of(
            new ProjectColumn("Backlog", new ArrayList<>(List.of(LEFTMOST_TASK, "Implement Habits", "Implement Pets"))),
            new ProjectColumn("In Progress", new ArrayList<>(List.of("Implement the alarm"))),
            new ProjectColumn("Completed", new ArrayList<>(List.of(EXISTING_TASK, "Create Unit Tescascascascascascascsacascascascsacascsaccsats")))
        )
    );

    @Test
    public void givenTask_whenTransferredToTheLeft_thenTaskShouldMoveColumn() {
        // Arrange
        ProjectColumn source = sampleProject.getColumns().get(2);
        ProjectColumn target = sampleProject.getColumns().get(1);
        ProjectController controller = ProjectController.getInstance();
        controller.setProject(sampleProject);

        // Act
        controller.transferTask(EXISTING_TASK, TransferDirection.LEFT);


        // Assert
        assertTrue(target.getTasks().contains(EXISTING_TASK));
        assertFalse(source.getTasks().contains(EXISTING_TASK));
    }

    @Test
    public void givenTask_whenTransferredToTheLeftAndGoingOutOfBounds_thenShouldDoNothing() {
        // Arrange
        ProjectColumn source = sampleProject.getColumns().get(0);
        ProjectController controller = ProjectController.getInstance();
        controller.setProject(sampleProject);

        // Act
        controller.transferTask(LEFTMOST_TASK, TransferDirection.LEFT);


        // Assert
        assertTrue(source.getTasks().contains(LEFTMOST_TASK));
    }

    @Test
    public void givenTask_whenTransferredToTheRight_thenTaskShouldMoveColumn() {
        // Arrange
        ProjectColumn source = sampleProject.getColumns().get(0);
        ProjectColumn target = sampleProject.getColumns().get(1);
        ProjectController controller = ProjectController.getInstance();
        controller.setProject(sampleProject);

        // Act
        controller.transferTask("Implement Habits", TransferDirection.RIGHT);

        // Assert
        assertTrue(target.getTasks().contains("Implement Habits"));
        assertFalse(source.getTasks().contains("Implement Habits"));
    }

    @Test
    public void givenTask_whenItDoesNotExistInTheProject_thenAddTask() {
        // Arrange
        ProjectColumn target = sampleProject.getColumns().get(0);
        ProjectController controller = ProjectController.getInstance();
        controller.setProject(sampleProject);

        // Act
        controller.addTask(NON_EXISTING_TASK, target);

        // Assert
        assertTrue(target.getTasks().contains(NON_EXISTING_TASK));
    }

    @Test
    public void givenTask_whenItExistsInTheProject_thenDoNothing() {
        // Arrange
        ProjectColumn target = sampleProject.getColumns().get(0);
        ProjectController controller = ProjectController.getInstance();
        controller.setProject(sampleProject);

        // Act
        controller.addTask(LEFTMOST_TASK, target);

        // Assert
        assertTrue(target.getTasks().contains(LEFTMOST_TASK));
    }
}
