import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class NonBlockingClient {

    private TTransport transport;
    private ThriftService.Client client;


    private NonBlockingClient(String ip, int port) {
        transport = new TFramedTransport(new TSocket("192.168.162.128", 4711));
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new ThriftService.Client(protocol);
    }

    private void open() throws TTransportException {
        this.transport.open();
    }

    private void close() {
        this.transport.close();
    }

    private int add(int zahl1, int zahl2) throws TException {
        return client.add(zahl1, zahl2);
    }

    private int multiply(int zahl1, int zahl2) throws TException {
        return client.multiply(zahl1, zahl2);
    }



    public static void main(String[] args) throws TException {

        String ip = "192.168.162.128";
        int port = 4711;

        NonBlockingClient client = new NonBlockingClient(ip, port);

        client.open();

        int zahl1 = 12;
        int zahl2 = 5;

        int add = client.add(zahl1, zahl2);
        int multiply = client.multiply(zahl1, zahl2);

        System.out.println("add     : " + add);
        System.out.println("multiply: " + multiply);

        client.close();
    }

}