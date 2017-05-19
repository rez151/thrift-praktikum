import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;



public class Server
{
    public static void main(String[] argus) throws TTransportException {

        TServerSocket socket = new TServerSocket(4712);
        ThriftService.Iface impl=new ThriftServiceImpl();
        ThriftService.Processor<ThriftService.Iface> processor = new ThriftService.Processor<ThriftService.Iface>(impl);

        TThreadPoolServer.Args args = new TThreadPoolServer.Args(socket).processor(processor);
        TServer server = new TThreadPoolServer(args);

        System.out.println("1: Starting nonblocking server on port 4711 ...");
        server.serve();
    }
}
