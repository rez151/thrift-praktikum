import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;



public class Server
{

    private TServer server;
    private int port;

    public static void main(String[] argus) throws TTransportException {
        int port = 4711;
        Server easyThriftServer = new Server(port);
        easyThriftServer.start();
    }

    private void start() {
        System.out.println("Starting server on port " + port);
        this.server.serve();
    }

    public Server(int port) throws TTransportException {
        this.port = port;

        TServerSocket socket = new TServerSocket(port);
        ThriftService.Iface impl=new ThriftServiceImpl();
        ThriftService.Processor<ThriftService.Iface> processor = new ThriftService.Processor<ThriftService.Iface>(impl);
        TThreadPoolServer.Args args = new TThreadPoolServer.Args(socket).processor(processor);

        this.server = new TThreadPoolServer(args);
        System.out.println("Server created on port " + port);
    }
}
