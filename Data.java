package transmitter;

public class Data {
	long time;
	double tilt;
	double temperature;
	
	public Data(int id, long time, int tilt, int temp) {
		this.time = time;
		this.tilt = tilt;
		temperature = temp;
	}

	public void convert() {
		long days = (long)Math.floor(time/24);
		long hours  = time%24;
		time = days*100+hours;
	}
}
