package transmitter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class TestTransmiter implements Runnable {
	private final static int PACKETSIZE = 100;
	static DatagramSocket socket;
	private static int port = 1229;
	private static int other_port = 2045;
	static InetAddress host;
	static Thread thread; 
	
	public TestTransmiter() {
		
	}


	@Test
	public void TestSendNumberDataSets() {
		try {
			DatagramPacket p = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
			sendDataPiece(1);
			socket.receive(p);
			byte[] b = p.getData();
			ByteBuffer wrap = ByteBuffer.wrap(b);
			int response = wrap.getInt();
			assertEquals(response, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void TestSendId() {
		int[] i = {1,1,1,1};
		sendDataPiece(1);
		DatagramPacket reciever = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
		try {
			socket.receive(reciever);
			sendIntArray(i);
			socket.receive(reciever);
			
			byte[] b = reciever.getData();
			ByteBuffer wrap = ByteBuffer.wrap(b);
			int response = wrap.getInt();
			assertEquals(1, response);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void TestSendTime() {
		int[] i = {1,1,1,1};
		sendDataPiece(1);
		DatagramPacket reciever = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
		try {
			socket.receive(reciever);
			sendIntArray(i);
			socket.receive(reciever);
			
			byte[] b = reciever.getData();
			ByteBuffer wrap = ByteBuffer.wrap(b);
			int response = wrap.getInt();
			assertEquals(1, response);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void TestSendTemp() {
		int[] i = {1,1,1,1};
		sendDataPiece(1);
		DatagramPacket reciever = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
		try {
			socket.receive(reciever);
			sendIntArray(i);
			socket.receive(reciever);
			
			byte[] b = reciever.getData();
			ByteBuffer wrap = ByteBuffer.wrap(b);
			int response = wrap.getInt();
			assertEquals(1, response);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void TestSendTempLow() {

		int[] i = {1,1,-1,1};
		sendDataPiece(1);
		DatagramPacket reciever = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
		try {
			socket.receive(reciever);
			sendIntArray(i);
			socket.receive(reciever);
			
			byte[] b = reciever.getData();
			ByteBuffer wrap = ByteBuffer.wrap(b);
			int response = wrap.getInt();
			assertEquals(0, response);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void TestSendTempHigh() {
		int[] i = {1,1,32769,1};
		sendDataPiece(1);
		DatagramPacket reciever = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
		try {
			socket.receive(reciever);
			sendIntArray(i);
			socket.receive(reciever);
			
			byte[] b = reciever.getData();
			ByteBuffer wrap = ByteBuffer.wrap(b);
			int response = wrap.getInt();
			assertEquals(0, response);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void TestSendTilt() {
		int[] i = {1,1,1,1};
		sendDataPiece(1);
		DatagramPacket reciever = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
		try {
			socket.receive(reciever);
			sendIntArray(i);
			socket.receive(reciever);
			
			byte[] b = reciever.getData();
			ByteBuffer wrap = ByteBuffer.wrap(b);
			int response = wrap.getInt();
			assertEquals(1, response);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void TestSendTiltHigh() {
		int[] i = {1,1,1,32769};
		sendDataPiece(1);
		
		DatagramPacket reciever = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
		try {
			socket.receive(reciever);
			sendIntArray(i);
			socket.receive(reciever);
			
			byte[] b = reciever.getData();
			ByteBuffer wrap = ByteBuffer.wrap(b);
			int response = wrap.getInt();
			assertEquals(0, response);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void TestSendTiltLow() {
		int[] i = {1,1,1,-1};
		sendDataPiece(1);
		DatagramPacket reciever = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
		try {
			socket.receive(reciever);
			sendIntArray(i);
			socket.receive(reciever);
			
			byte[] b = reciever.getData();
			ByteBuffer wrap = ByteBuffer.wrap(b);
			int response = wrap.getInt();
			assertEquals(0, response);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void SendSet() {
		int[] i = {1,1,1,1};
		sendDataPiece(1);
		DatagramPacket reciever = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
		try {
			socket.receive(reciever);
			sendIntArray(i);
			socket.receive(reciever);
			
			byte[] b = reciever.getData();
			ByteBuffer wrap = ByteBuffer.wrap(b);
			int response = wrap.getInt();
			assertEquals(1, response);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void SendSetHigh() {
		int[] i = {1,1,1,1,1};
		sendDataPiece(1);
		DatagramPacket reciever = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
		try {
			socket.receive(reciever);
			sendIntArray(i);
			socket.receive(reciever);
			
			byte[] b = reciever.getData();
			ByteBuffer wrap = ByteBuffer.wrap(b);
			int response = wrap.getInt();
			assertEquals(0, response);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void SendSetSmall() {
		int[] i = {1,1,1};
		sendDataPiece(1);
		DatagramPacket reciever = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
		try {
			socket.receive(reciever);
			sendIntArray(i);
			socket.receive(reciever);
			
			byte[] b = reciever.getData();
			ByteBuffer wrap = ByteBuffer.wrap(b);
			int response = wrap.getInt();
			assertEquals(0, response);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendIntArray(int[] a) {
		ByteBuffer bb = ByteBuffer.allocate(a.length*4);
		IntBuffer ib = bb.asIntBuffer();
		for(int i:a) ib.put(i);
		byte[] bytes = bb.array();
		
		DatagramPacket p  = new DatagramPacket(bytes,bytes.length,host,other_port);
		DatagramPacket r  = new DatagramPacket(new byte[PACKETSIZE],PACKETSIZE);
		try {
			socket.send(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Transmitter t = new Transmitter();
	}

	private void sendDataPiece(int send) {
		int sendNum = send;
		ByteBuffer ba = ByteBuffer.allocate(4);
		ba.putInt(sendNum);
		byte[] b = ba.array();
		DatagramPacket packet = new DatagramPacket(b, b.length, host, other_port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@AfterClass
	public static void endTesting() {
		thread.stop();
		socket.close();
	}


	@BeforeClass
	public static void setUpTests() {
		try {
			host = InetAddress.getByName("127.0.0.1");
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			System.err.println("this is bad ");
			e1.printStackTrace();
		}
		other_port++;
		ThreadStarter th = new ThreadStarter(other_port);
		thread = new Thread(th);
		thread.start();

		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	@Before
	public  void setUp() {
		other_port++;
		ThreadStarter th = new ThreadStarter(other_port);
		thread= new Thread(th);
		thread.start();
	}
	@SuppressWarnings("deprecation")
	@After
	public void takeDown() {
//		thread.stop();
	}

}
