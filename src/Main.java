import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Wybierz: \n" +
                "1) Serwer\n" +
                "2) Klient");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                new Server().run();
                break;
            case 2:
                new Client().run();
                break;
        }
    }
}
