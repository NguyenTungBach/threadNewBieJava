package javathread;
// C1, tài nguyên sẽ được tách biệt riêng
public class MyThread extends Thread{
    private int count = 0;
    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            count++;
            System.out.printf("test thread: %s count %d\n", this.getName(),count);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
