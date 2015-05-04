package part1;

import java.util.ArrayList;
import java.util.Arrays;

import org.gnu.glpk.GLPK;
import org.gnu.glpk.SWIGTYPE_p_double;
import org.gnu.glpk.SWIGTYPE_p_int;
import org.gnu.glpk.glp_prob;

public class GLPKTimeTableFinder
{
  
  static int numTeams;
  static int numDates;
  static ArrayList<Integer[]> patternSets;
  static ArrayList<String> patterns;
  
  public GLPKTimeTableFinder(ArrayList<Integer[]> ps, ArrayList<String> pats){
    patternSets = ps;
    patterns = pats;
    numTeams = patternSets.get(0).length;
    numDates = patterns.get(0).length();
  }
  
  public boolean getTimeTable(int index){

    if(index > patternSets.size())
      return false;
    else{
      ArrayList<String> patternSet = new ArrayList<String>();
      for(int i = 0; i < patternSets.get(index).length; i++){
        patternSet.add(patterns.get(patternSets.get(index)[i]));
      }
      
      glp_prob lp;
      try{
        lp = GLPK.glp_create_prob();
        System.out.println("Problem created");
        GLPK.glp_set_prob_name(lp, "myProblem");
        GLPK.glp_set_obj_dir(lp, GLPK.GLP_MIN);
        int rowNumHelper = numTeams * (numTeams - 1);
        GLPK.glp_add_rows(lp, 1 + rowNumHelper + rowNumHelper*numDates + rowNumHelper*(numDates/2));
        int i = 1;
        //set first row bound (invalid ijk combo)
        GLPK.glp_set_row_name(lp, i, "Invalid ijk");
        GLPK.glp_set_row_bnds(lp, i++, GLPK.GLP_FX, 0, 0);
        
        //set next set (one ij)
        for(; i <= (1 + rowNumHelper); i++){
          GLPK.glp_set_row_name(lp, i, "oneIJ" + i);
          GLPK.glp_set_row_bnds(lp, i, GLPK.GLP_FX, 1, 1);
        }
        
        //set third set (ij + ji <= 1
        for(; i <= (1 + rowNumHelper + rowNumHelper*numDates); i++){
          GLPK.glp_set_row_name(lp, i, "IJJI" + i);
          GLPK.glp_set_row_bnds(lp, i, GLPK.GLP_DB, 0, 1);
        }
        
        //set fourth set (mirroring)
        for(; i <= (1 + rowNumHelper + rowNumHelper*numDates + rowNumHelper*(numDates/2)); i++){
          GLPK.glp_set_row_name(lp, i, "Mirror" + i);
          GLPK.glp_set_row_bnds(lp, i, GLPK.GLP_FX, 0, 0);
        }
        
        int size = numTeams*numTeams*numDates + 1;
        GLPK.glp_add_cols(lp, size);
        for (i = 1 ; i <= size; i++)
          {
            GLPK.glp_set_col_name(lp, i, "x"+i);
            GLPK.glp_set_col_bnds(lp, i, GLPK.GLP_DB, 0, 1);
            GLPK.glp_set_obj_coef(lp, i, 1);
            GLPK.glp_set_col_kind(lp, i, GLPK.GLP_BV);
          }//for
        
        ArrayList<Integer[]> ijCouples = new ArrayList<Integer[]>();
        for(int a = 0; a < numTeams; a++){
          for(int b = 0; b < numTeams; b++){
            if(a != b){
              Integer[] couple = {a, b};
              ijCouples.add(couple);
            }
          }
        }
        
//        for(Integer[] couple: ijCouples){
//          System.out.println(Arrays.toString(couple));
//        }
        
        int total_size = (1 + rowNumHelper + rowNumHelper*numDates + rowNumHelper*(numDates/2)) * (size - 1) + 1;
        System.out.println("Total size = " + total_size);
        SWIGTYPE_p_int ia = GLPK.new_intArray(total_size);
        SWIGTYPE_p_int ja = GLPK.new_intArray(total_size);
        SWIGTYPE_p_double ar = GLPK.new_doubleArray(total_size);
        int count = 1;
        int iCount = 1;
        int jCount = 1;
        
        System.out.println("starting");
        //invalid ijk combo
        for(i = 0; i < numTeams; i++){
          for(int j = 0; j < numTeams; j++){
            for(int k = 0; k < numDates; k++){
              //System.err.println("i: " + i + ", j: " + j + ", k: " + k);
              //System.err.println("iCount: " + iCount + "jCount: " + jCount + "rowCnt: " + 1);
              GLPK.intArray_setitem(ja, jCount++, ijkToIndex(i, j, k) + 1);
              GLPK.intArray_setitem(ia, iCount++, 1);
              if(i == j){
                GLPK.doubleArray_setitem(ar, count++, 1);
              }
              else{
                GLPK.doubleArray_setitem(ar, count++, 0);
              }
            }
          }
        }
        
        System.out.println("part 2");
        int rowCnt = 2;
        //one ij
        //for(; rowCnt <= 1 + rowNumHelper; rowCnt++){
          for(Integer[] couple: ijCouples){
            for(i = 0; i < numTeams; i++){
              for(int j = 0; j < numTeams; j++){
                for(int k = 0; k < numDates; k++){
                  //System.err.println("i: " + i + ", j: " + j + ", k: " + k);
                  //System.err.println("iCount: " + iCount + "jCount: " + jCount + "rowCnt: " + rowCnt);
                  GLPK.intArray_setitem(ja, jCount++, ijkToIndex(i, j, k) + 1);
                  GLPK.intArray_setitem(ia, iCount++, rowCnt);
                  if(i == couple[0] && j == couple[1]){
                    GLPK.doubleArray_setitem(ar, count++, 1);
                  }//if
                  else{
                    GLPK.doubleArray_setitem(ar, count++, 0);
                  }//else
                }//for k
              }//for j 
            }//for i
            rowCnt++;
          }// for each
        //}//for row
        System.out.println("part 3");

        for(int k2 = 0; k2 < numDates; k2++){
          for(Integer[] couple: ijCouples){
            for(i = 0; i < numTeams; i++){
              for(int j = 0; j < numTeams; j++){
                for(int k = 0; k < numDates; k++){
                  //System.err.println("i: " + i + ", j: " + j + ", k: " + k);
                  //System.err.println("iCount: " + iCount + "jCount: " + jCount + "rowCnt: " + rowCnt);
                  GLPK.intArray_setitem(ja, jCount++, ijkToIndex(i, j, k) + 1);
                  GLPK.intArray_setitem(ia, iCount++, rowCnt);
                  if((i == couple[0] && j == couple[1]) && k2 == k){
                    GLPK.doubleArray_setitem(ar, count++, 1);
                  }//if
                  else if((i == couple[1] && j == couple[0]) && k2 == k){
                    GLPK.doubleArray_setitem(ar, count++, 1);
                  }//if
                  else{
                    GLPK.doubleArray_setitem(ar, count++, 0);
                  }//else
                }//for k
              }//for j
            }//for i
            rowCnt++;
          }//for in
        }//for k2
        System.out.println("part 4");
        for(int k2 = 0; k2 < numDates/2; k2++){
          for(Integer[] couple: ijCouples){
            for(i = 0; i < numTeams; i++){
              for(int j = 0; j < numTeams; j++){
                for(int k = 0; k < numDates; k++){
                  //System.err.println("i: " + i + ", j: " + j + ", k: " + k);
                  //System.err.println("iCount: " + iCount + "jCount: " + jCount + "rowCnt: " + rowCnt);
                  GLPK.intArray_setitem(ja, jCount++, ijkToIndex(i, j, k) + 1);
                  GLPK.intArray_setitem(ia, iCount++, rowCnt);
                  if((i == couple[0] && j == couple[1]) && k2 == k){
                    GLPK.doubleArray_setitem(ar, count++, 1);
                  }//if
                  else if((i == couple[1] && j == couple[0]) && k2 == (k + numDates/2)){
                    GLPK.doubleArray_setitem(ar, count++, -1);
                  }//if
                  else{
                    GLPK.doubleArray_setitem(ar, count++, 0);
                  }//else
                }//for k
              }//for j
            }//for i
            rowCnt++;
          }//for in
        }//for k2
        
        GLPK.glp_load_matrix(lp, total_size, ia, ja, ar);
        GLPK.glp_simplex(lp, null);
        int ret = GLPK.glp_intopt(lp, null);
        
        if(ret == 0){
          System.out.println("Got Here");
        }
      }
      catch(Exception e){
        e.printStackTrace();
      }
      
    }
    
    
    
    return true;
  }
  
  public static int ijkToIndex(int i, int j, int k){
    return i + numTeams*j + numTeams*numTeams*k;
  }
  
  public static int[] indexToijk(int index){
    int k = index/(numTeams*numTeams);
    int j = (index - (k*numTeams*numTeams))/numTeams;
    int i = index - (k*numTeams*numTeams) - (j*numTeams);
    int [] returnVals = {i, j, k};
    return returnVals;
  }
  
  public static void main(String args[]){
    ArrayList<String> pats = HomeAwayGenerator.makePatterns("HHHHBAAAA");
    ArrayList<Integer[]> ps = GLPKPatternSetFinder.findPatternSet(pats);
    GLPKTimeTableFinder ttf = new GLPKTimeTableFinder(ps, pats);
    ttf.getTimeTable(0);
  }

}
