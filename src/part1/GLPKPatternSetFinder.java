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
      try{
        lp = GLPK.glp_create_prob();
        System.out.println("Problem created");
        GLPK.glp_set_prob_name(lp, "myProblem");
        GLPK.glp_set_obj_dir(lp, GLPK.GLP_MIN);
        GLPK.glp_add_rows(lp, 2 * patternLength + 1);
        
        //Setting the names and bounds for each constraint
        for (int i = 1 ; i <= patternLength; i++)
        {
        	GLPK.glp_set_row_name(lp, i, "H"+i);
        	GLPK.glp_set_row_bnds(lp, i, GLPK.GLP_FX, 4, 4);
        	GLPK.glp_set_row_name(lp, i + patternLength, "A"+(i+patternLength));
        	GLPK.glp_set_row_bnds(lp, i + patternLength, GLPK.GLP_FX, 4, 4);
        }//for
       
        //The last one (based on b)
       GLPK.glp_set_row_name(lp, 2 * patternLength + 1, "Team Constraints");
       GLPK.glp_set_row_bnds(lp, 2 * patternLength + 1, GLPK.GLP_FX, 9, 9);
       
       
       GLPK.glp_add_cols(lp, size);
       
       //Setting the names and bounds for each equation
       for (int i = 1 ; i <= size; i++)
       {
    	   GLPK.glp_set_col_name(lp, i, "x"+i);
    	   GLPK.glp_set_col_bnds(lp, i, GLPK.GLP_BV, 0, 1);
    	   GLPK.glp_set_obj_coef(lp, i, b[i-1]);
       }//for
       
       int total_size = (((2 * patternLength) + 1) * size) + 1 ;
       SWIGTYPE_p_int ia = GLPK.new_intArray(total_size);
       SWIGTYPE_p_int ja = GLPK.new_intArray(total_size);
       SWIGTYPE_p_double ar = GLPK.new_doubleArray(total_size);
       int count = 1;
       for (int i = 1; i <= 2 * patternLength + 1 ; i++) {
    	   for (int j = 1 ; j <= size ; j++)
    	   {
    		   ia[i] = i;
    		   ja[j] = j;
    		   SWIGTYPE_p_double value = 1 ;
    		   if ( i <= patternLength )
    		   {
    			   value = h[i][j];
    			   
    		   }
    		   else if (i <= 2 * patternLength)
    		   {
    			   value = a[i][j];
    		   }
    		   ar[count++]=value; 
    	   }//for
       }//for
       
       GLPK.glp_load_matrix(lp, total_size, ia, ja, ar);
       GLPK.glp_simplex(lp, null);
        
      }//try
      catch(Exception e){
        e.printStackTrace();
      }
      return patternSets;
  }
}
