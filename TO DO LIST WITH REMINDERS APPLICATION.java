import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/*
-----------------------------------------------------
Mini project: To-Do List Application
Question: CS 310 - Advanced Programming
Written by: [Your Name], [Your Student ID]
-----------------------------------------------------
This program allows users to manage their tasks by adding, viewing, editing, and deleting them efficiently.
*/

public class Main {
    private TaskManager taskManager = new TaskManager();

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("To-Do List");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new FlowLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Your To-Do List, [Your Name]!");
        JButton addButton = new JButton("Add Task");
        JButton viewButton = new JButton("View Tasks");

        frame.add(welcomeLabel);
        frame.add(addButton);
        frame.add(viewButton);

        addButton.addActionListener(this::openAddTaskWindow);
        viewButton.addActionListener(e -> openViewTasksWindow());

        frame.setVisible(true);
    }

    private void openAddTaskWindow() {
        JFrame addFrame = new JFrame("Add New Task");
        addFrame.setSize(350, 250);
        addFrame.setLayout(new GridLayout(5, 2));

        JTextField titleField = new JTextField();
        JTextField descField = new JTextField();
        JTextField reminderField = new JTextField();

        JButton saveButton = new JButton("Save Task");

        addFrame.add(new JLabel("Title:"));
        addFrame.add(titleField);
        addFrame.add(new JLabel("Description:"));
        addFrame.add(descField);
        addFrame.add(new JLabel("Reminder (optional):"));
        addFrame.add(reminderField);
        addFrame.add(new JLabel(""));
        addFrame.add(saveButton);

        saveButton.addActionListener(ev -> {
            String title = titleField.getText().trim();
            String desc = descField.getText().trim();
            String reminder = reminderField.getText().trim();

            // Input validation
            if (title.isEmpty() || desc.isEmpty()) {
                JOptionPane.showMessageDialog(addFrame, "Title and Description are required.");
                return;
            }

            Task newTask = new Task(title, desc, reminder);
            taskManager.addTask(newTask);
            JOptionPane.showMessageDialog(addFrame, "Task added successfully!");
            addFrame.dispose();
        });

        addFrame.setVisible(true);
    }

    private void openViewTasksWindow() {
        JFrame viewFrame = new JFrame("Your Tasks");
        viewFrame.setSize(400, 300);

        JTextArea taskArea = new JTextArea();
        taskArea.setEditable(false);

        refreshTaskList(taskArea); // Refresh task list

        JButton editButton = new JButton("Edit Selected Task");
        JButton deleteButton = new JButton("Delete Selected Task");

        editButton.addActionListener(e -> {
            String selectedTask = taskArea.getSelectedText();
            if (selectedTask != null && !selectedTask.isEmpty()) {
                new EditTaskDialog(taskManager, selectedTask, taskArea).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(viewFrame, "Please select a task to edit.");
            }
        });

        deleteButton.addActionListener(e -> {
            String selectedTask = taskArea.getSelectedText();
            if (selectedTask != null && !selectedTask.isEmpty()) {
                new DeleteTaskDialog(taskManager, selectedTask, taskArea).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(viewFrame, "Please select a task to delete.");
            }
        });

        viewFrame.setLayout(new BorderLayout());
        viewFrame.add(new JScrollPane(taskArea), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        viewFrame.add(buttonPanel, BorderLayout.SOUTH);

        viewFrame.setVisible(true);
    }

    private void refreshTaskList(JTextArea taskArea) {
        taskArea.setText(""); // Clear current tasks
        for (Task t : taskManager.getTasks()) {
            taskArea.append(t.toString() + "\n");
        }
    }

    // كلاس لحذف المهمة
    class DeleteTaskDialog extends JDialog {
        public DeleteTaskDialog(TaskManager taskManager, String selectedTask, JTextArea taskArea) {
            setTitle("Delete Task");
            setSize(300, 150);
            setLayout(new FlowLayout());

            JLabel confirmLabel = new JLabel("Are you sure you want to delete this task?");
            JButton confirmButton = new JButton("Yes");
            JButton cancelButton = new JButton("No");

            confirmButton.addActionListener(e -> {
                String title = selectedTask.split(" - ")[0];
                taskManager.deleteTask(title);
                JOptionPane.showMessageDialog(this, "Task deleted successfully!");
                refreshTaskList(taskArea); // Refresh the task list
                dispose();
            });

            cancelButton.addActionListener(e -> dispose());

            add(confirmLabel);
            add(confirmButton);
            add(cancelButton);

            setModal(true); // Makes the dialog modal
            setLocationRelativeTo(null); // Center the dialog
            setVisible(true);
        }
    }

    // كلاس لتعديل المهمة
    class EditTaskDialog extends JDialog {
        public EditTaskDialog(TaskManager taskManager, String selectedTask, JTextArea taskArea) {
            setTitle("Edit Task");
            setSize(350, 250);
            setLayout(new GridLayout(5, 2));

            String title = selectedTask.split(" - ")[0];
            Task taskToEdit = taskManager.getTaskByTitle(title);

            JTextField titleField = new JTextField(taskToEdit.getTitle());
            JTextField descField = new JTextField(taskToEdit.getDescription());
            JTextField reminderField = new JTextField(taskToEdit.getReminder());

            JButton saveButton = new JButton("Save Changes");

            add(new JLabel("Title:"));
            add(titleField);
            add(new JLabel("Description:"));
            add(descField);
            add(new JLabel("Reminder (optional):"));
            add(reminderField);
            add(new JLabel(""));
            add(saveButton);

            saveButton.addActionListener(ev -> {
                String newTitle = titleField.getText().trim();
                String newDesc = descField.getText().trim();
                String newReminder = reminderField.getText().trim();

                if (newTitle.isEmpty() || newDesc.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Title and Description are required.");
                    return;
                }

                taskManager.updateTask(taskToEdit, new Task(newTitle, newDesc, newReminder));
                JOptionPane.showMessageDialog(this, "Task updated successfully!");
                refreshTaskList(taskArea); // Refresh the task list
                dispose();
            });

            setModal(true); // Makes the dialog modal
            setLocationRelativeTo(null); // Center the dialog
            setVisible(true);
        }
    }

    // كلاس لتمثيل المهمة
    class Task {
        private String title;
        private String description;
        private String reminder;

        public Task(String title, String description, String reminder) {
            this.title = title;
            this.description = description;
            this.reminder = reminder;
        }

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getReminder() { return reminder; }

        @Override
        public String toString() {
            return title + " - " + description + (reminder.isEmpty() ? "" : " (Reminder: " + reminder + ")");
        }
    }

    class TaskManager {
        private java.util.List<Task> tasks = new java.util.ArrayList<>();

        public void addTask(Task task) {
            tasks.add(task);
        }

        public void updateTask(Task oldTask, Task newTask) {
            int index = tasks.indexOf(oldTask);
            if (index != -1) {
                tasks.set(index, newTask);
            }
        }

        public void deleteTask(String title) {
            tasks.removeIf(task -> task.getTitle().equals(title));
        }

        public Task getTaskByTitle(String title) {
            for (Task task : tasks) {
                if (task.getTitle().equals(title)) {
                    return task;
                }
            }
            return null;
        }

        public java.util.List<Task> getTasks() {
            return tasks;
        }
    }
}