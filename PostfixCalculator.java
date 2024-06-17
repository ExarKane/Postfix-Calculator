import java.util.*;
import java.io.*;

class InvalidPostfixExpressionException extends Exception {
    public InvalidPostfixExpressionException(String message) {
        super(message);
    }
}

public class PostfixCalculator {

    private List<String> history = new ArrayList<>();

    public int evaluatePostfix(String postfixExpression) throws InvalidPostfixExpressionException {
        Stack<Integer> stack = new Stack<>();
        Scanner scanner = new Scanner(postfixExpression);
        Set<String> operators = new HashSet<>(Arrays.asList("+", "-", "*", "/", "%", "^"));

        while (scanner.hasNext()) {
            if (scanner.hasNextInt()) {
                stack.push(scanner.nextInt());
            } else {
                String token = scanner.next();
                if (operators.contains(token)) {
                    if (stack.size() < 2) {
                        throw new InvalidPostfixExpressionException("Error: Invalid postfix expression (missing operand)");
                    }
                    int operand2 = stack.pop();
                    int operand1 = stack.pop();
                    switch (token) {
                        case "+":
                            stack.push(operand1 + operand2);
                            break;
                        case "-":
                            stack.push(operand1 - operand2);
                            break;
                        case "*":
                            stack.push(operand1 * operand2);
                            break;
                        case "/":
                            if (operand2 == 0) {
                                throw new InvalidPostfixExpressionException("Error: Division by zero");
                            }
                            stack.push(operand1 / operand2);
                            break;
                        case "%":
                            if (operand2 == 0) {
                                throw new InvalidPostfixExpressionException("Error: Division by zero");
                            }
                            stack.push(operand1 % operand2);
                            break;
                        case "^":
                            stack.push((int) Math.pow(operand1, operand2));
                            break;
                        default:
                            throw new InvalidPostfixExpressionException("Error: Invalid operator");
                    }
                } else {
                    throw new InvalidPostfixExpressionException("Error: Invalid token '" + token + "'");
                }
            }
        }

        if (stack.size() != 1) {
            throw new InvalidPostfixExpressionException("Error: Invalid postfix expression (extra operands)");
        }

        int result = stack.pop();
        history.add(postfixExpression + " = " + result);
        return result;
    }

    public void evaluateExpressionsFromFile(String filename) {
        try {
            Scanner fileScanner = new Scanner(new File(filename));
            while (fileScanner.hasNextLine()) {
                String expression = fileScanner.nextLine();
                try {
                    int result = evaluatePostfix(expression);
                    System.out.println("Result: " + result);
                } catch (InvalidPostfixExpressionException e) {
                    System.out.println(e.getMessage());
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found");
        }
    }

    public void displayHistory() {
        System.out.println("Calculation History:");
        for (String record : history) {
            System.out.println(record);
        }
    }

    public static void main(String[] args) {
        PostfixCalculator calculator = new PostfixCalculator();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Postfix Calculator Menu:");
            System.out.println("1. Evaluate Expression");
            System.out.println("2. Evaluate Expressions from File");
            System.out.println("3. Display Calculation History");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter postfix expression: ");
                    String expression = scanner.nextLine();
                    try {
                        int result = calculator.evaluatePostfix(expression);
                        System.out.println("Result: " + result);
                    } catch (InvalidPostfixExpressionException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    System.out.print("Enter filename: ");
                    String filename = scanner.nextLine();
                    calculator.evaluateExpressionsFromFile(filename);
                    break;
                case 3:
                    calculator.displayHistory();
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }
}
