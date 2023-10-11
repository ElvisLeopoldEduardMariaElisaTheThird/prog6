import server.CommandManager;
import server.Reader;
import server.Writer;

import java.util.HashSet;

public class Main {

    public static void main(String[] args) throws Exception {


        HashSet<String> Workers = new HashSet<String>();
        Writer LocalWriter = new Writer();
        //LocalWriter.write("10, Vanya");
        Reader LocalReader = new Reader();
        LocalReader.readd();
        //server.ScannerClass scanClass = new server.ScannerClass();
        //scanClass.scan();
        CommandManager CM = new CommandManager();
        //CM.generalCommands();

        /*server.Worker worker = scanClass.scanWorker();
        LocalReader.addWorker(worker);
        LocalWriter.write(worker.toString());
        LocalReader.getWorkers();*/
        //server.CollectionManager CoM = new server.CollectionManager();
        //CoM.readCollection();

        


    }
}