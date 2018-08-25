import java.util.ArrayList;

public class Main extends AbstractMain {
	ArrayList<Integer> 月 = new ArrayList<Integer>();
	ArrayList<Integer> 日 = new ArrayList<Integer>();
	ArrayList<Integer> 点 = new ArrayList<Integer>();
	ArrayList<Integer> 分 = new ArrayList<Integer>();
	ArrayList<String> 信息 = new ArrayList<String>();
	String 谁;
	
	//有信息来时reply会反应
	public void reply(String msg, String from) throws Exception {
		//第一步
		//5代表最多分成5部分，第5个数字以后合为一部分
		String[] parts = msg.split(" ",5);
		//第二步
		// 月 日 点 分
		int 月 = Integer.parseInt(parts[0]);
		int 日 = Integer.parseInt(parts[1]);
		int 点 = Integer.parseInt(parts[2]);
		int 分 = Integer.parseInt(parts[3]);
		String 信息 = (parts[4]);
		
		this.月.add(月);
		this.日.add(日);
		this.点.add(点);
		this.分.add(分);
		this.信息.add(信息);
		谁=from;
	}
	//每分钟被叫
	@Override
	public void scheduledCallback(Integer year,Integer month, Integer day, Integer hour,Integer min,Integer weekday) throws Exception{
		System.out.format("now is %d-%d-%d %d-%d %d\n",year,month,day,hour,min,weekday);
		for(int i = 0; i < 月.size(); i=i+1) {
			if(月.get(i)==month+1 && 日.get(i)==day && 点.get(i)==hour && 分.get(i)==min ) {
				this.sendMessage(信息.get(i), 谁);
			}
		}
	}
}
