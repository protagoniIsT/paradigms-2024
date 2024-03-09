package expression.generic.types;

import expression.generic.interfaces.Type;

public class BooleanType implements Type<Boolean> {

    public Boolean add(Boolean firstOperand, Boolean secondOperand) {
        return firstOperand | secondOperand;
    }

    public Boolean subtract(Boolean firstOperand, Boolean secondOperand) {
        return firstOperand ^ secondOperand;
    }

    public Boolean multiply(Boolean firstOperand, Boolean secondOperand) {
        return firstOperand & secondOperand;
    }

    public Boolean divide(Boolean firstOperand, Boolean secondOperand) {
        return (firstOperand ? 1 : 0) / (secondOperand ? 1 : 0) != 0;
    }

    @Override
    public Boolean parseNumToType(int value) {
        return value != 0;
    }

    @Override
    public Boolean parseStrToType(String value) {
        return Integer.parseInt(value) != 0;
    }
}
