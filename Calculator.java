import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame implements ActionListener {
    private final JTextField display = new JTextField("0");
    private double result = 0;
    private String operator = "=";
    private boolean start = true;

    public Calculator() {
        setTitle("Basic Calculator");
        setSize(320, 420);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setFont(new Font("Arial", Font.PLAIN, 24));
        add(display, BorderLayout.NORTH);

        String[] buttons = {
            "7","8","9","/",
            "4","5","6","*",
            "1","2","3","-",
            "0",".","=","+",
            "C"
        };

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 5, 5));
        for (String text : buttons) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Arial", Font.PLAIN, 20));
            btn.addActionListener(this);
            panel.add(btn);
            // Make the "C" span remaining cells visually by adding empty labels if needed
            if (text.equals("C")) {
                // add 3 empty placeholders to fill the grid nicely
                panel.add(new JLabel());
                panel.add(new JLabel());
                panel.add(new JLabel());
            }
        }

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(panel, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if ("0123456789.".indexOf(cmd) >= 0) {
            if (start) {
                display.setText(cmd.equals(".") ? "0." : cmd);
                start = false;
            } else {
                // prevent multiple decimals
                if (cmd.equals(".") && display.getText().contains(".")) return;
                display.setText(display.getText() + cmd);
            }
        } else if (cmd.equals("C")) {
            display.setText("0");
            start = true;
            result = 0;
            operator = "=";
        } else {
            // operator pressed
            if (start) {
                // allow changing operator before entering next number
                if (cmd.equals("-")) {
                    display.setText("-");
                    start = false;
                } else {
                    operator = cmd;
                }
            } else {
                double x = Double.parseDouble(display.getText());
                calculate(x);
                operator = cmd;
                start = true;
            }
        }
    }

    private void calculate(double x) {
        switch (operator) {
            case "+":
                result += x;
                break;
            case "-":
                result -= x;
                break;
            case "*":
                result *= x;
                break;
            case "/":
                if (x == 0) {
                    JOptionPane.showMessageDialog(this, "Cannot divide by zero", "Error", JOptionPane.ERROR_MESSAGE);
                    result = 0;
                } else {
                    result /= x;
                }
                break;
            case "=":
                result = x;
                break;
        }
        // Display result without trailing .0 when integer
        if (result == (long) result) {
            display.setText(String.format("%d", (long) result));
        } else {
            display.setText(String.format("%s", result));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculator calc = new Calculator();
            calc.setVisible(true);
        });
    }
}