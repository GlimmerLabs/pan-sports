import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class GetSchools {
	public static void main(String arg[]) throws Exception{
		fakeSchedule(getSchools());
	}
	public static ArrayList<String> getSchools() throws IOException
	{
		BufferedReader in = new BufferedReader(new FileReader(
				"/home/knoebber17/public_html/scheduling/schools.txt"));
		System.out.println("Opened schools.txt");
		ArrayList<String> schools = new ArrayList<String>();
		String school;
		// first two lines will be garbage
		in.readLine();
		in.readLine();
		while (!((school = in.readLine()).equals(")"))) {
			school = removeSymbol(school);
			schools.add(school);
		}
		in.close();
		return schools;
	}
	/**
	 * removes the garbage from the school name. The php script writes the array like this: 
	 *     [0] => Grinnell
         *     [1] => Beliot
         *     [2] => Carrol
         * This get rids of the [0] => part. 
         */
	public static String removeSymbol(String school) {
		while (!Character.isLetter(school.charAt(0)))
			school = school.substring(1);
		return school;
	}

	public static void fakeSchedule(ArrayList<String> schools) throws Exception
	{
		File out = new File("/home/knoebber17/public_html/scheduling/schedule.txt");
		PrintWriter writer = new PrintWriter(out);
		String choice;
		String line;
		Random rand = new Random();
		for(int i =0;i<schools.size();i++)
		{
			line = schools.get(i);
			choice = schools.get(rand.nextInt(schools.size()));
			line += " vs "+choice;
			writer.println(line);
			System.out.println(line);
		}
		writer.close();
	}
}
