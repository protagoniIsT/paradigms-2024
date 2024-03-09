package expression.generic.operations;

import expression.generic.interfaces.Operand;
import expression.generic.interfaces.Type;

public class Const<T> implements Operand<T> {

    private final T value;

    public Const(T value) {
        this.value = value;
    }

    public T evaluate(T value1, T value2, T value3, Type<T> type) {
        return this.value;
    }
}
