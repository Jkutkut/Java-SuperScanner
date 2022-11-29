package jkutkut;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Scanner;


/**
 * Custom wrapper for the java.util.Scanner class able to error-handle the input given to it.
 * It features a few methods to make the interaction with the user easier.
 * Due to the fact that the Scanner class is <i>final</i>, it is not possible to
 * extend it directly.
 *
 * @author "Jkutkut -- Jorge Re"
 */
public abstract class SuperScanner {
    private final Scanner sc;

    // ******* Constructors (Same as original) *******

    public SuperScanner(File source) throws FileNotFoundException {
        sc = new Scanner(source);
    }

    public SuperScanner(InputStream in) {
        sc = new Scanner(in);
    }

    public SuperScanner(Path source) throws IOException {
        sc = new Scanner(source);
    }

    public SuperScanner(Readable source) {
        sc = new Scanner(source);
    }

    public SuperScanner(ReadableByteChannel source) {
        sc = new Scanner(source);
    }

    public SuperScanner(String str) {
        sc = new Scanner(str);
    }

    public SuperScanner(File source, Charset charset) throws IOException {
        sc = new Scanner(source, charset);
    }

    public SuperScanner(File source, String charsetName) throws IOException {
        sc = new Scanner(source, charsetName);
    }

    public SuperScanner(ReadableByteChannel source, Charset charset) {
        sc = new Scanner(source, charset);
    }

    public SuperScanner(ReadableByteChannel source, String charsetName) {
        sc = new Scanner(source, charsetName);
    }

    /**
     * Close the scanner.
     */
    public void close() {
        sc.close();
    }

    // ******* Methods to obtain input ********
    // ******* String ********
    /**
     * Get a String from the user.
     * @param question - Question to show using System.out
     * @return Response given by the scanner.
     */
    public String getString(String question) {
        System.out.print(question);
        return sc.nextLine();
    }

    /**
     * Get a String from the user.
     * @param question - Question to show using System.out
     * @param minLen - min length of String
     * @param maxLen - max length of String
     * @return Response given by the scanner meeting the criteria.
     */
    public String getString(String question, int minLen, int maxLen) {
        String str;
        while (true) {
            str = getString(question);

            if (str.length() < minLen)
                System.out.println(getErrorMinLenString(minLen));
            else if (str.length() > maxLen)
                System.out.println(getErrorMaxLenString(maxLen));
            else
                return str;
        }
    }

    protected abstract String getErrorMinLenString(int minLen);
    protected abstract String getErrorMaxLenString(int minLen);

    /**
     * Get a String from the user that is in the given array.
     * @param question - Question to show using System.out
     * @param options - Array of Strings to compare the response with.
     * @return Response given by the scanner meeting the criteria.
     */
    public String getStringIn(String question, String[] options) {
        if (options.length == 0)
            throw new IllegalArgumentException(getErrorNoOptions());
        String str;
        question += " [" + String.join(", ", options) + "] ";
        while (true) {
            str = getString(question);
            for (String option : options)
                if (str.equals(option))
                    return str;
            System.out.println(getErrorInvalidOption());
        }
    }

    protected abstract String getErrorNoOptions();
    protected abstract String getErrorInvalidOption();

    public String getFileName(String question) {
        String str;
        while (true) {
            str = getString(question, 1, Integer.MAX_VALUE);
            if (new File(str).exists())
                return str;
            System.out.println(getErrorFileNotFound());
        }
    }

    protected abstract String getErrorFileNotFound();

    // ******* Integer ********
    /**
     * Get an int from the user.
     * @param question - Question to show using System.out.print
     * @return Integer given by Scanner
     */
    public int getInt(String question) {
        while (true) {
            try {
                System.out.print(question);
                return Integer.parseInt(sc.nextLine());
            }
            catch (NumberFormatException e) {
                System.out.println(getErrorNotInt());
            }
        }
    }

    protected abstract String getErrorNotInt();

    /**
     * Get a positive int from the user.
     * @param question - Question to show using System.out
     * @return Integer greater or equal to 0
     */
    public int getNatural(String question) {
        int n = 0;
        boolean isNotNatural = true;
        while (isNotNatural) {
            n = getInt(question);

            if (n >= 0)
                isNotNatural = false;
            else
                System.out.println(getErrorNotNatural());
        }
        return n;
    }

    protected abstract String getErrorNotNatural();

    /**
     * Get an int in the given range.
     * @param question - Question to show using System.out
     * @param min - min value of the desired int
     * @param max - max value of the desired int
     * @return Integer inside the interval [min, max]
     */
    public int getIntInRange(String question, int min, int max) {
        if (min > max) {
            int swap = min;
            min = max;
            max = swap;
        }

        int n = 0;
        boolean isNotValid = true;
        while (isNotValid) {
            n = getInt(question);

            if (n >= min && n <= max)
                isNotValid = false;
            else
                System.out.println(getErrorIntNotInRange(min, max));
        }
        return n;
    }

    protected abstract String getErrorIntNotInRange(int min, int max);


