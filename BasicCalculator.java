import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class BasicCalculator extends JFrame implements ActionListener {

    private JTextField display;
    private JButton btn[];

    public BasicCalculator() {
        super("Basic Calculator");

        display = new JTextField();
        Font bf = display.getFont().deriveFont(Font.PLAIN, 40f);
        display.setFont(bf);
        display.setEditable(false);
        getContentPane().add(display, BorderLayout.NORTH);
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(4, 4));

        String button_name[] = { "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+" };
        btn = new JButton[button_name.length];
        
        for (int i = 0; i < button_name.length; i++) {
            btn[i] = new JButton(button_name[i]);
            btn[i].addActionListener(this);
            buttons.add(btn[i]);
        }
        getContentPane().add(buttons, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent event) {
        String user_input = event.getActionCommand();
        if (user_input.equals("=")) {
            String expression = display.getText();
            try {
                double ans = evaluateExpression(expression);
                display.setText(Double.toString(ans));
            } catch (IllegalArgumentException ex) {
                display.setText(ex.getMessage());
            }
        } else if (user_input.equals("C")) {
            display.setText("");
        } else {
            display.setText(display.getText() + user_input);
        }
    }

    private double evaluateExpression(String expression) {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        return evaluator.evaluate(expression);
    }

    private class ExpressionEvaluator {
        private String expression;
        private int index;

        public double evaluate(String expression) {
            this.expression = expression;
            index = 0;
            double ans = parseTerm();
            if (index != expression.length()) {
                throw new IllegalArgumentException("Invalid expression");
            }
            return ans;
        }

        private double parseTerm() {
            double ans = parseFactor();
            while (index < expression.length()) {
                char operator = expression.charAt(index);
                if (operator != '*' && operator != '/') {
                    break;
                }
                index++;
                double operand = parseFactor();
                if (operator == '*') {
                    ans *= operand;
                } else {
                    ans /= operand;
                }
            }
            return ans;
        }

        private double parseFactor() {
            double ans = parseNumber();
            while (index < expression.length()) {
                char operator = expression.charAt(index);
                if (operator != '+' && operator != '-') {
                    break;
                }
                index++;
                double operand = parseNumber();
                if (operator == '+') {
                    ans += operand;
                } else {
                    ans -= operand;
                }
            }
            return ans;
        }

        private double parseNumber() {
            int start = index;
            while (index < expression.length() && Character.isDigit(expression.charAt(index))) {
                index++;
            }
            if (index == start) {
                throw new IllegalArgumentException("Invalid expression");
            }
            return Double.parseDouble(expression.substring(start, index));
        }
    }
public static void main(String[] args) {
    BasicCalculator calculator = new BasicCalculator();
    calculator.setSize(400, 400);
    calculator.setVisible(true);
    calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}
}
