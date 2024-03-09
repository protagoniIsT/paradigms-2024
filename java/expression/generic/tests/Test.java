package expression.generic.tests;

import expression.generic.GenericTabulator;

import java.util.Arrays;
import java.util.Objects;

public class Test {
    public static void main(String[] args) throws Exception {
        GenericTabulator t = new GenericTabulator();
        Object[][][] table = t.tabulate("i", "x + -30", -2147483648, -2147483641, -2147483648, -2147483640, -2147483648, -2147483646);
        System.out.println(Arrays.deepToString(table));
    }
}
