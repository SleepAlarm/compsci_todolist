package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ToDoListController {

    @FXML
    private ListView<String> taskListView;

    @FXML
    private TextField taskTextField;

    @FXML
    private Button addTaskButton;

    @FXML
    private Button deleteTaskButton;

    private String currentUsername; 

    public void initialize(String username) {
        this.currentUsername = username;
        loadTasksFromDatabase();
        taskListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                deleteSelectedTask();
            }
        });
    }

    @FXML
    public void addTask(ActionEvent event) {
        String newTask = taskTextField.getText().trim();

        if (!newTask.isEmpty()) {
            taskListView.getItems().add(newTask);
            try {
                DBUtilitaires.dbExecuteQuery("INSERT INTO tasks (username, task) VALUES ('" + currentUsername + "', '" + newTask + "')");
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            taskTextField.clear();
        }
    }

    @FXML
    public void deleteTask(ActionEvent event) {
        deleteSelectedTask();
    }

    private void deleteSelectedTask() {
        int selectedIndex = taskListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            String selectedTask = taskListView.getItems().get(selectedIndex);

            taskListView.getItems().remove(selectedIndex);

            try {
                DBUtilitaires.dbExecuteQuery("DELETE FROM tasks WHERE username = '" + currentUsername + "' AND task = '" + selectedTask + "'");
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadTasksFromDatabase() {
        try {
            String query = "SELECT task FROM tasks WHERE username = '" + currentUsername + "'";
            ResultSet resultSet = DBUtilitaires.dbExecute(query);
            while (resultSet.next()) {
                taskListView.getItems().add(resultSet.getString("task"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
