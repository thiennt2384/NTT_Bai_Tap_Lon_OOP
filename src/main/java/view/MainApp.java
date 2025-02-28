package view;

import controller.TaskController;
import controller.UserController;
import controller.SubjectController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Task;
import model.User;
import model.Subject;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javafx.scene.text.Font;

public class MainApp extends Application {
    private TaskController taskController = new TaskController();
    private UserController userController = new UserController();
    private SubjectController subjectController = new SubjectController();
    private TableView<Task> taskTable = new TableView<>();
    private User currentUser;
    private ObservableList<Task> tasks;

    @Override
    public void start(Stage primaryStage) {
        Stage loginStage = new Stage();
        VBox loginPane = new VBox(10);
        loginPane.setPadding(new Insets(20));
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Đăng Nhập");
        loginButton.setPrefSize(150, 40);
        loginButton.setFont(Font.font("Arial", 14));
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10; -fx-background-radius: 10;");

        Button registerButton = new Button("Đăng Ký");
        registerButton.setPrefSize(150, 40);
        registerButton.setFont(Font.font("Arial", 14));
        registerButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 10; -fx-background-radius: 10;");

        loginPane.getChildren().addAll(usernameField, passwordField, loginButton, registerButton);

        loginButton.setOnAction(e -> {
            currentUser = userController.dangNhap(usernameField.getText(), passwordField.getText());
            if (currentUser != null) {
                loginStage.close();
                showMainWindow(primaryStage);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Sai tên đăng nhập hoặc mật khẩu!");
                alert.show();
            }
        });

        registerButton.setOnAction(e -> {
            showRegisterWindow(loginStage);
        });

        Scene loginScene = new Scene(loginPane, 300, 250);
        loginStage.setScene(loginScene);
        loginStage.setTitle("Đăng Nhập");
        loginStage.show();
    }

    private void showRegisterWindow(Stage loginStage) {
        Stage registerStage = new Stage();
        VBox registerPane = new VBox(10);
        registerPane.setPadding(new Insets(20));
        TextField regUsernameField = new TextField();
        regUsernameField.setPromptText("Username");
        PasswordField regPasswordField = new PasswordField();
        regPasswordField.setPromptText("Password");
        TextField regEmailField = new TextField();
        regEmailField.setPromptText("Email");

        Button submitButton = new Button("Xác Nhận Đăng Ký");
        submitButton.setPrefSize(150, 40);
        submitButton.setFont(Font.font("Arial", 14));
        submitButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-padding: 10; -fx-background-radius: 10;");

        registerPane.getChildren().addAll(regUsernameField, regPasswordField, regEmailField, submitButton);

        submitButton.setOnAction(e -> {
            String username = regUsernameField.getText();
            String password = regPasswordField.getText();
            String email = regEmailField.getText();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ thông tin!");
                alert.show();
            } else {
                if (userController.isUsernameExists(username)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Tên đăng nhập '" + username + "' đã tồn tại! Vui lòng chọn tên khác.");
                    alert.show();
                } else {
                    userController.dangKy(username, password, email);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Đăng ký thành công! Hãy đăng nhập.");
                    alert.showAndWait();
                    registerStage.close();
                }
            }
        });

