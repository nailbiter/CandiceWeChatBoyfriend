import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Backbone implements HttpHandler {
	Replier r_ = null;
	private final static int PORT = 8000;
	public Backbone(Replier r) {
		r_ = r;
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(PORT),0);
			server.createContext("/wechat",this);
			server.setExecutor(null);
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	@Override
	public void handle(HttpExchange t) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader in = new BufferedReader(new InputStreamReader(t.getRequestBody()));                                                 
        String inputLine;
        String from = null, to = null, msg = null;
        while ((inputLine = in.readLine()) != null) {                                                                                      
            System.out.format("%s\n",inputLine);
            if(inputLine.contains("ToUserName")) {
            	
            }
        }                                                                                                                                  
                                                                                                                                           
        try{                                                                                                                               
            StringBuilder sb = new StringBuilder();                                                                                        
            sb.append("<xml><ToUserName><![CDATA[ou5KB0QpWLoyae77pXFPZqEQhu1Q]]></ToUserName>");                                           
            sb.append("<FromUserName><![CDATA[gh_135a0801a87a]]></FromUserName>");                                                         
            sb.append(String.format("<CreateTime>%d</CreateTime>",100500));                                                                
            sb.append("<MsgType><![CDATA[text]]></MsgType>");                                                                              
            sb.append(String.format("<Content><![CDATA[%s]]></Content>",r_.reply("")));                                                          
            sb.append("</xml>");                                                                                                           
                                                                                                                                           
            String response = sb.toString();                                                                                               
            byte[] bs = response.getBytes(java.nio.charset.StandardCharsets.UTF_8);                                                        
            t.getResponseHeaders().set("Content-Type", "application/xml; charset=" + "UTF-8");                                             
            t.sendResponseHeaders(0, bs.length);                                                                                           
                                                                                                                                           
            OutputStream os = t.getResponseBody();                                                                                         
            os.write(bs);                                                                                                                  
            os.close();                                                                                                                    
        }                                                                                                                                  
        catch(Exception e){                                                                                                                
           e.printStackTrace(System.out);                                                                                                  
        }                                                                                                                                  
                                                                                                                                           
        System.out.format("\n"); 
	}

}
