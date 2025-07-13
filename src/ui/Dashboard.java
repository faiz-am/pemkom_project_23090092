package ui;

import model.QueueItem;
import model.SmartQueue;
import util.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Queue;

public class Dashboard extends JFrame {
    private final SmartQueue<String> queue = new SmartQueue<>(); //Pemrograman Generic
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JTextArea logDisplay = new JTextArea();
    private final JLabel currentProcessingLabel = new JLabel("Currently Processing: -");

    private boolean isProcessing = false;
    private Thread processingThread;

    public Dashboard() {
        setTitle("Smart Queue Dashboard");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Queue list
        JList<String> queueList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(queueList);

        // Buttons
        JButton addButton = new JButton("Add to Queue");
        JButton removeButton = new JButton("Remove First");
        JButton startButton = new JButton("Start Processing");
        JButton stopButton = new JButton("Stop Processing");
        JButton saveButton = new JButton("Save Queue");
        JButton loadButton = new JButton("Load Queue");
        JButton clearButton = new JButton("Clear Queue");

        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(clearButton);

        // Log panel
        logDisplay.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logDisplay);
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Processing Log"));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(currentProcessingLabel, BorderLayout.NORTH);
        bottomPanel.add(logScrollPane, BorderLayout.CENTER);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // Button actions
        addButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter name:");
            if (name != null && !name.trim().isEmpty()) {
                queue.enqueue(name);
                updateDisplay();
            }
        });

        removeButton.addActionListener(e -> {
            QueueItem<String> removed = queue.dequeueNow();
            if (removed != null) {
                log("Manually removed: " + removed.getData());
            } else {
                JOptionPane.showMessageDialog(this, "Queue is empty!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
    updateDisplay();
});


        clearButton.addActionListener(e -> {
            queue.clear();
            updateDisplay();
        });

        saveButton.addActionListener(e -> {
            FileUtil.saveQueueToFile(queue.getAllItems(), "queue.dat");  //Serialization
            JOptionPane.showMessageDialog(this, "Queue saved successfully!");
        }); //Cryptography

        loadButton.addActionListener(e -> {
            Queue<QueueItem<String>> loaded = FileUtil.loadQueueFromFile("queue.dat");   //Serialization
            if (loaded != null) {
                queue.setItems(loaded);
                updateDisplay();
            }
        }); //Cryptography

        startButton.addActionListener((ActionEvent e) -> {
            isProcessing = true;
            processingThread = new Thread(() -> { //Multi Threading
                try {
                    while (isProcessing) {
                        QueueItem<String> item = queue.dequeue();

                        SwingUtilities.invokeLater(() -> {
                            // Update current processing label
                            currentProcessingLabel.setText("Currently Processing: " + item.getData());

                            // Show popup
                            JOptionPane.showMessageDialog(this,
                                    "Processing: " + item.getData(),
                                    "Smart Queue",
                                    JOptionPane.INFORMATION_MESSAGE);

                            // Log and update UI
                            log(item.getData());
                            updateDisplay();
                        });

                        Thread.sleep(3000); // simulate processing time
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            });
            processingThread.start();
        });

        stopButton.addActionListener(e -> {
            isProcessing = false;
            if (processingThread != null) {
                processingThread.interrupt();
            }
        });

        setVisible(true);
    }

    private void updateDisplay() {
        listModel.clear();
        for (QueueItem<String> item : queue.getAllItems()) {
            listModel.addElement(item.getData());
        }
        if (queue.getAllItems().isEmpty()) {
            currentProcessingLabel.setText("Currently Processing: -");
        }
    }

    private void log(String text) {
        logDisplay.append("Processed: " + text + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Dashboard::new);
    }
}
