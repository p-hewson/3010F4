package transmitter;

public class ThreadStarter implements Runnable {
	private int port;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Transmitter t = new Transmitter(port);
	}
	public ThreadStarter(int port) {
		this.port = port;
	}

}
