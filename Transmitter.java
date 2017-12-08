package connecting;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.xml.crypto.Data;

import com.mysql.cj.api.jdbc.Statement;
import com.mysql.cj.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;

public class Transmitter {
	private Connection connect = null;
	private java.sql.Statement statement = null;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet = null;
	

	public Transmitter(ArrayList<int[]> dataList) throws DataException{
				ArrayList<Data> listData = new ArrayList<Data>();
				
	}
	
	// CollectData loops i times and takes in the number of Data Packets that the
	// Arduino sends and trys to put them into
	// a data structure

	public ArrayList<Set> convert(ArrayList<int[]> rawList)throws DataException {
		ArrayList<Set> dataList = new ArrayList<Set>();
		for(int i = 0;i<rawList.size();i++) {
			int time = rawList.get(i)[0];
			int temp = rawList.get(i)[1];
			int tilt = rawList.get(i)[2];
			int id = 2; //TODO still need to get this but for now the only Arduino we have has this id
			Set d = new Set(id,time,temp,tilt);
			if(d.isGood()) {
				dataList.add(d);
			}else {
				DataException de = new DataException((Set) d);
				throw(de);
			}
		}
		return dataList;
	}
	
	public void transmit(ArrayList<Set> dataList, String website) {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://" + website);

			statement = connect.createStatement();
			for (Set data : dataList) {
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