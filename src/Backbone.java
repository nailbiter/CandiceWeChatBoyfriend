import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import it.sauronsoftware.cron4j.Scheduler;

public class Backbone implements HttpHandler {
	private String accessToken = "null"; 
	private Replier r_ = null;
	private final static int PORT = 8000;
	private String myId = null;
	private Scheduler s;
	public Backbone(AbstractMain r) {
		r_ = r;
		try {
			Scheduler s = new Scheduler();
			s.schedule("* * * * *",r);
			s.start();
			
			getAccessKey();
			HttpServer server = HttpServer.create(new InetSocketAddress(PORT),0);
			server.createContext("/wechat",this);
			server.setExecutor(null);
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void getAccessKey() throws IOException {
		URL url = new URL(String.format("https://api.wechat.com/cgi-bin/token?grant_type=%s&appid=%s&secret=%s", 
				"client_credential", "wx7acaf1802332b47f","8eb33930a2c6d5d5c20ed4a18c6130c3"));
		
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);
		con.setDoOutput(true);
        
        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(
        		  new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
		    content.append(inputLine);
		}
        in.close();
        
        String inputLine1 = content.toString();
        Pattern r = Pattern.compile("\"access_token\":\"([^\"]*)\"");
    	Matcher m = r.matcher(inputLine1);
    	if(m.find())
    		accessToken = m.group(1);
    	System.out.format("token=%s\n",accessToken);
	}
	@Override
	public void handle(HttpExchange t) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(t.getRequestBody()));                                                 
        String inputLine;
        String from = null, to = null, msg = null;
        try {
	        while ((inputLine = in.readLine()) != null) {                                                                                      
	            System.out.format("%s\n",inputLine);
	            if(inputLine.contains("ToUserName")) {
	            	Pattern r = Pattern.compile("CDATA\\[([^\\]]*)\\]");
	            	Matcher m = r.matcher(inputLine);
	            	if(m.find())
	            		to = m.group(1);
	            }
	            if(inputLine.contains("FromUserName")) {
	            	Pattern r = Pattern.compile("CDATA\\[([^\\]]*)\\]");
	            	Matcher m = r.matcher(inputLine);
	            	if(m.find())
	            		from = m.group(1);
	            }
	            if(inputLine.contains("Content")) {
	            	Pattern r = Pattern.compile("CDATA\\[([^\\]]*)\\]");
	            	Matcher m = r.matcher(inputLine);
	            	if(m.find())
	            		msg = m.group(1);
	            }
	        }
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
        
        System.out.format("to=%s\n",to);
        System.out.format("from=%s\n",from);
        System.out.format("msg=%s\n",msg);
        myId = to;
        
        try{
        	this.sendTextMessage("", from,t);
        	r_.reply(msg,from);
        }                                                                                                                                  
        catch(Exception e){                                                                                                                
           e.printStackTrace(System.out);                                                                                                  
        }                                                                                                                                  
                                                                                                                                           
        System.out.format("\n"); 
	}
	private void sendTextMessage(String msg, String to, HttpExchange t) throws IOException {
		StringBuilder sb = new StringBuilder();                                                                                        
        sb.append(String.format("<xml><ToUserName><![CDATA[%s]]></ToUserName>",to));                                           
        sb.append(String.format("<FromUserName><![CDATA[%s]]></FromUserName>",myId));                                                         
        sb.append(String.format("<CreateTime>%d</CreateTime>",System.currentTimeMillis()));                                                                
        sb.append("<MsgType><![CDATA[text]]></MsgType>");                                                                              
        sb.append(String.format("<Content><![CDATA[%s]]></Content>",msg));                                                          
        sb.append("</xml>");                                                                                              
                                                                                                                                       
        String response = sb.toString();                                                                                               
        byte[] bs = response.getBytes(java.nio.charset.StandardCharsets.UTF_8);                                                        
        t.getResponseHeaders().set("Content-Type", "application/xml; charset=" + "UTF-8");                                             
        t.sendResponseHeaders(0, bs.length);                                                                                        
                                                                                                                                       
        OutputStream os = t.getResponseBody();                                                                                         
        os.write(bs);                                                                                                                  
        os.close();
	}
	public void sendMessage(String s,String to) {
		try {
			URL url = new URL(String.format("https://api.wechat.com/cgi-bin/message/custom/send?access_token=%s", accessToken));
			
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			con.setDoOutput(true);
			
			StringBuilder sb = new StringBuilder();
			String pad = "    ";
			sb.append(String.format("%s{\n", ""));
			sb.append(String.format("%s\"touser\":\"%s\",\n", pad,to));
			sb.append(String.format("%s\"msgtype\":\"text\",\n", pad,to));
			sb.append(String.format("%s\"text\":\n", pad,to));
			sb.append(String.format("%s{\n", pad));
			sb.append(String.format("%s\"content\":\"%s\"\n", pad+pad,s));
			sb.append(String.format("%s}\n", pad));
			sb.append(String.format("%s}\n", ""));
			
			String response = sb.toString();                                                                                               
	        byte[] bs = response.getBytes(java.nio.charset.StandardCharsets.UTF_8);
	        OutputStream out = con.getOutputStream();
//	        out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
	        out.write(bs);
	        out.close();
//	        con.connect();
	        
	        int status = con.getResponseCode();
	        BufferedReader in = new BufferedReader(
	        		  new InputStreamReader(con.getInputStream()));
    		String inputLine;
    		StringBuffer content = new StringBuffer();
    		while ((inputLine = in.readLine()) != null) {
    		    content.append(inputLine);
    		}
	        in.close();
	        System.out.println(content.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
