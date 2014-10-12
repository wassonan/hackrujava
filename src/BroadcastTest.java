
import java.net.*;
import java.io.*;

public class BroadcastTest {

	public static void main(String[] args) {
	      if (args.length < 2) {
	         System.out.println
	            ("Usage: java MulticastListener 224.0.0.1 Herong");
	         //return;
	      }
	      String ip = "224.0.0.1";
	      String name = "Wasson An";
	      int port = 48317;
	      try {
	         InetAddress group = InetAddress.getByName(ip);
	         MulticastSocket s = new MulticastSocket(port);
	         s.joinGroup(group);

	         String msg = "Greeting from "+name;
	         System.out.println("Sending out: "+msg);
	         DatagramPacket data = new DatagramPacket(
	            msg.getBytes(), msg.length(), group, port);
	         s.send(data);
	         
	         byte[] buffer = new byte[10*1024];
	         data = new DatagramPacket(buffer, buffer.length);
	         while (true) {
	            s.receive(data);
	            System.out.println("Received: "+ 
	               (new String(buffer, 0, data.getLength())));
	         }
	      } catch (IOException e) {
	         System.out.println(e.toString());
	      }
	   }
}
