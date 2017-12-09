package connecting;

public class Set {
	private int id;
	private int time;
	private double tilt;
	private double temperature;
	static private final int ATDMAX  =  65536;
	static private final double TILTMAX= 30;
	static private final double TILTMIN= -15;
	static private final double TEMPMAX= 40;
	static private final double TEMPMIN = -40;
	private boolean good;
	
	public Set(int id, long time, int tilt, int temp) {
		this.id = id;
		convert(time,tilt,temp);
		convert(time,tilt,temp);
	}
	// converts the Data to Meaning full data
	public void convert(long date,int tlt,int temp) {
		if(date<0||tlt<0||temp<0||tlt>32767||temp>32767) {
			good = false;
		}
		int days = (int) Math.floor(date/24);
		int hours  = time%24;
		time = days*100+hours;
		tilt = ((tlt/ATDMAX)*(TILTMAX-TILTMIN))+TILTMIN;
		temperature = ((temp/ATDMAX)*(TEMPMAX-TEMPMIN))+TEMPMIN;
		
	}
	// getters no need for setters
	public int getId() {
		return id;
	}
	public int getTime() {
		return time;
	}
	public double getTemp() {
		return temperature;
	}
	public double getTilt(){
		return tilt;
	}
	public boolean isGood() {
		return good;
	}
}
