package expression.exceptions;
import expression.Add;
import expression.BasicExpressionInterface;
import expression.TripleExpression;

public class CheckedAdd extends Add implements TripleExpression {
    public CheckedAdd(BasicExpressionInterface firstOperand, BasicExpressionInterface secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public int calculate(int firstOperand, int secondOperand) {
        if ((secondOperand > 0 && firstOperand > Integer.MAX_VALUE - secondOperand) ||
                (secondOperand < 0 && firstOperand < Integer.MIN_VALUE - secondOperand)) {
            throw new ArithmeticException("Overflow");
        }
        return firstOperand + secondOperand;
    }
}