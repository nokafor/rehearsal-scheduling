public class Event
{
    private int first;
    private int last;
    private String data;
    
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
    
    public boolean conflict(Event that)
    {
        boolean intersects = false;
        if (that.first >= first && that.first <= last) //if that.low is between low and high
            intersects = true;
        
        else if (that.last >= first && that.last <= last)
            intersects = true;
        
        if (that.last < that.first && last < first)
            intersects = true;
                
        if (that.first == last || that.last == first)
            intersects = false;

        return intersects;
    }

    public Event concatenate(Event that) {
        int start;
        int end;
        String info;

        if (first < that.first) {
            start = first;
            info = data + ", " + that.data;
        }

        else {
            start = that.first;
            info = that.data + ", " + data;
        }

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
    
    public static void main(String[] args) { 
// set up some events 
        Event ev0 = new Event(1300, 1500, "Rehearsal1"); 
        Event ev1 = new Event(2200, 200, "conflict1"); 
        Event ev2 = new Event(2100, 100, "conflict2"); 
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

    }
    
}