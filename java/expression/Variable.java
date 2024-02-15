package expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Variable implements BasicExpressionInterface {
    private final String variable;

    private final int index;

    private static HashMap<String, Integer> allowedVariables = new HashMap<>();

    public static void exportVariables(List<String> vars) {
        if (vars != null) {
            for (int i = allowedVariables.size(); i < vars.size(); i++) {
                if (!allowedVariables.containsKey(vars.get(i))) {
                    allowedVariables.put(vars.get(i), i);
                }
            }
        }
    }

    public static HashMap<String, Integer> getVariables() {
        return allowedVariables;
    }

    public Variable(String variable) {
        this.variable = variable;
        if (variable.charAt(0) == '$') {
            this.index = Integer.parseInt(variable.substring(1));
        } else {
            this.index = -1;
        }
    }

    public Variable(int index) {
        this.index = index;
        this.variable = null;
    }

    @Override
    public int evaluate(int value) {
        return value;
    }

    @Override
    public int evaluate(int value1, int value2, int value3) {
        if (variable.equals("x")) {
            return evaluate(value1);
        } else if (variable.equals("y")) {
            return evaluate(value2);
        }
        return evaluate(value3);
    }

    @Override
    public int evaluate(List<Integer> variables) {
        if (index != -1) {
            return variables.get(index);
        } else if (variable != null) {
            if (allowedVariables.containsKey(variable)) {
                return variables.get(allowedVariables.get(variable));
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return variable;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof final Variable var) {
            return variable.equals(var.variable);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(variable) * 300;
    }
}