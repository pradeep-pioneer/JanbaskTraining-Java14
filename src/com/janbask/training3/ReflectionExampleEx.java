package com.janbask.training3;

public class ReflectionExampleEx extends ReflectionExample {
    public String extendedField;
    public ReflectionExampleEx(){
        extendedField="";
    }

    public void doSomethingWhackyHere(){
        System.out.println("I am doing something whacky here");
    }

    public void doSomethingWhackyWithParameter(int repeatitions){
        for (int i=0; i<repeatitions; i++){
            System.out.println("Doing something whacky repeatition: " + i);
        }
    }
}
