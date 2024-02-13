package expression;
import expression.exceptions.ExpressionParser;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        ExpressionParser parser = new ExpressionParser();
        int res = parser.parse("$0", List.of("$0", "$1", "$2", "$3", "$4", "$5", "$6"))
                .evaluate(List.of(820987301, -594177010, 157863246, -1685179451, -1476525147, -781293477, -634827633));
        //$0=820987301, $1=-594177010, $2=157863246, $3=-1685179451, $4=-1476525147, $5=-781293477, $6=-634827633
        System.out.println(res);
    }
}