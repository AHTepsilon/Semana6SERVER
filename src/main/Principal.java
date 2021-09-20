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
	
	int xCord, yCord;
	
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
		
		//System.out.println(x + ", " + y);
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
							
							try
							{
								String[] coords = line.split(":");
								xCord = Integer.parseInt(coords[0]);
								yCord = Integer.parseInt(coords[1]);
								int Bcol = Integer.parseInt(coords[2]);
							
								System.out.println("Received message: " + xCord + " " + yCord + " " + Bcol);
							
								if(xCord <= 5 && yCord <= 5)
								{
									move(xCord, yCord);
								}
								else
								{
									r = xCord;
									g = yCord;
									b = Bcol;
								}
								
							}catch(ArrayIndexOutOfBoundsException arrErr)
							{
								move(xCord, yCord);
							}
							
							//move(lineConverted);

							if(line == "1")
							{
								PVector vector = new PVector(x, y+5);
								pos.add(vector);
							}
							if(line == "2")
							{
								PVector vector = new PVector(x, y-5);
								pos.add(vector);
							}
							if(line == "3")
							{
								PVector vector = new PVector(x-5, y);
								pos.add(vector);
							}
							if(line == "4")
							{
								PVector vector = new PVector(x+5, y);
								pos.add(vector);
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
	
	public void move(int index, int index2)
	{
		x = index + x;
		y = index2 + y;
	}

}
