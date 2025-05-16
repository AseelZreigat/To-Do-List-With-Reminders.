import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

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

        JLabel welcomeLabel = new JLabel("Welcome to Your To-Do List!");
        JButton addButton = new JButton("Add Task");
        JButton viewButton = new JButton("View Tasks");

        frame.add(welcomeLabel);
        frame.add(addButton);
        frame.add(viewButton);

        addButton.addActionListener(this::openAddTaskWindow);
        viewButton.addActionListener(e -> openViewTasksWindow());

        frame.setVisible(true);
    }

    private void openAddTaskWindow(ActionEvent e) {
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

        if (taskManager.getTasks().isEmpty()) {
            taskArea.setText("No tasks added yet.");
        } else {
            for (Task t : taskManager.getTasks()) {
                taskArea.append(t.toString() + "\n");
            }
        }

        viewFrame.add(new JScrollPane(taskArea));
        viewFrame.setVisible(true);
    }

    // كلاس داخلي بسيط لتمثيل مهمة
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

        public java.util.List<Task> getTasks() {
            return tasks;
        }
    }
}


