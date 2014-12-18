import java.util.*;

public class Schedule
{
    // schedule holds the name of the day of the week, and that day's events
    private TreeMap<String, Day> schedule;
    private String name; // label for the schedule

    public Schedule(String name)
    {
        schedule = new TreeMap<String, Day>();
        this.name = name;
    }
    
    public String name() {
        return name;
    }

    public Collection<Day> getDays() {
    	return schedule.values();
    }

    public Day get(String name) {
    	return schedule.get(name);
    }

    public boolean equals(Schedule that) {
        return name.equals(that.name);
    }

    public void addDay(String day, Day events) {
        schedule.put(day, events);
    }

    // add an event to a specific day
    public void add(String day, Event ev)
    {
        Day current;

        if (schedule.containsKey(day)) {
            current = schedule.get(day);
        }

        else current = new Day(day);

        current.addEvent(ev);
        schedule.put(day, current);
    }

    // add a rehearsal to a specific day
    public void addRehearsal(String day, Event ev) {
        Day current;

        if (schedule.containsKey(day)) {
            current = schedule.get(day);
        }

        else current = new Day(day);

        current.addRehearsal(ev);
        schedule.put(day, current);   
    }

    // function used mostly in casting
    // add the days/events of this schedule to that schedule
    public void addTo(Schedule that) {
        for (Map.Entry<String, Day> entry : schedule.entrySet()) {

            String name = entry.getKey();
            Day current = entry.getValue();
            Queue<Event> allEvents = current.allEvents();

            for (Event ev : allEvents)
                that.add(name, ev);
        }
    }

    // function used mostly in casting, where it would be called by the
    // rehearsal schedule to compare with a cast's schedule
    // compares the two schedules and returns a schedule of the free times
    public Schedule freeTimes(Schedule that) {
    	Collection<Day> mySchedule = schedule.values();
    	Collection<Day> otherSchedule = that.getDays();

    	Schedule noConflicts = new Schedule("Free Times");

        // for every day in the schedule
    	for (Day myD : mySchedule)  {
            // make a copy of it
    		Day currentDay = new Day(myD.name());
    		Queue<Event> current = myD.allEvents();

    		currentDay.addEvents(current);

            // find it's matching day in the other schedule
    		for (Day otherD : otherSchedule) {
    			if (myD.equals(otherD)) {
    				Queue<Event> other = otherD.allEvents();

                    // find any conflicting events, and remove them from the
                    // copy of the current day
    				for (Event ev : other) {
    					for (Event e : current) {
    						if (ev.conflict(e)) {
    							if (currentDay.contains(e))
    								currentDay.remove(e);
    						}

    					}
    				}
    			}
    		}

            // add day with non-conflicting events into calendar
    		noConflicts.addDay(currentDay.name(), currentDay);
    	}

        return noConflicts;
    }
    
    // format and print the schedule
    public void show()
    {
        System.out.println(name);
        System.out.println("============");
        for (Map.Entry<String, Day> entry : schedule.entrySet()) {
            
            Day current = entry.getValue();
            current.show();
        }
        
        System.out.println();
    }

    // test method
    public static void main(String[] args) { 

        // set up some events  
        Event ev1 = new Event(330, 500, "conflict1"); 
        Event ev2 = new Event(815, 1000, "conflict2"); 
        Event ev3 = new Event(1000, 200, "conflict3"); 
        Event ev4 = new Event(500, 600, "conflict4");
        Event ev5 = new Event(700, 830, "conflict5"); 
        Event ev6 = new Event(500, 800, "conflict6"); 
        Event ev7 = new Event(800, 1000, "conflict7");  


        // set up some dates 
        String d1 = "Monday";
        String d2 = "Wednesday";
        String d3 = "Thursday";
        String d4 = "Sunday";

        Schedule s = new Schedule("Test");

        s.add(d1, ev3);
        s.add(d2, ev4);
        s.add(d2, ev5);
        s.add(d3, ev6);
        s.add(d3, ev7);
        s.add(d4, ev1);
        s.add(d4, ev2);
        s.add(d4, ev3);

        
        // Output the events for each date 
        s.show(); 

        System.out.println(s.equals(new Schedule("Test")));
    }    
}