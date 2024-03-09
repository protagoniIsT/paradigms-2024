package expression.generic.types;

import expression.generic.interfaces.Type;

public class ByteType implements Type<Byte> {

    @Override
    public Byte add(Byte firstOperand, Byte secondOperand) {
        return (byte) (firstOperand + secondOperand);
    }

    @Override
    public Byte subtract(Byte firstOperand, Byte secondOperand) {
        return (byte) (firstOperand - secondOperand);
    }

    @Override
    public Byte multiply(Byte firstOperand, Byte secondOperand) {
        return (byte) (firstOperand * secondOperand);
    }

    @Override
    public Byte divide(Byte firstOperand, Byte secondOperand) {
        return (byte) (firstOperand / secondOperand);
    }

    @Override
    public Byte parseNumToType(int value) {
        return (byte) value;
    }

    @Override
    public Byte parseStrToType(String value) {
        return Byte.parseByte(value);
    }
}
