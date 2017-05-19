import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;



public class Server
{
    public static void main(String[] argus) throws TTransportException {

        TNonblockingServerTransport transport = new TNonblockingServerSocket(4711);
        ThriftService.Iface impl=new ThriftServiceImpl();
        ThriftService.Processor<ThriftService.Iface> processor = new ThriftService.Processor<ThriftService.Iface>(impl);

        TNonblockingServer.Args args = new TNonblockingServer.Args(transport).processor(processor);
        TServer server = new TNonblockingServer(args);

        System.out.println("1: Starting nonblocking server on port 4711 ...");
        server.serve();
    }
}
