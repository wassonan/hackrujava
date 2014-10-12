import java.util.Random;

/* Wasson An
 * This is the main driver class
 */

public class Driver {
	
	public static Broadcaster b;
	public static String ip;
	public static int port;

	public static void main(String[] args){
		
		initialize();
	} //main
	
	public static void initialize(){
		
		startGUI();
		startServer();
		setupBroadcast();
		startSending();
	} //initialize
	
	//starts the GUI
	public static void startGUI(){
		
	} //startGUI
	
	//sets up Broadcaster
	public static void setupBroadcast(){
		
		b = new Broadcaster("224.0.0.1", 48317, ip + " " + port);
		System.out.print(ip + " " + port + "\n");
	} //setupBroadcast
	//starts sending information to multicast address
	public static void startSending(){

		b.start();
	} //startSending
	
	// sets up the server accepting connections
	public static void startServer(){
		
		Random r = new Random();
		port = r.nextInt(1000) + 43210;
		port = 8080;
		final Server s = new Server(port);
		ip = s.getIP();
		
		Thread t;

		try{
			
			t = new Thread(new Runnable(){
				
				public void run(){
					
					System.out.println("Starting Server");
					s.start();
				} //run
			});
			
			t.start();
		} catch(Exception e){
		} //try catch
	} //startServer
} //Driver
