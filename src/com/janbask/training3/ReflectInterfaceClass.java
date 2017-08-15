package com.janbask.training3;

public class ReflectInterfaceClass implements ReflectInterface {

    public String testMethod(String name){
        return String.format("\nHello %s", name);
    }
}
