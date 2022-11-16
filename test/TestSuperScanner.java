package jkutkut.test;

import jkutkut.SuperScanner;

public class TestSuperScanner {
    public static void main(String[] args) {
        SuperScanner sc = new SuperScanner.En(System.in);

        int i = sc.getIntInRange("Enter an integer: ", 0, 100);
        String s = sc.getString("Enter a string: ");

        System.out.println("You entered: " + i + " and " + s + "\n\n");

        sc = new SuperScanner.Es(System.in);
        i = sc.getIntInRange("Introduce un entero: ", 0, 100);
        s = sc.getString("Introduce una cadena: ");

        System.out.println("Has introducido: " + i + " y " + s);

        sc.close();
    }
}
