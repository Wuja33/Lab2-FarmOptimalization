package moduls;

import java.util.ArrayList;
import java.util.List;

public class Farm {
    private final int index;
    private List<Integer> foodOnFarm;
    private List<Integer> animalsOnFarm = new ArrayList<>();

    public Farm(List<Integer> line)
    {
        this.index = line.get(0);
        line.remove(0);
        foodOnFarm = line;
    }
    public void print()
    {
        System.out.println("Farm -  Index: "+index+"\n"+foodOnFarm);
    }

    public Integer getAmountAllFood()
    {
        int help=0;
        for (Integer food: foodOnFarm) {
            help += food;
        }
        return help;
    }

    public String StringListAnimalsOnFarm()
    {
        String help = "";
        for (int i=0;i<animalsOnFarm.size();i++) {
            if (i!= animalsOnFarm.size()-1)
                help += animalsOnFarm.get(i).toString()+", ";
            else
                help += animalsOnFarm.get(i).toString();
        }
        return help;
    }
    public void addAnimaltoFarm(Animal animal)
    {
        animalsOnFarm.add(animal.getIndex());
    }

    public List<Integer> getAnimalsOnFarm() {
        return animalsOnFarm;
    }

    public List<Integer> getFoodOnFarm() {
        return foodOnFarm;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "Farm{" +
                "index=" + index +
                ", foodOnFarm=" + foodOnFarm +
                ", animalsOnFarm=" + animalsOnFarm +
                '}';
    }

}
