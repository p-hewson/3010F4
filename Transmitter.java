package transmitter;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Transmitter {
	private final static int PACKETSIZE = 100;
	private String url;
	private int port = 1010; // this value will change, but I am using this value for now

	public static void main(String[] args0) {
		Transmitter transmitter = new Transmitter();

	}

	public Transmitter() {
		recieve();

	}

	public ArrayList<Data> recieve() {

		ArrayList<Data> dataList = new ArrayList<Data>();
		DatagramSocket socket = null;
		try {

			socket = new DatagramSocket(port);

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean done = false;

			DatagramPacket packet = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
			try {
				
				socket.receive(packet);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ByteBuffer wrap = ByteBuffer.wrap(packet.getData()); // takes the Byte[] and makes an int out of it
			int num = wrap.getInt();
			if(num <=0 ) {
				
			}else {
				try {
			dataList = CollectData(num,socket);	
				}catch(Exception e) {
					
				}
			}
			
		

		return null;
	}

	public void Transmit() {

	}
	// CollectData loops i times and takes in the number of Data Packets that the Arduino sends and trys to put them into
	// a data structure 
	public ArrayList<Data> CollectData(int i, DatagramSocket socket) throws IOException {
			int id=0;
			int tilt=0;
			int temp=0;
			long time = 0;
			ArrayList<Data> dataList = new ArrayList<Data>();
			byte[] b = new byte[1];
			byte[] bFalse = new byte[1];
			b[0] = 1;
			bFalse[0]=0;
			DatagramPacket ack = new DatagramPacket(b,1);
			DatagramPacket bad = new DatagramPacket(b,1);
			DatagramPacket pack = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
			socket.send(ack);
			for(int l=0; l<i;l++) {
				// if good is false then the data collect on this loop is ignored because one of the numbers is a wrong value
				boolean good = true;
				// gets the number from the receiver program
				 try {
					id = getNumber(socket);
					time = getNumber(socket);
					temp = getNumber(socket);
					tilt = getNumber(socket);
				 }catch(Exception e) {
					 
				 }
				 // checks to make sure that the data taken in is valid and within a reasonable range
				 // shouldn't be a problem becasue both programs are on the same machine, but better safe then sorry
				 if(id<=0) {
					 good = false;
				 }
				 if(time<=0) {
					 good = false;
				 }
				 if(tilt<=0) {
					 good = false;
				 }
				 if(temp<=0) {
					 good = false;
				 }
				 // if 
				 
				 if(good) {
					 Data d = new Data(id,time,temp,tilt);
					 dataList.add(d);
				 }else {
					 l--;
				 }
				
			}
			return dataList;
	}
	
	
	public int getNumber(DatagramSocket s) throws Exception {
		int num;
		DatagramPacket pack = new DatagramPacket(new byte[PACKETSIZE],PACKETSIZE);
		s.receive(pack);
		byte [] number = pack.getData();
		ByteBuffer wrapped = ByteBuffer.wrap(number);
		num = wrapped.getInt();
		return num;
	}
	
}
