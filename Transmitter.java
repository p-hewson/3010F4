package transmitter;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Transmitter  {
	private final static int PACKETSIZE = 100;
	private String url;
	InetAddress host ;
	private int port = 1021; // this value will change, but I am using this value for now
	private DatagramSocket socket = null;
	
	public static void main(String[] args0) {
		Transmitter transmitter = new Transmitter();

	}

	public Transmitter() {
		try {
			host = InetAddress.getByName("127.0.0.1") ;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		recieve();

	}

	
	public ArrayList<Data> recieve() {
		
		ArrayList<Data> dataList = new ArrayList<Data>();
		
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
			System.out.println("got "+ num);
			if(num <=0 ) {
				byte[] b = new byte[1];
				b[0]= 0;
			}else {
				try {
			dataList = CollectData(num,socket,packet.getPort());	
				}catch(Exception e) {
					
				}
			}
			
		

		return null;
	}

	public void Transmit() {

	}

	// CollectData loops i times and takes in the number of Data Packets that the
	// Arduino sends and trys to put them into
	// a data structure
	public ArrayList<Data> CollectData(int i, DatagramSocket socket,int other_port) throws IOException {
		String end;
		int id = 0;
		int tilt = 0;
		int temp = 0;
		long time = 0;
		ArrayList<Data> dataList = new ArrayList<Data>();
		byte[] b = new byte[1];
		byte[] bFalse = new byte[1];
		b[0] = 1;
		bFalse[0] = 0;
		DatagramPacket ack = new DatagramPacket(b, 1,host,other_port);
		DatagramPacket bad = new DatagramPacket(b, 1,host,other_port);
		DatagramPacket pack = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
		System.out.println("Before send");
		socket.send(ack);
		System.out.println("before the other sends");
		for (int l = 0; l < i; l++) {
			// if good is false then the data collect on this loop is ignored because one of
			// the numbers is a wrong value
			boolean good = true;
			// gets the number from the receiver program
			// checks to make sure that the data taken in is valid and within a reasonable
			// range
			// shouldn't be a problem becasue both programs are on the same machine, but
			// better safe then sorry
			
			//creates an exception that can be thrown and caught if bad things happen
			Exception wrong = new Exception();
			try {
				// getting and checking id
				id = getNumber(socket);

				if (id <= 0) {
					socket.send(bad);
					good = false;
					throw wrong;
				} else if(id==(Integer)null){
					good = false;
					socket.send(bad);
					throw wrong;
				}else {
				
					socket.send(ack);
				}
				// getting and checking the timestamp
				time = getNumber(socket);
				if (time <= 0) {
					good = false;
					socket.send(bad);
					throw wrong;
				} else if(time==(Integer)null){
					good = false;
					socket.send(bad);
					throw wrong;
				}else {
					socket.send(ack);
				}
				// getting and checking the temperature value
				temp = getNumber(socket);
				if (temp <= 0||temp>65536) {
					good = false;
					socket.send(bad);
					throw wrong;
				} else if(id==(Integer)null){
					good = false;
					socket.send(bad);
					throw wrong;
				}else {
					socket.send(ack);
				}
				// getting and checking the tilt value
				tilt = getNumber(socket);
				if (tilt <= 0||temp>65536) {
					good = false;
					socket.send(bad);
					throw wrong;
				} else if(id==(Integer)null){
					good = false;
					socket.send(bad);
					throw wrong;
				}else {
					socket.send(ack);
				}
				DatagramPacket p = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
				socket.receive(p);
				end = p.toString();
				if(end.equals("end")) {
					socket.send(ack);
				}else {
					good = false;
					socket.send(bad);
					throw wrong;
				}
			} catch (Exception e) {
				// TODO need to figure out how to deal with this kind of error
				System.out.println("bad");
			}

			// if

			if (good) {
				Data d = new Data(id, time, temp, tilt);
				dataList.add(d);
			} else {
				l--;
			}

		}
		socket.close();
		return dataList;
	}

	public int getNumber(DatagramSocket s) throws Exception {
		int num;
		DatagramPacket pack = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
		s.receive(pack);
		if(pack.getData().toString().equals("end")) {
			return (Integer)null;
		}
		byte[] number = pack.getData();
		ByteBuffer wrapped = ByteBuffer.wrap(number);
		num = wrapped.getInt();
		return num;
	}

	public void transmit(ArrayList<Data> dataList) {
			
	}


}
