import org.apache.thrift.TException;

/**
 * Created by reserchr on 18.05.17.
 */
public class ThriftServiceImpl implements ThriftService.Iface {
    public int add(int zahl1, int zahl2) throws TException {
        return zahl1 + zahl2;
    }

    public int multiply(int zahl1, int zahl2) throws TException {
        return zahl1 * zahl2;
    }
}
