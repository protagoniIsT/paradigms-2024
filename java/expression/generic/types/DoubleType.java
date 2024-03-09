package expression.generic.types;

import expression.generic.interfaces.Type;

public class DoubleType implements Type<Double> {

    public Double add(Double firstOperand, Double secondOperand) {
        return firstOperand + secondOperand;
    }

    public Double subtract(Double firstOperand, Double secondOperand) {
        return firstOperand - secondOperand;
    }

    public Double multiply(Double firstOperand, Double secondOperand) {
        return firstOperand * secondOperand;
    }

    public Double divide(Double firstOperand, Double secondOperand) {
        return firstOperand / secondOperand;
    }

    @Override
    public Double parseNumToType(int value) {
        return Double.parseDouble(String.valueOf(value));
    }

    @Override
    public Double parseStrToType(String value) {
        return Double.parseDouble(value);
    }
}
