package com.arithmeticOperations;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class performs the arithmetic operations on the fractions.
 * Arithmetic operations that we are supporting are:
 * + adds two fractions
 * - Subtracts two fractions
 * * multiples two fractions
 * % division of two fractions (using % here instead of / to differentiate division operator from fraction sign)
 * Approach:
 * Approach is to split the input string initially for Addition and Subtraction operators.
 * further split this substring for multiply and division operators.
 * compute results for multiply and division operations since they have high arithmetic precedence.
 * and continue computing results for Addition and Subtraction Operators since they have low precedence.
 * Addition & Subtraction: I am trying to make the denominator same by finding LCM of all denominators and multiplying that number to each denominator.
 * Multiplication: multiplying two numerators and denominators
 * Division: Multiple numerator and denominator with reciprocal of second numerator and denominator.
 * below are some sample inputs and outputs: (using "%" here instead of "/" to differentiate division operator from fraction sign)
 * * 1/2 * 3_3/4  ---> 1_7/8
 * * 2_3/8 + 9/8 ---> 3_1/2
 * 5/2 + 3/2 * 5/6 + 7/8 % 2/3 --> 5_1/16
 * 5_1/2*5/6%2/3 --> 6_7/8
 * 5_1/2+5/6+2/3 -->  7
 * 5_1/2*5/6%0/3 --> java.lang.IllegalArgumentException: denominator value should not be zero
 * 5_1/2*5/6%3/0 --> java.lang.IllegalArgumentException: denominator value should not be zero
 * throws IllegalArgumentException if a fraction with denominator of zero is provided or computed denominator value is zero
 * @author Pavani Gorantla
 */
class ArithmeticOperations {

    public static void main(final String[] args) {
        System.out.println("Enter input String... ");
        // Using Scanner for Getting Input from User
        final Scanner in = new Scanner(System.in);
        final String s = in.nextLine();
        System.out.println("You entered string " + s);
        final ArithmeticOperations operations = new ArithmeticOperations();
        System.out.println(operations.fractionAddition(s));
    }

    /**
     * This method computes numerator and denominator of a provided fraction including mixed fraction
     * @param expression is the fraction value for which numerator and denominator needs to be computed
     * @return an array of size 2 with numerator and denominator details.
     */
    private int[] computeNumeratorAndDenominotorOfFraction(String expression){
        int wholeNumberInfraction = 0;
        if(expression.split("_").length > 1) {
            final String[] expressions = expression.split("_");
            wholeNumberInfraction = Integer.parseInt(expressions[0].trim());
            expression = expressions[1].trim();
        }
        final String[] fractions = expression.split("/");
        int numerator = Integer.parseInt(fractions[0]);
        if(numerator == 0) {
            return new int[] {0,1};
        }
        int denominator = 1;
        if(!fractions[1].trim().isEmpty()) {
            denominator = Integer.parseInt(fractions[1]);
            if(denominator == 0) {
                throw new IllegalArgumentException("denominator value should not be zero");
            }
        }
        numerator += wholeNumberInfraction*denominator;
        return new int[] {numerator,denominator};
    }

