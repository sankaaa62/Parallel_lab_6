import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private static int serverPort;
    private static String address;

    private static Socket clientSocket; //сокет для общения
    private static BufferedReader keyboard; // ридер читающий с консоли
    private static DataInputStream in; // поток чтения из сокета
    private static DataOutputStream out; // поток записи в сокет

    public static void main(String[] ar) {
        serverPort = 4004;
        address = "localhost";


        try {
            InetAddress ipAddress = InetAddress.getByName(address); // создаем объект IP-адрес.
            clientSocket = new Socket(ipAddress, serverPort); // создаем сокет

            // Запускаем потоки
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

            // Создаем поток для чтения с клавиатуры.
            keyboard = new BufferedReader(new InputStreamReader(System.in));
            String line = null;
            System.out.println("Сервер способенн формировать массив случайных чисел заданной длины и считать его сумму в многопоточном режиме.");
            System.out.println();
            System.out.println("Задайте размер массива числом и нажмите Enter.");
            System.out.println();

            while (true) {
                line = keyboard.readLine(); // ждем пока пользователь введет что-то и нажмет кнопку Enter.
                System.out.println();

                System.out.println("Отправка запроса серверу...");
                out.writeUTF(line); // отсылаем введенную строку текста серверу.
                out.flush(); // заставляем поток закончить передачу данных.
                System.out.println("Отправка завершена.");
                System.out.println();

                System.out.println("Ожидание ответа...");
                line = in.readUTF(); // ждем пока сервер отошлет строку текста.
                System.out.println("Ответ получен.");
                System.out.println();

                System.out.println("Сумма массива: " + line);

                System.out.println();
                System.out.println("Задайте размер следующего массива.");
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
