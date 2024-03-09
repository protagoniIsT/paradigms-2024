package expression.generic.operations;

import expression.generic.interfaces.Operand;
import expression.generic.interfaces.Type;

public class Divide<T> extends AbstractBinaryOperation<T> {

    public Divide(Operand<T> firstExpression, Operand<T> secondExpression) {
        super(firstExpression, secondExpression);
    }

    @Override
    protected T calculate(T firstOperand, T secondOperand, Type<T> type) {
        return type.divide(firstOperand, secondOperand);
    }
}
