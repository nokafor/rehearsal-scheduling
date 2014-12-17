import java.util.*;

//can make it concatenate overlapping events

public class Day
{
    private Queue<Event> conflicts;
    private String name;

    public Day(String name)
    {
        conflicts = new LinkedList<Event>();
        this.name = name;
    }
    
    public void addEvent(Event ev)
    {
        Event event = ev;
        Object[] events = conflicts.toArray();
        int length = events.length;

        for (int i = 0; i < length; i++) {
            Event e = (Event) events[i];
            if (event.getStart() == e.getEnd() || event.getEnd() == e.getStart() || event.conflict(e)) {
                event = event.concatenate(e);
                conflicts.remove(e);
            }
        }

        // for (Event e : conflicts) {
        //     // if an event conflicts...
        //     if (event.getStart() == e.getEnd() || event.getEnd() == e.getStart() || event.conflict(e)) {
        //         System.out.println("checkpoint");
        //         event = event.concatenate(e);
        //         conflicts.remove(e);
        //     }
        // }
        
        conflicts.add(event);
    }

    public boolean addEvents(Collection<Event> events) {
        return conflicts.addAll(events);
    }

    //rehearsal times cannot and should not overlap
    public void addRehearsal(Event ev) {
        conflicts.add(ev);
    }
    
    public boolean equals(Day that) {
        if (name.equals(that.name))
            return true;
        else return false;
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

    //output events of day; one event per line
    public void show()
    {
        System.out.println(name + ": ");
        System.out.println();

        for (Event ev : conflicts) {
            System.out.println(ev.toString());
        }

        System.out.println();
    }

    public Queue<Event> allEvents() {
        return conflicts;
    }

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