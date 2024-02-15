package expression;
import expression.exceptions.ExpressionParser;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        ExpressionParser parser = new ExpressionParser();
        int res = parser.parse("atvf + atvf * kriijf", List.of("atvf", "kriijf")).evaluate(List.of(2, 3));
        System.out.println(Variable.getVariables());
        System.out.println(res);
    }
}