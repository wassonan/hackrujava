/*Wasson An
 * Pd. 8
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class WassonAnWebServer {

	//0 param constructor
	public WassonAnWebServer(){

		ServerSocket server = null; //the socket for the server
		String ip = null;

		try{

			server = new ServerSocket(8080);

			Enumeration en = NetworkInterface.getNetworkInterfaces();
			while(en.hasMoreElements()){
			    NetworkInterface ni=(NetworkInterface) en.nextElement();
			    Enumeration ee = ni.getInetAddresses();
			    while(ee.hasMoreElements()) {
			        InetAddress ia= (InetAddress) ee.nextElement();
			        System.out.println(ia.getHostAddress());
			    }
			 }

			System.out.println("IP Adress: " + InetAddress.getLocalHost().getHostAddress());
			System.out.println("Port Number: " + server.getLocalPort());
		}catch(Exception e){

			System.out.println("Server not set up. #1");
			System.exit(-1);
		}//catch

		//infinite loop
		for(;;){
			try{

				new Thread(new Client(server.accept())).start(); //the thread for the client
			}catch(Exception e){

				System.exit(-1);
			}//catch

		}//for

	}//default constructor


	class Client implements Runnable{

		Socket sock; //the socket for the client
		Scanner in; //the Scanner reading in infromation
		DataOutputStream out; //the output stream
		PrintWriter pOut;

		public Client(Socket s){

			try{

				sock = s;
				in = new Scanner(s.getInputStream());
				out = new DataOutputStream(sock.getOutputStream());
				pOut = new PrintWriter(sock.getOutputStream());

			}catch(Exception e){

				System.out.println("Problem initializing Client. #3");
				System.exit(-1);
			}//catch
		}//Socket constructor

		//what runs when the thread is created
		public void run(){

			String request = in.nextLine(); //the request from the web browser
			String method = request.substring(0, request.indexOf(" ")); //what the web browser wants

			//find out what type of command
			if(method.equals("GET")){

				String fileName = request.substring(request.indexOf(" "), request.indexOf("HTTP") - 1); //the file name
				fileName = fileName.substring(2);

				File file = null; //the file the client requested

				file = new File(fileName);
				FileInputStream fIn = null;

				try{
					fIn = new FileInputStream(file);
				}catch(FileNotFoundException e){

					pOut.write("HTTP/1.1 404 File Not Found\r\n");
					pOut.write("\r\n");
					pOut.write("HTTP/1.1 404 File Not Found\r\n");
					pOut.write("\r\n");
					pOut.close();
					return;
				}//catch

				byte[] buffer = new byte[1024];//the array of bytes to be filled and sent

				pOut.write("HTTP/1.1 200 OK\r\n");
				pOut.write("Connection: close\r\n");
				pOut.write("Content-Length: " + file.length() + "\r\n");
				pOut.write("\r\n");
				pOut.flush();

				try{

					int bytesRead;
					while ((bytesRead = fIn.read(buffer)) != -1 ) {

						out.write(buffer, 0, bytesRead);
					}//while

					out.flush();
					sock.close();
				}catch(IOException e){
					System.out.println("Trouble reading and sending file. #5");
					System.out.println(e);
				}//catch
			}//if GET

			else if(method.equals("HEAD")){

				String fileName = request.substring(request.indexOf(" "), request.indexOf("HTTP") - 1); //the file name
				fileName = fileName.substring(2);

				File file = null; //the file the client requested
				file = new File(fileName);
				FileInputStream fIn = null;

				try{
					fIn = new FileInputStream(file);
				}catch(FileNotFoundException e){

					pOut.write("HTTP/1.1 404 File Not Found\r\n");
					pOut.write("\r\n");
					pOut.flush();
					return;
				}//catch

				pOut.write("HTTP/1.1 200 OK\r\n");
				pOut.write("Connection: close\r\n");
				pOut.write("Content-Length: " + file.length() + "\r\n");
				pOut.write("" + file.lastModified() + "\r\n");
				pOut.flush();
				return;
			}//else if HEAD

			else if(method.equals("POST")){

				return;
			}//else if POST

			else{

				pOut.write("HTTP/1.1 400 Bad Request \r\n");
				pOut.write("\r\n");
				return;
			}//else
		}//run
	}//Client


	//the Driver
	public static void main(String[] args){

		WassonAnWebServer w = new WassonAnWebServer();
	}//main
}//WebServer