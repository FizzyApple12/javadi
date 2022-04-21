package com.fizzyapple12.javadi;

public class DiExceptions {
    public static class InstanceNotFoundException extends RuntimeException { }
    public static class MultipleInstancesFoundException extends RuntimeException { }
    public static class IncompleteBindingException extends RuntimeException { }
    public static class RuleBuilderException extends RuntimeException { }
}
