import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class Server2 {

	// private static final int sPort = 8000;   //The server will be listening on this port number
	static int sPort = 8000;    //The server will be listening on this port number
	ServerSocket sSocket;   //serversocket used to listen on port number 8000
	Socket connection = null; //socket for the connection with the client
	String message;    //message received from the client
	String MESSAGE;    //uppercase message send to the client
	
	
	ObjectOutputStream out;  //stream write to the socket
	ObjectInputStream in;    //stream read from the socket

	public static void main(String[] args) throws Exception {
		System.out.println("The server is running."); 
        	ServerSocket listener = new ServerSocket(sPort);
		int clientNum = 1;
        	try {
            		while(true) {
                		new Handler(listener.accept(),clientNum).start();
				System.out.println("Client "  + clientNum + " is connected!");
				clientNum++;
            			}
        	} finally {
            		listener.close();
        	} 
 
    	}

	/**
     	* A handler thread class.  Handlers are spawned from the listening
     	* loop and are responsible for dealing with a single client's requests.
     	*/
    	private static class Handler extends Thread {
        	private String message;    //message received from the client
		private String MESSAGE;    //uppercase message send to the client
		private Socket connection;
        	private ObjectInputStream in;	//stream read from the socket
        	private ObjectOutputStream out;    //stream write to the socket
		private int no;	
		private String usernamedot;
		private String passworddot;	//The index number of the client
		private String servercommand;
		private String [] namearr;
	    private String arrayString;
		private String namefile;
		private String namefile1;

        	public Handler(Socket connection, int no) {
            		this.connection = connection;
	    		this.no = no;
        	}

        public void run() {
 		try{
			//initialize Input and Output streams
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			try{
				while(true)
				{
					// //receive the message sent from the client
					// message = (String)in.readObject();
					// //show the message to the user
					// System.out.println("Receive message: " + message + " from client " + no);
					// //Capitalize all letters in the message
					// MESSAGE = message.toUpperCase();
					// //send MESSAGE back to the client
					// sendMessage(MESSAGE);
					usernamedot = (String)in.readObject();
					passworddot = (String)in.readObject();
					if(usernamedot.equals("user") && passworddot.equals("pass")){
						sendMessage("login succesfull");
						break;
					}
					else {
						sendMessage("login failed");
					}
				}
				servercommand = (String)in.readObject();
				System.out.println("coming here");
				// System.out.println(servercommand);
				switch(servercommand) {
					case "getdirectory":
						getdirectory();
						break;
					case "getfile":
						namefile = (String)in.readObject();
						getfile(namefile);
						break;
					case "uploadfile":
						namefile1 = (String)in.readObject();
						uploadfile(namefile1);
						break;
					default:
						System.out.println("invalid command");
					}
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// catch(ClassNotFoundException classnot){
			// 		System.err.println("Data received in unknown format");
			// 	}
		}
		catch(IOException ioException){
			System.out.println("Disconnect with Client " + no);
		}
		finally{
			//Close connections
			try{
				in.close();
				out.close();
				connection.close();
			}
			catch(IOException ioException){
				System.out.println("Disconnect with Client " + no);
			}
		}
	}

	public void getdirectory(){
		File folder = new File(System.getProperty("user.dir"));
		File[] listOfFiles = folder.listFiles();
		namearr = new String[listOfFiles.length];
		for (int i = 0; i < listOfFiles.length; i++) {
		if (listOfFiles[i].isFile()) {
			namearr[i] = listOfFiles[i].getName();
		}
		else if (listOfFiles[i].isDirectory()) {
			namearr[i] = ("Directory " + listOfFiles[i].getName());
		}
		}
		arrayString = Arrays.toString(namearr);
		sendMessage(arrayString);
	}

	public void getfile(String namefile) throws IOException{
		System.out.println(namefile);
		System.out.println("namefile");

		// File file = new File(System.getProperty("user.dir")+"/"+ namefile);
		// long len = file.length();
		// byte[] bits = new byte[8192];
		// InputStream inp = new FileInputStream(file);
        // OutputStream outp = connection.getOutputStream();
		// int c;
		// while((c = inp.read(bits))>0){
		// 	out.write(bits,0,c);
		// }
		// outp.close();
		// inp.close();
		boolean resu = false;
		File file = new File(System.getProperty("user.dir")+"/"+ namefile);
		if(file.exists()){
			if (file.exists()){
				try {
					FileInputStream fileInputStream = new FileInputStream(file);
					// String message = "download" + " " + fileName + " " + Long.toString(upfile.length());
					// sendMessage(message);
					int bytesRead = 0;
					byte[] bits = new byte[8192];
					while ((bytesRead = fileInputStream.read(bits, 0, bits.length)) > 0) {
						out.write(bits, 0, bytesRead);
					}
					out.flush();
					fileInputStream.close();
					resu = true;
					System.out.println("File " + namefile + " sent to client");
				}
				catch (IOException e)
				{
					System.out.println("Error sending file." + e);
				}
			}
			else{
				sendMessage("#");
				System.out.println("File " + namefile + " does not exist.");
			}
			// return resu;
		}
	}

	public void uploadfile(String namefile1) throws IOException{
		DataInputStream dis = new DataInputStream(connection.getInputStream());
		// String fname = (String)in.readObject();
		System.out.println(namefile1);
		FileOutputStream fos = new FileOutputStream(namefile1);
		byte[] buffer = new byte[4096];
		
		int filesize = 15123; // Send file size in separate msg
		int read = 0;
		int totalRead = 0;
		int remaining = filesize;
		while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
			totalRead += read;
			remaining -= read;
			System.out.println("read " + totalRead + " bytes.");
			fos.write(buffer, 0, read);
		}
		
		fos.close();
		dis.close();
	}

	//send a message to the output stream
	public void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("Send message: " + msg + " to Client " + no);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}

    }

}
