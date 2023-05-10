package moduls;

import java.util.HashMap;
import java.util.List;

public class Food {
    public static int amountTypesofFood = 0;
    // Typ jedzenia - ilość
    HashMap<Integer,Integer> foodsAnimal = new HashMap<>();

    public Food(List<Integer> list)
    {
        for (int i =0;i<list.size();i+=2)
        {
            foodsAnimal.put(list.get(i), list.get(i+1));
            if ((list.size()/2)>amountTypesofFood) //znajdowanie ilości typów jedzenia
                amountTypesofFood = list.size()/2;
        }
    }

    public HashMap<Integer, Integer> getFoodsAnimal() {
        return foodsAnimal;
    }
}
