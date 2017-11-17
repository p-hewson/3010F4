package transmitter;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class TestTransmiter {
	private final static int PACKETSIZE = 100;
	DatagramSocket socket;
	private int port = 1011;

	public TestTransmiter() {
		Transmitter t = new Transmitter();
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			TestSendNumberDataSets();

			TestSendId();
			TestSendTime();
			TestSendTemp();
			TestSendTilt();
			SendEnd();
			
			TestSendId();
			TestSendTime();
			TestSendTempLow();
			TestSendTiltLow();
			SendEnd();
			
			TestSendId();
			TestSendTime();
			TestSendTempHigh();
			TestSendTiltHigh();
			SendEnd();
			
			SendSet();
			SendSetHigh();
			SendSetSmall();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void TestSendNumberDataSets() throws IOException {
		int i = 3;
		byte[] b = new byte[1];
		b[0] = 3;
		DatagramPacket p = new DatagramPacket(b, PACKETSIZE);

		socket.send(p);

		socket.receive(p);
		int num;
		byte[] number = p.getData();
		ByteBuffer wrapped = ByteBuffer.wrap(number);
		num = wrapped.getInt();
		assertEquals(num, 1);
	}

	@Test
	public void TestSendId() {

		byte[] b = new byte[1];
		b[0] = 3;
		DatagramPacket p = new DatagramPacket(b, PACKETSIZE);
		try {
			socket.send(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			socket.receive(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		b = p.getData();
		int num;
		ByteBuffer wrapped = ByteBuffer.wrap(b);
		num = wrapped.getInt();
		assertEquals(num, 1);

	}

	@Test
	public void TestSendTime() {
		int sendNum = 1;
		ByteBuffer ba = ByteBuffer.allocate(4);
		ba.putInt(sendNum);
		byte[] b = ba.array();
		DatagramPacket p = new DatagramPacket(b, PACKETSIZE);
		try {
			socket.send(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			socket.receive(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] data = p.getData();
		ByteBuffer wrapped = ByteBuffer.wrap(data);
		int response = wrapped.getInt();
		assertEquals(response, 1);
	}

	@Test
	public void TestSendTemp() {
		int sendNum = 1;
		ByteBuffer ba = ByteBuffer.allocate(4);
		ba.putInt(sendNum);
		byte[] b = ba.array();
		DatagramPacket p = new DatagramPacket(b, PACKETSIZE);
		try {
			socket.send(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			socket.receive(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] data = p.getData();
		ByteBuffer wrapped = ByteBuffer.wrap(data);
		int response = wrapped.getInt();
		assertEquals(response, 1);

	}

	@Test
	public void TestSendTempLow() {
		int sendNum = -1;
		ByteBuffer ba = ByteBuffer.allocate(4);
		ba.putInt(sendNum);
		byte[] b = ba.array();
		DatagramPacket p = new DatagramPacket(b, PACKETSIZE);
		try {
			socket.send(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			socket.receive(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] data = p.getData();
		ByteBuffer wrapped = ByteBuffer.wrap(data);
		int response = wrapped.getInt();
		assertEquals(response, 0);

	}

	@Test
	public void TestSendTempHigh() {
		long sendNum = 214748365;
		ByteBuffer ba = ByteBuffer.allocate(4);
		ba.putLong(sendNum);
		byte[] b = ba.array();
		DatagramPacket p = new DatagramPacket(b, PACKETSIZE);
		try {
			socket.send(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			socket.receive(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] data = p.getData();
		ByteBuffer wrapped = ByteBuffer.wrap(data);
		int response = wrapped.getInt();
		assertEquals(response, 0);
	}

	@Test
	public void TestSendTilt() {
		int sendNum = 1;
		ByteBuffer ba = ByteBuffer.allocate(4);
		ba.putInt(sendNum);
		byte[] b = ba.array();
		DatagramPacket p = new DatagramPacket(b, PACKETSIZE);
		try {
			socket.send(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			socket.receive(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] data = p.getData();
		ByteBuffer wrapped = ByteBuffer.wrap(data);
		int response = wrapped.getInt();
		assertEquals(response, 1);

	}

	@Test
	public void TestSendTiltHigh() {
		int sendNum = 99000;
		ByteBuffer ba = ByteBuffer.allocate(4);
		ba.putInt(sendNum);
		byte[] b = ba.array();
		DatagramPacket p = new DatagramPacket(b, PACKETSIZE);
		try {
			socket.send(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			socket.receive(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] data = p.getData();
		ByteBuffer wrapped = ByteBuffer.wrap(data);
		int response = wrapped.getInt();
		assertEquals(response, 0);

	}

	@Test
	public void TestSendTiltLow() {
		int sendNum = -1;
		ByteBuffer ba = ByteBuffer.allocate(4);
		ba.putInt(sendNum);
		byte[] b = ba.array();
		DatagramPacket p = new DatagramPacket(b, PACKETSIZE);
		try {
			socket.send(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			socket.receive(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] data = p.getData();
		ByteBuffer wrapped = ByteBuffer.wrap(data);
		int response = wrapped.getInt();
		assertEquals(response, 0);
	}

	@Test
	public void SendSet() {
		SendData(4);
		String end = "end";
		byte[] b = end.getBytes();
		DatagramPacket p = new DatagramPacket(b, PACKETSIZE);
		try {
			socket.send(p);
			socket.receive(p);
			byte[] data = p.getData();
			ByteBuffer wrapped = ByteBuffer.wrap(data);
			int response = wrapped.getInt();
			assertEquals(response, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void SendSetHigh() {
		boolean good = SendData(5);
		assertFalse(good);
	}

	@Test
	public void SendSetSmall() {
		SendData(3);
		String end = "end";
		byte[] b = end.getBytes();
		DatagramPacket p = new DatagramPacket(b, PACKETSIZE);
		try {
			socket.send(p);
			socket.receive(p);
			byte[] data = p.getData();
			ByteBuffer wrapped = ByteBuffer.wrap(data);
			int response = wrapped.getInt();
			assertEquals(response, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean SendData(int repeat) {
		for (int i = 0; i < repeat; i++) {
			int sendNum = 1;
			ByteBuffer ba = ByteBuffer.allocate(4);
			ba.putInt(sendNum);
			byte[] b = ba.array();
			DatagramPacket p = new DatagramPacket(b, PACKETSIZE);
			try {
				socket.send(p);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				socket.receive(p);
				byte[] data = p.getData();
				ByteBuffer wrapped = ByteBuffer.wrap(data);
				int response = wrapped.getInt();
				if (response == 0) {
					return false;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte[] data = p.getData();
			ByteBuffer wrapped = ByteBuffer.wrap(data);
			int response = wrapped.getInt();

		}
		return true;
	}

	private void SendEnd() {
		String end = "end";
		byte[] b = end.getBytes();
		DatagramPacket p = new DatagramPacket(b, PACKETSIZE);
		try {
			socket.send(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
