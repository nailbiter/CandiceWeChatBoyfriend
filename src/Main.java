public class Main extends AbstractMain {
	@Override
	public void reply(String msg, String from) {
		//第一步
		String[] parts = msg.split(" ",5);
		//第二步
		// 月 日 点 分
		int 月 = Integer.parseInt(parts[0]);
	}
	@Override
	public void scheduledCallback(int year,int month, int day, int hour,int min,int weekday) {
		System.out.format("now is %d-%d-%d %d-%d %d",year,month,day,hour,min,weekday);
	}
}
