public class Main extends AbstractMain {
	int 箱子 = 0;
	boolean 拿到了一个 = false;
	@Override
	public void reply(String msg, String from) throws Exception{
		if(拿到了一个) {
			int num1 = Integer.parseInt(msg);
			sendMessage(Integer.toString(箱子 - num1), from);
			拿到了一个=false;
		}else {
			箱子= Integer.parseInt(msg);
			拿到了一个=true;
		}
	}
	@Override
	public void scheduledCallback(int year,int month, int day, int hour,int min,int weekday) throws Exception{
		System.out.format("now is %d-%d-%d %d-%d %d",year,month,day,hour,min,weekday);
	}
}
