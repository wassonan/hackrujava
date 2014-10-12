/* Wasson An
 * This class broadcasts the message
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Broadcaster {

	private boolean cont; //whether or not to continue broadcasting
	private String ip; //the ip address to broadcast at
	private int port; //the the port to broadcast at
	private String message; //the message to send
	private InetAddress group = null;
	private MulticastSocket sock = null;
	private DatagramPacket data = null;
	private Thread send;

	private Object lock = new Object();

	//String, int, String constructor
	public Broadcaster(String i, int p, String m){

		cont = false;
		ip = i;
		port = p;
		message = m;
		
		send = new Thread(new Sender());

		try{

			group = InetAddress.getByName(ip);
			sock = new MulticastSocket(port);
			sock.joinGroup(group);
			data = new DatagramPacket(message.getBytes(), message.length(),
					group, port);
			send.start();
		} catch(IOException e){

		} //try catch

	} //3 param constructtor

	//starts the Broadcaster
	public void start(){

		cont = true;
	} //start
	
	//stops the Broadcaster from sending
	public void stop(){
		
		cont = false;
	} //stop

	private class Sender implements Runnable{

		public void run(){

			try{
				for(;;){
					if(cont){

						//System.out.println("Sending Message");
						sock.send(data);
					} //if
					
					Thread.sleep(1000);
				} //for
			} catch(Exception e){

				e.printStackTrace();
			} //try catch
		} //run
	} //Sender
} //Broadcaster
