package expression.generic.interfaces;

public interface Type<T> {

    T add(T firstOperand, T secondOperand);

    T subtract(T firstOperand, T secondOperand);

    T multiply(T firstOperand, T secondOperand);

    T divide(T firstOperand, T secondOperand);

    T parseNumToType(int value);

    T parseStrToType(String value);
}

