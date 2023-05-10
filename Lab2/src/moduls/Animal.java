package moduls;

public class Animal {
    private final int type;
    private final int index;
    private Food food;


    public Animal(int type, int index, Food food)
    {
        this.type = type;
        this.index = index;
        this.food = food;
    }


    public void print()
    {
        System.out.println("Animal - Type: "+type+" Index: "+index+"\n"+food.foodsAnimal);
    }
    public int getType() {
        return type;
    }

    public Food getFood() {
        return food;
    }

    public int getIndex() {
        return index;
    }
}
