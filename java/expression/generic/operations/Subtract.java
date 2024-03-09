package expression.generic.operations;

import expression.generic.interfaces.Operand;
import expression.generic.interfaces.Type;
import expression.generic.operations.AbstractBinaryOperation;

public class Subtract<T> extends AbstractBinaryOperation<T> {
    public Subtract(Operand<T> firstOperand, Operand<T> secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    protected T calculate(T firstOperand, T secondOperand, Type<T> type) {
        return type.subtract(firstOperand, secondOperand);
    }
}
