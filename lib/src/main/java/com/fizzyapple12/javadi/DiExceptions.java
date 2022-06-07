package com.fizzyapple12.javadi;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.WeakHashMap;

public class DiExceptions {
    public static class InstanceNotFoundException extends RuntimeException {
        public InstanceNotFoundException(Class<?> searchClass, DiContext context, List<DiRule> rules, WeakHashMap<UUID, Object> objectPool) {
            System.err.println();
            System.err.println();
            System.err.println("Instance not found while resolving " + searchClass.getName());

            System.err.println("- You might have forgotten to bind() the class before resolving it.");

            List<Class<?>> values = new ArrayList<>();
            context.memberClass = searchClass;
            
            for (DiRule rule : rules) {
                switch (rule.retrievalMode) {
                    case Resolve:
                    case Create:
                        if (rule.instanceClass != null && rule.instanceClass.getSimpleName().contains(searchClass.getName())) values.add(rule.instanceClass);
                        break;
                    case Return:
                        if (rule.targetObject != null && rule.targetObject.getClass().getSimpleName().contains(searchClass.getName())) values.add(rule.targetObject.getClass());
                        break;
                    default:
                        break;
                }
            }

            if (values.size() > 0) {
                System.err.println("- Found " + ((values.size() > 1) ? "classes with similar names: " : "a class with a similar name: "));

                for (Class<?> similarClass : values) {
                    System.err.println("| - " + similarClass.getName());
                }

                System.err.println("| You might have imported the wrong class.");
            }

            System.err.println();
            System.err.println();
        }
    }
    public static class MultipleInstancesFoundException extends RuntimeException {
        public MultipleInstancesFoundException(Class<?> searchClass, DiContext context, List<DiRule> rules, WeakHashMap<UUID, Object> objectPool) {        
            System.err.println();
            System.err.println();
            System.err.println("Multiple instances found while resolving " + searchClass.getName());

            System.err.println("- You might have accidentally duplicated a bind() statement");

            if (context.id == null || context.id.isEmpty() || context.id.isBlank()) {
                System.err.println("- ID is blank, you might have forgotten to bind() with and ID");
            }

            System.err.println();
            System.err.println();
        }
    }
    public static class IncompleteBindingException extends RuntimeException {
        public IncompleteBindingException(DiRule rule, boolean foundRecursion) {        
            System.err.println();
            System.err.println();

            switch (rule.retrievalMode) {
                case Resolve:
                case Create:
                    if (rule.instanceClass == null) {
                        System.err.println("An incomplete binding was found");
                    } else {
                        System.err.println("An incomplete binding was found for " + rule.instanceClass.getName());
                    }
                    break;
                case Return:
                    if (rule.targetObject == null) {
                        System.err.println("An incomplete binding was found");
                    } else {
                        System.err.println("An incomplete binding was found for " + rule.targetObject.getClass().getName());

                        Object instance = rule.container.objectPool.get(rule.targetObject);

                        if (instance != null) System.err.println("- No instance is set for resolution");
                    }

                    break;
                default:
                    break;
            }

            if (foundRecursion) System.err.println("- Found potential recursion in resolution, you may have bound the class to itself");

            System.err.println();
            System.err.println();
        }
    }
    public static class RuleBuilderException extends RuntimeException {
        public RuleBuilderException(String reason) {        
            System.err.println();
            System.err.println();
            System.err.println("A Rule Builder exception was thrown: ");

            System.err.println(reason);

            System.err.println();
            System.err.println();
        }
    }
}
