package part1;

import java.util.ArrayList;
import java.util.Stack;

import net.sf.javailp.Problem;
public class HomeAwayGenerator
{

  /**
   * makePatterns - Takes a string of a sequence of HAB letters and generates all
   * desirable permutations of them. Returns them in an arraylist. Core permutation algorithm
   * is adapted from a python algorithm discussed at 
   * http://stackoverflow.com/questions/18227931/iterative-solution-for-finding-string-permutations
   * @param str
   * @return
   */
  public static ArrayList<String> makePatterns(String str)
  {
    //make a new Stack
    Stack<Character> stk = new Stack<Character>();
    //push all characters to the stack
    for (char c : str.toCharArray())
      stk.push(c);
    
    //create and initialize an arraylist of results
    ArrayList<String> results = new ArrayList<String>();
    results.add(stk.pop().toString());
    //while stack isn't empty
    while (stk.size() != 0)
      {
        //pop a character
        char c = stk.pop();
        //create a dummy results arraylist to pass perms into
        ArrayList<String> newResults = new ArrayList<String>();
        //for every string in the results
        for (String s : results)
          {
            //for every feasible position in the string,
            for (int i = 0; i <= s.length(); i++)
              {
                //put the popped character there
                String nextString = s.substring(0, i) + c + s.substring(i);
                if (!(newResults.contains(nextString)))
                  {
                    //put it in results if its not a duplicate
                    newResults.add(nextString);
                  }//if
              }//for
          }//for
        //put newResults back in results and wait for garbage collection to pick up newResults
        results = newResults;
      }//while
    
    //optionally mirror the sequences
    results = mirror(results);
    
    //filter results by putting good sequences in a new array
    ArrayList<String> resultsGoodSequence = new ArrayList<String>();
    for (String c : results)
      if (!(c.contains("HHH") || c.contains("AAA")))
        resultsGoodSequence.add(c);

    return resultsGoodSequence;
  }

  /**
   * mirror - Takes a list of sequences and mirrors them according to the
   * Neuheiser and Trick paper i.e. if slot i is H, then slot i + 9 mod 18
   * is A
   * @param list
   * @return
   */
  public static ArrayList<String> mirror(ArrayList<String> list)
  {
    ArrayList<String> mirrored = new ArrayList<String>();
    //for every string in the list
    for (String str : list)
      {
        //make a new string to ammend
        String strFinal = str;
        for (int i = 0; i < str.length(); i++)
          {
            //put the correct character on the end
            if(str.charAt(i) == 'H'){
              strFinal += "A";
            }
            else if(str.charAt(i) == 'A'){
              strFinal += "H";
            }
            else{
              strFinal += "B";
            }
              
          }
        mirrored.add(strFinal);
      }
    return mirrored;
  }

  public static void main(String args[])
  {
    ArrayList<String> results = makePatterns("HHHHBAAAA");
    //results = mirror(results);
    
    for (String i : results)
      {
        System.out.println(i);
      }
  }

}
