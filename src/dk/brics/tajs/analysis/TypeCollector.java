package dk.brics.tajs.analysis;

import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.Collections;

import java.util.List;

public class TypeCollector {
    private static List<VariableLocationType> typeInfoList = Collections.newList();

    public static void record(String variableName, SourceLocation sourceLocation, Value value) {
        typeInfoList.add(new VariableLocationType(variableName, sourceLocation, value));
    }

    public static void getTypeInformation() {
        for (VariableLocationType variableLocationType : typeInfoList) {
            System.out.println(variableLocationType.getName() + " : " + variableLocationType.getLocation() + " : " + variableLocationType.getValue());
        }
    }

    public static void reset() {
        typeInfoList = Collections.newList();
    }

    private static class VariableLocationType {
        private String name;
        private SourceLocation location;
        private Value value;

        private VariableLocationType(String name, SourceLocation sourceLocation, Value value) {
            this.name = name;
            this.location = sourceLocation;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Value getValue() {
            return value;
        }

        public SourceLocation getLocation() {
            return location;
        }
    }
}
