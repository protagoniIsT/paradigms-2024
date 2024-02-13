package expression.exceptions;

import expression.BasicExpressionInterface;
import expression.Multiply;
import expression.TripleExpression;

public class CheckedMultiply extends Multiply implements TripleExpression {
    public CheckedMultiply(BasicExpressionInterface firstOperand, BasicExpressionInterface secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    protected int calculate(int firstOperand, int secondOperand) {
        long result = (long) firstOperand * (long) secondOperand;
        if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
            throw new ArithmeticException("Overflow");
        }
        return (int) result;
    }
}