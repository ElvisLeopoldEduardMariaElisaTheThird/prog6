package client;

import java.io.*;
import java.net.Socket;

public class clientTest {

    public static void main(String[] args) throws Exception{
        try(Socket socket = new Socket("localhost", 3345);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());)
        {
            System.out.println("Client connected to socket.");
            while(!socket.isOutputShutdown()){
                if(br.ready()){
                    Thread.sleep(100);
                    String clientCommand = br.readLine();
                    oos.writeObject(clientCommand);
                    oos.flush();
                    Thread.sleep(100);
                    if(clientCommand.equalsIgnoreCase("exit")){
                        Thread.sleep(200);
                        Object ent = ois.readObject();
                        String in = ent.toString();
                        System.out.println(in);

                        break;
                    }
                    Thread.sleep(200);
                    Object ent = ois.readObject();
                    String in = ent.toString();
                    System.out.println(in);
                }
            }

        } catch (Exception e) {
            System.out.println("No available server");
        }
    }
}
