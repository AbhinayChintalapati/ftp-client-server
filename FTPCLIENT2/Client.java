import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class Client {
	Socket requestSocket;           //socket connect to the server
	ObjectOutputStream out;         //stream write to the socket
	ObjectInputStream in;          //stream read from the socket
	// InputStream inp = null;
	String message;                //message send to the server
	String MESSAGE;                //capitalized message read from the server
	String username;
	String password;
	String response;
	String command;
	String filename;
	String [] splitarr;
	DataOutputStream dos;
	FileInputStream fis;

	public static void main(String args[])
	{
		Client client = new Client();
		client.run();
	}

	public void Client() {}

	void run()
	{
		try{
			System.out.println("Enter FTP IP port");
			Scanner scan = new Scanner(System.in); 
			String ftpclient = scan.nextLine();
			if(ftpclient.equals("ftpclient localhost 8000"))
			{
			//create a socket to connect to the server
			requestSocket = new Socket("localhost", 8000);
			System.out.println("Connected to localhost in port 8000");
			//initialize inputStream and outputStream
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			
			//get Input from standard input
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			while(true)
			{
				// System.out.print("Hello, please input a sentence: ");
				// //read a sentence from the standard input
				// message = bufferedReader.readLine();
				// //Send the sentence to the server
				// sendMessage(message);
				// //Receive the upperCase sentence from the server
				// MESSAGE = (String)in.readObject();
				// //show the message to the user
				// System.out.println("Receive message: " + MESSAGE);
				System.out.println("Enter Username");
				username = bufferedReader.readLine();
				sendMessage(username);
				System.out.println("Enter Password");
				password = bufferedReader.readLine();
				sendMessage(password);
				response = (String)in.readObject();
				System.out.println(response);
				if(response.equals("login succesfull")){
					break;
				}
			}
			System.out.println("Enter the command");
			command = bufferedReader.readLine();
			splitarr = command.split(" ",2);
			switch(splitarr[0]) {
				case "dir":
					sendMessage("getdirectory");
					break;
				case "get":
					sendMessage("getfile");
					sendMessage(splitarr[1]);
					// System.out.println(splitarr[1]);
					receive(splitarr[1]);
					break;
				case "upload":
					sendMessage("uploadfile");
					sendMessage(splitarr[1]);
					sendfile(splitarr[1]);
					
					break;
				default:
					System.out.println("invalid command");
			}
			try{
			filename = (String)in.readObject();
			String [] dirs = filename.split(",",-1);
			for(String a : dirs){
			System.out.println(a);
			System.out.println("\n");
			}
			}catch (EOFException exception) {
			//    System.out.println("error");
			}catch (OptionalDataException exception){
				// System.out.println("optionaldataexecption");
			}
			// InputStream inpu = null;
			// OutputStream outpu = null;
			// try{
			// 	// inpu = requestSocket.getInputStream();
			// 	// outpu = requestSocket.getOutputStream();
			// 	// byte [] bits = new byte[8192];
			// 	// int c;
			// 	// while ((c = inpu.read(bits)) > 0) {
			// 	// outpu.write(bits, 0, c);
			// 	File newfile = new File()
			// }
			// inpu.close();
			// outpu.close();
			// }catch (IOException ex) {
			// 	ex.printStackTrace();
			// }
			
			// out.close();
			// try{
				
			// }
			}
			else{
				run();
			}
		}
		catch (ConnectException e) {
    			System.err.println("Connection refused. You need to initiate a server first.");
		} 
		catch ( ClassNotFoundException e ) {
            		System.err.println("Class not found");
        	} 
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//Close connections
			try{
				in.close();
				fis.close();
				dos.close();
				out.close();
				requestSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
		
	}

	void receive(String fname){
		File newfile = new File(fname);
		try{
			if (newfile.exists()){
				newfile.delete();
			}
			newfile.createNewFile();
			OutputStream fileStream = new FileOutputStream(newfile);
			int bytesRead = 0;
			long len = 0;
			byte[] bits = new byte[8192];
			int count;
			int c = 100000;
			while(c != 0){
				while ((count = in.read(bits))>0){
					bytesRead = in.read(bits, 0, bits.length);
					fileStream.write(bits, 0, bytesRead);
				}
				c--;
			}
			
			fileStream.close();
			// result = true;

		} 
		catch (IOException e) {
			System.out.println("Error while running get: " + e);
		}
	}

	void sendfile(String fname) throws IOException{
		dos = new DataOutputStream(requestSocket.getOutputStream());
		fis = new FileInputStream(fname);
		byte[] buffer = new byte[4096];
		while (fis.read(buffer) > 0) {
			dos.write(buffer);
		}
		
		
	}
	
	//send a message to the output stream
	void sendMessage(String msg)
	{
		try{
			//stream write the message
			out.writeObject(msg);
			out.flush();
			System.out.println("Send message: " + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
}
