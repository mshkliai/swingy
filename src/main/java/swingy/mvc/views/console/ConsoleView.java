package swingy.mvc.views.console;

import swingy.mvc.Controller;
import swingy.mvc.models.Enemy;
import swingy.mvc.views.IView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ConsoleView implements IView {
    private static final String TEXT_MAKE_TRAVEL = "\n0) Exit\n\n     1) North\n2) West     3) East\n     4) South\n\"gui\" - for gui-mode";

    private static final Scanner scanner = new Scanner(System.in);

    private Controller controller;
    private ArrayList<char[]> map;
    private int numStat = 0;

    public ConsoleView(Controller controller) {
        this.controller = controller;
        map = new ArrayList<>();
    }

    @Override
    public void ChooseHero() {
        try {
            controller.setHero(new ConsoleChooseHero(scanner).getHero());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawGameObjects() {
        drawMap();
        System.out.println(TEXT_MAKE_TRAVEL);
        controller.keyPressed(getNiceValue());
    }

    @Override
    public void viewRepaint() {
        this.drawGameObjects();
    }

    @Override
    public void scrollPositionManager() {

    }

    @Override
    public void updateData() {

    }

    @Override
    public boolean simpleDialog(String message) {
        System.out.println(message.concat("\n 1) Yes     2) No"));

        int key = getNiceValue();

        while (key != 38 && key != 37) {
            System.err.println("Unknown value.");
            key = getNiceValue();
        }

        return (key == 38);
    }

    @Override
    public String get_Type() {
        return "console";
    }

    @Override
    public void   addLog(String text) {
        System.out.println(text);
    }

    @Override
    public void   close() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void    drawMap() {
        char[] buff = new char[controller.getSizeMap()];
        Arrays.fill(buff, '.');

        map.clear();
        for (int i = 0; i < controller.getSizeMap(); ++i) {
            map.add(buff.clone());
        }

        map.get(controller.getHero().getPosition().y)[controller.getHero().getPosition().x] = 'H';
        for (Enemy enemy : controller.getEnemies()) {
            map.get(enemy.getPosition().y)[enemy.getPosition().x] = 'E';
        }

        numStat = 0;
        for (char[] str : map) {
            System.out.println("     ".concat(String.valueOf(str)).concat(this.getStat(numStat++)));
        }
    }

    private int    getNiceValue() {
        String str;

        while (true) {
            str = "";

            while (str.isEmpty()) {
                try {
                    str = scanner.nextLine();
                }
                catch (Exception e) {
                    System.err.println("CTRL+D is bad!");
                    System.exit(0);
                }
            }

            if (str.equals("gui")) {
                return -2;
            }
            else if (!str.matches("^[0-9]+")) {
                System.err.println("Enter nice value !");
            }
            else {
                break;
            }
        }

        int value = -3;
        switch (Integer.parseInt(str)) {
            case 1: value = 38; break;
            case 2: value = 37; break;
            case 3: value = 39; break;
            case 4: value = 40; break;
            case -2: value = -2;break;

            case 0:
                controller.saveHero();
                System.exit(0);
        }
        return value;
    }

    private String  getStat(int numStat) {
        if (numStat > 9) {
            return "";
        }

        StringBuilder stat = new StringBuilder("       ");

        switch (numStat) {
            case 0: stat.append("Name: ").append(controller.getHero().getName());      break;
            case 1: stat.append("Type: ").append(controller.getHero().getType());      break;
            case 2: stat.append("Level: ").append(controller.getHero().getLevel());    break;
            case 3: stat.append("Location [").append(controller.getHero().getPosition().x).append(", ")
                    .append(controller.getHero().getPosition().y).append("]");         break;
            case 4: stat.append("Exp: ").append(controller.getHero().getExp()).append("/")
                    .append(controller.getHero().getNeccesaryExp());                   break;
            case 5: stat.append("Attack: ").append(controller.getHero().getAttack());  break;
            case 6: stat.append("Defense: ").append(controller.getHero().getDefense());break;
            case 7: stat.append("Hp: ").append(controller.getHero().getHP()).append("/")
                    .append(controller.getHero().getMaxHp());                          break;
            case 8:
                if (controller.getHero().getArtifact() != null && !controller.getHero().getArtifact().getType().isEmpty()) {
                    stat.append("Artifact-").append(controller.getHero().getArtifact().getType()).append(": ")
                            .append(controller.getHero().getArtifact().getValue());
                }
                break;
        }

        return stat.toString();
    }
}
