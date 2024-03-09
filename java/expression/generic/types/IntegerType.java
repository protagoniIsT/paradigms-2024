package expression.generic.types;

import expression.generic.interfaces.Type;

public class IntegerType implements Type<Integer> {


    @Override
    public Integer add(Integer firstOperand, Integer secondOperand) {
        return firstOperand + secondOperand;
    }

    @Override
    public Integer subtract(Integer firstOperand, Integer secondOperand) {
        return firstOperand - secondOperand;
    }

    @Override
    public Integer multiply(Integer firstOperand, Integer secondOperand) {
        return firstOperand * secondOperand;
    }

    @Override
    public Integer divide(Integer firstOperand, Integer secondOperand) {
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