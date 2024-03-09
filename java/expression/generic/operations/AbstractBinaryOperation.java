package expression.generic.operations;

import expression.generic.interfaces.Operand;
import expression.generic.interfaces.Type;

public abstract class AbstractBinaryOperation<T> implements Operand<T> {

    protected final Operand<T> firstExpression;

    protected final Operand<T> secondExpression;

    protected AbstractBinaryOperation(Operand<T> firstExpression, Operand<T> secondExpression) {
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
    }

    protected abstract T calculate(T firstOperand, T secondOperand, Type<T> type);

    @Override
    public T evaluate(T value1, T value2, T value3, Type<T> type) {
        return calculate(firstExpression.evaluate(value1, value2, value3, type),
                        secondExpression.evaluate(value1, value2, value3, type), type);
    }
}