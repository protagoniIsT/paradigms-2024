package expression;
import expression.exceptions.ExpressionParser;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        ExpressionParser parser = new ExpressionParser();
        int res = parser.parse("a + b * b", List.of("a", "b")).evaluate(List.of(2, 3));
        System.out.println(Variable.getVariables());
        System.out.println(res);
    }
}