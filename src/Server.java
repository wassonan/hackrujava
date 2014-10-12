
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

/* Wasson An
 * This is the server that handles requests
 */

public class Server {

	private String ip; 
	private int port;
	private ServerSocket server = null;

	public Server(int p){

		try{

			server = new ServerSocket(p);

			Enumeration en = NetworkInterface.getNetworkInterfaces();
			while(en.hasMoreElements()){
				NetworkInterface ni=(NetworkInterface) en.nextElement();
				Enumeration ee = ni.getInetAddresses();
				while(ee.hasMoreElements()) {
					InetAddress ia= (InetAddress) ee.nextElement();
					if(ia.getHostAddress().contains("192"))
						ip = ia.getHostAddress();
				}
			}

			port = server.getLocalPort();
			System.out.println("IP Address: " + ip);
			System.out.println("Port Number: " + server.getLocalPort());

		} catch(Exception e){

			System.out.println("Server not set up");
			return;
		} //try catch
	} //default constructor

	public void start(){

		for(;;){

			try{

				System.out.println("Waiting for client");
				new Thread(new Client(server.accept())).start();
				System.out.println("Got Client");
				System.out.flush();
			}catch(Exception e){

				System.out.println("Something dun fucked up");
			} //try catch
		} //for
	} //start

	public String getIP(){

		return ip;
	} //getIP

	public int getPort(){

		return port;
	} //getPort

	class Client implements Runnable{

		Socket sock;
		Scanner in;
		OutputStream out;
		PrintWriter pOut;

		public Client(Socket s){

			sock = s;
			try {
				in = new Scanner(sock.getInputStream());
				out = sock.getOutputStream();
				pOut = new PrintWriter(sock.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} //Client

		public void run(){

			try{
				System.out.println("Someone Connected");

				ArrayList<Mixer.Info> stuff;
				stuff = new ArrayList<Mixer.Info>(Arrays.asList(AudioSystem.getMixerInfo()));
				Mixer.Info def = null;

				for(Mixer.Info m : stuff){

					System.out.println(m.getName());
					if(m.getName().contains("default"))
						def = m;
				} //for

				System.out.println(def.toString());

				float sampleRate = 8000;
				int sampleSizeInBits = 8;
				int channels = 1;
				boolean signed = true;
				boolean bigEndian = true;
				AudioFormat format =  new AudioFormat(sampleRate, 
						sampleSizeInBits, channels, signed, bigEndian);

				TargetDataLine tdl = null;
				tdl = AudioSystem.getTargetDataLine(format, def);
				tdl.open(format);
				
				int framesize = format.getFrameSize();
				int numframes = 1024 / framesize;
				byte[] buffer = new byte[numframes * framesize];
				int offset = 0;
				
				for(;;){
				
					tdl.read(buffer, offset, buffer.length);
					out.write(buffer);
					out.flush();
					offset += buffer.length;
				} //for

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} //run
	} //Client
} //Server
