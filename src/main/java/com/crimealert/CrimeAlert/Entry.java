package com.crimealert.CrimeAlert;

import java.util.ArrayList;
import java.util.Optional;

import com.crimealert.CrimeAlert.Model.CrimeModel;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

        
            TextArea output = new TextArea();
            output.setEditable(false);
            output.setPrefHeight(250);
            output.setStyle("-fx-font-size: 14px; -fx-control-inner-background: #f9f9f9;");

            //buttons and format
            Button reportCrimeButton = new Button("Report Crime");
            Button loadCrimesButton = new Button("Load Crimes");

            String buttonStyle = "-fx-font-size: 15px; -fx-padding: 10 20; "
                               + "-fx-background-color: #2b5797; -fx-text-fill: white; "
                               + "-fx-background-radius: 8;";

            reportCrimeButton.setStyle(buttonStyle);
            loadCrimesButton.setStyle(buttonStyle);
            reportCrimeButton.setMaxWidth(Double.MAX_VALUE);
            loadCrimesButton.setMaxWidth(Double.MAX_VALUE);

            //report
            reportCrimeButton.setOnAction(event -> {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Report a Crime");
                dialog.setHeaderText("Enter Crime Details");
                dialog.setContentText("Description of the Crime:");

                Optional<String> result = dialog.showAndWait();

                result.ifPresent(description -> {
                    new Thread(() -> {
                        try {
                            client.postCrime(description);

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

            //load crimes
            loadCrimesButton.setOnAction(event -> {
                TextInputDialog dialog = new TextInputDialog("10");
                dialog.setTitle("Load Crimes");
                dialog.setHeaderText("Specify Search Radius");
                dialog.setContentText("Enter radius in miles (e.g., 10):");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(radiusString -> {
                    try {
                        final int selectedRadius = Integer.parseInt(radiusString.trim());
                        new Thread(() -> {
                            try {
                                ArrayList<CrimeModel> crimes = client.getCrimes(selectedRadius);
                                StringBuilder sb = new StringBuilder();

                                if (crimes.isEmpty()) {
                                    sb.append(String.format("No crimes found in your area (%d-mile radius).\n", selectedRadius));
                                } else {
                                    for (CrimeModel crime : crimes) {
                                        sb.append(String.format(
                                            "Crime at %s, located at %s, description: %s\n",
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
                        Alert errorAlert = new Alert(AlertType.ERROR);
                        errorAlert.setTitle("Input Error");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Please enter a valid number for the radius.");
                        errorAlert.showAndWait();
                    }
                });
            });

            //layout
            VBox root = new VBox(15, reportCrimeButton, loadCrimesButton, output);
            root.setPadding(new Insets(20));
            root.setAlignment(Pos.TOP_CENTER);
            root.setStyle("-fx-background-color: #ececec;");

            Scene scene = new Scene(root, 600, 430);

            primaryStage.setTitle("CrimeAlert");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
