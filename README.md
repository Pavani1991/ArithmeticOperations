# ArithmeticOperations

### This class performs the arithmetic operations on the fractions. 
Arithmetic operations that we are supporting are:
* '+' adds two fractions
* '-' Subtracts two fractions
* '*' multiples two fractions
* '%' division of two fractions (using % here instead of / to differentiate division operator from fraction sign)

### below are some sample inputs and outputs: (using "%" here instead of "/" to differentiate division operator from fraction sign)
 * 1/2 * 3_3/4  ---> 1_7/8
 * 2_3/8 + 9/8 ---> 3_1/2
 * 5/2 + 3/2 * 5/6 + 7/8 % 2/3 --> 5_1/16
 * 5_1/2 * 5/6 % 2/3 --> 6_7/8
 * 5_1/2 + 5/6 + 2/3 -->  7
 * throws IllegalArgumentException if a fraction with denominator of zero is provided or computed denominator value is zero
 * 5_1/2 * 5/6 % 0/3 --> java.lang.IllegalArgumentException: denominator value should not be zero
 * 5_1/2 * 5/6 % 3/0 --> java.lang.IllegalArgumentException: denominator value should not be zero

### command to compile and run code after cloning
```cd ArithmeticOperations/src```<br>
```javac com/arithmeticOperations/ArithmeticOperations.java```<br>
```java com/arithmeticOperations/ArithmeticOperations```<br>
