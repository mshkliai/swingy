package swingy;

import swingy.mvc.Controller;

public class Main
{
    public static void main(String[] args)
    {
        if (args.length != 1) {
            System.err.println("Usage: java -jar swingy.jar console or gui");
        }
        else {
            try {
                Controller controller = new Controller();
                controller.startGame(args[0]);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}
