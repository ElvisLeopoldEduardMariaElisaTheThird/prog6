package server;

public class CommandAnswer implements Runnable{

    private String entry;

    public CommandAnswer(String s){
        this.entry=s;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        System.out.println("Sent to client: "+entry);
    }
}
