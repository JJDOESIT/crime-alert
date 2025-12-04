package com.crimealert.CrimeAlert;

import java.util.ArrayList;
import java.util.Optional;

import com.crimealert.CrimeAlert.Model.CrimeModel;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Entry extends Application {

    private ApiClient client;
    //private TextArea output;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            client = new ApiClient();
            new Thread(() -> {
                try {
                    client.launchAPI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            // Note: This 'output' variable shadows the class field 'output', but we'll leave it as is per request.
            TextArea output = new TextArea();
            output.setEditable(false);

            // --- Report Crime Button ---
            Button reportCrimeButton = new Button("Report Crime");
            reportCrimeButton.setOnAction(event -> {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Report a Crime");
                dialog.setHeaderText("Enter Crime Details");
                dialog.setContentText("Description of the Crime");
                
                Optional<String> result = dialog.showAndWait();

                result.ifPresent(description -> {
                    new Thread(() -> {
                        try {
                            // call api to post
                            client.postCrime(description);

                            // success
                            Platform.runLater(() -> {
                                Alert alert = new Alert(AlertType.INFORMATION);
                                alert.setTitle("Success");
                                alert.setHeaderText(null);
                                alert.setContentText("Crime reported successfully: " + description);
                                alert.showAndWait();
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                            Platform.runLater(() -> {
                                Alert errorAlert = new Alert(AlertType.ERROR);
                                errorAlert.setTitle("Error");
                                errorAlert.setHeaderText("Failed to Report Crime");
                                errorAlert.setContentText("Could not connect to the server or database.");
                                errorAlert.showAndWait();
                            });
                        }
                    }).start();
                });
            });

            // --- Load Crimes Button (Uses TextInputDialog for Radius) ---
            Button loadCrimesButton = new Button("Load Crimes");

            loadCrimesButton.setOnAction(event -> {
                // 1. Prompt the user for the desired radius
                TextInputDialog dialog = new TextInputDialog("10"); // Default value of 10
                dialog.setTitle("Load Crimes");
                dialog.setHeaderText("Specify Search Radius");
                dialog.setContentText("Enter radius in miles (e.g., 10):");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(radiusString -> {
                    try {
                        final int selectedRadius = Integer.parseInt(radiusString.trim());
                        new Thread(() -> {
                            try {
                                // fetch with user radius
                                ArrayList<CrimeModel> crimes = client.getCrimes(selectedRadius);
                                StringBuilder sb = new StringBuilder();

                                // no crimes in radius
                                if (crimes.isEmpty()) {
                                    sb.append(String.format("No crimes found in your area (%d-mile radius).\n", selectedRadius));
                                } else {
                                    for (CrimeModel crime : crimes) {
                                        sb.append(String.format("Crime at %s, located at %s, description: %s\n",
                                                    crime.timestamp.toString(),
                                                    crime.getAddressString(),
                                                    crime.description));
                                    }
                                }

                                Platform.runLater(() -> output.setText(sb.toString()));

                            } catch (Exception e) {
                                e.printStackTrace();
                                Platform.runLater(() -> output.setText("ERROR: Could not fetch crimes from the server."));
                            }
                        }).start();
                    } catch (NumberFormatException e) {
                       // imput validation
                        Platform.runLater(() -> {
                            Alert errorAlert = new Alert(AlertType.ERROR);
                            errorAlert.setTitle("Input Error");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("Please enter a valid number for the radius.");
                            errorAlert.showAndWait();
                        });
                    }
                });
            });

            // --- UI Setup ---
            // Layout (VBox now contains both buttons)
            VBox root = new VBox(10, reportCrimeButton, loadCrimesButton, output);
            Scene scene = new Scene(root, 600, 400);

            primaryStage.setTitle("CrimeAlert");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}