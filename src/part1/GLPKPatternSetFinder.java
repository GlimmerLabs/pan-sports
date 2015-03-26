package part1;

import java.util.ArrayList;
import org.gnu.glpk.GLPK;
import org.gnu.glpk.GLPKConstants;
import org.gnu.glpk.GlpkException;
import org.gnu.glpk.SWIGTYPE_p_double;
import org.gnu.glpk.SWIGTYPE_p_int;
import org.gnu.glpk.glp_prob;
import org.gnu.glpk.glp_smcp;

public class GLPKPatternSetFinder
{
  
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
      glp_prob lp;
      glp_smcp parm;
      SWIGTYPE_p_int ind;
      SWIGTYPE_p_double val;
      int ret;
      try{
        lp = GLPK.glp_create_prob();
        System.out.println("Problem created");
        GLPK.glp_set_prob_name(lp, "myProblem");
        double objective = 0;
        while(objective < 1){
          for(Integer j : restrictedRows)
            b[j]++;
          
          GLPK.glp_add_cols(lp, size);
          for(int i = 1; i <= size; i ++){
            GLPK.glp_set_col_name(lp, i, "x" + i);
            GLPK.glp_set_col_kind(lp, i, GLPKConstants.GLP_BV);
            GLPK.glp_set_col_bnds(lp, 1, GLPKConstants.GLP_BV, 0, 1);
          }
          
          ind = GLPK.new_intArray(size);
          val = GLPK.new_doubleArray(size);
          
          GLPK.glp_add_rows(lp, 2*patternLength*size);
          double[] homeConstraintCoeff = new double[size];
          double[] awayConstraintCoeff = new double[size];
          
          for(int i = 0; i < patternLength; i++){
            for(int j = 0; j < size; j++){
              homeConstraintCoeff[j] = h[j][i];
              awayConstraintCoeff[j] = a[j][i];
            }
            
            //add terms
            GLPK.glp_set_row_name(lp, 1, "homeC1");
            GLPK.glp_set_row_bnds(lp, 1, GLPKConstants.GLP_FX, 4, 4);
            GLPK.intArray_setitem(ind, 1, 1);
            GLPK.intArray_setitem(ind, 2, 2);
            GLPK.doubleArray_setitem(val, 1, 1.);
            GLPK.doubleArray_setitem(val, 2, -.5);
            GLPK.glp_set_mat_row(lp, 1, 2, ind, val);
          }
          
          
        }
      }
      catch(Exception e){
        e.printStackTrace();
      }
      return patternSets;
  }
}
