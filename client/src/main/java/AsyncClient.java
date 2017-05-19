import org.apache.thrift.TException;

import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.*;

import java.io.IOException;

public class AsyncClient {

    private String ip;
    private int port;

    private ThriftService.AsyncClient client;

    private AsyncClient(String ip, int port) throws IOException, TException, InterruptedException {

        this.ip = ip;
        this.port = port;

        TBinaryProtocol.Factory factory = new TBinaryProtocol.Factory();
        TAsyncClientManager manager = new TAsyncClientManager();
        TNonblockingSocket socket = new TNonblockingSocket(this.ip, this.port);

        client = new ThriftService.AsyncClient(factory, manager, socket);
    }


    private void add(int zahl1, int zahl2) throws TException, IOException, InterruptedException {

        client.add(zahl1, zahl2, new AddMethodCallback());

        Thread.sleep(1000);

    }

    private void multiply(int zahl1, int zahl2) throws TException, IOException, InterruptedException {

        client.multiply(zahl1, zahl2, new MultiplyMethodCallback());

        Thread.sleep(1000);

    }

    public static void main(String[] args) throws TException, IOException, InterruptedException {

        String ip = "192.168.162.128";
        //String ip = "localhost";
        int port = 4711;

        AsyncClient client = new AsyncClient(ip, port);

        int zahl1 = 12;
        int zahl2 = 5;

        client.add(zahl1, zahl2);
        client.multiply(zahl1, zahl2);
        client.add(5, 5);
        client.multiply(5, 5);
        client.add(50, 50);
        client.multiply(5, 7);

    }


    private class AddMethodCallback implements AsyncMethodCallback<ThriftService.AsyncClient.add_call> {
        public void onComplete(ThriftService.AsyncClient.add_call response) {
            try {
                int result = response.getResult();
                System.out.println("Add: " + result);
            } catch (TException e) {
                e.printStackTrace();
            }
        }

        public void onError(Exception exception) {
            System.out.println("Add Error");
        }
    }

    private class MultiplyMethodCallback implements AsyncMethodCallback<ThriftService.AsyncClient.multiply_call> {
        public void onComplete(ThriftService.AsyncClient.multiply_call response) {
            try {
                int result = response.getResult();
                System.out.println("Multiply: " + result);
            } catch (TException e) {
                e.printStackTrace();
            }

        }

        public void onError(Exception exception) {
            System.out.println("Multiply Error");

        }
    }
}

