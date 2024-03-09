package expression.generic.types;

import expression.generic.interfaces.Type;

import java.math.BigInteger;

public class BigIntegerType implements Type<BigInteger> {

    @Override
    public BigInteger add(BigInteger firstOperand, BigInteger secondOperand) {
        return firstOperand.add(secondOperand);
    }

    @Override
    public BigInteger subtract(BigInteger firstOperand, BigInteger secondOperand) {
        return firstOperand.subtract(secondOperand);
    }

    @Override
    public BigInteger multiply(BigInteger firstOperand, BigInteger secondOperand) {
        return firstOperand.multiply(secondOperand);
    }

    @Override
    public BigInteger divide(BigInteger firstOperand, BigInteger secondOperand) {
        return firstOperand.divide(secondOperand);
    }

    @Override
    public BigInteger parseNumToType(int value) {
        return BigInteger.valueOf(value);
    }

    @Override
    public BigInteger parseStrToType(String value) {
        return new BigInteger(value);
    }
}
