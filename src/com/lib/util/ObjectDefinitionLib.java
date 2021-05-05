package com.lib.util;

public class ObjectDefinitionLib {
	
	//xpaths
	public String signin = "//div[@class='header clearfix']/a[2]";
	public String compose_mail = "//a[contains(text(),'Compose')]";
	public String to = "//div/input[@id='message-to-field']";
	public String subject = "//div/input[@placeholder='Subject']";
	public String mail_body ="//div[@id='editor-container']/div[1]/div[1]"; /// "//div[@id='editor-container']";
	public String attachment = "//span/div[@data-test-id='popover-container']";
	public String attach_file ="//li[@name='attach-from-computer']/button";  // "//span[contains(text(),'Attach files from computer')]";
	public String send_btn = "//button[@title='Send this email']";
	public String attached_file = "//div[@data-test-id='attachment-container']";
	public String sent_folder = "//span[contains(text(),'Sent')]";
	public String message_subject_1 = "//span[@data-test-id='message-subject' and title='";
	public String message_subject_2 = "']";
	public String sign_out = "//span[contains(text(),'Sign out')]";
	//public String another_account = "//div[@class='bottom-cta']/a";
	public String user_details = "//div[@class='card-content']";
	
	//ids
	public String username_txtbox = "login-username";
	public String signin_btn = "login-signin";
	public String password_txtbox = "login-passwd";
	public String next_btn = "login-signin";
	public String account = "ybarAccountMenuOpener";
	
	//linkText
	public String another_account = "Use another";

}
