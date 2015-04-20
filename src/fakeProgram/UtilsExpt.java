package fakeProgram;

import java.io.PrintWriter;

public class UtilsExpt
{

  public static void main(String[] args)
  throws Exception 
  {
    PrintWriter pen = new PrintWriter(System.out, true);
    //BigDecimal first = new BigDecimal(2) ;
    //BigDecimal approx = new BigDecimal (0.0000005) ;
    //BigDecimal answer =  Utils.sqrt(first, approx) ;
    Calculator calc = new Calculator();
    
    /*
    calc.evaluate("2 + 3");               // Output: 5
    calc.evaluate("2 + 3 * 4");           // Output: 20 (or 14)
    calc.evaluate("r0 = 2 + 3");          // Output: 5
    calc.evaluate("r0 + 2");              // Output: 7
    calc.evaluate("r0");                  // Output: 5
    calc.evaluate("r1 = r0 * r0");        // Output: 25
    System.out.println(calc.evaluate("r1 + r0 * 2").toString());         // Output 60 (or 35)
    */
   
    //pen.println(calc.eval("r1 = r0 * 2").toString());
    calc.userInterface();
    pen.close() ;
  } //main

}// class UtilsExpt
