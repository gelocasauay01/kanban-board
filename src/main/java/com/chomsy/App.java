package com.chomsy;

import com.chomsy.views.MainWindow;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        final String ICON_PATH = "images/icon.png";
        final String APP_NAME = "Kanban Board";
        final double WINDOW_WIDTH = 1080;
        final double WINDOW_HEIGHT = 720;
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream(ICON_PATH)));
        MainWindow mainWindow = new MainWindow();
        Scene scene = new Scene(mainWindow, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.getIcons().add(icon);
        stage.setTitle(APP_NAME);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}