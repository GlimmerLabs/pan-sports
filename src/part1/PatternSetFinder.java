package part1;

import java.util.ArrayList;

import gurobi.*;

public class PatternSetFinder
{
  public static void findPatternSet(ArrayList<String> patterns)
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

    for (int i = 0; i < size; i++)
      {
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
      }
    
    try
      {
        GRBEnv env = new GRBEnv("schedule.log");
        GRBModel model = new GRBModel(env);
        
        int count = 0;
        double objective = 0;
        while(objective < 1){
          for(Integer j : restrictedRows){
            b[j]++;
          }
          
          GRBVar[] x = new GRBVar[size];
          for(int i = 0; i < size; i++){
            x[i] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "x" + i);
          }
        
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
            }
          
            homeExpr.addTerms(homeConstraintCoeff, x);
            awayExpr.addTerms(awayConstraintCoeff, x);
          
            model.addConstr(homeExpr, GRB.EQUAL, 4, "homeC" + i);
            model.addConstr(awayExpr, GRB.EQUAL, 4, "awayC" + i);
          }
        
          GRBLinExpr numTeamsConstraint = new GRBLinExpr();
          numTeamsConstraint.addTerms(coeff, x);
          model.addConstr(numTeamsConstraint, GRB.EQUAL, 9, "numTeamsC");
          
          model.optimize();
          for(int i = 0; i < size; i++){
            int[] used = new int[9];
            int index = 0;
            if(x[i].get(GRB.DoubleAttr.X) == 1.0){
              System.out.println(patterns.get(i) + " " + i);
              used[index++] = i;
            }  
            for(int j: used)
              restrictedRows.add(j);
          }
          objective = model.get(GRB.DoubleAttr.ObjVal);
        }
        
        model.dispose();
        env.dispose();
        
      }
    catch (GRBException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
  }

  public static void main(String args[])
  {
    ArrayList<String> patterns = HomeAwayGenerator.makePatterns("HHHHBAAAA");
    findPatternSet(patterns);
  }
}
