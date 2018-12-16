import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class Server implements Runnable {

    private Student[] students = null;

    private void populateStudents() {
        students = new Student[10];
        students[0] = new Student("Jaromir", "Szczepański", 209681);
        students[1] = new Student("Ireneusz", "Grabowski", 209738);
        students[2] = new Student("Izolda", "Symanska", 209308);
        students[3] = new Student("Kasia", "Adamczyk", 209327);
        students[4] = new Student("Korneliusz", "Jasiński", 209926);
        students[5] = new Student("Natasza", "Chmielewska", 209324);
        students[6] = new Student("Marian", "Gorski", 209927);
        students[7] = new Student("Zdzisław", "Rutkowski", 209487);
        students[8] = new Student("Danuta", "Walczak", 209550);
        students[9] = new Student("Klaudia", "Jaworska", 209451);
    }

    @Override
    public void run() {
        populateStudents();
        try {
            ServerSocket listener = new ServerSocket(7878);
            System.out.println("Server running.");

            while(true) {
                Socket client = listener.accept();
                System.out.println("Got new connection - " + client.getInetAddress().toString());
                //new Thread(new ServerSession(client, students)).run();
                new ServerSession(client, students).run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ServerSession implements Runnable {

        private Socket clientConnection;
        private String connectionName; // for logging purposes
        private Student[] students;
        private ObjectInputStream objectInputStream;
        private ObjectOutputStream objectOutputStream;

        public ServerSession(Socket clientConnection, Student[] students) {
            this.clientConnection = clientConnection;
            this.students = students;
            connectionName = UUID.randomUUID().toString();
        }

        @Override
        public void run() {
            try {
                Integer studentIndexNo;
                objectOutputStream = new ObjectOutputStream(clientConnection.getOutputStream());
                objectInputStream = new ObjectInputStream(clientConnection.getInputStream());

                System.out.println("Connection handler " + connectionName + " started for " + clientConnection.getInetAddress().toString());
                if(!objectInputStream.readObject().equals("i am legit client")) {
                    System.out.println(connectionName + ": not our legit client, exiting...");
                    clientConnection.close();
                    return;
                }

                System.out.println(connectionName + ": waiting for student query...");

                studentIndexNo = (Integer) objectInputStream.readObject();
                System.out.println(connectionName + ": got query for " + studentIndexNo);

                Student matchingStudent = null;
                int foundAt = -1;
                synchronized (students) {
                    for (int i = 0; i < students.length; i++) {
                        if (students[i].getIndexNo().equals(studentIndexNo)) {
                            matchingStudent = students[i];
                            foundAt = i;
                        }
                    }
                }
                if(matchingStudent != null) System.out.println(connectionName + ": Sending student...");
                else System.out.println(connectionName + ": Student not found.");
                objectOutputStream.writeObject(matchingStudent);
                synchronized (students) {
                    System.out.println(connectionName + ": waiting for new student info...");
                    students[foundAt] = (Student) objectInputStream.readObject();
                }
            } catch (EOFException ignored) {}
            catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (ClassNotFoundException cnfe) {
                cnfe.printStackTrace();
            } finally {
                try {
                    System.out.println(connectionName + ": connection closed");
                    clientConnection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
