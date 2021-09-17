package main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class Principal extends PApplet
{

	public static void main(String[] args) 
	{
		PApplet.main("main.Principal");
	}
	
	@Override
	public void settings() //void Awake
	{
		size(500, 500);
	}
	
	private Socket socket;
	
	BufferedReader reader;
	BufferedWriter writer;
	
	InetAddress ipLocal;
	
	private ArrayList<PVector> pos;
	
	float x, y;
	int r, g, b;
	
	@Override
	public void setup() //void Start
	{
		pos = new ArrayList<PVector>();
		
		initServer();
		
		try {
			ipLocal = InetAddress.getLocalHost();
			System.out.println(ipLocal.getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		x = 250;
		y = 250;
		
		r = 255;
		g = 0;
		b = 0;
	}
	
	@Override
	public void draw() //void Update
	{		
		background(255);
		
		fill(r, g, b);
		noStroke();
		circle(x, y, 50);
		
	}
	
	public void initServer()
	{
		new Thread(
				() ->
				{
					try {
						ipLocal = InetAddress.getLocalHost();
						System.out.println("Starting port on ip " + ipLocal.getHostAddress());
						
						ServerSocket server = new ServerSocket(4000);
						System.out.println("Awaiting connection...");
						socket = server.accept();
						System.out.println("Client connected succesfully");
						
						InputStream is = socket.getInputStream();
						InputStreamReader isr = new InputStreamReader(is);
						BufferedReader reader = new BufferedReader(isr);
						
						OutputStream os = socket.getOutputStream();
						OutputStreamWriter osw = new OutputStreamWriter(os);
						writer = new BufferedWriter(osw);
						
						while(true)
						{
							System.out.println("Awaiting message...");
							String line = reader.readLine();
							System.out.println("Received message: " + line);
							
							if(line == "1")
							{
								y -= 1;
								System.out.println(y);
							}
							if(line == "2")
							{
								y += 1;
								System.out.println(y);
							}
							if(line == "3")
							{
								x -= 1;
								System.out.println(x);
							}
							if(line == "4")
							{
								x += 1;
								System.out.println(x);
							}
						}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				).start();
	}
	
	public void sendMessage(String msg)
	{
		new Thread(
				() ->
				{
					try {					
						writer.write(msg + "\n");
						writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}).start();
	}

}
