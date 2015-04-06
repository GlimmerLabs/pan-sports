package part1;

import java.util.ArrayList;

import org.gnu.glpk.GLPK;
import org.gnu.glpk.GLPKConstants;
import org.gnu.glpk.GlpkException;
import org.gnu.glpk.SWIGTYPE_p_double;
import org.gnu.glpk.SWIGTYPE_p_int;
import org.gnu.glpk.glp_iocp;
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
    	   GLPK.glp_set_col_bnds(lp, i, GLPK.GLP_DB, 0, 1);
    	   GLPK.glp_set_obj_coef(lp, i, b[i-1]);
    	   GLPK.glp_set_col_kind(lp, i, GLPK.GLP_BV);
       }//for
       
       int total_size = (((2 * patternLength) + 1) * size) ;
       SWIGTYPE_p_int ia = GLPK.new_intArray(total_size);
       SWIGTYPE_p_int ja = GLPK.new_intArray(total_size);
       SWIGTYPE_p_double ar = GLPK.new_doubleArray(total_size);
       int count = 1;
       int iCount = 1;
       int jCount = 1;
       for (int i = 1; i <= 2 * patternLength + 1 ; i++) {
    	   for (int j = 1 ; j <= size ; j++)
    	   {
    		   GLPK.intArray_setitem(ia, iCount++, i);
    		   GLPK.intArray_setitem(ja, jCount++, j);
    		   double value = 1 ;
    		   if ( i <= patternLength )
    		   {
    			   
    			   value = h[j-1][i-1];
    			   
    		   }
    		   else if (i <= 2 * patternLength)
    		   {
    			   value = a[j-1][i-patternLength-1];
    		   }
    		   GLPK.doubleArray_setitem(ar, count++, value);
    	   }//for
       }//for
       //glp_iocp parm = GLPK.GLP_ON;
       GLPK.glp_load_matrix(lp, total_size, ia, ja, ar);
      // GLPK.glp_init_iocp(parm);
       GLPK.glp_simplex(lp, null);
       int ret = GLPK.glp_intopt(lp, null);
        
    // Retrieve solution
       if (ret == 0)
         {
           write_lp_solution(lp);
         }
       else
         {
           System.out.println("The problem could not be solved");
         }
       // Free memory
       GLPK.glp_delete_prob(lp);
      }//try
      catch(Exception e){
        e.printStackTrace();
      }
      return patternSets;
  }
  
  /**
   * write simplex solution
   * @param lp problem
   */
   static void write_lp_solution(glp_prob lp)
   {
     int i;
     int n;
     String name;
     double val;
     double dval;
     int ival;
     name = GLPK.glp_get_obj_name(lp);
     val = GLPK.glp_get_obj_val(lp);
     System.out.print(name);
     System.out.print(" = ");
     System.out.println(val);
     n = GLPK.glp_get_num_cols(lp);
     for (i = 1; i <= n; i++)
       {
         name = GLPK.glp_get_col_name(lp, i);
         System.out.print(name);
         System.out.print(" = ");
         int kind = GLPK.glp_get_col_kind(lp, i);
         dval = GLPK.glp_mip_col_val(lp, i);
         if ((kind == GLPK.GLP_BV) || (kind == GLPK.GLP_IV)) {
        	 System.out.print ("integer ");
        	 ival = (int) dval;
        	 System.out.println(ival);
         }
         else {
        	 System.out.print ("other ");
        	 System.out.println(dval);
         }
       }
   }
   
   public static void main(String[] arg)
   {
	 //  public static ArrayList<Integer[]> findPatternSet(ArrayList<String> patterns)
	   findPatternSet(HomeAwayGenerator.makePatterns("HHHHBAAAA"));
   }
}
