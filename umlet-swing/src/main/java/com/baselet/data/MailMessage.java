package com.baselet.data;

import java.util.LinkedList;
import java.util.List;
import java.io.File;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

public class MailMessage {
	private boolean useAuthentication;
	private String host;
	private String user;
	private String password;

	private List<File> attachments;

	private MimeMessage message;

	private String from;
	private String subject;
	private String text;

	private String[] toRecipients;
	private String[] ccRecipients;
	private String[] bccRecipients;

	private Session mailSession;

	public MailMessage() {

	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean isUseAuthentication() {
		return useAuthentication;
	}

	public void setUseAuthentication(boolean useAuthentication) {
		this.useAuthentication = useAuthentication;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void addAttachment(File attachment) {
		if(attachment == null) {
			attachments = new LinkedList<File>();
		}

		attachments.add(attachment);
	}


	public List<File> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<File> attachments) {
		this.attachments = attachments;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String[] getToRecipients() {
		return toRecipients;
	}

	public void setToRecipients(String[] toRecipients) {
		this.toRecipients = toRecipients;
	}

	public String[] getCCRecipients() {
		return ccRecipients;
	}

	public void setCCRecipients(String[] ccRecipients) {
		this.ccRecipients = ccRecipients;
	}

	public String[] getBCCRecipients() {
		return bccRecipients;
	}

	public void setBCCRecipients(String[] bccRecipients) {
		this.bccRecipients = bccRecipients;
	}

	public Session getMailSession() {
		return mailSession;
	}

	public void setMailSession(Session mailSession) {
		this.mailSession = mailSession;
	}

	public MimeMessage getMessage() {
		return message;
	}

	public void setMessage(MimeMessage message) {
		this.message = message;
	}
}
