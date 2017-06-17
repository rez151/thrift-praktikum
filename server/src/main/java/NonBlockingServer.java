import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;


public class NonBlockingServer {

    private TServer server;
    private int port;

    public static void main(String[] argus) throws TTransportException {
        int port;

        try {
            port = Integer.parseInt(argus[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("using standardport on 4712");
            port = 4712;
        }

        NonBlockingServer nonBlockServer = new NonBlockingServer(port);
        nonBlockServer.start();
    }

    private void start() {
        System.out.println("Starting nonblocking server on port " + port);
        this.server.serve();
    }

    public NonBlockingServer(int port) throws TTransportException {
        this.port = port;

        TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(port);
        ThriftService.Processor<ThriftServiceImpl> processor = new ThriftService.Processor<ThriftServiceImpl>(new ThriftServiceImpl());

        this.server = new TNonblockingServer(new TNonblockingServer.Args(serverTransport).
                processor(processor));

        System.out.println("Nonblocking server created on port " + port);
    }
}
