import sys

# your gen-py dir
sys.path.append('gen-py')

# Example files
from ThriftService import ThriftService
from ThriftService.ttypes import *

# Thrift files 
from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

try:
    host = "192.168.162.128"
    port = 4712
    zahl1 = 3
    zahl2 = 4
    # Init thrift connection and protocol handlers

    transport = TSocket.TSocket(host, port)
    transport = TTransport.TFramedTransport(transport)
    protocol = TBinaryProtocol.TBinaryProtocol(transport)

    # Set client to our Example
    client = ThriftService.Client(protocol)

    # Connect to server
    transport.open()

    # Run showCurrentTimestamp() method on server
    sum = client.add(zahl1, zahl2)
    product = client.multiply(zahl1, zahl2)

    print 'Summe  : ', zahl1, ' + ', zahl2, " = ", sum
    print 'Produkt: ', zahl1, ' * ', zahl2, " = ", product

    # Assume that you have a job which takes some time
    # but client sholdn't have to wait for job to finish 
    # ie. Creating 10 thumbnails and putting these files to sepeate folders
    # client.asynchronousJob()

    # Close connection
    transport.close()

except Thrift.TException, tx:
    print 'Something went wrong : %s' % (tx.message)
