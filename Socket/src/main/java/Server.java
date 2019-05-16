import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server {
    private final static int THREAD_POOL_SIZE = 5;

    private static Socket clientSocket; //сокет для общения
    private static ServerSocket server; // серверсокет
    private static DataInputStream in; // поток чтения из сокета
    private static DataOutputStream out; // поток записи в сокет

    public static void main(String[] ar)    {
        int port = 4004;
        try {
            server = new ServerSocket(port);
            System.out.println("Сервер запущен!");
            System.out.println();

            clientSocket = server.accept();
            System.out.println("Соединение установлено.");
            System.out.println();

            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

            String line = null;
            while(true) {
                line = in.readUTF();
                System.out.println("Получено сообщение : " + line + ".");
                System.out.println();
                final int count = Integer.parseInt(line);

                System.out.println("Формирование массива случайных чисел заданного размера (" + count + "):");

                double[] massive = new double[count];
                for (int i = 0; i < count; i++){
                    massive[i] = Math.random() * 1000;
                    System.out.println(i + " элемент: " + massive[i]);
                }
                System.out.println();


                System.out.println("Вычисление суммы массива с использованием " + THREAD_POOL_SIZE + " потоков.");

                ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
                double[] sumArray = new double[THREAD_POOL_SIZE];
                int intrvalSize = count / THREAD_POOL_SIZE;
                for (int i = 0; i < THREAD_POOL_SIZE; i++) {
                    final int thread_part = i;
                    executorService.execute (() -> {
                        for (int j = thread_part * intrvalSize; j < (thread_part + 1) * intrvalSize; j++) {
                            sumArray[thread_part] += massive[j];
                        }
                    });
                }
                executorService.shutdown();
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

                double sum = 0;
                for (double el : sumArray){
                    sum += el;
                }
                System.out.println("Вычисленно значение суммы: (" + sum + ").");

                System.out.println();
                System.out.println("Отправка результата.");
                out.writeUTF(Double.toString(sum));
                out.flush();

                System.out.println("Результат отправлен клиенту.");

                System.out.println();
                System.out.println("Ожидается следующее сообщение...");
                System.out.println();
            }
        } catch(Exception x) { x.printStackTrace(); }
    }
}
