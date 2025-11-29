import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumer {
    private final Queue<String> buffer = new LinkedList<>();
    private final int capacity = 5;

    public void produce(String msg) throws InterruptedException {
        synchronized (buffer) {
            while (buffer.size() == capacity) buffer.wait();
            buffer.add(msg);
            System.out.println("Produced: " + msg);
            buffer.notifyAll();
        }
    }

    public void consume() throws InterruptedException {
        synchronized (buffer) {
            while (buffer.isEmpty()) buffer.wait();
            String msg = buffer.remove();
            System.out.println("Consumed: " + msg);
            buffer.notifyAll();
        }
    }

    public static void main(String[] args) {
        ProducerConsumer pc = new ProducerConsumer();

        Runnable producer = () -> {
            for (int i = 1; i <= 10; i++)
                try { pc.produce("Msg " + i); Thread.sleep(100); } catch (Exception ignored) {}
        };

        Runnable consumer = () -> {
            for (int i = 1; i <= 10; i++)
                try { pc.consume(); Thread.sleep(150); } catch (Exception ignored) {}
        };

        new Thread(producer).start();
        new Thread(producer).start();
        new Thread(consumer).start();
        new Thread(consumer).start();
    }
}
