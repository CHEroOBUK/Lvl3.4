public class MFU {
    private final Object printingBlocker = new Object();
    private final Object scanningBlocker = new Object();
    private volatile boolean isPrintLocked = false;
    private volatile boolean isScanLocked = false;

    public static void main(String[] args) {
        final MFU mfu = new MFU();

        Thread firstFloor = new Thread(() -> {
            mfu.scan("Документ с 1 этажа");
            mfu.copy("Документ с 1 этажа");
            mfu.print("Документ с 1 этажа");
        });
        Thread secondFloor = new Thread(() -> {
            mfu.copy("Документ с 2 этажа");
            mfu.print("Документ с 2 этажа");
            mfu.scan("Документ с 2 этажа");
        });
        Thread thirdFloor = new Thread(() -> {
            mfu.print("Документ с 3 этажа");
            mfu.scan("Документ с 3 этажа");
            mfu.copy("Документ с 3 этажа");
        });
        firstFloor.start();
        secondFloor.start();
        thirdFloor.start();

    }

    public void print(String doc){
        synchronized (printingBlocker) {
            try {
                while (isPrintLocked) {
                    printingBlocker.wait();
                }
                isPrintLocked = true;
                System.out.println("Выполнение печати " + doc + "...");
                Thread.sleep(1000);
                System.out.println(doc + " распечатан");
                isPrintLocked = false;
                printingBlocker.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void scan(String doc){
        synchronized (scanningBlocker) {
            try {
                while (isScanLocked) {
                    scanningBlocker.wait();
                }
                isScanLocked = true;
                System.out.println("Выполнение сканирования " + doc + "...");
                Thread.sleep(1000);
                System.out.println(doc + " отсканирован");
                isScanLocked = false;
                scanningBlocker.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void copy(String doc){
        synchronized (scanningBlocker){
            try {
                while (isScanLocked) {
                    scanningBlocker.wait();
                }
                isScanLocked = true;
                System.out.println("Выполнение ксерокопии " + doc + " (сканирование)...");
                Thread.sleep(1000);
                System.out.println(doc + " отсканирован");
                isScanLocked = false;
                scanningBlocker.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (printingBlocker){
                try {
                    while (isPrintLocked) {
                        printingBlocker.wait();
                    }
                    isPrintLocked = true;
                    System.out.println("Выполнение ксерокопии " + doc + " (печать)...");
                    Thread.sleep(1000);
                    System.out.println(doc + " распечатан");
                    isPrintLocked = false;
                    printingBlocker.notifyAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
