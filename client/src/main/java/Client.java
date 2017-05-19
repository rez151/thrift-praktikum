import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class Client {


    public static void main(String[] args) throws TException {
        TTransport transport;


        transport = new TFramedTransport(new TSocket("localhost", 4711));
        TProtocol protocol = new TBinaryProtocol(transport);
        ThriftService.Client client = new ThriftService.Client(protocol);
        transport.open();
        int add = client.add(1, 3);
        int multiply = client.multiply(10, 3);

        System.out.println("add: " + add);
        System.out.println("multiply: " + multiply);

        transport.close();
    }
}
