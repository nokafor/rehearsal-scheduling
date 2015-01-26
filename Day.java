import java.util.*;
import java.io.*;

public class Day
{
    private Queue<Event> conflicts; // collection of events during the day
    private String name; // name of the day of week

    // NOTE: name should equal a day of the week, and nothing else
    public Day(String name)
    {
        conflicts = new LinkedList<Event>();
        this.name = name;
    }
    
    // adds an event to the collection, and if it conflicts with another event,
    // both events are concatenated together
    public void addEvent(Event ev)
    {
        Event event = ev;
        Object[] events = conflicts.toArray();
        int length = events.length;

        // go through the existing list of conflicts
        for (int i = 0; i < length; i++) {
            Event e = (Event) events[i];

            // if the inputed event conflicts with an already existent conflict, 
            // concatenate the two events and remove the already existent conflict
            // from the day's list of conflicts
            if (event.getStart() == e.getEnd() || event.getEnd() == e.getStart() || event.conflict(e)) {
                event = event.concatenate(e);
                conflicts.remove(e);
            }
        }
        
        // add the event to the day's list of events
        conflicts.add(event);
    }

    // function used if making a rehearsal schedule
    // just adds rehearsal time to list, since rehearsals shouldn't
    // be concatenated
    public void addRehearsal(Event ev) {
        conflicts.add(ev);
    }

    // this function is to make the casting process easier
    public boolean addEvents(Collection<Event> events) {
        return conflicts.addAll(events);
    }
    
    public boolean equals(Day that) {
        return name.equals(that.name);
    }

    public String name() {
        return name;
    }

    public boolean contains(Event ev) {
        return conflicts.contains(ev);
    }

    public boolean remove(Event ev) {
        return conflicts.remove(ev);
    }

    public boolean isEmpty() {
        return conflicts.isEmpty();
    }

    public Queue<Event> allEvents() {
        return conflicts;
    }

    // output events of day; one event per line
    public void show()
    {
        System.out.println(name + ": ");
        System.out.println();

        for (Event ev : conflicts) {
            System.out.println(ev.toString());
        }

        System.out.println();
    }

    public String toString() {
        String output = name + ":\n\n";

        for (Event ev : conflicts) {
                output += ev.toString();
                output += "\n";
        }

        output += "\n";

        return output;
    }

    // test method
    public static void main(String[] args) { 
    
        // set up some events 
        Event ev1 = new Event(1700, 2000, "conflict1"); 
        Event ev2 = new Event(2200, 200, "conflict2"); 
        Event ev3 = new Event(1300, 1500, "conflict3");

        Event ev = new Event(2100, 100, "conflict");

        // Add to the same day 
        Day tues = new Day("Tuesday"); 
        tues.addEvent(ev1); 
        tues.addEvent(ev2); 
        tues.addEvent(ev3); 
        tues.addEvent(ev);
        
        // Output the events for the Day 
        tues.show(); 

        Day tues2 = new Day("Wednesday");
        System.out.println(tues.equals(tues2));
    }
}