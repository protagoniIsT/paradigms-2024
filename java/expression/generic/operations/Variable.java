package expression.generic.operations;

import expression.generic.interfaces.Operand;
import expression.generic.interfaces.Type;

public class Variable<T> implements Operand<T> {

    private final String variable;

    public Variable(String variable) {
        this.variable = variable;
    }

    @Override
    public T evaluate(T value1, T value2, T value3, Type<T> type) {
        if (variable.equals("x")) {
            return value1;
        } else if (variable.equals("y")) {
            return value2;
        }
        return value3;
    }
}