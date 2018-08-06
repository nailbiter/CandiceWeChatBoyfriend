
public class Main implements Replier {
	static Backbone bb = null;
	public static void main(String[] args) {
		bb = new Backbone(new Main());
	}

	@Override
	public String reply(String msg, String from) {
		bb.sendMessage("privet!",from);
		bb.sendMessage("shalom!",from);
		return "唐琳是白痴!!";
	}

}
