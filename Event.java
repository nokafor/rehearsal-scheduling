public class Event
{
    private int first; // beginning time of event
    private int last; // ending time of event
    private String data; // information about the event
    
    // NOTE: time should be inputed in military time, with midnight = 2400
    // as opposed to 0
    public Event(int start, int end, String info)
    {
        first = start;
        last = end;
        data = info;
    }
    
    public int getStart()
    {
        return first;
    }
    
    public int getEnd()
    {
        return last;
    }
    
    public String getInfo()
    {
        return data;
    }
    
    // do two events conflict with each other?
    public boolean conflict(Event that)
    {

        // if one starts where the other ends, the events don't conflict
        if (that.first == last || that.last == first)
            return false;

        // check if the start time of other event falls between start
        // and finish of this event
        if (that.first >= first && that.first <= last)
            return true;
        
        // check if end time of other event falls between start and
        // finish of this event
        if (that.last >= first && that.last <= last)
            return true;
        
        // check if other event wraps around military clock
        if (that.last < that.first) {
            // if both wrap, eevents conflict
            if (last < first) return true;

            // if this event starts after that ends, then events conflict
            else if (last > that.first) return true;
        }

        // check if this event wraps around military clock and 
        // other event ends after this starts
        if (last < first && that.last > first)  return true;
        

        return false;
    }

    // combine two overlapping events into just one
    public Event concatenate(Event that) {
        int start;
        int end;
        String info;

        // figure out which event starts first
        if (first < that.first) {
            start = first;
            info = data + ", " + that.data;
        }

        else {
            start = that.first;
            info = that.data + ", " + data;
        }


        // figure out which event ends last
        if (that.last < that.first && last >= first) {
            end = that.last;
        }

        else if (last < first && that.last >= that.first) {
            end = last;
        }

        else {
            
            if (last > that.last) end = last;
            else end = that.last;
        }

        return new Event(start, end, info);
    }

    
    public String toString()
    {
        String output = first + "-" + last + ": " + data;
        
        return output;
    }
    
    // test method
    public static void main(String[] args) { 
        // set up some events 
        Event ev0 = new Event(1300, 1500, "Rehearsal1"); 
        Event ev1 = new Event(2200, 200, "conflict1"); 
        Event ev2 = new Event(2100, 2300, "conflict2"); 
        Event ev3 = new Event(1000, 1300, "conflict3"); 
        //System.out.println(ev0); 
        //System.out.println(ev1); 
        
        System.out.println("Does " + ev0.getInfo() + " conflict with " 
                           + ev1.getInfo() + "?"); 
        if (ev0.conflict(ev1)) System.out.println("Yes."); 
        else System.out.println("No."); 


        System.out.println("Does " + ev0.getInfo() + " conflict with " 
                           + ev2.getInfo() + "?"); 
        if (ev0.conflict(ev2)) System.out.println("Yes."); 
        else System.out.println("No."); 


        System.out.println("Does " + ev0.getInfo() + " conflict with " 
                           + ev3.getInfo() + "?"); 
        if (ev0.conflict(ev3)) System.out.println("Yes."); 
        else System.out.println("No."); 

        System.out.println();

        if (ev1.getStart() == ev2.getEnd() || ev1.getEnd() == ev2.getStart() || ev1.conflict(ev2)) 
            System.out.println(ev2.concatenate(ev1));
        else System.out.println("not concatenated");

    }
}