    // ******* Float ********
    /**
     * Get a float from the user.
     * @param question - Question to show using System.out.print
     * @return Float given by Scanner
     */
    public float getFloat(String question) {
        while (true) {
            try {
                System.out.print(question);
                return Float.parseFloat(sc.nextLine());
            }
            catch (NumberFormatException e) {
                System.out.println(getErrorNotFloat());
            }
        }
    }

    protected abstract String getErrorNotFloat();

    /**
     * Get a positive float from the user.
     * @param question - Question to show using System.out
     * @param min - min value of the desired int
     * @param max - max value of the desired int
     * @return Float inside the interval [min, max]
     */
    public float getFloatInRange(String question, float min, float max) {
        if (min > max) {
            float swap = min;
            min = max;
            max = swap;
        }

        float n = 0;
        boolean isNotValid = true;
        while (isNotValid) {
            n = getFloat(question);

            if (n >= min && n <= max)
                isNotValid = false;
            else
                System.out.println(getErrorFloatNotInRange(min, max));
        }
        return n;
    }

    protected abstract String getErrorFloatNotInRange(float min, float max);

    // ******* Implementations ********

    public static class En extends SuperScanner {

        // ******* Constructors (Same as original) *******

        public En(File source) throws FileNotFoundException {
            super(source);
        }

        public En(InputStream in) {
            super(in);
        }

        public En(Path source) throws IOException {
            super(source);
        }

        public En(Readable source) {
            super(source);
        }

        public En(ReadableByteChannel source) {
            super(source);
        }

        public En(String str) {
            super(str);
        }

        public En(File source, Charset charset) throws IOException {
            super(source, charset);
        }

        public En(File source, String charsetName) throws IOException {
            super(source, charsetName);
        }

        public En(ReadableByteChannel source, Charset charset) {
            super(source, charset);
        }

        public En(ReadableByteChannel source, String charsetName) {
            super(source, charsetName);
        }

        @Override
        protected String getErrorMinLenString(int minLen) {
            return String.format("The string must have at least %d characters.", minLen);
        }

        @Override
        protected String getErrorMaxLenString(int minLen) {
            return String.format("The string must have at most %d characters.", minLen);
        }

        @Override
        protected String getErrorNoOptions() {
            return "There are no options to choose from.";
        }

        @Override
        protected String getErrorInvalidOption() {
            return "The option is not valid.";
        }

        @Override
        protected String getErrorFileNotFound() {
            return "The file does not exist.";
        }

        @Override
        protected String getErrorNotInt() {
            return "The value is not a valid integer.";
        }

        @Override
        protected String getErrorNotNatural() {
            return "The number must be a natural -> [0, inf)";
        }

        @Override
        protected String getErrorIntNotInRange(int min, int max) {
            return String.format("The number must be an integer in the range [%d, %d]", min, max);
        }

        @Override
        protected String getErrorNotFloat() {
            return "The value is not a valid float.";
        }

        @Override
        protected String getErrorFloatNotInRange(float min, float max) {
            return String.format("The number must be a float in the range [%f, %f]", min, max);
        }
    }

    public static class Es extends SuperScanner {

        public Es(File source) throws FileNotFoundException {
            super(source);
        }

        public Es(InputStream in) {
            super(in);
        }

        public Es(Path source) throws IOException {
            super(source);
        }

        public Es(Readable source) {
            super(source);
        }

        public Es(ReadableByteChannel source) {
            super(source);
        }

        public Es(String str) {
            super(str);
        }

        public Es(File source, Charset charset) throws IOException {
            super(source, charset);
        }

        public Es(File source, String charsetName) throws IOException {
            super(source, charsetName);
        }

        public Es(ReadableByteChannel source, Charset charset) {
            super(source, charset);
        }

        public Es(ReadableByteChannel source, String charsetName) {
            super(source, charsetName);
        }

        @Override
        protected String getErrorMinLenString(int minLen) {
            return String.format("La cadena debe tener al menos %d caracteres.", minLen);
        }

        @Override
        protected String getErrorMaxLenString(int minLen) {
            return String.format("La cadena debe tener al menos %d caracteres.", minLen);
        }

        @Override
        protected String getErrorNoOptions() {
            return "No hay opciones para elegir.";
        }

        @Override
        protected String getErrorInvalidOption() {
            return "La opción no es válida.";
        }

        @Override
        protected String getErrorFileNotFound() {
            return "El archivo no existe.";
        }

        @Override
        protected String getErrorNotInt() {
            return "El valor no es un entero válido.";
        }

        @Override
        protected String getErrorNotNatural() {
            return "El número debe ser natural -> [0, inf)";
        }

        @Override
        protected String getErrorIntNotInRange(int min, int max) {
            return String.format("El número debe ser un entero en el rango [%d, %d]", min, max);
        }

        @Override
        protected String getErrorNotFloat() {
            return "El valor no es un float válido.";
        }

        @Override
        protected String getErrorFloatNotInRange(float min, float max) {
            return String.format("El número debe ser un float en el rango [%f, %f]", min, max);
        }
    }
}
