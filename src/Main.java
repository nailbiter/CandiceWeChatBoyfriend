public class Main extends AbstractMain {
	public void reply(String msg, String from) throws Exception {
		//第一步
		String[] parts = msg.split(" ",5);
		//第二步
		// 月 日 点 分
		int 月 = Integer.parseInt(parts[0]);
		int 日 = Integer.parseInt(parts[1]);
		int 点 = Integer.parseInt(parts[2]);
		int 分 = Integer.parseInt(parts[3]);
	}
	@Override
	public void scheduledCallback(Integer year,Integer month, Integer day, Integer hour,Integer min,Integer weekday) throws Exception{
		System.out.format("now is %d-%d-%d %d-%d %d",year,month,day,hour,min,weekday);
	}
}
