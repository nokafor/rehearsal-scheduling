import java.io.*;
import java.util.*;

public class Casting {
	// new comparator/sorting method to sort the final results, by number of available casts
	public static SortedSet<Map.Entry<String, List<String>>> sortByValues(Map<String, List<String>> map) {
		
		SortedSet<Map.Entry<String, List<String>>> sortedEntries = new TreeSet<Map.Entry<String, List<String>>>(
			new Comparator<Map.Entry<String, List<String>>>() {
				@Override public int compare(Map.Entry<String, List<String>> e1, Map.Entry<String, List<String>> e2) {
					Integer x = (Integer) e1.getValue().size();
					Integer y = (Integer) e2.getValue().size();
					int res = x.compareTo(y);
					return res != 0 ? res : 1;
				}
			});

		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	}

	// Method that does most of the casting work
	// Command line arguments = dancer conflicts
	public static void main(String[] args) {

		// get rehearsal schedule
		Schedule rehearsalSchedule = new Schedule("Rehearsal Times");
		TreeMap<String, List<String>> rehearsalBreakdown = new TreeMap<String, List<String>>();
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

					rehearsalBreakdown.put(new String(currentDay[0] + ", " + ev.toString()), new ArrayList<String>());
				}
			}
		}

		catch(Exception e) {
			System.out.println("Error opening/reading rehearsal schedule. Please try again.");
			//e.printStackTrace();
			return;
		}

		//rehearsalSchedule.show();
		// for (Map.Entry<String, List<String>> entry : rehearsalBreakdown.entrySet()) {
		// 	System.out.println(entry.getKey());
		// }


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

		// get and print the available rehearsal times for each cast
		TreeMap<String, Schedule> castAvailabilities = new TreeMap<String, Schedule>();
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("CastingOutput.txt"), "utf-8"));

			String finalBreakdown1 = "Available Rehearsals for Each Cast\n\n";

			for (Schedule cast : casts) {
				Schedule rehearsalTimes = rehearsalSchedule.freeTimes(cast.name(), cast);
				castAvailabilities.put(cast.name(), rehearsalTimes);
				//writer.write(rehearsalTimes.toString());
				finalBreakdown1 += rehearsalTimes.toString();
			}

			// check how many casts are free on each day/rehearsal
			String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
			int length = daysOfWeek.length;

			// go through each casts schedule
			for (Map.Entry<String, Schedule> entry : castAvailabilities.entrySet()) {
				Schedule current = entry.getValue(); // current cast's schedule
				
				// check the data for each day of the week
				for (int i = 0; i < length; i++) {
					
					// get the current day (and all its events)
					// for both the cast and rehearsal
					Day currentDay = current.get(daysOfWeek[i]);
					Queue<Event> currentEvents = currentDay.allEvents();
					Day rehearsalDay = rehearsalSchedule.get(daysOfWeek[i]);
					Queue<Event> rehearsals = rehearsalDay.allEvents();

					// note for which rehearsals the cast is free
					for (Event ev : rehearsals) {
						for (Event e : currentEvents) {
							// check if events are the same
							if (ev.getStart() == e.getStart() && ev.getEnd() == e.getEnd()) {
								List<String> availableCasts = rehearsalBreakdown.get(daysOfWeek[i] + ", " + ev.toString());
								availableCasts.add(entry.getKey());
							}
						}
					}
				}
			}

			// for(int i = 0; i < length; i++) {
			// 	System.out.println("# of Casts Available on " + daysOfWeek[i] + ": " + available[i]);
			// }

			// sort then print the breakdown
			SortedSet<Map.Entry<String, List<String>>> sortedRehearsals = sortByValues(rehearsalBreakdown);
			String finalBreakdown2 = "Available Casts for Each Rehearsal\n\n";
			for (Map.Entry<String, List<String>> entry : sortedRehearsals) {
				String rehearsal = entry.getKey();
				List<String> availCasts = entry.getValue();

				finalBreakdown2 += rehearsal;
				finalBreakdown2 += "\n======================\n";

				for (String s : availCasts) {
					finalBreakdown2 += s;
					finalBreakdown2 += ", ";
				}

				finalBreakdown2 += "\n\n";
			}

			writer.write(finalBreakdown1);
			writer.write("\n");
			writer.write(finalBreakdown2);

			//String formattedOutput = "%40s %s";
			//writer.write(String.format(formattedOutput, finalBreakdown1, finalBreakdown2));
		}

		catch(Exception e) {
			e.printStackTrace();
		}

		finally {
			try {writer.close();} catch(Exception e) {}
		}

		

		// save all free times to text file?
	}
}