        Scene registerScene = new Scene(registerPane, 300, 250);
        registerStage.setScene(registerScene);
        registerStage.setTitle("Đăng Ký Tài Khoản");
        registerStage.show();
    }

    private void showMainWindow(Stage primaryStage) {
        // Tạo bảng công việc
        TableColumn<Task, Integer> sttCol = new TableColumn<>("STT");
        sttCol.setCellValueFactory(cellData -> {
            int rowIndex = taskTable.getItems().indexOf(cellData.getValue()) + 1;
            return new javafx.beans.property.SimpleIntegerProperty(rowIndex).asObject();
        });
        sttCol.setPrefWidth(50);
        sttCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Task, String> nameCol = new TableColumn<>("Tên Công Việc");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        nameCol.setPrefWidth(150);

        TableColumn<Task, String> descCol = new TableColumn<>("Mô Tả");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setPrefWidth(200);

        TableColumn<Task, LocalDate> deadlineCol = new TableColumn<>("Hạn Chót");
        deadlineCol.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        deadlineCol.setPrefWidth(100);

        TableColumn<Task, String> priorityCol = new TableColumn<>("Ưu Tiên");
        priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
        priorityCol.setPrefWidth(100);

        TableColumn<Task, String> statusCol = new TableColumn<>("Trạng Thái");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        TableColumn<Task, String> subjectCol = new TableColumn<>("Loại Môn Học");
        subjectCol.setCellValueFactory(cellData -> {
            Task task = cellData.getValue();
            for (Subject subject : subjectController.getAllSubjects()) {
                if (subject.getSubjectId() == task.getSubjectId()) {
                    return new javafx.beans.property.SimpleStringProperty(subject.getSubjectName());
                }
            }
            return new javafx.beans.property.SimpleStringProperty("Không xác định");
        });
        subjectCol.setPrefWidth(150);

        TableColumn<Task, String> deadlineStatusCol = new TableColumn<>("Cách hạn chót");
        deadlineStatusCol.setCellValueFactory(cellData -> {
            Task task = cellData.getValue();
            String status = task.getStatus() != null ? task.getStatus() : "Chua lam";
            if (status.equals("Đa hoan thanh")) {
                return new javafx.beans.property.SimpleStringProperty("Xong");
            } else if (task.getDeadline() == null) {
                return new javafx.beans.property.SimpleStringProperty("Không xác định");
            } else {
                long daysDifference = ChronoUnit.DAYS.between(LocalDate.now(), task.getDeadline());
                if (daysDifference >= 0) {
                    return new javafx.beans.property.SimpleStringProperty("Đến hạn: " + daysDifference + " ngày");
                } else {
                    return new javafx.beans.property.SimpleStringProperty("Quá hạn: " + Math.abs(daysDifference) + " ngày");
                }
            }
        });
        deadlineStatusCol.setPrefWidth(150);
        deadlineStatusCol.setCellFactory(column -> new TableCell<Task, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setFont(Font.font("Arial", 14));
                    if (item.equals("Xong")) {
                        setStyle("-fx-text-fill: #00CC00;"); // Xanh lá đậm cho "Xong"
                    } else if (item.startsWith("Đến hạn")) {
                        setStyle("-fx-text-fill: #0066CC;"); // Xanh dương dịu cho đến hạn
                    } else if (item.startsWith("Quá hạn")) {
                        setStyle("-fx-text-fill: #CC0000;"); // Đỏ dịu cho quá hạn
                    } else {
                        setStyle("-fx-text-fill: #666666;"); // Xám cho "Không xác định"
                    }
                }
            }
        });

        taskTable.getColumns().addAll(sttCol, nameCol, descCol, deadlineCol, priorityCol, statusCol, subjectCol, deadlineStatusCol);

        // Tô màu dựa trên trạng thái
        taskTable.setRowFactory(tv -> new TableRow<Task>() {
            @Override
            public void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (task == null || empty) {
                    setStyle("");
                } else {
                    String status = task.getStatus() != null ? task.getStatus() : "Chua lam";
                    switch (status) {
                        case "Chua lam":
                            setStyle("-fx-background-color: #FF9999;");
                            break;
                        case "Dang lam":
                            setStyle("-fx-background-color: #FFFF99;");
                            break;
                        case "Da hoan thanh":
                            setStyle("-fx-background-color: #99FF99;");
                            break;
                        default:
                            setStyle("-fx-background-color: #FF9999;");
                    }
                }
            }
        });

        // Tải dữ liệu
        tasks = FXCollections.observableArrayList(taskController.getTasksByUser(currentUser.getUserId()));
        taskTable.setItems(tasks);

        // Đặt bảng trong ScrollPane để có thanh trượt
        ScrollPane scrollPane = new ScrollPane(taskTable);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #D3D3D3; -fx-border-radius: 5;");

        // Tùy chỉnh các nút
        HBox buttonPane = new HBox(15);
        buttonPane.setPadding(new Insets(15));
        buttonPane.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: #D3D3D3; -fx-border-radius: 5;");

        Button addTaskButton = new Button("Thêm Công Việc");
        addTaskButton.setPrefSize(200, 60);
        addTaskButton.setFont(Font.font("Arial", 18));
        addTaskButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 15; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 2, 2);");

        Button addSubjectButton = new Button("Thêm Môn Học");
        addSubjectButton.setPrefSize(200, 60);
        addSubjectButton.setFont(Font.font("Arial", 18));
        addSubjectButton.setStyle("-fx-background-color: #9C27B0; -fx-text-fill: white; -fx-padding: 15; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 2, 2);");

        Button editButton = new Button("Sửa Công Việc");
        editButton.setPrefSize(200, 60);
        editButton.setFont(Font.font("Arial", 18));
        editButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 15; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 2, 2);");

        Button deleteButton = new Button("Xóa Công Việc");
        deleteButton.setPrefSize(200, 60);
        deleteButton.setFont(Font.font("Arial", 18));
        deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-padding: 15; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 2, 2);");

        Button completeButton = new Button("Đánh Dấu Hoàn Thành");
        completeButton.setPrefSize(280, 60);
        completeButton.setFont(Font.font("Arial", 18));
        completeButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-padding: 15; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 2, 2);");

        buttonPane.getChildren().addAll(addTaskButton, addSubjectButton, editButton, deleteButton, completeButton);

        addTaskButton.setOnAction(e -> {
            if (subjectController.getAllSubjects().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Chưa có môn học nào! Vui lòng thêm môn học trước.");
                alert.showAndWait();
            } else {
                showAddTaskWindow(tasks);
            }
        });

        addSubjectButton.setOnAction(e -> {
            showAddSubjectWindow();
        });

        editButton.setOnAction(e -> {
            Task selected = taskTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showEditTaskWindow(tasks, selected);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Chọn một công việc để sửa!");
                alert.show();
            }
        });

        deleteButton.setOnAction(e -> {
            Task selected = taskTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, 
                    "Bạn có chắc muốn xóa công việc '" + selected.getTaskName() + "'?");
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        taskController.deleteTask(selected.getTaskId());
                        tasks.setAll(taskController.getTasksByUser(currentUser.getUserId()));
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Đã xóa công việc thành công!");
                        alert.show();
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Chọn một công việc để xóa!");
                alert.show();
            }
        });

        completeButton.setOnAction(e -> {
            Task selected = taskTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                // Cập nhật trạng thái ngay trong đối tượng Task
                selected.setStatus("Đã hoàn thành");
                taskController.markAsCompleted(selected.getTaskId());
                // Làm mới bảng để hiển thị "Xong" ngay lập tức
                taskTable.refresh();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Đã đánh dấu hoàn thành công việc '" + selected.getTaskName() + "'!");
                alert.show();
                // Đồng bộ với cơ sở dữ liệu
                tasks.setAll(taskController.getTasksByUser(currentUser.getUserId()));
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Chọn một công việc để đánh dấu hoàn thành!");
                alert.show();
            }
        });

        VBox root = new VBox(10, scrollPane, buttonPane);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #FFFFFF;");
        Scene scene = new Scene(root, 1100, 650);
        primaryStage.setTitle("Quản Lý Công Việc - " + (currentUser != null ? currentUser.getUsername() : "Khách"));
        primaryStage.setScene(scene);
        primaryStage.show();

        // Kiểm tra công việc quá hạn
        checkOverdueTasks();
    }

    private void checkOverdueTasks() {
        StringBuilder overdueMessage = new StringBuilder("Công việc quá hạn cần làm ngay:\n");
        boolean hasOverdue = false;

        for (Task task : tasks) {
            if (task.getDeadline() != null && task.getDeadline().isBefore(LocalDate.now()) && 
                (task.getStatus() == null || !task.getStatus().equals("Đã hoàn thành"))) {
                overdueMessage.append("- ").append(task.getTaskName()).append(" (Hạn: ")
                              .append(task.getDeadline()).append(")\n");
                hasOverdue = true;
            }
        }

        if (hasOverdue) {
            Alert alert = new Alert(Alert.AlertType.WARNING, overdueMessage.toString());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Không có công việc nào quá hạn.");
            alert.showAndWait();
        }
    }

    private void showAddTaskWindow(ObservableList<Task> tasks) {
        Stage addTaskStage = new Stage();
        VBox addTaskPane = new VBox(15);
        addTaskPane.setPadding(new Insets(20));
        addTaskPane.setStyle("-fx-background-color: #F9F9F9; -fx-border-color: #E0E0E0; -fx-border-radius: 5;");

        TextField nameField = new TextField();
        nameField.setPromptText("Tên công việc");
        nameField.setFont(Font.font("Arial", 14));
        TextField descField = new TextField();
        descField.setPromptText("Mô tả");
        descField.setFont(Font.font("Arial", 14));
        DatePicker deadlinePicker = new DatePicker(LocalDate.now().plusDays(1));
        deadlinePicker.setStyle("-fx-font-size: 14pt;");

        ComboBox<String> priorityComboBox = new ComboBox<>();
        priorityComboBox.setPromptText("Chọn mức độ ưu tiên");
        ObservableList<String> priorities = FXCollections.observableArrayList("Cao", "Trung bình", "Thấp");
        priorityComboBox.setItems(priorities);
        priorityComboBox.setValue("Trung bình");
        priorityComboBox.setStyle("-fx-font-size: 14pt;");

        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.setPromptText("Chọn trạng thái");
        ObservableList<String> statuses = FXCollections.observableArrayList("Chưa làm", "Đang làm", "Đã hoàn thành");
        statusComboBox.setItems(statuses);
        statusComboBox.setValue("Chưa làm");
        statusComboBox.setStyle("-fx-font-size: 14pt;");

        ComboBox<Subject> subjectComboBox = new ComboBox<>();
        subjectComboBox.setPromptText("Chọn môn học");
        ObservableList<Subject> subjects = FXCollections.observableArrayList(subjectController.getAllSubjects());
        subjectComboBox.setItems(subjects);
        subjectComboBox.setCellFactory(param -> new ListCell<Subject>() {
            @Override
            protected void updateItem(Subject item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getSubjectName());
                }
            }
        });
        subjectComboBox.setButtonCell(new ListCell<Subject>() {
            @Override
            protected void updateItem(Subject item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getSubjectName());
                }
            }
        });
        subjectComboBox.setStyle("-fx-font-size: 14pt;");

        Button submitButton = new Button("Thêm Công Việc");
        submitButton.setPrefSize(150, 40);
        submitButton.setFont(Font.font("Arial", 14));
        submitButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 2, 2);");

        addTaskPane.getChildren().addAll(nameField, descField, deadlinePicker, priorityComboBox, statusComboBox, subjectComboBox, submitButton);

        submitButton.setOnAction(e -> {
            String taskName = nameField.getText();
            String description = descField.getText();
            LocalDate deadline = deadlinePicker.getValue();
            String priority = priorityComboBox.getValue();
            String status = statusComboBox.getValue();
            Subject selectedSubject = subjectComboBox.getValue();

            if (taskName.isEmpty() || description.isEmpty() || deadline == null || 
                priority == null || status == null || selectedSubject == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng chọn đầy đủ thông tin!");
                alert.show();
            } else {
                try {
                    boolean success = taskController.addTask(taskName, description, deadline, priority, status, 
                                                             currentUser.getUserId(), selectedSubject.getSubjectId());
                    if (success) {
                        tasks.setAll(taskController.getTasksByUser(currentUser.getUserId()));
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Đã thêm công việc thành công!");
                        alert.showAndWait();
                        addTaskStage.close();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Không thể thêm công việc! Kiểm tra cơ sở dữ liệu.");
                        alert.show();
                    }
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Lỗi: " + ex.getMessage());
                    alert.show();
                }
            }
        });

        Scene addTaskScene = new Scene(addTaskPane, 450, 400);
        addTaskStage.setScene(addTaskScene);
        addTaskStage.setTitle("Thêm Công Việc Mới");
        addTaskStage.show();
    }

    private void showEditTaskWindow(ObservableList<Task> tasks, Task taskToEdit) {
        Stage editTaskStage = new Stage();
        VBox editTaskPane = new VBox(15);
        editTaskPane.setPadding(new Insets(20));
        editTaskPane.setStyle("-fx-background-color: #F9F9F9; -fx-border-color: #E0E0E0; -fx-border-radius: 5;");

        TextField nameField = new TextField(taskToEdit.getTaskName());
        nameField.setPromptText("Tên công việc");
        nameField.setFont(Font.font("Arial", 14));
        TextField descField = new TextField(taskToEdit.getDescription());
        descField.setPromptText("Mô tả");
        descField.setFont(Font.font("Arial", 14));
        DatePicker deadlinePicker = new DatePicker(taskToEdit.getDeadline());
        deadlinePicker.setStyle("-fx-font-size: 14pt;");

        ComboBox<String> priorityComboBox = new ComboBox<>();
        priorityComboBox.setPromptText("Chọn mức độ ưu tiên");
        ObservableList<String> priorities = FXCollections.observableArrayList("Cao", "Trung bình", "Thấp");
        priorityComboBox.setItems(priorities);
        priorityComboBox.setValue(taskToEdit.getPriority());
        priorityComboBox.setStyle("-fx-font-size: 14pt;");

        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.setPromptText("Chọn trạng thái");
        ObservableList<String> statuses = FXCollections.observableArrayList("Chưa làm", "Đang làm", "Đã hoàn thành");
        statusComboBox.setItems(statuses);
        statusComboBox.setValue(taskToEdit.getStatus());
        statusComboBox.setStyle("-fx-font-size: 14pt;");

        Button submitButton = new Button("Lưu Thay Đổi");
        submitButton.setPrefSize(150, 40);
        submitButton.setFont(Font.font("Arial", 14));
        submitButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 10; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 2, 2);");

        editTaskPane.getChildren().addAll(nameField, descField, deadlinePicker, priorityComboBox, statusComboBox, submitButton);

        submitButton.setOnAction(e -> {
            String taskName = nameField.getText();
            String description = descField.getText();
            LocalDate deadline = deadlinePicker.getValue();
            String priority = priorityComboBox.getValue();
            String status = statusComboBox.getValue();

            if (taskName.isEmpty() || description.isEmpty() || deadline == null || 
                priority == null || status == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng chọn đầy đủ thông tin!");
                alert.show();
            } else {
                taskController.updateTask(taskToEdit.getTaskId(), taskName, description, deadline, priority, status);
                tasks.setAll(taskController.getTasksByUser(currentUser.getUserId()));
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Đã cập nhật công việc thành công!");
                alert.showAndWait();
                editTaskStage.close();
            }
        });

        Scene editTaskScene = new Scene(editTaskPane, 450, 400);
        editTaskStage.setScene(editTaskScene);
        editTaskStage.setTitle("Sửa Công Việc: " + taskToEdit.getTaskName());
        editTaskStage.show();
    }

    private void showAddSubjectWindow() {
        Stage addSubjectStage = new Stage();
        VBox addSubjectPane = new VBox(15);
        addSubjectPane.setPadding(new Insets(20));
        addSubjectPane.setStyle("-fx-background-color: #F9F9F9; -fx-border-color: #E0E0E0; -fx-border-radius: 5;");

        TextField subjectNameField = new TextField();
        subjectNameField.setPromptText("Tên môn học");
        subjectNameField.setFont(Font.font("Arial", 14));

        Button submitButton = new Button("Thêm Môn Học");
        submitButton.setPrefSize(150, 40);
        submitButton.setFont(Font.font("Arial", 14));
        submitButton.setStyle("-fx-background-color: #9C27B0; -fx-text-fill: white; -fx-padding: 10; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 2, 2);");

        addSubjectPane.getChildren().addAll(subjectNameField, submitButton);

        submitButton.setOnAction(e -> {
            String subjectName = subjectNameField.getText();
            if (subjectName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập tên môn học!");
                alert.show();
            } else {
                try {
                    boolean success = subjectController.addSubject(subjectName);
                    if (success) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Đã thêm môn học thành công!");
                        alert.showAndWait();
                        addSubjectStage.close();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Không thể thêm môn học! Kiểm tra cơ sở dữ liệu.");
                        alert.show();
                    }
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Lỗi: " + ex.getMessage());
                    alert.show();
                }
            }
        });

        Scene addSubjectScene = new Scene(addSubjectPane, 300, 150);
        addSubjectStage.setScene(addSubjectScene);
        addSubjectStage.setTitle("Thêm Môn Học Mới");
        addSubjectStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}