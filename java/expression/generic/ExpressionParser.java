package expression.generic;

import expression.generic.interfaces.Operand;
import expression.generic.interfaces.Type;
import expression.generic.operations.*;

import javax.naming.InvalidNameException;
import java.security.InvalidParameterException;

public class ExpressionParser<T> {
    private String expression;
    private int index;

    private Type<T> type;

    public Operand<T> parse(String expression, Type<T> type) throws InvalidNameException {
        this.expression = expression;
        this.index = 0;
        this.type = type;
        validateExpression();
        return parseExpression();
    }

    private Operand<T> parseExpression() throws InvalidNameException {
        isCorrectBracketSequence();
        Operand<T> result = parseTerm();
        while (true) {
            if (match('+')) {
                result = createBinaryOperation(result, parseTerm(), '+');
            } else if (match('-')) {
                result = createBinaryOperation(result, parseTerm(), '-');
            } else {
                break;
            }
        }
        return result;
    }

    private Operand<T> parseTerm() throws InvalidNameException {
        Operand<T> result = parseFactor();
        while (true) {
            if (match('*')) {
                result = createBinaryOperation(result, parseFactor(), '*');
            } else if (match('/')) {
                result = createBinaryOperation(result, parseFactor(), '/');
            } else {
                break;
            }
        }
        return result;
    }

    private Operand<T> parseFactor() throws InvalidNameException {
        skipWhitespace();
        if (match('(')) {
            Operand<T> result = parseExpression();
            expect(')');
            return result;
        } else if (match('-')) {
            if (Character.isDigit(peek())) {
                return new Const<>(parseNumber(true));
            }
            return new Negate<>(parseFactor());
        } else if (Character.isDigit(peek())) {
            Operand<T> result = new Const<>(parseNumber(false));
            checkNextSymbolAfterVarOrNumber();
            return result;
        } else if (isVariable(peek())) {
            Operand<T> result = new Variable<>(parseVariable());
            checkNextSymbolAfterVarOrNumber();
            return result;
        }
//        } else if (isUnaryOperationPart(peek())) {
//            String operation = parseUnaryOperator();
//            if (operation.equals("pow2") || operation.equals("log2")) {
//                if (peek() != '(') {
//                    skipWhitespace();
//                    if (Character.isLetterOrDigit(peek()) && !Character.isWhitespace(expression.charAt(index - 1))) {
//                        throw new RuntimeException("Unary operation '" + operation + "' must be followed by a whitespace");
//                    }
//                }
//            }
//            return createUnaryOperation(parseFactor(), operation);
//        }
        else if (isOperator(peek())) {
            throw new InvalidParameterException("Unexpected operator: " + peek() + " on position " + index);
        } else if (peek() != '\0') {
            throw new InvalidParameterException("Unexpected character: " + peek() + " on position " + index);
        } else {
            throw new InvalidParameterException("Unexpected end of expression");
        }
    }

    private String parseUnaryOperator() {
        StringBuilder operation = new StringBuilder();
        while (isUnaryOperationPart(peek()) && operation.length() != 4) {
            operation.append(peek());
            movePointer();
        }
        return operation.toString();
    }

    private T parseNumber(boolean isNegative) {
        StringBuilder number = new StringBuilder();
        if (isNegative) {
            number.append("-");
        }
        while (Character.isDigit(peekWithoutSkipWhitespace())) {
            number.append(expression.charAt(index++));
        }
        try {
            return type.parseStrToType(String.valueOf(number));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid number format: " + number);
        }
    }

    private String parseVariable() {
        StringBuilder variable = new StringBuilder();
        while (index < expression.length() && isVariable(peek())) {
            variable.append(expression.charAt(index++));
        }
        String var = variable.toString();
        if (!var.equals("x") && !var.equals("y") && !var.equals("z")) {
            throw new InvalidParameterException("Invalid variable: " + var + " on position " + index);
        }
        return var;
    }

    private Operand<T> createBinaryOperation(Operand<T> leftOp, Operand<T> rightOp, char operation) {
        return switch (operation) {
            case '+' -> new Add<>(leftOp, rightOp);
            case '-' -> new Subtract<>(leftOp, rightOp);
            case '*' -> new Multiply<>(leftOp, rightOp);
            case '/' -> new Divide<>(leftOp, rightOp);
            default -> throw new IllegalStateException("Unknown binary operator: " + operation);
        };
    }

    private Operand<T> createUnaryOperation(Operand<T> operand, String operation) {
//        return switch (operation) {
//            case "-" -> new Negate<>(operand);
////            case "~" -> new Not(operand);
////            case "log2" -> new CheckedLog2(operand);
////            case "pow2" -> new CheckedPow2(operand);
//            default -> throw new RuntimeException("Unknown unary operator: " + operation);
//        };
        if (operand.toString().equals("-")) {
            return new Negate<>(operand);
        }
        throw new RuntimeException("Unknown unary operator: " + operation);
    }

    private static boolean isLineSeparator(char c) {
        return c == '\n' || c == '\t' || c == '\r' || c == '\f';
    }

    private boolean isUnaryOperationPart(char c) {
        return c == 'p' || c == 'o' || c == 'w' || c == 'l' || c == 'g' || c == '2';
    }

    private boolean isVariable(char c) {
        return c == 'x' || c == 'y' || c == 'z';
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' ||
                c == '/' || c == '^' || c == '|' || c == '&';
    }

    private boolean isBrackets(char c) {
        return c == '(' || c == ')';
    }

    private boolean isLegalCharacter(char c) {
        return Character.isDigit(c) || isVariable(c) || isOperator(c) || isBrackets(c) ||
                Character.isWhitespace(c) || c == 'p' || c == 'o' || c == 'w' || c == 'g' || c == 'l' || c == '2';
    }

    private boolean isWhitespaceOrInvisible(char c) {
        return Character.isWhitespace(c) || isLineSeparator(c);
    }

    private void validateExpression() {
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (!isLegalCharacter(c)) {
                throw new InvalidParameterException("Invalid character: " + c + " on position " + index);
            }
        }
    }

    private void isCorrectBracketSequence() {
        int balance = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') {
                balance++;
            } else if (c == ')') {
                balance--;
            }
            if (balance < 0) {
                throw new IllegalStateException("Unmatched closing parenthesis found");
            }
        }
        if (balance != 0) {
            throw new IllegalStateException("Unmatched opening parenthesis found");
        }
    }

    private void checkNextSymbolAfterVarOrNumber() throws InvalidNameException {
        skipWhitespace();
        if (index < expression.length()) {
            char nextChar = expression.charAt(index);
            if (!isOperator(nextChar) && nextChar != ')') {
                throw new InvalidNameException("Error while parsing: unexpected character '" + nextChar + "' after number or variable");
            }
        }
    }

    private boolean match(char expected) {
        skipWhitespace();
        if (index < expression.length() && expression.charAt(index) == expected) {
            index++;
            return true;
        }
        return false;
    }

    private void movePointer() {
        if (index < expression.length()) {
            index++;
        }
    }

    private void expect(char expected) {
        if (peek() != expected) {
            throw new IllegalStateException("Expected '" + expected + "', actual: '" + peek() + "'");
        }
        movePointer();
    }

    private char peek() {
        skipWhitespace();
        if (index >= expression.length()) return '\0';
        return expression.charAt(index);
    }

    private char peekWithoutSkipWhitespace() {
        if (index >= expression.length()) return '\0';
        return expression.charAt(index);
    }

    private void skipWhitespace() {
        while (index < expression.length() && isWhitespaceOrInvisible(expression.charAt(index))) {
            index++;
        }
    }
}