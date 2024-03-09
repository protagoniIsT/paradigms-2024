package expression.generic.operations;

import expression.generic.interfaces.Operand;
import expression.generic.interfaces.Type;

public class Negate<T> implements Operand<T> {

    private final Operand<T> value;

    public Negate(Operand<T> value) {
        this.value = value;
    }

    public T evaluate(T value1, T value2, T value3, Type<T> type) {
        final T sign = type.parseNumToType(-1);
        final T result = value.evaluate(value1, value2, value3, type);
        return type.multiply(sign, result);
    }
}
