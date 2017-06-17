import org.apache.thrift.TException;

import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.*;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Scanner;

public class AsyncClient implements Watcher {
    private String ip;
    private String port;

    private ThriftService.AsyncClient client;
    private ZooKeeper zk;
    private Watcher dataWatcher = new Watcher() {
        public void process(WatchedEvent watchedEvent) {
            System.out.println("Data Watcher = " + watchedEvent);
            connected = false;
            getMyData();
        }
    };
    private boolean connected;

    private void getMyData() {
        zk.getData("/MyThriftService", dataWatcher, dataCallback, null);
    }

    private AsyncCallback.DataCallback dataCallback = new AsyncCallback.DataCallback() {
        @Override
        public void processResult(int returnCode, String path,
                                  Object context, byte[] data, Stat stat) {
            String serviceSocket = new String(data);
            System.out.println("Service Socket = " + serviceSocket);

            String ip = serviceSocket.substring(0, 15);
            String port = serviceSocket.substring(16);

            try {
                connect(ip, Integer.parseInt(port));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TException e) {
                e.printStackTrace();
            }
        }
    };

    AsyncClient() throws IOException, TException, InterruptedException {
        this.connected = false;

        String zooConnect = "192.168.162.128:2181";
        zk = new ZooKeeper(zooConnect, 15000, this);
        getMyData();
    }

    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent);
    }

    void connect(String ip, int port) throws IOException, TException, InterruptedException {

        System.out.println("Thrift Client: Connect to " + ip + ":" + port);

        TBinaryProtocol.Factory factory = new TBinaryProtocol.Factory();
        TAsyncClientManager manager = new TAsyncClientManager();
        TNonblockingSocket socket = new TNonblockingSocket(ip, port);

        client = new ThriftService.AsyncClient(factory, manager, socket);
        connected = true;
        calc();
    }

    void calc() throws InterruptedException, TException, IOException {
        Scanner scan = new Scanner(System.in);


        int zahl1;
        int zahl2;

        while (connected) {
            System.out.println("a: Add");
            System.out.println("m: Multiply");

            String operation = scan.nextLine();
            while (!operation.equals("a") && !operation.equals("m")) {
                operation = scan.nextLine();
            }

            System.out.print("first number: ");
            zahl1 = scan.nextInt();
            System.out.print("second number: ");
            zahl2 = scan.nextInt();

            if (operation.equals("a"))
                add(zahl1, zahl2);
            if (operation.equals("m"))
                multiply(zahl1, zahl2);
        }

    }

    void add(int zahl1, int zahl2) throws TException, IOException, InterruptedException {

        client.add(zahl1, zahl2, new AddMethodCallback());

        Thread.sleep(1000);

    }


    void multiply(int zahl1, int zahl2) throws TException, IOException, InterruptedException {

        client.multiply(zahl1, zahl2, new MultiplyMethodCallback());

        Thread.sleep(1000);
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
            connected = false;
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
            connected = false;
        }
    }
}

