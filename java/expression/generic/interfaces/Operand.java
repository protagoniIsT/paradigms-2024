package expression.generic.interfaces;

public interface Operand<T> {
    T evaluate(T value1, T value2, T value3, Type<T> type);
}
