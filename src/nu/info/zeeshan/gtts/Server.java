package nu.info.zeeshan.gtts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
	public static void main(String args[]){
		ServerSocket ss=null;
		Socket cs=null;
		
		try{
			ss=new ServerSocket(23456);
			String str;
			while(true){
				cs=ss.accept();
				System.out.println("new Client");
				PrintWriter output=new PrintWriter(cs.getOutputStream(),true);
				while(true){
					System.out.println("Enter Single line text");
					str=br.readLine();
					output.println(str);
					if(output.checkError())
						break;
					System.out.println("done");
					
				}
				cs.close();
				System.out.println("Client disconnected");
			}
			//ss.close();
		}catch(Exception e){
			e.printStackTrace();
			try {
				if(cs!=null)
					cs.close();
				if(ss!=null)
					ss.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
	}
}
