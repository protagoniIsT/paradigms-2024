package expression;

import java.util.List;
import java.util.Objects;

public abstract class AbstractBinaryOperation implements BasicExpressionInterface {
    protected final BasicExpressionInterface firstExpression;
    protected final BasicExpressionInterface secondExpression;

    protected AbstractBinaryOperation(BasicExpressionInterface firstExpression, BasicExpressionInterface secondExpression) {
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
    }

    protected abstract int calculate(int firstOperand, int secondOperand);
    protected abstract String getOperator();

    @Override
    public int evaluate(int value) {
        return calculate(firstExpression.evaluate(value), secondExpression.evaluate(value));
    }

    @Override
    public int evaluate(int value1, int value2, int value3) {
        return calculate(firstExpression.evaluate(value1, value2, value3), secondExpression.evaluate(value1, value2, value3));
    }

    @Override
    public int evaluate(List<Integer> variables) {
        return calculate(firstExpression.evaluate(variables), secondExpression.evaluate(variables));
    }

    @Override
    public String toString() {
        StringBuilder exp = new StringBuilder();
        exp.append("(").append(firstExpression.toString()).append(this.getOperator())
                       .append(secondExpression.toString()).append(")");
        return exp.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof AbstractBinaryOperation expression) {
            return firstExpression.equals(expression.firstExpression)
                    && this.getClass().equals(object.getClass())
                    && secondExpression.equals(expression.secondExpression);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstExpression, secondExpression, this.getOperator()) * 300;
    }
}