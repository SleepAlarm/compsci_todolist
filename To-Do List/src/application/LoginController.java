package application;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private PasswordField password_textfield;

    @FXML
    private Button signin;

    @FXML
    private Button signup;

    @FXML
    private TextField username_textfield;

    @FXML
    private Label statusLabel;
    
//save

    @FXML
    public void handleSignInButtonAction(ActionEvent event) {
        String username = username_textfield.getText();
        String password = password_textfield.getText();

        try {
            String query = "SELECT * FROM todolist WHERE username = '" + username + "' AND password = '" + password + "'";
            ResultSet resultSet = DBUtilitaires.dbExecute(query);

            if (resultSet.next()) {
                openToDoListPage();
            } else {
                statusLabel.setText("Invalid username or password.");
            }
        } catch (SQLException e) {
            statusLabel.setText("Database error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            statusLabel.setText("Class not found error: " + e.getMessage());
        }
    }

    private void openToDoListPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ToDoList.fxml"));
            Parent root = loader.load();

            ToDoListController toDoListController = loader.getController();

            toDoListController.initialize(username_textfield.getText());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("To-Do List");
            stage.show();
            Stage loginStage = (Stage) signin.getScene().getWindow();
            loginStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handleSignUpButtonAction(ActionEvent event) {
        String username = username_textfield.getText();
        String password = password_textfield.getText();

        try {
            String checkQuery = "SELECT * FROM todolist WHERE username = '" + username + "'";
            System.out.println(checkQuery);
            ResultSet checkResult = DBUtilitaires.dbExecute(checkQuery);

            if (checkResult.next()) {
                statusLabel.setText("Username already exists. Please choose another.");
                
            } else {
                String insertQuery = "INSERT INTO todolist (username, password) VALUES ('" + username + "', '" + password + "')";
                DBUtilitaires.dbExecuteQuery(insertQuery);

                statusLabel.setText("Sign up successful! You can now sign in.");
            }
        } catch (SQLException e) {
            statusLabel.setText("Database error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            statusLabel.setText("Class not found error: " + e.getMessage());
        }
    }
}
