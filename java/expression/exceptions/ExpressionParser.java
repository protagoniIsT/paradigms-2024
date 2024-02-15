package expression.exceptions;

import expression.*;

import javax.lang.model.type.UnknownTypeException;
import javax.naming.InvalidNameException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.addAll;

public class ExpressionParser implements TripleParser, ListParser {
    private String expression;
    private int index;
    public final List<String> allowedVariables = List.of("x", "y", "z");

    public BasicExpressionInterface parse(String expression) throws InvalidNameException {
        if (Variable.getVariables().isEmpty()) {
            Variable.exportVariables(allowedVariables);
        }
        this.expression = expression;
        this.index = 0;
        validateExpression();
        return parseExpression();
    }

    @Override
    public ListExpression parse(String expression, List<String> variables) throws Exception {
        Variable.exportVariables(variables);
        return parse(expression);
    }

    public List<String> getVars() {
        return allowedVariables;
    }

    private BasicExpressionInterface parseExpression() throws InvalidNameException {
        isCorrectBracketSequence();
        BasicExpressionInterface result = parseTerm();
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

    private BasicExpressionInterface parseTerm() throws InvalidNameException {
        BasicExpressionInterface result = parseFactor();
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

    private BasicExpressionInterface parseFactor() throws InvalidNameException {
        skipWhitespace();
        if (match('(')) {
            BasicExpressionInterface result = parseExpression();
            expect(')');
            return result;
        } else if (match('{')) {
            BasicExpressionInterface result = parseExpression();
            expect('}');
            return result;
        } else if (match('[')) {
            BasicExpressionInterface result = parseExpression();
            expect(']');
            return result;
        } else if (match('-')) {
            if (Character.isDigit(peek())) {
                return new Const(parseNumber(true));
            }
            return new CheckedNegate(parseFactor());
        } else if (Character.isDigit(peek())) {
            BasicExpressionInterface result = new Const(parseNumber(false));
            checkNextSymbolAfterVarOrNumber();
            return result;
        } else if (isVariable(peek()) || peek() == '$') {
            BasicExpressionInterface result = new Variable(parseVariable());
            checkNextSymbolAfterVarOrNumber();
            return result;
        } else if (isUnaryOperationPart(peek())) {
            String operation = parseUnaryOperator();
            if (operation.equals("pow2") || operation.equals("log2")) {
                if (!isOpeningBracket(peek())) {
                    skipWhitespace();
                    if (Character.isLetterOrDigit(peek()) && !Character.isWhitespace(expression.charAt(index - 1))) {
                        throw new RuntimeException("Unary operation '" + operation + "' must be followed by a whitespace");
                    }
                }
            }
            return createUnaryOperation(parseFactor(), operation);
        } else if (isOperator(peek())) {
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

    private int parseNumber(boolean isNegative) {
        StringBuilder number = new StringBuilder();
        if (isNegative) {
            number.append("-");
        }
        while (Character.isDigit(peekWithoutSkipWhitespace())) {
            number.append(expression.charAt(index++));
        }
        try {
            return Integer.parseInt(number.toString());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid number format: " + number);
        }
    }

    private String parseVariable() {
        StringBuilder variableBuilder = new StringBuilder();
        if (peek() == '$') {
            variableBuilder.append(expression.charAt(index++));
            while (index < expression.length() && Character.isDigit(expression.charAt(index))) {
                variableBuilder.append(expression.charAt(index++));
            }
            if (variableBuilder.length() == 1) {
                throw new InvalidParameterException("Expected digit after $ at position " + index);
            }
        }
        else if (Character.isLetter(peek())) {
            while (index < expression.length() && Character.isLetter(expression.charAt(index))) {
                variableBuilder.append(expression.charAt(index++));
            }
        } else {
            throw new InvalidParameterException("Invalid variable start at position " + index);
        }
        String variable = variableBuilder.toString();
        if (!Variable.getVariables().containsKey(variable) && !isSpecialVariablePattern(variable)) {
            throw new InvalidParameterException("Invalid variable: " + variable + " at position " + index);
        }
        return variable;
    }

    private BasicExpressionInterface createBinaryOperation(TripleExpression leftOp, TripleExpression rightOp, char operation) {
        return switch (operation) {
            case '+' -> new CheckedAdd((BasicExpressionInterface) leftOp, (BasicExpressionInterface) rightOp);
            case '-' -> new CheckedSubtract((BasicExpressionInterface) leftOp, (BasicExpressionInterface) rightOp);
            case '*' -> new CheckedMultiply((BasicExpressionInterface) leftOp, (BasicExpressionInterface) rightOp);
            case '/' -> new CheckedDivide((BasicExpressionInterface) leftOp, (BasicExpressionInterface) rightOp);
            case '^' -> new Xor((BasicExpressionInterface) leftOp, (BasicExpressionInterface) rightOp);
            case '|' -> new Or((BasicExpressionInterface) leftOp, (BasicExpressionInterface) rightOp);
            case '&' -> new And((BasicExpressionInterface) leftOp, (BasicExpressionInterface) rightOp);
            default -> throw new IllegalStateException("Unknown binary operator: " + operation);
        };
    }

    private BasicExpressionInterface createUnaryOperation(TripleExpression operand, String operation) {
        return switch (operation) {
            case "-" -> new CheckedNegate((BasicExpressionInterface) operand);
            case "~" -> new Not(operand);
            case "log2" -> new CheckedLog2(operand);
            case "pow2" -> new CheckedPow2(operand);
            default -> throw new IllegalStateException("Unknown unary operator: " + operation);
        };
    }

    private boolean isSpecialVariablePattern(String variable) {
        return variable.charAt(0) == '$' && variable.length() > 1 && variable.substring(1).chars().allMatch(Character::isDigit);
    }

    private static boolean isLineSeparator(char c) {
        return c == '\n' || c == '\t' || c == '\r' || c == '\f';
    }

    private boolean isUnaryOperationPart(char c) {
        return c == 'p' || c == 'o' || c == 'w' || c == 'l' || c == 'g' || c == '2';
    }

    private boolean isVariable(char c) {
        if (c == 'x' || c == 'y' || c == 'z') {
            return true;
        }
        for (String v : Variable.getVariables().keySet()) {
            if (v.contains(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' ||
                c == '/' || c == '^' || c == '|' || c == '&';
    }

    private boolean isOpeningBracket(char c) {
        return c == '(' || c == '{' || c == '[';
    }

    private boolean isClosingBracket(char c) {
        return c == ')' || c == '}' || c == ']';
    }

    private boolean isBracket(char c) {
        return isOpeningBracket(c) || isClosingBracket(c);
    }

    private boolean isLegalCharacter(char c) {
        return Character.isDigit(c) || isVariable(c) || isOperator(c) || isBracket(c) ||
                Character.isWhitespace(c) || isUnaryOperationPart(c) || c == '$';
    }

    private boolean isInvisibleCharacter(char c) {
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
        int balance1 = 0;
        int balance2 = 0;
        int balance3 = 0;
        for (char c : expression.toCharArray()) {
            switch (c) {
                case '(' -> balance1++;
                case ')' -> {
                    balance1--;
                    if (balance1 < 0) {
                        throw new IllegalStateException("Unmatched closing parenthesis found: )");
                    }
                }
                case '{' -> balance2++;
                case '}' -> {
                    balance2--;
                    if (balance2 < 0) {
                        throw new IllegalStateException("Unmatched closing parenthesis found: }");
                    }
                }
                case '[' -> balance3++;
                case ']' -> {
                    balance3--;
                    if (balance3 < 0) {
                        throw new IllegalStateException("Unmatched closing parenthesis found: ]");
                    }
                }
                default -> {}
            }
        }
        if (!(balance1 == 0 && balance2 == 0 && balance3 == 0)) {
            throw new IllegalStateException("Unmatched opening parenthesis found");
        }
    }

    private void checkNextSymbolAfterVarOrNumber() throws InvalidNameException {
        skipWhitespace();
        if (index < expression.length()) {
            char nextChar = expression.charAt(index);
            if (!isOperator(nextChar) && !isClosingBracket(nextChar)) {
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
        while (index < expression.length() && isInvisibleCharacter(expression.charAt(index))) {
            index++;
        }
    }
}