package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class serverTest {


    static ExecutorService executorService = Executors.newFixedThreadPool(5);
    public static CollectionManager ColM = new CollectionManager();
    public static AuthrizationManager AM = new AuthrizationManager();
    public static boolean flag=true;
    public static boolean authorized = false;
    public static void main(String[] args) throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(5);
        ExecutorService cahcedExecutor = Executors.newCachedThreadPool();

        while (flag) {

            try (ServerSocket server = new ServerSocket(3345)) {
                Socket client = server.accept();
                System.out.println("Connection accepted.");

                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(client.getInputStream());


                String returnFile;

                Thread serverThread = new Thread(()->{
                   try{
                       BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                       String command;
                       while ((command = reader.readLine()) != null) {
                           // Обрабатываем команду с сервера
                           System.out.println("Server command: " + command);
                           if(command.equalsIgnoreCase("save")){
                               ColM.LW.write(ColM.LR.getWorkersToWrite());
                           }
                           if(command.equalsIgnoreCase("exit")){
                               out.flush();
                               client.close();
                               flag=false;
                               System.exit(1);
                               break;
                           }
                       }
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
                });
                serverThread.start();


                while (!client.isClosed()) {



                    Object entt = in.readObject();
                    String entry = entt.toString();
                    Runnable reader = new CommadnReader(entry);

                    if(AM.isAuthorized()) {
                        Runnable handler = new CommandAnswer(entry);

                        returnFile = ColM.getCommand(entry);
                        out.writeObject(returnFile);
                    } else if (!AM.isAuthorized()) {
                        returnFile = AM.authorize(entry);
                        out.writeObject(returnFile);
                    }

                    if (entry.equalsIgnoreCase("exit")) {
                        out.flush();
                        Thread.sleep(300);

                        client.close();
                    }
                    out.flush();
                }
                System.out.println("Client disconnected");
                AM.notAuthorized();
                in.close();
                out.close();
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
