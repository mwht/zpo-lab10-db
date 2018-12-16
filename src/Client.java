import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("IP/hostname: ");
        String remoteHost = scanner.nextLine();
        try {
            socket = new Socket(remoteHost, 7878);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject("i am legit client");

            objectInputStream = new ObjectInputStream(socket.getInputStream());

            System.out.print("Student's index no: ");
            Integer studentIndexNo = scanner.nextInt();

            System.out.println("Sending query for student...");
            objectOutputStream.writeObject(studentIndexNo);

            System.out.println("Downloading student data...");
            Student responseStudent = (Student) objectInputStream.readObject();

            if(responseStudent != null) {
                System.out.println(responseStudent);
            } else {
                System.out.println("Sorry, student not found.");
            }

            String response = "";
            while(!(response.toLowerCase().equals("y") || response.toLowerCase().equals("n"))) {
                System.out.print("Add grade? [Y/N] ");
                response = scanner.nextLine();
                if (response.toLowerCase().equals("y")) {
                    Integer newGrade;
                    System.out.print("Grade: ");
                    newGrade = scanner.nextInt();
                    responseStudent.dodajOcene(newGrade);
                    objectOutputStream.writeObject(responseStudent);
                }
            }
        } catch (EOFException ignored) {}
        catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
