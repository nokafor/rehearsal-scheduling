import java.io.*;
import java.util.*;

public class Casting {
	// have all files in conflicts folder be command line argument
	public static void main(String[] args) {

		// get rehearsal schedule
		Schedule rehearsalSchedule = new Schedule("Rehearsal Times");

		String rehearsalFileName = "Rehearsals.txt";
		Scanner reader;
		try {
			reader = new Scanner(new File(rehearsalFileName));

			while (reader.hasNextLine()) {
				String[] currentDay = reader.nextLine().split(", ");
				int length = currentDay.length;

				for (int i = 1; i < length-2; i=i+3) {
					Event ev = new Event(Integer.parseInt(currentDay[i]), Integer.parseInt(currentDay[i+1]), currentDay[i+2]);
					rehearsalSchedule.addRehearsal(currentDay[0], ev);
				}
			}
		}

		catch(Exception e) {
			System.out.println("Error opening/reading rehearsal schedule. Please try again.");
		}

		//rehearsalSchedule.show();


		//get conflicts for each person
		int N = args.length;
		List<Schedule> dancers = new ArrayList<Schedule>();

		for (int x = 0; x < N; x++) {
			try {
				reader = new Scanner(new File(args[x]));

				String[] dancerName = args[x].split("\\W+");
				
				Schedule dancer = new Schedule(dancerName[1]);
				//System.out.println(dancer.name() + ": ");

				while (reader.hasNextLine()) {
					String[] currentDay = reader.nextLine().split(", ");
					int length = currentDay.length;

					for (int i = 1; i < length-2; i=i+3) {
						Event ev = new Event(Integer.parseInt(currentDay[i]), Integer.parseInt(currentDay[i+1]), currentDay[i+2]);
						dancer.add(currentDay[0], ev);
					}
				}

				dancers.add(dancer);
			}
			catch(Exception e) {
				System.out.println("Error opening/reading conflicts. Please try again.");
			}
			//dancer.show();
			//System.out.println();
		}

		//System.out.println(dancers.size());


		// create consolidated schedule for each cast
		List<Schedule> casts = new ArrayList<Schedule>();

		try {
			reader = new Scanner(new File("Casting.txt"));

			while (reader.hasNextLine()) {
				String[] currentCast = reader.nextLine().split(", ");
				int length = currentCast.length;

				ArrayList<String> casting = new ArrayList<String>(Arrays.asList(Arrays.copyOfRange(currentCast, 1, length)));
				Schedule cast = new Schedule(currentCast[0]);

				for (String name : casting) {
					for (Schedule dancer : dancers) {
						if (name.equals(dancer.name())) {
							//System.out.println("found");
							dancer.addTo(cast);
						}
					}
				}

				casts.add(cast);
				
			}
		}

		catch(Exception e) {
			System.out.println("Error opening/reading casting information. Please try again.");
		}

		//System.out.println(casts.size());

		// for (Schedule cast : casts) {
		// 	cast.show();
		// }

		//compare cast schedule to rehearsal schedule
		TreeMap<String, Schedule> castAvailabilities = new TreeMap<String, Schedule>();

		for (Schedule cast : casts) {
			Schedule rehearsalTimes = rehearsalSchedule.freeTimes(cast);
			castAvailabilities.put(cast.name(), rehearsalTimes);
			System.out.println(cast.name());
			rehearsalTimes.show();
		}

		//check how many casts are free on which days
		String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
		int length = daysOfWeek.length;
		int[] available = new int[length];

		for (Map.Entry<String, Schedule> entry : castAvailabilities.entrySet()) {
			Schedule current = entry.getValue();
			for (int i = 0; i < length; i++) {
				Day currentDay = current.get(daysOfWeek[i]);

				if (!currentDay.isEmpty())
					available[i]++;
			}
		}

		for(int i = 0; i < length; i++) {
			System.out.println("# of Casts Available on " + daysOfWeek[i] + ": " + available[i]);
		}

		// save all free times to text file?
		//can we check how many are free per rehearsal?
	}
}