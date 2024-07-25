package com.chomsy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.chomsy.models.Project;
import com.chomsy.models.ProjectColumn;
import com.chomsy.views.KanbanBoard;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Project project = new Project(
            "Real Playing Game",
            List.of(
                new ProjectColumn("Backlog", List.of("Implement Database", "Implement Habits", "Implement Pets")),
                new ProjectColumn("In Progress", List.of("Implement the alarm")),
                new ProjectColumn("Completed", List.of("Create Prototype", "Create Unit Tescascascascascascascsacascascascsacascsaccsats"))
            )
         );
        KanbanBoard kanbanBoard = new KanbanBoard(project);
        final String ICON_PATH = "images/icon.png";
        Image icon = new Image(getClass().getResourceAsStream(ICON_PATH));

        VBox.setVgrow(kanbanBoard, Priority.ALWAYS);
        scene = new Scene(new VBox(createMenuBar(), kanbanBoard), 1080, 720);
        stage.getIcons().add(icon);
        stage.setTitle("Kanban Board");
        stage.setScene(scene);
        stage.show();
    }

    private static MenuBar createMenuBar() {
        Menu menu = new Menu("File");

        MenuItem open = new MenuItem("Open");
        open.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(null);
            System.out.println(file);
        });

        MenuItem newItem = new MenuItem("New");
        newItem.setOnAction(e -> {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setHeaderText("New Project Title");
            inputDialog.showAndWait();
        });

        menu.getItems().addAll(open, newItem);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);
        return menuBar;
    }

    public static void main(String[] args) {
        launch(args);
    }

}