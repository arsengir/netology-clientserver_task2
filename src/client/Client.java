package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 23444;
    private static final int SIZE_BUFFER = 1024;

    public static void main(String[] args) throws IOException {

        final InetSocketAddress socketAddress = new InetSocketAddress(HOST, PORT);
        final SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(socketAddress);

        try (Scanner scanner = new Scanner(System.in)) {
            final ByteBuffer inputBuffer = ByteBuffer.allocate(SIZE_BUFFER);
            while (true) {
                System.out.println("Введите строку для обработки(или 'end' для выхода):");
                String msg = scanner.nextLine() + "\n";
                if ("end\n".equals(msg)) {
                    break;
                }
                socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));

                StringBuilder stringBuilder = new StringBuilder();
                while (true) {
                    final int bytesCount = socketChannel.read(inputBuffer);
                    final String s = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                    inputBuffer.clear();
                    stringBuilder.append(s);
                    if (s.endsWith("\n")) {
                        System.out.print("Обработанная строка: " + stringBuilder);
                        break;
                    }
                }
                System.out.println();
            }
        } finally {
            socketChannel.close();
        }
    }
}
