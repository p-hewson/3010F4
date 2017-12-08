package connecting;
//To Run this please have Transmitter in the same Project as this so they can be linked together

// two programs so one can pass of data while the other one goes and deals with other things 

// imports for file reading
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
// imports for JSON
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//Imports for Serial, which ackts like a file hence Input Stream
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class Reciever {
	// creating Constants
	private byte DATA = 0x01; // Data packet (only from sensor to base)
	private byte STAT = 0x02; // Status packet
	private byte ROLL = 0x04; // Roll call command (only from base to sensor)
	private byte UPLD = 0x08; // Upload command (only from base to sensor)
	private byte CINT = 0x10; // Change interval (only from base to sensor)
	int[] adresses;
	
	
	private int port = 1010;
	private int other_port = 1020;
	private InetAddress local_host;

	public static void main(String[] args
			) {
		Reciever r = new Reciever();
	}

	public Reciever() {
		adresses = new int[1];
		adresses[0]= 2;
		try {
			// setting things up to communicate with the RFArduino
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier("/dev/ttyUSB0");

			CommPort commPort = portIdentifier.open(this.getClass().toString(), 2000);
			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);

				InputStream in = serialPort.getInputStream();
				OutputStream out = serialPort.getOutputStream();
				boolean done = false;
				while (!done) {
					String[] options = { "ROLL", "STATUS", "DATA"};
					String selectedValue = (String) JOptionPane.showInputDialog(null, "Choose an option",
							"Choose next task", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
					ArrayList<int[]> values = new ArrayList<int[]>();
					ArrayList<int[]> statList = new ArrayList<int[]>();
					int[] state;
					switch (selectedValue){
						case ("ROLL"):
							ArrayList<int[]> statusList = rollCall(in,out);
							for(int[] status : statusList) {
							printStatus(status);
							}
						break;
						case("STATUS"):
							state = getStatus(in,out);
							printStatus(state);
						break;
						case("DATA"):
							values = getData(in,out);
							sendToTransmitter(values);
						break;	
							
					}
						
				}

			}

		} catch (NoSuchPortException e) {
			
			e.printStackTrace();
		} catch (PortInUseException e) {
			
			e.printStackTrace();
		} catch (UnsupportedCommOperationException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
	
			e.printStackTrace();
		}
	}
	
	
	public ArrayList<int[]> rollCall(InputStream in, OutputStream out){
		try {
			out.write(ROLL);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<int[]> status = new ArrayList<int[]> ();
		status.add(getStatus(in,out));
		return status;
	}

	public int[] getDataPiece(InputStream in, OutputStream out) {
		byte[] b = new byte[100];
		JSONArray array = new JSONArray();
		JSONParser parser = new JSONParser();
		int[] value = new int[4];
		try {
			in.read(b);
			String s = new String(b).trim();
			array = (JSONArray) parser.parse(s);
			int time = Integer.parseInt(array.get(0).toString());
			int temp = Integer.parseInt(array.get(1).toString());
			int tilt = Integer.parseInt(array.get(2).toString());

			value[0] = adresses[0];// TODO need to find out how to get the id still
			value[1] = time;
			value[2] = temp;
			value[3] = tilt;

		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ParseException e) {
		
			e.printStackTrace();
		}

		return value;
	}

	public ArrayList<int[]> getData(InputStream in, OutputStream out) {
		ArrayList<int[]> dataList = new ArrayList<int[]>();
		
		try {
			ByteBuffer bb = ByteBuffer.allocate(4);
			byte[] write = new byte[5];
			bb.putInt(adresses[0]);
			byte[] adress = bb.array();
			write[0] = UPLD;
			for(int i=1;i<5;i++) {
				write[i]= adress[i-1];
			}
			out.write(DATA);
			byte[] good = new byte[4];
			byte[] bad = new byte[4];
			byte[] b = new byte[4];
			
			bb.putInt(1);
			good = bb.array();
			bb.putInt(0);
			bad = bb.array();
			
		
			in.read(b);
			ByteBuffer wrap = ByteBuffer.wrap(b);
			int numPieces = wrap.getInt();
			for (int i = 0; i < numPieces; i++) {
				int[] datapiece = getDataPiece(in, out);
				out.write(good);
				dataList.add(datapiece);
			}
			return dataList;
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return null;
	}

	public int[] getStatus(InputStream in, OutputStream out) {
		// setting up variables
		int[] state = new int[3];
		JSONArray array = new JSONArray();
		JSONParser parser = new JSONParser();
		ArrayList<int[]> status = new ArrayList<int[]>();
		byte[] good = new byte[4];
		byte[] bad = new byte[4];
		byte[] b = new byte[4];
		byte[] adress = new byte[4];
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putInt(1);
		good = bb.array();
		bb.putInt(0);
		bad = bb.array();
		bb.putInt(adresses[0]);
		adress = bb.array();
		// talking to arduino
		try {
			byte[] write = new byte[5];
			write[0] = STAT;
			for(int i = 1;i<5;i++) {
				write[i]= adress[i-1];
			}	
			out.write(write);
			in.read(new byte[100]);
			String s = new String(b).trim();
			array = (JSONArray) parser.parse(s);
			
			state[0] = Integer.parseInt(array.get(0).toString());
			state[1] = Integer.parseInt(array.get(1).toString());
			state[2] = Integer.parseInt(array.get(2).toString());
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ParseException e) {
		
			e.printStackTrace();
		}
		return state;
	}

	public void sendToTransmitter(ArrayList<int[]> dataList) {
			try {
				Transmitter t = new Transmitter(dataList);
			} catch (DataException e) {
				e.printStackTrace();
			}

	}
	
	
	public void printStatus(int[] status) {
		System.out.println("Status of Data Logger\n");
		System.out.println("id: "+status[0]+"\n");
		System.out.println("id: "+status[1]+"\n");
		System.out.println("id: "+status[2]+"\n");
	}


}