package javathread;

public class DemoMyThread {
    public static void main(String[] args) {
//        MyThread myThread = new MyThread();
//        myThread.setName("Luong1");
//        MyThread myThread1 = new MyThread();
//        myThread1.setName("Luong2");
//        myThread.start();
//        myThread1.start();
//        System.out.println("This is main thread, Luong3");

//        MyThread2 myThread2 = new  MyThread2();
//        Thread t = new Thread(myThread2);
//        t.start();

//        MyThread t1 = new MyThread(); // giúp tách ra 1 luồng
//        t1.setName("Luong1");
//        t1.start();
//        MyThread t2 = new MyThread(); // giúp tách ra 1 luồng
//        t2.setName("Luong2");
//        t2.start();

        MyThread2 thread1 = new MyThread2(); // giúp dùng chung 1 luồng
        Thread t1 = new Thread(thread1);
        t1.setName("MyThread2 Luong1");
        Thread t2 = new Thread(thread1);
        t2.setName("MyThread2 Luong2");
        t1.start();
        t2.start();
    }
}
