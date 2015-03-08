package part1;

import java.util.ArrayList;
import java.util.Arrays;

import gurobi.*;

public class PatternSetFinder
{
  /**
   * Use integer programming to put the home away patterns into sets of 
   * patterns.
   * 
   * @param patterns: an ArrayList of home away patterns
   * 
   * @return An ArrayList of indexes that are feasible patterns.
   * Stored in integer arrays 
   */
  public static ArrayList<Integer[]> findPatternSet(ArrayList<String> patterns)
  {
    //Number of patterns available
    int size = patterns.size();
    //length of each pattern
    int patternLength = patterns.get(0).length();

    double[][] h = new double[size][patternLength];
    double[][] a = new double[size][patternLength];
    double[] b = new double[size];
    double[] coeff = new double[size];

    ArrayList<Integer> restrictedRows = new ArrayList<Integer>();
    ArrayList<Integer[]> patternSets = new ArrayList<Integer[]>();

    for (int i = 0; i < size; i++)
      {
        //find bad patterns that we don't want to use
        if(patterns.get(i).substring(0, 2).equals("AA") || patterns.get(i).substring(0, 3).equals("BAA")
            ||patterns.get(i).substring(16, 18).equals("AA")){
              b[i] = 1;
        }
        else{
          b[i] = 0;
        }

        coeff[i] = 1;
        for (int j = 0; j < patternLength; j++)
          {

            char current = patterns.get(i).charAt(j);

            if (current == 'H')
              {
              h[i][j] = 1;
              a[i][j] = 0;
              }
            else if (current == 'A')
              {
              h[i][j] = 0;
              a[i][j] = 1;
              }
            else
              {
                h[i][j] = 0;
                a[i][j] = 0;
              }
          }
      }//for

    try
    {
      GRBEnv env = new GRBEnv("schedule.log");
      GRBModel model = new GRBModel(env);
      model.getEnv().set(GRB.IntParam.OutputFlag, 0);
      int count = 0;
      double objective = 0;
      while(objective < 1){
        for(Integer j : restrictedRows)
          b[j]++;


        GRBVar[] x = new GRBVar[size];
        for(int i = 0; i < size; i++)
          x[i] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "x" + i);


        model.update();

        GRBLinExpr expr = new GRBLinExpr();
        expr.addTerms(b, x);
        model.setObjective(expr, GRB.MINIMIZE);

        double[] homeConstraintCoeff = new double[size];
        double[] awayConstraintCoeff = new double[size];

        for(int i = 0; i < patternLength; i++){
          GRBLinExpr homeExpr = new GRBLinExpr();
          GRBLinExpr awayExpr = new GRBLinExpr();

          for(int j = 0; j < size; j++){
            homeConstraintCoeff[j] = h[j][i];
            awayConstraintCoeff[j] = a[j][i];
          }//for

          homeExpr.addTerms(homeConstraintCoeff, x);
          awayExpr.addTerms(awayConstraintCoeff, x);

          model.addConstr(homeExpr, GRB.EQUAL, 4, "homeC" + i);
          model.addConstr(awayExpr, GRB.EQUAL, 4, "awayC" + i);
        }//for

        GRBLinExpr numTeamsConstraint = new GRBLinExpr();
        numTeamsConstraint.addTerms(coeff, x);
        model.addConstr(numTeamsConstraint, GRB.EQUAL, 9, "numTeamsC");
        int[] used = new int[9];
        model.optimize();
        int index = 0;
        for(int i = 0; i < size; i++){

          if(x[i].get(GRB.DoubleAttr.X) == 1.0)
            {
              //System.out.println(patterns.get(i) + " " + i);
              restrictedRows.add(i);
              used[index++] = i;
            }//if
        }// for

        //In order to add the array as Integers instead of ints
        Integer[] rows = new Integer[used.length];
        for(int n = 0; n < used.length; n++)
          rows[n] = used[n];


        patternSets.add(rows);  
        objective = model.get(GRB.DoubleAttr.ObjVal);
      }//while

      model.dispose();
      env.dispose();

    }//try 
    catch (GRBException e)
    {
      e.printStackTrace();
    }
    return patternSets;
  }

  public static void main(String args[])
  {
    long startTime = System.currentTimeMillis();
    ArrayList<String> patterns = HomeAwayGenerator.makePatterns("HHHHBAAAA");
    ArrayList<Integer[]> patternSets = findPatternSet(patterns);
    long endTime = System.currentTimeMillis();
    System.out.println("Found "+patternSets.size()+" pattern sets" + " in "+(endTime - startTime)+"ms");
    /*
    for(Integer[] set: patternSets)
      System.out.println(Arrays.toString(set)); 
      */
  }
}
