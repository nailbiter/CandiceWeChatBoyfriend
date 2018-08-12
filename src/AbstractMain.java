public abstract class AbstractMain implements Replier {
	static Backbone bb = null;
	public static void main(String[] args) {
		bb = new Backbone(new Main());
	}
	abstract public void reply(String msg, String from);
}
