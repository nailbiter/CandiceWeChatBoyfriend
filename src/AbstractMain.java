import java.util.Date;

public abstract class AbstractMain implements Replier, Runnable {
	static Backbone bb = null;
	public static void main(String[] args) {
		bb = new Backbone(new Main());
	}
	abstract public void reply(String msg, String from);
	abstract public void scheduledCallback(int year,int month, int day, int hour,int min,int weekday);
	public void run() {
		Date d = new Date();
		scheduledCallback(d.getYear()+1900,d.getMonth(),d.getDate(),d.getHours(),d.getMinutes(),d.getDay());
	}
}
