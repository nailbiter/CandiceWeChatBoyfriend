public class Main extends AbstractMain {
	int 箱子 = 0;
	boolean 拿到了一个 = false;
	@Override
	public void reply(String msg, String from) {
		if(拿到了一个) {
			int num1 = Integer.parseInt(msg);
			bb.sendMessage(Integer.toString(箱子 - num1), from);
			拿到了一个=false;
		}else {
			箱子= Integer.parseInt(msg);
			拿到了一个=true;
		}
		
		//int num2 = Integer.parseInt(msg);
	}
}
