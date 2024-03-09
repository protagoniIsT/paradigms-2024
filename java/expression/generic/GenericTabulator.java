package expression.generic;

import expression.generic.interfaces.Operand;
import expression.generic.interfaces.Tabulator;
import expression.generic.interfaces.Type;
import expression.generic.types.*;

import javax.naming.InvalidNameException;

public class GenericTabulator implements Tabulator {
    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        return fillTable(expression, getType(mode), x1, x2, y1, y2, z1, z2);
    }

    private<T> Object[][][] fillTable(String expression, Type<T> type, int x1, int x2, int y1, int y2, int z1, int z2) throws InvalidNameException {
        Operand<T> expressionValue = new ExpressionParser<T>().parse(expression, type);
        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int i = 0; i <= x2 - x1; i++) {
            for (int j = 0; j <= y2 - y1; j++) {
                for (int k = 0; k <= z2 - z1; k++) {
                    try {
                        result[i][j][k] = expressionValue.evaluate(type.parseNumToType(x1 + i),
                                                                   type.parseNumToType(y1 + j),
                                                                   type.parseNumToType(z1 + k),
                                                                                               type);
                    } catch (Exception e) {
                        result[i][j][k] = null;
                    }
                }
            }
        }
        return result;
    }



    private Type<?> getType(String mode) {
        return switch (mode) {
            case "i" -> new CheckedIntegerType();
            case "d" -> new DoubleType();
            case "bi" -> new BigIntegerType();
            case "u" -> new IntegerType();
            case "b" -> new ByteType();
            case "bool" -> new BooleanType();
            default -> throw new IllegalStateException("Unexpected mode: '" + mode + "'. Allowed: 'i', 'd', 'bi', 'u', 'b', 'bool'");
        };
    }
}
