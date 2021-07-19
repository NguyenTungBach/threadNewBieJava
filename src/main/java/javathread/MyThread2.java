package javathread;
//C2, tài nguyên sẽ dùng chung
public class MyThread2 implements Runnable{
    private int count = 0;
    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            count++;
            System.out.printf("test thread: %s count %d\n", Thread.currentThread().getName(),count);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
