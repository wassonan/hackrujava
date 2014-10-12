

import java.io.OutputStream;
import java.util.ArrayList;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.Operation;
import javax.obex.ResponseCodes;


public class BluetoothTest {

	private static Object lock = new Object();
	private static Object arrayLock = new Object();
	private static ArrayList<RemoteDevice> deviceids = new ArrayList<RemoteDevice>();

	public static void main(String[] args){

		BluetoothTest bt= new BluetoothTest();
		bt.connect();
		//bt.connect2();
	} //main

	//hopefully connect via bluetooth
	public void connect(){

		try{

			LocalDevice localDevice = LocalDevice.getLocalDevice();
			DiscoveryAgent agent = localDevice.getDiscoveryAgent();
			agent.startInquiry(DiscoveryAgent.GIAC, new MyDiscoveryListener());

			try{
				synchronized(lock){
					lock.wait();
				}
			} catch(InterruptedException e) {

				e.printStackTrace();				
			} //try catch

			System.out.println("Device Inquiry Completed.");

		}catch(Exception e){

			e.printStackTrace();
			System.exit(-1);
		} //catch

	} //connect

	public void connect2(){

		UUID[] uuidSet = new UUID[1];
		uuidSet[0] = new UUID (0x1105);

		int[] attrIDs = new int[] {
				0x0100
		};

		LocalDevice localDevice = null;
		try {
			localDevice = LocalDevice.getLocalDevice();
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}
		DiscoveryAgent agent = localDevice.getDiscoveryAgent();

		for(RemoteDevice r : deviceids){

			try {
				agent.searchServices(null, uuidSet, r, new MyDiscoveryListener());
				synchronized(lock){
					lock.wait();
				}
			} catch (BluetoothStateException | InterruptedException e) {
				e.printStackTrace();
			}
		} //for
	} //connect2

	private static void sendMessageToDevice(String serverURL) {

		try{

			System.out.println("Connecting to " + serverURL);
			ClientSession clientSession = (ClientSession)Connector.open(serverURL);
			HeaderSet hsConnectReply = clientSession.connect(null);

			if(hsConnectReply.getResponseCode() != ResponseCodes.OBEX_HTTP_OK){
				System.out.println("Failed to connect");
				return;
			} //if

			HeaderSet hsOperation = clientSession.createHeaderSet();
			hsOperation.setHeader(HeaderSet.NAME, "Hello.txt");
			hsOperation.setHeader(HeaderSet.TYPE, "text");

			Operation putOperation = clientSession.put(hsOperation);

			byte data[] = "Hello World !!!".getBytes("iso-8859-1");

			OutputStream os = putOperation.openOutputStream();
			os.write(data);
			os.close();

			putOperation.close();
			clientSession.disconnect(null);
			clientSession.close();
		} catch(Exception e){
			e.printStackTrace();
		} //try catch
	} //sendMessageToDevice

	public class MyDiscoveryListener implements DiscoveryListener{

		public void deviceDiscovered(RemoteDevice btDevice, DeviceClass arg1){

			String name;

			try{
				name = btDevice.getFriendlyName(false) + ": " + btDevice.getBluetoothAddress();
			} catch (Exception e) {

				name = btDevice.getBluetoothAddress();
			} //try catch

			System.out.println("device found: " + name);

			synchronized(arrayLock){

				deviceids.add(btDevice);
			} //synchronized
		} //device Discovered

		public void inquiryCompleted(int arg0) {

			synchronized(lock){
				lock.notify();
			}
		} //inquiryCompleted

		public void serviceSearchCompleted(int arg0, int arg1){

			synchronized(lock){
				lock.notify();
			}
		} //serviceSearchCompleted

		public void servicesDiscovered(int arg0, ServiceRecord[] services) {

			for(int i = 0; i < services.length; i++){

				String url = services[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT,
						false);

				if(url == null){
					continue;
				}

				DataElement serviceName = services[i].getAttributeValue(0x0100);
				if(serviceName != null) {
					System.out.println("service " + serviceName.getValue() + " found " + url);
					if(serviceName.getValue().equals("OBEX Object Push")){

						sendMessageToDevice(url);
					} //if
				} else{

					System.out.println("service found " + url);
				} //else
			} //for
		} //serviceDiscovered
	} //MyDiscoveryListener
} //BluetoothTest
