
public class Main implements Replier {

	public static void main(String[] args) {
		new Backbone(new Main());
	}

	@Override
	public String reply(String msg) {
		return "唐琳是白痴";
	}

}
