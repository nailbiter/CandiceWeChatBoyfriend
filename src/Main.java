
public class Main implements Replier {
	static Backbone bb = null;
	public static void main(String[] args) {
		bb = new Backbone(new Main());
	}

	@Override
	public void reply(String msg, String from) {
		bb.sendMessage("你输入了: "+msg,from);
		bb.sendMessage("记得，"+getName()+"是白痴!",from);
	}
	private String getName() {
		return "唐琳";
	}
}
