package tools;

import moduls.Animal;
import moduls.Farm;
import moduls.Food;

import java.util.*;

public abstract class Calculation {
    private static int P;
    private static List<Animal> listAnimals = new ArrayList<>();
    private static final List<Farm> listFarms = new ArrayList<>();
    private static final HashMap<Integer, List<Integer>> foodTable = new HashMap<>();

    public static void start(String filePathFood, String filePathAnimal, String filePathFarm, String separator) {
        createListFoodsForAnimals(filePathFood, separator);
        createListofAnimals(filePathAnimal, separator);
        createListofFarms(filePathFarm, separator);
        sortAnimals();
        calculateBestOption();
        }


    private static void createListofAnimals(String filePath, String separator) {
        List<List<Integer>> listofLines = Import.createListofParametersFromFile(filePath, separator);
        for (List<Integer> line : listofLines) {
            if (foodTable.containsKey(line.get(1)))
                listAnimals.add(new Animal(line.get(1), line.get(0), new Food(foodTable.get(line.get(1)))));
            else
            {
                System.out.println("Nie wszystkie zwierzęta mają określony rodzaj żywności");
                System.exit(-1);
            }
        }
    }

    private static void createListFoodsForAnimals(String filePath, String separator) {
        List<List<Integer>> listofLines = Import.createListofParametersFromFile(filePath, separator);
        for (List<Integer> line : listofLines) {
            List<Integer> helpList = new ArrayList<>();
            if (foodTable.containsKey(line.get(0))) //jeśli dane zwierze już istnieje
            {
                helpList = foodTable.get(line.get(0));
                helpList.add(line.get(1));
                helpList.add(line.get(2));
                foodTable.replace(line.get(0), helpList);
            } else //jeśli dane zwierze nie istnieje
            {
                helpList.add(line.get(1));
                helpList.add(line.get(2));
                foodTable.put(line.get(0), helpList);
            }
        }
    }
    private static void createListofFarms(String filePath, String separator) {
        List<List<Integer>> listofLines = Import.createListofParametersFromFile(filePath, separator);
        for (List<Integer> line : listofLines) {
            if ((line.size() - 1) >= Food.amountTypesofFood) //czy każda farma ma wymaganą ilość jedzenia każdego typu
                listFarms.add(new Farm(line));
            else
            {
                System.out.println("Nie wszystkie farmy mają określony rodzaj żywności");
                System.exit(-1);
            }
        }
    }
    private static void sortAnimals()
    {
        List<List<Integer>> listHelptoSort = new ArrayList<>();
        List<Integer> listTypesAnimal = new ArrayList<>(foodTable.keySet()); //lista kluczy (lista typów zwierząt)

        for (int i=0;i<listTypesAnimal.size();i++)
        {
            listHelptoSort.add(Arrays.asList(listTypesAnimal.get(i), Collections.max(foodTable.get(listTypesAnimal.get(i))))); //tworzenie listy list, gdzie 1:typ zwierzęcia 2:max ilość wymagana z któregoś rodzaju jedzenia
        }

        listHelptoSort.sort((p1,p2)->{ //sortowanie typów zwierząt według ilości pożywienia
            return p2.get(1) - p1.get(1);
        });

        List<Animal> listAnimalHelp = new ArrayList<>();

        for (int i = 0; i < listHelptoSort.size(); i++) { //sortowanie listy zwierząt, według posortowanych typów zwierząt
            for (int j = 0; j < listAnimals.size(); j++) {
                if (listAnimals.get(j).getType()==listHelptoSort.get(i).get(0))
                    listAnimalHelp.add(listAnimals.get(j));
            }
        }
        listAnimals = listAnimalHelp; //podmiana listy
    }
    private static int findBestFarm(Animal animal)
    {
        //Piramida ważności
        //1.mod 2.quotient 3.delta
        int mod=0;
        int modHelp=0;
        int delta=0;
        int deltaHelp=0;
        int quotient=0;
        int quotientHelp=0;
        int bestFarmIndex=-1;
        int bestFarmIndexHelp;
        boolean ifFindFarm=false;
        boolean ifEnoughFood;
        for (int i=0;i<listFarms.size();i++) {
            bestFarmIndexHelp = listFarms.get(i).getIndex();
            ifEnoughFood = true;

            for (int j = 0; j < animal.getFood().getFoodsAnimal().size(); j++) {
                if (animal.getFood().getFoodsAnimal().containsKey(j + 1)) {
                    if (listFarms.get(i).getFoodOnFarm().get(j) < animal.getFood().getFoodsAnimal().get(j + 1)) //sprawdź czy na farmie jest wystarczająco jedzenia
                    {
                        ifEnoughFood = false;
                        break;
                    }
                    if (listFarms.get(i).getFoodOnFarm().get(j) % animal.getFood().getFoodsAnimal().get(j + 1) == 0) //jeśli ilość jedzenie na farmie (modulo) ilość jedzenia wymagane przez zwierze == 0
                    {
                        modHelp++; //zliczanie liczby liczb, które maja reszte z dzielenia 0
                        if (listFarms.get(i).getFoodOnFarm().get(j) / animal.getFood().getFoodsAnimal().get(j + 1) == 1) //jeśli reszta z dzielenia==0, to oblicz iloraz, jeśli wyjdzie 1, to dodaj do zmiennej
                            quotientHelp++;
                    }
                    deltaHelp += listFarms.get(j).getFoodOnFarm().get(j) - animal.getFood().getFoodsAnimal().get(j + 1);
                }
            }

            if(ifEnoughFood) {
                if (modHelp > mod) //jeśli dana farma oferuje więcej liczb podzielnych przez ilość zapotrzebowanego jedzenia -> zmień farme
                {
                    ifFindFarm=true;
                    bestFarmIndex = bestFarmIndexHelp;
                    mod = modHelp;
                    quotient = quotientHelp;
                    delta = deltaHelp;
                } else if (modHelp == mod) //jeśli dana farma oferuje tyle samo liczb podzielnych przez ilość zapotrzebowanego jedzenia -> sprawdź ilość ilorazów = 1
                {
                    if (quotientHelp > quotient) //jeśli dana farma więcej ilorazów = 1 -> zmień farme
                    {
                        ifFindFarm=true;
                        bestFarmIndex = bestFarmIndexHelp;
                        mod = modHelp;
                        quotient = quotientHelp;
                        delta = deltaHelp;
                    } else if (quotientHelp == quotient) //jeśli dana farma oferuje tyle samo ilorazów=1 -> sprawdź delte
                    {
                        if (deltaHelp < delta) //jeśli dana farma oferuje mniejszą delte -> zmień farme
                        {
                            ifFindFarm=true;
                            bestFarmIndex = bestFarmIndexHelp;
                            mod = modHelp;
                            quotient = quotientHelp;
                            delta = deltaHelp;
                        }
                        else if (deltaHelp > delta && bestFarmIndex==-1)
                        {
                            ifFindFarm=true;
                            bestFarmIndex = bestFarmIndexHelp;
                            mod = modHelp;
                            quotient = quotientHelp;
                            delta = deltaHelp;
                        }
                    }
                }
            }
            quotientHelp = 0;
            modHelp = 0;
            deltaHelp = 0;
        }

        if (ifFindFarm)
            return bestFarmIndex;
        else return -1;
    }

