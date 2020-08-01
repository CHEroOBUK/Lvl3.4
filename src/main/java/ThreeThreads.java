public class ThreeThreads {
    private final Object monitor = new Object();
    private volatile char abc = 'C';

    public static void main(String[] args) {
        ThreeThreads threeThreads = new ThreeThreads();

        Thread aMaker = new Thread(threeThreads::printA);
        Thread bMaker = new Thread(threeThreads::printB);
        Thread cMaker = new Thread(threeThreads::printC);

        aMaker.start();
        bMaker.start();
        cMaker.start();
        
    }

    public void printA() {
        synchronized (monitor) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (abc != 'C') {
                        monitor.wait();
                    }
                    System.out.print("A");
                    abc = 'A';
                    monitor.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printB() {
        synchronized (monitor) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (abc != 'A') {
                        monitor.wait();
                    }
                    System.out.print("B");
                    abc = 'B';
                    monitor.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printC() {
        synchronized (monitor) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (abc != 'B') {
                        monitor.wait();
                    }
                    System.out.print("C");
                    abc = 'C';
                    monitor.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
