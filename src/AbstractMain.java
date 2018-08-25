import java.util.Date;

public abstract class AbstractMain implements Replier, Runnable {
	static private Backbone bb = null;
	public static void main(String[] args) {
		bb = new Backbone(new Main());
	}
	abstract public void reply(String msg, String from) throws Exception;
//	abstract public void scheduledCallback(int year,int month, int day, int hour,int min,int weekday) throws Exception;
	public void run() {
		Date d = new Date();
        try{
		    scheduledCallback(d.getYear()+1900,d.getMonth(),d.getDate(),d.getHours(),d.getMinutes(),d.getDay());
        }
        catch(Exception e){
        	e.printStackTrace();
        }
	}
	void sendMessage(String s,String to) {
		bb.sendMessage(s, to);
	}
	abstract public void scheduledCallback(Integer year, Integer month, Integer day, Integer hour, Integer min, Integer weekday) throws Exception;
}
