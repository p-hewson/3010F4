package transmitter;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.mysql.cj.api.jdbc.Statement;
import com.mysql.cj.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;

public class Transmitter {
	private final static int PACKETSIZE = 100;
	private String url;
	InetAddress host;
	private int port = 1021; // this value will change, but I am using this value for now
	private DatagramSocket socket = null;
	private Connection connect = null;
	private java.sql.Statement statement = null;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet = null;
	
	public static void main(String[] args0) {
		Transmitter transmitter = new Transmitter();

	}

	public Transmitter() {
		// will nead to find a way to not hard code this
		String website = "null.com";
		try {
			host = InetAddress.getByName("127.0.0.1");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Data> dataList =recieve() ;
		transmit(dataList,website);
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
		System.out.println("got " + num);
		if (num <= 0) {
			byte[] b = new byte[1];
			b[0] = 0;
		} else {
			try {
				dataList = CollectData(num, socket, packet.getPort());
			} catch (Exception e) {

			}
		}

		return null;
	}

	public void Transmit() {

	}

	// CollectData loops i times and takes in the number of Data Packets that the
	// Arduino sends and trys to put them into
	// a data structure
	public ArrayList<Data> CollectData(int i, DatagramSocket socket, int other_port) throws IOException {
		int id = 0;
		int tilt = 0;
		int temp = 0;
		long time = 0;
		ArrayList<Data> dataList = new ArrayList<Data>();
		byte[] b = new byte[1];
		byte[] bFalse = new byte[1];
		b[0] = 1;
		bFalse[0] = 0;
		DatagramPacket ack = new DatagramPacket(b, 1, host, other_port);
		DatagramPacket bad = new DatagramPacket(b, 1, host, other_port);
		DatagramPacket pack = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
		System.out.println("Before send");
		socket.send(ack);
		System.out.println("before the other sends");
		// the other program sends an int[] with id time temp and tilt in that order
		for (int l = 0; l < i; l++) {
			DatagramPacket p = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
			socket.receive(p);
			byte[] byteArray = p.getData();

			int[] dataSet = new int[byteArray.length / 4];
			ByteBuffer.wrap(byteArray).asIntBuffer().get(dataSet);
			boolean good = true;
			if (dataSet.length == 4) {
				id = dataSet[0];
				time = dataSet[1];
				temp = dataSet[2];
				tilt = dataSet[3];
				if (id < 0) {

					good = false;
				}
				time = dataSet[1];
				if (time < 0) {
					good = false;
				}
				temp = dataSet[3];
				if (temp < 0 && temp > 32768) {
					good = false;
				}
				tilt = dataSet[4];
				if (tilt < 0 && tilt > 32768) {
					good = false;
				}

			}else {
				good = false;
			}
			if(good) {
				Data d = new Data(id,time,temp,tilt);
				dataList.add(d);
			}
		}
		socket.close();
		return dataList;

	}

	public int getNumber(DatagramSocket s) throws Exception {
		int num;
		DatagramPacket pack = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
		s.receive(pack);
		if (pack.getData().toString().equals("end")) {
			return (Integer) null;
		}
		byte[] number = pack.getData();
		ByteBuffer wrapped = ByteBuffer.wrap(number);
		num = wrapped.getInt();
		return num;
	}

	public void transmit(ArrayList<Data> dataList, String website) {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://" + website);

			statement = connect.createStatement();
			for (Data data : dataList) {
				double tilt = data.getTilt();
				double temp = data.getTemp();
				int id = data.getId();
				int time = data.getId();

				String stmt = "INSERT INTO collected_data VALUES (" + id + " , " + time + "," + temp + " , " + tilt
						+ " );";
				java.sql.PreparedStatement preparedStmt = connect.prepareStatement(stmt);
				preparedStmt.execute();
			}

		} catch (Exception e) {

		}
	}

}
