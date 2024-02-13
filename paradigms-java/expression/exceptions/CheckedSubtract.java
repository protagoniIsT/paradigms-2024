package expression.exceptions;

import expression.BasicExpressionInterface;
import expression.Subtract;
import expression.TripleExpression;

public class CheckedSubtract extends Subtract implements TripleExpression {
    public CheckedSubtract(BasicExpressionInterface firstOperand, BasicExpressionInterface secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    protected int calculate(int firstOperand, int secondOperand) {
        if ((secondOperand < 0 && firstOperand > Integer.MAX_VALUE + secondOperand) ||
                (secondOperand > 0 && firstOperand < Integer.MIN_VALUE + secondOperand)) {
            throw new ArithmeticException("Overflow");
        }
        return firstOperand - secondOperand;
    }
}