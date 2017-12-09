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
	


	public static void main(String[] args) {
		Reciever r = new Reciever();
	}

	public Reciever() {
		adresses = new int[1];
		adresses[0]= 2;
		try {
			// setting things up to communicate with the RFArduino
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier("/dev/ttyUSB0");

			CommPort commPort = portIdentifier.open(this.getClass().toString(), 2000); // linking the port to a input stream
			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);

				InputStream in = serialPort.getInputStream();
				OutputStream out = serialPort.getOutputStream();
				boolean done = false;
				// until the user says that they are finished
				// 
				while (!done) {
					// brings a gui up that lets the user select the next action
					String[] options = { "ROLL", "STATUS", "DATA","END"};
					String selectedValue = (String) JOptionPane.showInputDialog(null, "Choose an option",
							"Choose next task", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
					ArrayList<int[]> values = new ArrayList<int[]>();
					ArrayList<int[]> statList = new ArrayList<int[]>();
					String[] state;
					// 
					switch (selectedValue){
						case ("ROLL"):
							// ass only one arduino is active at the moment not worrying about that yet
							ArrayList<String[]> statusList = rollCall(in,out);
							for(String[] status : statusList) {
							printStatus(status);
							}
						break;
						case("STATUS"):
							//get the Status of an Arduino (theres only one)
							state = getStatus(in,out);
							printStatus(state);
						break;
						case("DATA"):
							// get the data off the Arduion
							values = getData(in,out);
							sendToTransmitter(values);
						break;	
						case("END"):
							done = true;
							JOptionPane.showMessageDialog(null,"Good Bye");
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
	
	// calls the get Stattus method to get the values
	public ArrayList<String[]> rollCall(InputStream in, OutputStream out){
		try {
			out.write(ROLL);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String[]> status = new ArrayList<String[]> ();
		status.add(getStatus(in,out));
		return status;
	}

	public int[] getDataPiece(InputStream in, OutputStream out) {
		byte[] b = new byte[100];
		JSONArray array = new JSONArray();
		JSONParser parser = new JSONParser();
		int[] value = new int[4];
		try {
			// takes in the value
			in.read(b);
			String s = new String(b).trim();
			array = (JSONArray) parser.parse(s);
			// turns it into a JSON String and then decodes it
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
		// asks the Arduino for the Data
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
				//loops through and acks after every DataPiece
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

	public String[] getStatus(InputStream in, OutputStream out) {
		// setting up variables
		String[] state = new String[3];
		JSONArray array = new JSONArray();
		JSONParser parser = new JSONParser();
		// seting up variables
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
			// create the message with the request and the adress
			out.write(write);
			in.read(new byte[100]);
			String s = new String(b).trim();
			array = (JSONArray) parser.parse(s);
			
			state[0] = (array.get(0).toString());
			state[1] = (array.get(1).toString());
			state[2] = (array.get(2).toString());
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ParseException e) {
		
			e.printStackTrace();
		}
		return state;
	}
	// creates a transmitter and gives it the dataList
	public void sendToTransmitter(ArrayList<int[]> dataList) {
			try {
				Transmitter t = new Transmitter(dataList);
			} catch (DataException e) {
				e.printStackTrace();
			}

	}
	
	// prints the Status
	public void printStatus(String[] status) {
		System.out.println("Status of Data Logger\n");
		System.out.println("id: "+status[0]+"\n");
		System.out.println("id: "+status[1]+"\n");
		System.out.println("id: "+status[2]+"\n");
	}


}