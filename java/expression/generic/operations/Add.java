package expression.generic.operations;

import expression.generic.interfaces.Operand;
import expression.generic.interfaces.Type;

public class Add<T> extends AbstractBinaryOperation<T> {

    public Add(Operand<T> firstOperand, Operand<T> secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    protected T calculate(T firstOperand, T secondOperand, Type<T> type) {
        return type.add(firstOperand, secondOperand);
    }
}
