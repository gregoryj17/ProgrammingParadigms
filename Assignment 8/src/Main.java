import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.awt.Desktop;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class Main
{

	static ArrayList<Message> messages = new ArrayList<>();
	static ArrayList<Action> actions = new ArrayList<>();
	static String marioPlayer;
	static String goombaPlayer;

	static void sendLine(PrintWriter out, String line)
	{
		out.print(line); // Send over the socket
		out.print("\r\n");
		System.out.println(line); // Print it to the console too, just to make debugging easier
	}

	static void onGet(OutputStream os, String url) throws Exception
	{
		PrintWriter out = new PrintWriter(os, true);
		String filename = url.substring(1); // cut off the initial "/"
		File f = new File(filename);
		Path path = Paths.get(filename);
		String dateString = (new Date()).toGMTString();
		System.out.println("----------The server replied: ----------");
		if(f.exists() && !f.isDirectory())
		{
			// Read the file from disk
			byte[] fileContents = Files.readAllBytes(path);

			// Send the headers
			sendLine(out, "HTTP/1.1 200 OK");
			sendLine(out, "Content-Type: " + Files.probeContentType(path));
			sendLine(out, "Content-Length: " + Integer.toString(fileContents.length));
			sendLine(out, "Date: " + dateString);
			sendLine(out, "Last-Modified: " + dateString);
			sendLine(out, "Connection: close");
			sendLine(out, "");
			out.flush();

			// Send the payload
			os.write(fileContents);
			String blobHead = fileContents.length < 60 ? new String(fileContents) : new String(fileContents, 0, 60) + "...";
			System.out.println(blobHead);
		}
		else
		{
			// Make an error message
			String payload = "404 - File not found: " + filename;

			// Send HTTP headers
			sendLine(out, "HTTP/1.1 200 OK");
			sendLine(out, "Content-Type: text/html");
			sendLine(out, "Content-Length: " + Integer.toString(payload.length()));
			sendLine(out, "Date: " + dateString);
			sendLine(out, "Last-Modified: " + dateString);
			sendLine(out, "Connection: close");
			sendLine(out, "");

			// Send the payload
			sendLine(out, payload);
		}
	}

	static void onPost(OutputStream os, String url, char[] incomingPayload)
	{
		// Parse the incoming payload
		System.out.println("----------------------------------------");
		String payload = String.valueOf(incomingPayload);
		System.out.println("Received the following payload: " + payload);

		Json json = Json.parse(payload);
		if(json.getString("type").equals("msg")) {
			String sender = json.getString("fav_num");
			String message = json.getString("message");
			messages.add(new Message(sender, message));
		}
		else if(json.getString("type").equals("getmsg")){
			int i = (int)json.getLong("lastreceived");
			System.out.println(i);
			for(;i<messages.size();i++){
				String newmsg = "{\"type\":\"msg\",\"msg\":\""+messages.get(i).message+"\",\"fav_num\":\""+messages.get(i).sender+"\",\"id\":"+(i+1)+"}";
				sendResponse(os, url, incomingPayload, newmsg);
			}
		}
		else if(json.getString("type").equals("getaction")){
			int i = (int)json.getLong("lastreceived");
			System.out.println(i);
			for(;i<actions.size();i++){
				String newmsg = "{\"type\":\"action\",\"character\":\""+actions.get(i).character+"\",\"movement\":\""+actions.get(i).movement+"\",\"id\":"+(i+1)+"}";
				sendResponse(os, url, incomingPayload, newmsg);
			}
		}
		else if(json.getString("type").equals("character")){
			if(json.getString("character").equals("mario")){
				marioPlayer=json.getString("fav_num");
			}
			else if(json.getString("character").equals("goomba")){
				goombaPlayer=json.getString("fav_num");
				if(marioPlayer==json.getString("fav_num")){
					marioPlayer="";
				}
			}
		}
		else if(json.getString("type").equals("action")){
			if(json.getString("fav_num").equals(marioPlayer)){
				actions.add(new Action("mario",json.getString("move")));
			}
			else if(json.getString("fav_num").equals(goombaPlayer)){
				actions.add(new Action("goomba",json.getString("move")));
			}
		}

		// Make a response
		String response = "{\"type\":\"closer\",\"msg\":\"Thanks for the spiffy message\",\"fav_num\":-1}";

		// Send HTTP headers
		System.out.println("----------The server replied: ----------");
		String dateString = (new Date()).toGMTString();
		PrintWriter out = new PrintWriter(os, true);
		sendLine(out, "HTTP/1.1 200 OK");
		sendLine(out, "Content-Type: application/json");
		sendLine(out, "Content-Length: " + Integer.toString(response.length()));
		sendLine(out, "Date: " + dateString);
		sendLine(out, "Last-Modified: " + dateString);
		sendLine(out, "Connection: close");
		sendLine(out, "");

		// Send the response
		sendLine(out, response);
		out.flush();
	}

	static void sendResponse(OutputStream os, String url, char[] incomingPayload, String response){
		// Send HTTP headers
		System.out.println("----------The server replied: ----------");
		String dateString = (new Date()).toGMTString();
		PrintWriter out = new PrintWriter(os, true);
		sendLine(out, "HTTP/1.1 200 OK");
		sendLine(out, "Content-Type: application/json");
		sendLine(out, "Content-Length: " + Integer.toString(response.length()));
		sendLine(out, "Date: " + dateString);
		sendLine(out, "Last-Modified: " + dateString);
		sendLine(out, "Connection: keep-alive");
		sendLine(out, "");

		// Send the response
		sendLine(out, response);
		out.flush();
	}

	public static void main(String[] args) throws Exception
	{
		// Make a socket to listen for clients
		int port = 1234;
		String ServerURL = "http://localhost:" + Integer.toString(port) + "/page.html";
		ServerSocket serverSocket = new ServerSocket(port);

		// Start the web browser
		if(Desktop.isDesktopSupported())
			Desktop.getDesktop().browse(new URI(ServerURL));
		else
			System.out.println("Please direct your browser to " + ServerURL);

		// Handle requests from clients
		while(true)
		{
			Socket clientSocket = serverSocket.accept(); // This call blocks until a client connects
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			OutputStream os = clientSocket.getOutputStream();

			// Read the HTTP headers
			String headerLine;
			int requestType = 0;
			int contentLength = 0;
			String url = "";
			System.out.println("----------A client said: ----------");
			while ((headerLine = in.readLine()) != null)
			{
				System.out.println(headerLine);
				if(headerLine.length() > 3 && headerLine.substring(0, 4).equals("GET "))
				{
					requestType = 1;
					url = headerLine.substring(4, headerLine.indexOf(" ", 4));
				}
				else if(headerLine.length() > 4 && headerLine.substring(0, 5).equals("POST "))
				{
					requestType = 2;
					url = headerLine.substring(5, headerLine.indexOf(" ", 5));
				}
				else if(headerLine.length() > 15 && headerLine.substring(0, 16).equals("Content-Length: "))
					contentLength = Integer.parseInt(headerLine.substring(16));
				if(headerLine.length() < 2) // Headers are terminated by a "\r\n" line
					break;
			}

			// Send a response
			if(requestType == 1)
			{
				onGet(os, url);
			}
			else if(requestType == 2)
			{
				// Read the incoming payload
				char[] incomingPayload = new char[contentLength];
				in.read(incomingPayload, 0, contentLength);
				String blobHead = incomingPayload.length < 60 ? new String(incomingPayload) : new String(incomingPayload, 0, 60) + "...";
				System.out.println(blobHead);
				onPost(os, url, incomingPayload);
			}
			else
				System.out.println("Received bad headers. Ignoring.");

			// Hang up
			os.flush();
			clientSocket.close();
		}
	}
}

class Message{
	public String sender;
	public String message;

	public Message(String sender, String message){
		this.sender=sender;
		this.message=message;
	}
}

class Action{
	public String character;
	public String movement;

	public Action(String character, String movement){
		this.character=character;
		this.movement=movement;
	}
}
