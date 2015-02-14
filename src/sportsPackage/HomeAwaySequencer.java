package sportsPackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class HomeAwaySequencer
{

  public static void main(String args[])
  {

    generateSequences2(7);

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
                teams.remove(0);
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

  public static void generateSequences2(int numTeams)
  {

    boolean oddNumTeams = (numTeams % 2 == 1);
    Random generator = new Random();

    ArrayList<Integer[]> teams = new ArrayList<Integer[]>();
    char[][] sequences = new char[numTeams][numTeams];

    for (int i = 0; i < numTeams; i++)
      {

        //make 3 digit sequences corresponding to Home away and Bye games remaining to be scheduled
        if (oddNumTeams)
          {
            Integer[] hab = { numTeams / 2, numTeams / 2, 1 };
            teams.add(hab);
          }
        else
          {
            Integer[] hab = { numTeams / 2, numTeams / 2, 0 };
            teams.add(hab);
          }

      }

    for (int i = 0; i < numTeams; i++)
      {
        int count = 0;
        //List of teams that need to be scheduled for a given week. 
        ArrayList<Integer> toBeScheduled = new ArrayList<Integer>();
        for (int j = 0; j < numTeams; j++)
          toBeScheduled.add(j);

        //while a full sequence is not yet generated fully,
        while (count < numTeams)
          {
            //condition for byes
            if (oddNumTeams && count == 0)
              {
                int team =
                    findTeamWithGame(2, teams, generator, numTeams,
                                     toBeScheduled);
                toBeScheduled.remove(toBeScheduled.indexOf(team));
                sequences[i][team] = 'B';
                teams.get(team)[2]--;
                count++;
              }
            else
              {
                int team1 =
                    findTeamWithGame(0, teams, generator, numTeams,
                                     toBeScheduled);
                toBeScheduled.remove(toBeScheduled.indexOf(team1));
                int team2 =
                    findTeamWithGame(1, teams, generator, numTeams,
                                     toBeScheduled);
                toBeScheduled.remove(toBeScheduled.indexOf(team2));

                sequences[i][team1] = 'H';
                sequences[i][team2] = 'A';

                teams.get(team1)[0]--;
                teams.get(team2)[1]--;

                count += 2;
              }

          }

      }
    for (int i = 0; i < numTeams; i++)
      {
        for (int j = 0; j < numTeams; j++)
          {
            System.out.print(sequences[i][j] + " ");
          }
        System.out.println();
      }
  }

  public static int findTeamWithGame(int type, ArrayList<Integer[]> teams,
                                     Random gen, int numTeams,
                                     ArrayList<Integer> toBeScheduled)
  {
    int index = -1;
    boolean found = false;
    while (!found)
      {
        int trialIndex = gen.nextInt(numTeams);
        if (teams.get(trialIndex)[type] != 0
            && toBeScheduled.contains(trialIndex))
          {
            found = true;
            index = trialIndex;
          }//if
      }//while

    return index;
  }//findTeamWithGame(int, ArrayList<Integer[]>, Random, int)

}
