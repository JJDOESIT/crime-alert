package com.crimealert.CrimeAlert;

import java.util.ArrayList;

import com.crimealert.CrimeAlert.Model.CrimeModel;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Entry extends Application {

    private ApiClient client;

    public static void main(String[] args) {
        // Launch JavaFX application
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize API client and launch backend in a separate thread
            client = new ApiClient();
            new Thread(() -> {
                try {
                    client.launchAPI(); // your backend start
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            // UI elements
            TextArea output = new TextArea();
            output.setEditable(false);

            Button loadCrimesButton = new Button("Load Crimes");
            loadCrimesButton.setOnAction(event -> {
                new Thread(() -> {  // run network requests in background
                    try {
                        client.postCrime("robbery");
                        client.postCrime("theft");
                        client.postCrime("jaywalking");

                        ArrayList<CrimeModel> crimes = client.getCrimes(10);
                        StringBuilder sb = new StringBuilder();
                        for (CrimeModel crime : crimes) {
                            sb.append(String.format("Crime at %s, located at %s, description: %s\n",
                                    crime.timestamp.toString(),
                                    crime.getAddressString(),
                                    crime.description));
                        }

                        // Update UI on JavaFX thread
                        javafx.application.Platform.runLater(() -> output.setText(sb.toString()));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            });

            VBox root = new VBox(10, loadCrimesButton, output);
            Scene scene = new Scene(root, 600, 400);

            primaryStage.setTitle("CrimeAlert");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
