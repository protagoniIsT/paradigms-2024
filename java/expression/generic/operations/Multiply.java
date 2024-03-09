package expression.generic.operations;

import expression.generic.interfaces.Operand;
import expression.generic.interfaces.Type;
import expression.generic.operations.AbstractBinaryOperation;

public class Multiply<T> extends AbstractBinaryOperation<T> {

    public Multiply(Operand<T> firstExpression, Operand<T> secondExpression) {
        super(firstExpression, secondExpression);
    }

    @Override
    protected T calculate(T firstOperand, T secondOperand, Type<T> type) {
        return type.multiply(firstOperand, secondOperand);
    }
}
