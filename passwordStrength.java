import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.regex.Pattern;

/**
 * A Java Swing application that evaluates password strength in real-time.
 * It checks for length, uppercase letters, numbers, and special characters.
 */
public class PasswordChecker extends JFrame {

    // UI Components
    private JPasswordField passwordField;
    private JProgressBar strengthBar;
    private JLabel strengthLabel;
    private JLabel[] requirementLabels;
    private final String[] requirements = {
        "At least 6 characters",
        "Contains an uppercase letter",
        "Contains a number",
        "Contains a special character (@#$&)"
    };

    public PasswordChecker() {
        // Initialize the frame
        setTitle("SecurePass - Strength Checker");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(new Color(245, 247, 250));

        // Header
        JLabel headerLabel = new JLabel("Password Strength Checker");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerLabel.setForeground(new Color(44, 62, 80));

        // Password Input
        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Strength Bar
        strengthBar = new JProgressBar(0, 100);
        strengthBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 15));
        strengthBar.setStringPainted(false);
        strengthBar.setValue(0);
        strengthBar.setForeground(Color.GRAY);

        // Strength Text Label
        strengthLabel = new JLabel("Strength: Empty");
        strengthLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        strengthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Requirements Checklist Panel
        JPanel checklistPanel = new JPanel();
        checklistPanel.setLayout(new GridLayout(4, 1, 5, 5));
        checklistPanel.setBackground(new Color(245, 247, 250));
        checklistPanel.setBorder(BorderFactory.createTitledBorder("Requirements"));

        requirementLabels = new JLabel[requirements.length];
        for (int i = 0; i < requirements.length; i++) {
            requirementLabels[i] = new JLabel("✕ " + requirements[i]);
            requirementLabels[i].setForeground(new Color(192, 57, 43)); // Red
            checklistPanel.add(requirementLabels[i]);
        }

        // Add components to main panel
        mainPanel.add(headerLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(new JLabel("Enter Password:"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(passwordField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(strengthBar);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(strengthLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(checklistPanel);

        add(mainPanel, BorderLayout.CENTER);

        // Add DocumentListener for real-time checking
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { checkPassword(); }
            @Override
            public void removeUpdate(DocumentEvent e) { checkPassword(); }
            @Override
            public void changedUpdate(DocumentEvent e) { checkPassword(); }
        });
    }

    /**
     * Core logic to evaluate the password and update UI.
     */
    private void checkPassword() {
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);
        
        // Logical evaluation synced perfectly with your updated requirements
        boolean hasMinLength = password.length() >= 6;
        boolean hasUppercase = password.matches(".*[A-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = Pattern.compile("[@#$&]").matcher(password).find();

        int score = 0;
        if (!password.isEmpty()) {
            if (hasMinLength) score += 25;
            if (hasUppercase) score += 25;
            if (hasDigit) score += 25;
            if (hasSpecial) score += 25;
        }

        updateUI(score, hasMinLength, hasUppercase, hasDigit, hasSpecial);
        
        // Zero out the array for security
        java.util.Arrays.fill(passwordChars, ' ');
    }

    /**
     * Updates the visuals based on calculated metrics.
     */
    private void updateUI(int score, boolean l, boolean u, boolean d, boolean s) {
        // Update progress bar value
        strengthBar.setValue(score);
        
        // Update checklist colors
        updateRequirement(0, l);
        updateRequirement(1, u);
        updateRequirement(2, d);
        updateRequirement(3, s);

        // Update color and text based on score
        if (score == 0) {
            strengthBar.setForeground(Color.GRAY);
            strengthLabel.setText("Strength: Empty");
        } else if (score <= 25) {
            strengthBar.setForeground(new Color(231, 76, 60)); // Red
            strengthLabel.setText("Strength: Very Weak");
        } else if (score <= 50) {
            strengthBar.setForeground(new Color(230, 126, 34)); // Orange
            strengthLabel.setText("Strength: Weak");
        } else if (score <= 75) {
            strengthBar.setForeground(new Color(241, 196, 15)); // Yellow
            strengthLabel.setText("Strength: Medium");
        } else {
            strengthBar.setForeground(new Color(46, 204, 113)); // Green
            strengthLabel.setText("Strength: Strong");
        }
    }

    private void updateRequirement(int index, boolean met) {
        if (met) {
            requirementLabels[index].setText("✓ " + requirements[index]);
            requirementLabels[index].setForeground(new Color(39, 174, 96)); // Green
        } else {
            requirementLabels[index].setText("✕ " + requirements[index]);
            requirementLabels[index].setForeground(new Color(192, 57, 43)); // Red
        }
    }

    public static void main(String[] args) {
        // Set Look and Feel to System default for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Run the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            PasswordChecker frame = new PasswordChecker();
            frame.setVisible(true);
        });
    }
}