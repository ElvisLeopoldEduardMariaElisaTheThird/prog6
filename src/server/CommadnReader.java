package server;

public class CommadnReader implements Runnable{

    private String entry;

    public CommadnReader(String s){
        this.entry=s;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        System.out.println("Read from client message: "+entry);
    }
}
