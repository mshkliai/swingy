package swingy.mvc.views.console;

import swingy.bd.DataBase;
import swingy.mvc.models.Hero;
import swingy.mvc.models.heroBuilder.DirectorHero;

import java.util.List;
import java.util.Scanner;

public class ConsoleChooseHero
{
    private DirectorHero builder;
    private List<String> names;
    private Hero       hero;
    private Scanner      scanner;

    public ConsoleChooseHero(Scanner scanner)
    {
        this.builder = new DirectorHero();
        this.hero = null;
        this.scanner = scanner;
    }

    public  Hero  getHero() throws Exception
    {
        int value;

        DataBase.getDb().connectDb();
        names = DataBase.getDb().getNames();

        while (hero == null)
        {
            System.out.println("0) Exit\n1) Select previous created hero\n2) Create new hero");

            value = this.getNiceValue();

            switch (value)
            {
                case 0: System.exit(0);
                case 1: this.oldHeroesManager();    break;
                case 2: this.heroCreator();         break;
            }
        }

        return hero;
    }

    private void    oldHeroesManager() throws Exception
    {
        int          index;
        int          value;

        while (true)
        {
            index = 0;

            if (names.size() == 0) {
                System.out.println("You haven't heroes, create them.");
                return;
            }

            System.out.println("0) come back");
            for (String name : names)
                System.out.println(++index + ") " + name);

            if ( (value = this.getNiceValue()) == 0 )
                break;
            else if ( value <= index)
            {
                System.out.println(DataBase.getDb().getHero(names.get(value - 1)).getFormattedInfo());
                System.out.println("\nMake choice: 1) Select   2) Remove   3) Cancel");

                int choice = this.getNiceValue();

                if (choice == 1)
                    hero = DataBase.getDb().getHero(names.get(value - 1));
                else if (choice == 2)
                {
                    try
                    {
                        DataBase.getDb().remove( names.get(value - 1) );
                        names.remove(value - 1);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (choice == 1 || choice == 3)
                    break;
            }
        }
    }

    private void    heroCreator()
    {
        int value;

        while (true)
        {
            System.out.println("0) come back\n1) Human\n2) Ork\n3) Elf");

            if ( (value = this.getNiceValue()) == 0 )
                break;
            if ( (value > 0 && value < 4) )
            {
                switch (value)
                {
                    case 1: this.tryCreateNewHero("Human"); break;
                    case 2: this.tryCreateNewHero("Ork");   break;
                    case 3: this.tryCreateNewHero("Elf");   break;
                }
            }
        }
    }

    private void    tryCreateNewHero(String type)
    {
        String nameHero = "";
        String error = "";
        Hero newHero = builder.buildbyType(type);

        System.out.println(newHero.getFormattedInfo() + "\nCreate him ? 1) Yes   2) No");

        int value = this.getNiceValue();

        if (value == 2)
            return;
        else
        {
            while (error != null)
            {
                System.out.print("Enter name: ");
                while (nameHero.equals(""))
                    nameHero = scanner.nextLine();

                error = builder.trySetName(newHero, nameHero);
                for (String name : names)
                    if (name.equals(nameHero))
                        error = "Hero with that name already created";

                if (error != null)
                    System.err.println(error);

                nameHero = "";
            }

            try
            {
                DataBase.getDb().addNewHero(newHero);
                this.names.add(newHero.getName());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int    getNiceValue()
    {
        String str;

        while (true)
        {
            str = "";

            while (str.equals(""))
                str = scanner.nextLine();

            if (!str.matches("^[0-9]+"))
                System.err.println("Enter nice value !");
            else
                return Integer.parseInt(str);
        }
    }
}