package com.chomsy.views;

import com.chomsy.models.Project;
import com.chomsy.models.ProjectColumn;

import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.util.stream.Collectors;

public class KanbanBoard extends VBox {
    
    private Project project;
    
    public KanbanBoard(Project project) {
        super();
        this.project = project;
        setStyle("-fx-padding: 2em");
        getChildren().addAll(
            createTitle(this.project.getTitle()),
            createColumns(this.project.getColumns())
        );
    }

    private static Label createTitle(String title) {
        Label label = new Label(title);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 2em; -fx-padding: 1em 0 1em 0");
        return label;
    }

    private static HBox createColumns(List<ProjectColumn> columns) {
        HBox columnContainer = new HBox(20);
        columnContainer.getChildren().addAll(
            columns
                .stream()
                .map(KanbanColumn::new)
                .collect(Collectors.toList())
        );
        VBox.setVgrow(columnContainer, Priority.ALWAYS);
        return columnContainer;
    }
}