    private static void calculateBestOption()
    {
        int bestFarmIndex;
        String outputCopy ="";
        for (Farm farm:
             listFarms) {
            outputCopy+=farm.getIndex()+"; "+farm.getAmountAllFood()+"; "+farm.StringListAnimalsOnFarm()+"\n";
            P += farm.getAmountAllFood();
        }
        outputCopy+="\nP: "+P;

        for (Animal animal: listAnimals) {
            bestFarmIndex = findBestFarm(animal);
            if (bestFarmIndex != -1) {
                for (int i = 0; i < animal.getFood().getFoodsAnimal().size(); i++) {
                    if (animal.getFood().getFoodsAnimal().containsKey(i + 1)) {
                        listFarms.get(bestFarmIndex - 1).getFoodOnFarm().set(i, listFarms.get(bestFarmIndex - 1).getFoodOnFarm().get(i) - animal.getFood().getFoodsAnimal().get(i + 1)); //po wybraniu farmy, pomniejsz jej dostępną ilość jedzenia
                    }

                }
                listFarms.get(bestFarmIndex - 1).addAnimaltoFarm(animal); //dodanie zwierzaka do listy na farmie
            }
            else //znaleziono zwierze, które nigdzie nie pasuje
            {
                for (Farm farm :
                        listFarms) {
                }
                System.out.println(outputCopy);
                return;
            }
        }

        //po dodaniu wszystkich zwierząt, oblicz wynik i wypisz
        for (Farm farm: listFarms) {
            System.out.print(farm.getIndex()+"; "+farm.getAmountAllFood()+"; "+farm.StringListAnimalsOnFarm());
            P += farm.getAmountAllFood();
            System.out.println();
        }
        System.out.println("\nP: "+P);
    }
}