    /**
     * This method process the multiplications and division on fraction values.
     * @param expression is the string of fraction values for which multiplication and division values needs to be computed
     * @return a fraction value in array of size 2 with numerator and denominator details.
     */
    private int[] processMultiplicationAndDivision(final String expression) {
        final List<Character> sign = new ArrayList<>();
        // Sign list for the operators * and % in same order. as we find them in input.
        for (int i = 1; i < expression.length(); i++) {
            if (expression.charAt(i) == '*' || expression.charAt(i) == '%') {
                sign.add(expression.charAt(i));
            }
        }
        final List<Integer> num = new ArrayList<>();
        final List<Integer> den = new ArrayList<>();
        for (final String sub : expression.split("[*%]")) {
            if (sub.length() > 0) {
                final int[] numeratorAndDenominotor = computeNumeratorAndDenominotorOfFraction(sub.trim());
                num.add(numeratorAndDenominotor[0]);
                den.add(numeratorAndDenominotor[1]);
            }
        }
        return computeMultiplicationAndDivision(sign, num, den);
    }
    // calculate num and den from the given list and apply multiply and div operators.
    private int[] computeMultiplicationAndDivision(final List<Character> sign, final List<Integer> numerators, final List<Integer> denominators) {
        int num = numerators.get(0);
        int den = denominators.get(0);
        for (int i = 1; i < numerators.size(); i++) {
            if (sign.get(i - 1) == '*') {
                num *= numerators.get(i);
                den *= denominators.get(i);
            } else if (sign.get(i - 1) == '%') {
                num *= denominators.get(i);
                den *= numerators.get(i);
            }
        }
        return new int[]{num, den};
    }
    // calculate num and den from the given list and apply add and subtract operators.
    private int[] computeAdditionAndSubtraction(final List<Character> sign, final List<Integer> numerators, final List<Integer> denominators) {
        int lcm = 1;
        for (final int x : denominators) {
            if(x == 0) {
                throw new IllegalArgumentException("denominator value should not be zero");
            }
            lcm = lcm(lcm, x);
        }
        int res = (lcm / denominators.get(0)) * numerators.get(0);
        for (int i = 1; i < numerators.size(); i++) {
            if (sign.get(i - 1) == '+') {
                res += lcm / denominators.get(i) * numerators.get(i);
            } else if (sign.get(i - 1) == '-') {
                res -= lcm / denominators.get(i) * numerators.get(i);
            }
        }
        return new int[] {res, lcm};
    }
    // generate final result in form of a string. if numerator is greater than denominator which is a improper fraction, convert it into mixed number or return whole number.
    private String computeResult(final int[] result) {
        final int value = result[0];
        final int lcm = result[1];
        final int g = gcd(Math.abs(value), Math.abs(lcm));
        final Integer numerator = value / g;
        final Integer denominator = lcm / g;
        // Convert improper fraction to Mixed Number and return that result.
        if (numerator > denominator) {
            final Integer wholeNumber = numerator / denominator;
            final Integer reminder = numerator%denominator;
            if(reminder == 0) {
                return wholeNumber.toString();
            }
            return wholeNumber+"_"+ "" + reminder + "/" +denominator;
        }
        return (value / g) + "/" + (lcm / g);
    }

    private String fractionAddition(final String expression) {
        final List<Character> sign = new ArrayList<>();
        // maintaining the list of operators (+,-) as they come in the input stream.
        for (int i = 1; i < expression.length(); i++) {
            if (expression.charAt(i) == '+' || expression.charAt(i) == '-') {
                sign.add(expression.charAt(i));
            }
        }
        final List<Integer> num = new ArrayList<>();
        final List<Integer> den = new ArrayList<>();
        // split given input with regex pattern.
        // approach is to split the input string initially for operators (+,-), and split further for operators (* and /)
        // and compute results for * and / first since they have high arithmetic precedence.
        final String pattern = "[-+]";
        for (final String sub : expression.split(pattern)) {
            if (sub.split("[*%]").length > 1) {
                final int[] numAndDen = processMultiplicationAndDivision(sub);
                num.add(numAndDen[0]);
                den.add(numAndDen[1]);
            } else {
                if (sub.length() > 0) {
                    final int[] numAndDen = computeNumeratorAndDenominotorOfFraction(sub.trim());
                    num.add(numAndDen[0]);
                    den.add(numAndDen[1]);
                }
            }
        }
        if (expression.charAt(0) == '-') {
            num.set(0, -num.get(0));
        }
        final int[] result = computeAdditionAndSubtraction(sign, num, den);
        return computeResult(result);
    }
    // finding the Least common multiples of two given numbers.
    private int lcm(final int a, final int b) {
        return a * b / gcd(a, b);
    }
    // finding greatest common divisor for two given numbers.
    private int gcd(int a, int b) {
        while (b != 0) {
            final int t = b;
            b = a % b;
            a = t;
        }
        return a;
    }
}