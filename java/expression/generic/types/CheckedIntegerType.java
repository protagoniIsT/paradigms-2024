package expression.generic.types;

import expression.generic.interfaces.Type;

public class CheckedIntegerType implements Type<Integer> {

    public Integer add(Integer firstOperand, Integer secondOperand) {
        if ((secondOperand > 0 && firstOperand > Integer.MAX_VALUE - secondOperand) ||
                (secondOperand < 0 && firstOperand < Integer.MIN_VALUE - secondOperand)) {
            throw new ArithmeticException("Overflow");
        }
        return firstOperand + secondOperand;
    }


    public Integer subtract(Integer firstOperand, Integer secondOperand) {
        if ((secondOperand < 0 && firstOperand > Integer.MAX_VALUE + secondOperand) ||
                (secondOperand > 0 && firstOperand < Integer.MIN_VALUE + secondOperand)) {
            throw new ArithmeticException("Overflow");
        }
        return firstOperand - secondOperand;
    }

    public Integer multiply(Integer firstOperand, Integer secondOperand) {
        final int result = firstOperand * secondOperand;
        if (secondOperand != 0 && result / secondOperand != firstOperand
                || firstOperand != 0 && result / firstOperand != secondOperand) {
            throw new ArithmeticException("Overflow");
        }
        return firstOperand * secondOperand;
    }

    public Integer divide(Integer firstOperand, Integer secondOperand) {
        if (secondOperand == 0) {
            throw new ArithmeticException("Division by zero");
        }
        if (firstOperand == Integer.MIN_VALUE && secondOperand == -1) {
            throw new ArithmeticException("Overflow");
        }
        return firstOperand / secondOperand;
    }

    @Override
    public Integer parseNumToType(int value) {
        return value;
    }

    @Override
    public Integer parseStrToType(String value) {
        return Integer.parseInt(value);
    }
}
