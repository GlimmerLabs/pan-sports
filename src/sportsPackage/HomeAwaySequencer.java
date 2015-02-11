package sportsPackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class HomeAwaySequencer
{

  public static void main(String args[])
  {

    System.out.println(Arrays.toString(generateSequences(5, 5)));

  }

  public static String[] generateSequences(int numTeams, int numDays)
  {
    Random generator = new Random();
    boolean Home = true;
    ArrayList<Integer> teams = new ArrayList<Integer>();
    String[] sequences = new String[numTeams];

    for (int f = 0; f < numTeams; f++)
      {
        sequences[f] = "";
      }// for

    int i = 0;
    while (i < numDays)
      {
        int t = 0;
        int n = numTeams;
        for (int f = 0; f < numTeams; f++)
          {
            teams.add(f);
          }// for
        while (!teams.isEmpty())
          {
            if (teams.size() == 1 && numTeams % 2 != 0)
              {
                sequences[teams.get(0)] += "B";
                teams.remove(0) ;
              }//if
            else
              {
                t = generator.nextInt(n);
                if (Home)
                  {
                    sequences[teams.get(t)] += "H";
                  }//if
                else
                  {
                    sequences[teams.get(t)] += "A";
                  }//else
                Home = !Home;
                teams.remove(t);
                n--;
              }//else
          }//while
        i++;
      }//while

    return sequences;
  }//generateSequences(int,int)

}
