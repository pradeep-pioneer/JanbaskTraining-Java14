package com.janbask.training3;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import java.util.Scanner;

enum Spy { BLACK , WHITE }

public class Main {

    volatile int share;

    int instance;
    class Inner {
        final int testFinal = 23;
    }

    interface TestInterface{
        public String testMethod();
    }

    public static void main(String[] args) {
        //the get class example
        getClassExample();

        //check for interface
        TestInterface testInterface = new TestInterface() {
            @Override
            public String testMethod() {
                return "This is a Test";
            }
        };
        System.out.println(testInterface.getClass().getName());
        System.out.println("Is Interface: " + checkForInterface(testInterface.getClass()));
        ReflectInterface reflectInterface = new ReflectInterfaceClass();
        Class interfaceClass = ReflectInterface.class;
        System.out.println("reflectInterface object -> Is Interface: " + checkForInterface(reflectInterface.getClass()));
        System.out.println("ReflectInterface class -> Is Interface: " + checkForInterface(interfaceClass));

        //Check for array
        int[] integers = new int[10];
        System.out.println("Is Array: " + checkForArray(integers.getClass()));

        //check for primitive
        System.out.println("Is Primitive: " + checkForPrimitive(int.class));

        //Field enumeration - using getFields()
        Field[] fields = ReflectionExample.class.getFields();
        System.out.println("Using getFields() to enumerate all fields in type: " + ReflectionExample.class.getName());
        for (Field field:fields) {
            System.out.println(field.getName());
        }

        //Field enumeration - using getDeclaredFields
        Field[] fields1 = ReflectionExampleEx.class.getDeclaredFields();
        System.out.println("Using getDeclaredFields() to enumerate all fields in type: " + ReflectionExampleEx.class.getName());
        for (Field field:fields1) {
            System.out.println(field.getName());
        }

        //Dynamic value setting using field.
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the name of the filed for which you want to set value: ");
        String input = in.nextLine();
        try {
            //ToDo: Make this logic dynamic so that if the user enters a string for numeric field it should show a message and ask for value again
            Field field = ReflectionExampleEx.class.getField(input);
            System.out.println("Enter the value that you want to set: ");
            String value = in.nextLine();
            ReflectionExampleEx ex = new ReflectionExampleEx();
            if(field.getName()=="testField2")
                field.set(ex,Integer.parseInt(value));
            else
                field.set(ex, value);
            System.out.println("The vlaue of the field is: " + field.get(ex));
        }
        catch (NoSuchFieldException e){
            System.out.println("The field with name {" + input + "} was not found!");
        }
        catch (IllegalAccessException e){
            System.out.println("Cannot set value of the field, access not allowed!");
        }

        //ToDo: Assignment: list all methods on a type (print both declared and inherited separately)

        //Dynamic method invocation using Method
        System.out.println("Enter the name of the method you want to invoke: ");
        input = in.nextLine();
        try {
            Method method = ReflectionExampleEx.class.getMethod(input);
            ReflectionExampleEx ex = new ReflectionExampleEx();
            method.invoke(ex);

            System.out.println("trying to invoke <doSomethingWhackyWithParameter>: ");
            method = ReflectionExampleEx.class.getMethod("doSomethingWhackyWithParameter", int.class);
            System.out.println("Enter the value that you want to pass: ");
            String value = in.nextLine();
            System.out.println("invoking now...\n\n");
            method.invoke(ex, Integer.parseInt(value));
        }
        catch (NoSuchMethodException e){
            System.out.println("The method with name {" + input + "} was not found!");
        }
        catch (IllegalAccessException e){
            System.out.println("Cannot invoke method, access not allowed!");
        }
        catch (InvocationTargetException e){
            System.out.println("Cannot invoke method, target not valid!");
        }

        //Working with Modifiers
        checkModifiers("com.janbask.training3.Main", "volatile");
        checkModifiers("com.janbask.training3.Spy", "public");
        checkModifiers("com.janbask.training3.Main$Inner", "final");
        checkModifiers("java.lang.System", "public", "final", "static");
    }

    private static void checkModifiers(String className, String... modifiers){
        try {
            Class<?> c = Class.forName(className);
            int searchMods = 0x0;
            for (int i = 0; i < modifiers.length; i++) {
                searchMods |= modifierFromString(modifiers[i]);
            }
            Field[] flds = c.getDeclaredFields();
            System.out.format("Fields in Class '%s' containing modifiers:  %s%n",
                    c.getName(),
                    Modifier.toString(searchMods));
            boolean found = false;
            for (Field f : flds) {
                int foundMods = f.getModifiers();
                // Require all of the requested modifiers to be present
                if ((foundMods & searchMods) == searchMods) {
                    System.out.format("%-8s [ synthetic=%-5b enum_constant=%-5b ]%n",
                            f.getName(), f.isSynthetic(),
                            f.isEnumConstant());
                    found = true;
                }
            }

            if (!found) {
                System.out.format("No matching fields%n");
            }

            // production code should handle this exception more gracefully
        } catch (ClassNotFoundException x) {
            x.printStackTrace();
        }
    }

    private static int modifierFromString(String s) {
        int m = 0x0;
        if ("public".equals(s))           m |= Modifier.PUBLIC;
        else if ("protected".equals(s))   m |= Modifier.PROTECTED;
        else if ("private".equals(s))     m |= Modifier.PRIVATE;
        else if ("static".equals(s))      m |= Modifier.STATIC;
        else if ("final".equals(s))       m |= Modifier.FINAL;
        else if ("transient".equals(s))   m |= Modifier.TRANSIENT;
        else if ("volatile".equals(s))    m |= Modifier.VOLATILE;
        return m;
    }

    static boolean checkForInterface(Class classObject){
        return classObject.isInterface();
    }

    static boolean checkForArray(Class classObject){
        return classObject.isArray();
    }

    static boolean checkForPrimitive(Class classObject){
        return  classObject.isPrimitive();
    }

    static void getClassExample(){
        try {
            //1. Using forName method - notice the FQN along with package name
            Class c = Class.forName("com.janbask.training3.Main");
            System.out.println(c.getName());

            //2. using getClass method
            ReflectionExample example = new ReflectionExample();
            Class exampleClass = example.getClass();
            System.out.println(exampleClass.getName());

            //3. using class method on a type
            Class booleanClass = Boolean.class;
            System.out.println(booleanClass.getName());
        }
        catch (ClassNotFoundException e){
            System.out.println("There was an error: "+ e.getMessage());
        }
    }
}
