package fakeProgram;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Utils
{

  public static BigDecimal sqrt(BigDecimal n, BigDecimal epsilon)
  {
    MathContext mc = new MathContext(10, RoundingMode.HALF_EVEN); //change 10 to epsilon
    PrintWriter pen = new PrintWriter(System.out, true);
    BigDecimal a = BigDecimal.ONE;
    BigDecimal average = n;
    BigDecimal smaller = ((a.multiply(a)).subtract(n)).abs();

    while (smaller.compareTo(epsilon) == 1)
      {
        average = (a.add(n.divide(a, mc))).divide(new BigDecimal(2), mc);
        pen.println("average = " + average + " a = " + a);
        smaller = ((average.multiply(average)).subtract(n)).abs(); //a2 - n
        a = average;
      }//while

    //Citation Needed from StackOverflow - Harry has it
    return a;

  }//sqrt


  public static double recurExpt(double x, int p)
  {
    //Base case: When p = 0, result is 1
    if (p == 0)
      {
        return 1;
      } // if p is 0
        //Base case: When p = 1, result is x
    else if (p == 1)
      {
        return x;
      } // if p is 1
        //Recursive case: When p is 2k, x^(2k) = (x^k) * (x^k)
    else if (p % 2 == 0)
      {
        double tmp = recurExpt(x, p / 2);
        return tmp * tmp;
      } // if p is even
        //Recursive case: When p is odd, result is x*(x^(p-1))
    else
      {
        return recurExpt(x * x, (p - 1) / 2);
      } // if p is odd
  } // expt(double,int)
  
  
  public static double expt(double x, int p)
  {
    double result = 1.0;

    for (int i = 0; i < (p / 2); i++)
      {
        result *= x;
        System.out.println("result: "+result);
      }//for

    result *= result;

    if (p % 2 != 0)
      {
        result *= x;
      }//if
    
    return result;
  }//double expt(double x, double p)
}//class
