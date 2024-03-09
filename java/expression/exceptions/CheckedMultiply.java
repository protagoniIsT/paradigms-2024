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
        final int result = firstOperand * secondOperand;
        if (secondOperand != 0 && result / secondOperand != firstOperand
                || firstOperand != 0 && result / firstOperand != secondOperand) {
            throw new ArithmeticException("Overflow");
        }
        return firstOperand * secondOperand;
    }
}