tln("Client "  + clientNum + " is connected!");
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

        	public Handler(Socket connection, int no)  = (String)in.readObject();
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
		DataInputStream dis = new DataInputStream(connecti