package processplan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Process implements Serializable {

    private String name; //название процесса
    private int timeAppearance; //время появления в очереди
    private int priority; //приоритет
    private int processLong; //проолжительность очередного CPU burst
    private ArrayList<String> step = new ArrayList<String>();

    Process(String _name, int _timeA, int _priority, int _processLong)
    {
        this.name = _name;
        this.timeAppearance = _timeA;
        this.priority = _priority;
        this.processLong = _processLong;
    }

    public void executeProcess() {
        this.processLong = this.processLong - 1;
    }

    public String getName(){
        return this.name;
    }

    public int getTimeAppeapance(){
        return this.timeAppearance;
    }

    public int getPriority(){
        return this.priority;
    }

    public int getProcessSize(){
        return this.step.size();
    }

    public int getProcessLong(){
        return this.processLong;
    }

    public void addStep(String step){
        this.step.add(step);
    }

    public void getStep() {
        for (int i=0; i < step.size(); i++)
        {
            System.out.print(libFormat.format(step.get(i),3));
        }
    }

    public int getWaitTime() {
        int result = 0;
        for (int i=0; i < step.size(); i++)
        {
            if (step.get(i) == "Г") {
                result++;
            }
        }
        return result;
    }

    public int getExecuteTime() {
        int result = 0;
        for (int i=0; i < step.size(); i++)
        {
            if (step.get(i) != "-") {
                result++;
            }
        }
        return result;
    }

    @Override
    public String toString()
    {
        return libFormat.format(name, 8) +
                libFormat.format(timeAppearance, 17) +
                libFormat.format(priority, 10) +
                libFormat.format(processLong, 18);
    }
}