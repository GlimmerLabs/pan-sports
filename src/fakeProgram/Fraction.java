package fakeProgram;

import java.math.BigInteger;

public class Fraction
{
  
  /**
   * An implementation of a fraction class.
   *
   * @author Zoe Wolter and Nick Knoebber (From HW4)
   * Expanded by Harry Baker Nick Knoebber and Ajuna Kyaruzi for Homework 5
   *
   */
  
  //+------------------+---------------------------------------------
  // | Design Decisions |
  // +------------------+
  /*
  * (1) Denominators are always positive. Therefore, negative fractions are
  * represented with a negative numerator. Similarly, if a fraction has a
  * negative numerator, it is negative.
  *
  * (2) Fractions are not necessarily stored in simplified form. To obtain a
  * fraction in simplified form, one must call the simplify method.
  */

  // +--------+-------------------------------------------------------
  // | Fields |
  // +--------+
  /** The numerator of the fraction. Can be positive, zero or negative. */
  BigInteger num;
  /** The denominator of the fraction. Must be non-negative. */
  BigInteger denom;

  // +--------------+-------------------------------------------------
  // | Constructors |
  // +--------------+
  /**
  * Build a new fraction with numerator num and denominator denom.
  *
  * Warning! Not yet stable.
  */
  public Fraction(BigInteger num, BigInteger denom)
  {
    BigInteger gcd = num.gcd(denom);
    num = num.divide(gcd);
    denom = denom.divide(gcd);

    if (num.equals(0))
      {
        this.denom = BigInteger.valueOf(1);
      } // if
    else if (denom.compareTo(BigInteger.valueOf(0)) == -1)
      {
        this.num = num.negate();
        this.denom = denom.negate();
      } // else if
    else
      {
        this.num = num;
        this.denom = denom;
      } // else   
  } // Fraction(BigInteger, BigInteger)

  /**
  * Build a new fraction with numerator num and denominator denom.
  */
  public Fraction(int num, int denom)
  {
    this(BigInteger.valueOf(num), BigInteger.valueOf(denom));
  } // Fraction(int, int)

  /**
   * Build a new fraction based on a string of numerals
   */
  public Fraction(String str)
  {
    int divisorIndex = str.indexOf("/");
    int iNum;
    int iDenom;
    if (divisorIndex > 0)
      {
        iNum = Integer.valueOf(str.substring(0, divisorIndex));
        iDenom = Integer.valueOf(str.substring(divisorIndex + 1));
      } // if
    else
      {
        iNum = Integer.valueOf(str);
        iDenom = 1;
      } // else
    this.num = BigInteger.valueOf(iNum);
    this.denom = BigInteger.valueOf(iDenom);

    BigInteger gcd = this.num.gcd(this.denom);

    this.num = this.num.divide(gcd);
    this.denom = this.denom.divide(gcd);

    if (this.num.equals(0))
      {
        this.denom = BigInteger.valueOf(1);
      }// if
    else if (this.denom.compareTo(BigInteger.valueOf(0)) == -1)
      {
        this.num = this.num.negate();
        this.denom = this.denom.negate();
      } // else if

  } // Fraction(String)

  // +---------+------------------------------------------------------
  // | Methods |
  // +---------+

  /**
  * Express this fraction as a double.
  */
  public double doubleValue()
  {
    return this.num.doubleValue() / this.denom.doubleValue();
  } // doubleValue()

  /**
  * Add the fraction other to this fraction.
  */
  public Fraction add(Fraction addMe)
  {
    BigInteger resultNumerator;
    BigInteger resultDenominator;
    // The denominator of the result is the
    // product of this object's denominator
    // and addMe's denominator
    resultDenominator = this.denom.multiply(addMe.denom);
    // The numerator is more complicated
    resultNumerator =
        (this.num.multiply(addMe.denom)).add(addMe.num.multiply(this.denom));
    // Return the computed value
    return new Fraction(resultNumerator, resultDenominator);
  }// add(Fraction)

  /**
   * Returns the additive inverse of the original fraction
   */
  public Fraction negate()
  {
    return new Fraction(this.num.negate(), this.denom);
  } // negate()

  /**
   * Returns a fraction that is the original fraction and the multiplicand
   * multiplied together
   */
  public Fraction multiply(Fraction multiplicand)
  {
    return new Fraction(this.num.multiply(multiplicand.num),
                        this.denom.multiply(multiplicand.denom));
  } // multiply(Fraction)

  /**
   * Returns a fraction that results from subtracting the subtrahend from the
   * original fraction
   */
  public Fraction subtract(Fraction subtrahend)
  {
    this.num = this.num.multiply(subtrahend.denom);
    subtrahend.num = subtrahend.num.multiply(this.denom);

    this.denom = this.denom.multiply(subtrahend.denom);
    subtrahend.denom = this.denom;

    return new Fraction(this.num.subtract(subtrahend.num), this.denom);
  } // subtract(Fraction)

  /**
   * Returns a fraction that results from dividing the fraction by the divisor. 
   */
  public Fraction divide(Fraction divisor)
  {
    return new Fraction(this.num.multiply(divisor.denom),
                        this.denom.multiply(divisor.num));
  } // divide(Fraction)

  /**
   * Returns a fraction that results from raising the given fraction to the 
   * given exponent, which may be positive, negative, or zero. 
   */
  public Fraction pow(int expt)
  {
    if (expt < 0)
      {
        Fraction newFrac = new Fraction("1");
        newFrac = newFrac.divide(this.pow(expt * -1));
        return newFrac;
      } // if
    return new Fraction(this.num.pow(expt), this.denom.pow(expt));
  } // pow(int)

  /**
  * Convert this fraction to a string for ease of printing.
  */
  public String toString()
  {
    // Lump together the string represention of the numerator,
    // a slash, and the string representation of the denominator
    // return this.num.toString().concat("/").concat(this.denom.toString());
    return this.num + "/" + this.denom;
  } // toString()
}//class Fraction
