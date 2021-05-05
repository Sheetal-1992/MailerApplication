package com.lib.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class ApplicationUtility {
	Credentials cred;	
	public String getValue(String key) throws IOException
	{
		String value = null;
		Properties properties = new Properties();
		FileInputStream fileInput = new FileInputStream("./src/resources/config.properties");
		properties.load(fileInput);
		value = properties.getProperty(key);
		return value;
	}
	public BufferedReader read(String file_name) throws FileNotFoundException {
		FileReader fr = new FileReader("./src/resources/"+file_name);
		BufferedReader br = new BufferedReader(fr);
		return br;
	}
	public ArrayList<Credentials> readCredentials(String file_name) throws IOException
	{
		ArrayList<Credentials> user_details= new ArrayList<Credentials>();
		BufferedReader br = read(file_name);
		String data = "";
		while((data=br.readLine())!=null)
		{
		if(data.contains(","))
		{
			String[] user_content = data.split(",");
			cred = new Credentials(user_content[0],user_content[1]);
			user_details.add(cred);
		}
				}
		return user_details;	
	}
	public String readFile(String file_name) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		BufferedReader br = read(file_name);
		String body = "";
		while((body=br.readLine())!=null) {
			sb.append(body).append("\n");
		}		
		String mail_body = sb.toString();
		return mail_body;
	}	
	public void replaceText(String file_name, String text) throws IOException
	{
		FileWriter fw = new FileWriter("./src/resources/"+file_name);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(text);	
		bw.close();
	}
	
	public void uploadFile(String filePath) throws AWTException {
		StringSelection stringSelection = new StringSelection(filePath);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
 
        Robot robot = new Robot();
        robot.delay(4000);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);
        robot.delay(150);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
	}
}
