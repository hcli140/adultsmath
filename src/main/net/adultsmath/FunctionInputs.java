package main.net.adultsmath;

import java.util.Map;
import java.util.Objects;

public record FunctionInputs(Map<Variable, Double> inputsMap) {

    public FunctionInputs {
        if (Objects.isNull(inputsMap)) throw new NullMapConstructionException();
        for (Map.Entry<Variable, Double> entry : inputsMap.entrySet()) {
            if (Objects.isNull(entry.getKey())) throw new NullArgumentConstructionException();
            if (Objects.isNull(entry.getValue())) throw new NullArgumentConstructionException();
        }
    }

    public boolean contains(Variable targetVar) {
        for (Variable variable : inputsMap().keySet()) {
            if (variable.equals(targetVar)) return true;
        }
        return false;
    }

    public double get(Variable var) {
        if (!this.contains(var)) throw new NoSuchVariableException();
        return this.inputsMap().get(var);
    }

    private static class NullMapConstructionException extends RuntimeException {}

    private static class NullArgumentConstructionException extends RuntimeException {}

    private static class NoSuchVariableException extends RuntimeException {}
}
