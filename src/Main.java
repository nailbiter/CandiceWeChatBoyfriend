public class Main extends AbstractMain {
	Integer 箱子 = 0;
	boolean 拿到了一个 = false;
	@Override
	public void reply(String msg, String from) throws Exception{
		if(拿到了一个) {
			Integer num1 = Integer.parseInt(msg);
			sendMessage(Integer.toString(箱子 - num1), from);
			拿到了一个=false;
		}else {
			箱子= Integer.parseInt(msg);
			拿到了一个=true;
		}
	}
	@Override
	public void scheduledCallback(Integer year,Integer month, Integer day, Integer hour,Integer min,Integer weekday) throws Exception{
		System.out.format("now is %d-%d-%d %d-%d %d",year,month,day,hour,min,weekday);
	}
}
