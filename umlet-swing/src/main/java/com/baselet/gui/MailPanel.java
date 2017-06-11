package com.baselet.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.config.Config;
import com.baselet.control.config.ConfigMail;
import com.baselet.control.constants.Constants;
import com.baselet.control.enums.Program;
import com.baselet.control.util.Path;
import com.baselet.data.MailMessage;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.Notifier;
import com.baselet.diagram.io.DiagramFileHandler;

public class MailPanel extends JPanel {
	private static final Logger log = LoggerFactory.getLogger(MailPanel.class);

	private static final long serialVersionUID = 1L;

	private enum Mode {
		XML, GIF, PDF
	};

	/**
	 * Some int and String
	 */

	private final int paddingTop = 1;
	private final int paddingBottom = 1;
	private final int outerPaddingLeft = 15;
	private final int outerPaddingRight = 15;
	private final int halfHorizontalDividerSpace = 2;
	private final int verticalDividerSpace = 10;

	/**
	 * Components
	 */

	private final GridBagLayout layout = new GridBagLayout();

	private final JLabel lb_from = new JLabel("From:");
	private final JTextField tf_from = new JTextField();
	private final JLink lnk_smtpInfo = new JLink(Program.getInstance().getWebsite() + "/smtp.htm", "What is SMTP?");

	private final JLabel lb_smtp = new JLabel("SMTP:");
	private final JTextField tf_smtp = new JTextField();
	private final JCheckBox cb_smtp_auth = new JCheckBox();

	private final JLabel lb_smtpUser = new JLabel("User:");
	private final JTextField tf_smtpUser = new JTextField();

	private final JLabel lb_smtpPW = new JLabel("PW:");
	private final JPasswordField pf_smtpPW = new JPasswordField();
	private final JCheckBox cb_pwSave = new JCheckBox();

	private final JLabel lb_to = new JLabel("To:");
	private final JTextField tf_to = new JTextField();

	private final JLabel lb_cc = new JLabel("CC:");
	private final JTextField tf_cc = new JTextField();

	private final JLabel lb_bcc = new JLabel("BCC:");
	private final JTextField tf_bcc = new JTextField();

	private final JLabel lb_subject = new JLabel("Subject:");
	private final JTextField tf_subject = new JTextField();

	private final JTextArea ta_text = new JTextArea(5, 5);
	JScrollPane sp_text = new JScrollPane(ta_text);

	private final JCheckBox cb_attachXml = new JCheckBox();
	private final JCheckBox cb_attachGif = new JCheckBox();
	private final JCheckBox cb_attachPdf = new JCheckBox();

	private final JButton bt_send = new JButton("Send");
	private final JButton bt_cancel = new JButton("Cancel");

	private final JPanel panel_attachments = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private final JPanel panel_attachmentsWithButton = new JPanel(layout);

	// the padding between lines is different for the labels and text components of the grid bag layout
	private final Insets paddingLeftLabel = new Insets(paddingTop, outerPaddingLeft, paddingBottom, halfHorizontalDividerSpace);
	private final Insets paddingMessagebox = new Insets(paddingTop, outerPaddingLeft, paddingBottom, outerPaddingRight);
	private final Insets paddingText = new Insets(paddingTop, halfHorizontalDividerSpace, paddingBottom, outerPaddingRight);
	private final Insets paddingCheckbox = new Insets(paddingTop - 2, halfHorizontalDividerSpace, paddingBottom - 2, outerPaddingRight);
	private final Insets paddingRightLabel = new Insets(paddingTop, halfHorizontalDividerSpace, paddingBottom, halfHorizontalDividerSpace);
	private final Insets noPadding = new Insets(0, 0, 0, 0);

	// the label doesn't get any additional space. it's always as short as possible
	private final double noWeight = 0;
	private final double fullWeight = 1;
	private final double leftWeight = 0.75;
	private final double rightWeight = 0.25;

	// the constraint int to fill the width
	private final int fillWidth = GridBagConstraints.HORIZONTAL;
	private final int fillBoth = GridBagConstraints.BOTH;

	public MailPanel() {

		initAndFillComponents();

		setLayout(layout);
		setSize(new Dimension(0, Config.getInstance().getMail_split_position()));

		int line = 0;
		addComponent(this, layout, Box.createRigidArea(new Dimension(0, verticalDividerSpace)), 0, line, 10, 1, fillWidth, fullWeight, 0, noPadding);
		line++;
		addComponent(this, layout, lb_to, 0, line, 1, 1, fillWidth, noWeight, 0, paddingLeftLabel);
		addComponent(this, layout, tf_to, 1, line, 1, 1, fillWidth, leftWeight, 0, paddingText);
		addComponent(this, layout, lb_from, 2, line, 1, 1, fillWidth, noWeight, 0, paddingRightLabel);
		addComponent(this, layout, tf_from, 3, line, 1, 1, fillWidth, rightWeight, 0, paddingRightLabel);
		addComponent(this, layout, lnk_smtpInfo, 4, line, 1, 1, fillWidth, noWeight, 0, paddingText);
		line++;
		addComponent(this, layout, lb_cc, 0, line, 1, 1, fillWidth, noWeight, 0, paddingLeftLabel);
		addComponent(this, layout, tf_cc, 1, line, 1, 1, fillWidth, leftWeight, 0, paddingText);
		addComponent(this, layout, lb_smtp, 2, line, 1, 1, fillWidth, noWeight, 0, paddingRightLabel);
		addComponent(this, layout, tf_smtp, 3, line, 1, 1, fillWidth, rightWeight, 0, paddingRightLabel);
		addComponent(this, layout, cb_smtp_auth, 4, line, 1, 1, fillWidth, noWeight, 0, paddingText);
		line++;
		addComponent(this, layout, lb_bcc, 0, line, 1, 1, fillWidth, noWeight, 0, paddingLeftLabel);
		addComponent(this, layout, tf_bcc, 1, line, 1, 1, fillWidth, leftWeight, 0, paddingText);
		addComponent(this, layout, lb_smtpUser, 2, line, 1, 1, fillWidth, noWeight, 0, paddingRightLabel);
		addComponent(this, layout, tf_smtpUser, 3, line, 1, 1, fillWidth, rightWeight, 0, paddingRightLabel);
		line++;
		addComponent(this, layout, lb_subject, 0, line, 1, 1, fillWidth, noWeight, 0, paddingLeftLabel);
		addComponent(this, layout, tf_subject, 1, line, 1, 1, fillWidth, leftWeight, 0, paddingText);
		addComponent(this, layout, lb_smtpPW, 2, line, 1, 1, fillWidth, noWeight, 0, paddingRightLabel);
		addComponent(this, layout, pf_smtpPW, 3, line, 1, 1, fillWidth, rightWeight, 0, paddingRightLabel);
		addComponent(this, layout, cb_pwSave, 4, line, 1, 1, fillWidth, noWeight, 0, paddingCheckbox);
		line++;
		addComponent(this, layout, Box.createRigidArea(new Dimension(0, verticalDividerSpace)), 0, line, 10, 1, fillWidth, fullWeight, 0, noPadding);
		line++;
		addComponent(this, layout, sp_text, 0, line, 5, 1, fillBoth, leftWeight, 1, paddingMessagebox);
		line++;
		addComponent(this, layout, panel_attachmentsWithButton, 1, line, 5, 1, fillWidth, fullWeight, 0, noPadding);
		line++;
		addComponent(this, layout, Box.createRigidArea(new Dimension(0, verticalDividerSpace)), 0, line, 4, 1, fillWidth, fullWeight, 0, noPadding);

	}

	private void initAndFillComponents() {

		ta_text.setText(Constants.getDefaultMailtext());

		cb_pwSave.setText("save in config");
		cb_attachXml.setText("attach " + Program.getInstance().getExtension().toUpperCase());
		cb_attachGif.setText("attach GIF");
		cb_attachPdf.setText("attach PDF");
		cb_smtp_auth.setText("authentication");

		bt_send.addActionListener(new SendActionListener());
		bt_cancel.addActionListener(new CancelActionListener());
		cb_smtp_auth.addActionListener(new AuthentificationActionListener());

		// Set Tooltips
		String adressToolTip = "Separate multiple adresses with ','";
		cb_pwSave.setToolTipText("WARNING: The password is stored as plain text in " + Program.getInstance().getConfigName());
		tf_from.setToolTipText(adressToolTip);
		tf_to.setToolTipText(adressToolTip);
		tf_cc.setToolTipText(adressToolTip);
		tf_bcc.setToolTipText(adressToolTip);

		// Fill File Panel
		panel_attachments.add(cb_attachXml);
		panel_attachments.add(Box.createRigidArea(new Dimension(5, 0)));
		panel_attachments.add(cb_attachGif);
		panel_attachments.add(Box.createRigidArea(new Dimension(5, 0)));
		panel_attachments.add(cb_attachPdf);

		// Fill the superpanel which holds attachments and the send button
		addComponent(panel_attachmentsWithButton, layout, panel_attachments, 0, 0, 1, 1, fillWidth, fullWeight, 0, noPadding);
		addComponent(panel_attachmentsWithButton, layout, bt_send, 1, 0, 1, 1, fillWidth, fullWeight, 0, paddingText);
		addComponent(panel_attachmentsWithButton, layout, bt_cancel, 2, 0, 1, 1, fillWidth, fullWeight, 0, paddingText);

		setAllFonts();
		readConstants();
		checkVisibilityOfSmtpAuth();
	}

	private List<File> getAttachments() throws IOException {
		List<File> attachments = new LinkedList<File>();
		final String diagramName = "diagram_" + new SimpleDateFormat("yyyyMMdd_hhmmss").format(new Date());
		DiagramFileHandler fileHandler = CurrentDiagram.getInstance().getDiagramHandler().getFileHandler();

		if (cb_attachXml.isSelected()) {
			attachments.add(
				fileHandler.doSaveTempDiagram(diagramName, Program.getInstance().getExtension())
			);
		}

		if (cb_attachGif.isSelected()) {
			attachments.add(
				fileHandler.doSaveTempDiagram(diagramName, "gif")
			);
		}

		if (cb_attachPdf.isSelected()) {
			attachments.add(
				fileHandler.doSaveTempDiagram(diagramName, "pdf")
			);
		}

		return attachments;
	}

	private void showError(String msg, String title) {
		JOptionPane.showMessageDialog(
				this,
				msg,
				title,
				JOptionPane.ERROR_MESSAGE,
				UIManager.getIcon(
					"OptionPane.errorIcon"
				)
		);
	}

	private Properties getSMTPProperties(MailMessage message) {
		Properties properties = System.getProperties();

		// Set the SMTP Host
		properties.put("mail.smtp.host", message.getHost());

		// We want to close the connection immediately after sending
		properties.put("mail.smtp.quitwait", "false");

		// We want to use encryption if needed
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.ssl.protocols", "SSLv3 TLSv1");

		// If authentication is needed we set it to true
		if (message.isUseAuthentication()) {
			properties.put("mail.smtp.auth", "true");
		}
		else {
			properties.put("mail.smtp.auth", "false");
		}

		return properties;
	}

	public MailMessage getMailMessage() {
		MailMessage mailMessage = new MailMessage();
		mailMessage.setHost(tf_smtp.getText());
		mailMessage.setUser(tf_smtpUser.getText());
		mailMessage.setPassword(String.valueOf(pf_smtpPW.getPassword()));

		mailMessage.setToRecipients(removeWhitespaceAndSplitAt(tf_to.getText()));
		mailMessage.setCCRecipients(removeWhitespaceAndSplitAt(tf_cc.getText()));
		mailMessage.setBCCRecipients(removeWhitespaceAndSplitAt(tf_bcc.getText()));

		mailMessage.setFrom(tf_from.getText());
		mailMessage.setSubject(tf_subject.getText());
		mailMessage.setText(ta_text.getText());
		mailMessage.setUseAuthentication(cb_smtp_auth.isSelected());
		mailMessage.setAttachments(null);

		Properties props = getSMTPProperties(mailMessage);
		Session session = Session.getInstance(props);
		mailMessage.setMailSession(session);

		return mailMessage;
	}

	private void sendMail() {
		MailMessage mail = getMailMessage();

		// Set SMTP Authentication if the user or password field isn't empty
		if (!mail.getUser().isEmpty() || !mail.getPassword().isEmpty()) {
			mail.setUseAuthentication(true);
		}


		// Create the temp diagrams to send
		try {
			mail.setAttachments(
				getAttachments()
			);

		} catch (Exception e) {
			showError(
				"There has been an error with your diagram. Please make sure it's not empty.",
				"Diagram Error"
			);
			return;
		}

		try {
			setupMail(mail);

			String errorMsg = validateMail(mail);

			if (errorMsg != null) {
				showError(errorMsg, "Error");
				return;
			}

			sendMail(mail);

			Notifier.getInstance().showInfo("Email sent");
			closePanel();
		}

		catch (MessagingException e) {
			log.error("SMTP Error", e);
			showError("There has been an error with your smtp server." + Constants.NEWLINE + "Please recheck your smtp server and login data.", "SMTP Error");
		} catch (IOException e) {
			log.error("Mail Error", e);
			showError("There has been an error sending your mail." + Constants.NEWLINE + "Please recheck your input data.", "Sending Error");
		} catch (Throwable e) {
			log.error("Mail Error", e);
			showError("There has been an error sending your mail." + Constants.NEWLINE + "Please recheck your input data.", "Sending Error");
		} finally {
			if(mail.getAttachments() != null && !mail.getAttachments().isEmpty()) {
				for(File file : mail.getAttachments()) {
					Path.safeDeleteFile(file, false);
				}
			}
		}
	}

	private String validateMail(MailMessage mail) {
		if (mail.getHost().isEmpty()) {
			return "The SMTP field must not be empty";
		} else if (mail.getFrom().isEmpty()) {
			return "The FROM field must not be empty";
		} else if (mail.getToRecipients().length == 0) {
			return "The TO field must not be empty";
		} else {
			return null;
		}
	}

	private void sendMail(MailMessage mail) throws MessagingException {
		MimeMessage message = mail.getMessage();

		if (mail.isUseAuthentication()) {
			Transport transport = mail.getMailSession().getTransport("smtp");
			try {
				transport.connect(mail.getHost(), mail.getUser(), mail.getPassword());
				transport.sendMessage(message, message.getAllRecipients());
			} finally {
				transport.close();
			}
		}
		else { // No SMTP Authentication
			Transport.send(message);
		}
	}

	private void setRecipients(MimeMessage mimeMessage, MailMessage mailMessage) throws MessagingException {
		mimeMessage.setFrom(
			new InternetAddress(mailMessage.getFrom())
		);

		for(String to : mailMessage.getToRecipients()) {
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		}

		for(String cc : mailMessage.getCCRecipients()) {
			mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
		}

		for(String bcc : mailMessage.getBCCRecipients()) {
			mimeMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
		}
	}

	private void setupMail(MailMessage mail) throws MessagingException, IOException {
		MimeMessage message = getMimeMessage(mail);

		// Set all recipients of any kind (TO, CC, BCC)
		setRecipients(message, mail);

		message.setSubject(mail.getSubject());
	}

	public MimeMessage getMimeMessage(MailMessage mail) throws MessagingException, IOException {
		int attachmentLength = mail.getAttachments().size();

		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setText(mail.getText());

		MimeBodyPart[] attachmentParts = new MimeBodyPart[attachmentLength];

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(textPart);

		for(int i = 0; i < attachmentLength; i ++) {
			MimeBodyPart attachmentPart = new MimeBodyPart();
			attachmentParts[i] = attachmentPart;

			multipart.addBodyPart(attachmentPart);

		}

		MimeMessage mimeMessage = new MimeMessage(mail.getMailSession());

		mimeMessage.setContent(multipart);


		if(mail.getAttachments() != null) {
			int currentAttachment = 0;
			Iterator<File> iterator = mail.getAttachments().iterator();
			while(iterator.hasNext()) {
				File file = iterator.next();

				attachmentParts[currentAttachment ++].attachFile(
					file
				);
			}
		}

		return mimeMessage;
	}

	/**
	 * Adds a component to this panel
	 *
	 * @param gbl
	 *            The GridBagLayout of this component
	 * @param c
	 *            The Component to add
	 * @param x
	 *            The x value of grid where the component starts
	 * @param y
	 *            The y value of grid where the component starts
	 * @param width
	 *            How many spaces of the grid's width will be used by the component
	 * @param height
	 *            How many spaces of the grid's height will be used by the component
	 * @param fill
	 *            If the component's display area is larger than the component's requested size this param determines whether and how to resize the component
	 * @param weightx
	 *            Specifies how to distribute extra horizontal space.
	 * @param weighty
	 *            Specifies how to distribute extra vertical space.
	 * @param insets
	 *            Specifies the external padding of the component (= minimum amount of space between the component and the edges of its display area)
	 */
	private void addComponent(JPanel panel, GridBagLayout gbl, Component c, int x, int y, int width, int height, int fill, double weightx, double weighty, Insets insets) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.fill = fill;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbc.insets = insets;
		gbl.setConstraints(c, gbc);
		panel.add(c);
	}

	public String[] removeWhitespaceAndSplitAt(String inputString) {
		if (inputString.isEmpty()) {
			return new String[] {};
		}
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < inputString.length(); i++) {
			if (inputString.charAt(i) != ' ') {
				sb.append(inputString.charAt(i));
			}
		}
		return sb.toString().split(",");
	}

	private void storeConstants() {
		ConfigMail cfgMail = ConfigMail.getInstance();
		cfgMail.setMail_smtp(tf_smtp.getText());
		cfgMail.setMail_smtp_auth(cb_smtp_auth.isSelected());
		cfgMail.setMail_smtp_user(tf_smtpUser.getText());
		cfgMail.setMail_smtp_pw_store(cb_pwSave.isSelected());
		if (cb_pwSave.isSelected()) {
			cfgMail.setMail_smtp_pw(String.valueOf(pf_smtpPW.getPassword()));
		}
		else {
			cfgMail.setMail_smtp_pw("");
		}
		cfgMail.setMail_from(tf_from.getText());
		cfgMail.setMail_to(tf_to.getText());
		cfgMail.setMail_cc(tf_cc.getText());
		cfgMail.setMail_bcc(tf_bcc.getText());
		cfgMail.setMail_xml(cb_attachXml.isSelected());
		cfgMail.setMail_gif(cb_attachGif.isSelected());
		cfgMail.setMail_pdf(cb_attachPdf.isSelected());
	}

	private void readConstants() {
		ConfigMail cfgMail = ConfigMail.getInstance();
		tf_smtp.setText(cfgMail.getMail_smtp());
		cb_smtp_auth.setSelected(cfgMail.isMail_smtp_auth());
		tf_smtpUser.setText(cfgMail.getMail_smtp_user());
		cb_pwSave.setSelected(cfgMail.isMail_smtp_pw_store());
		pf_smtpPW.setText(cfgMail.getMail_smtp_pw());
		tf_from.setText(cfgMail.getMail_from());
		tf_to.setText(cfgMail.getMail_to());
		tf_cc.setText(cfgMail.getMail_cc());
		tf_bcc.setText(cfgMail.getMail_bcc());
		cb_attachXml.setSelected(cfgMail.isMail_xml());
		cb_attachGif.setSelected(cfgMail.isMail_gif());
		cb_attachPdf.setSelected(cfgMail.isMail_pdf());
	}

	private void setAllFonts() {

		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
		Font fontBold = new Font(Font.SANS_SERIF, Font.BOLD, 12);
		Font fontSmallItalic = new Font(Font.SANS_SERIF, Font.ITALIC, 10);

		lb_smtp.setFont(fontBold);
		tf_smtp.setFont(font);
		lb_smtpUser.setFont(fontBold);
		tf_smtpUser.setFont(font);
		lb_smtpPW.setFont(fontBold);
		pf_smtpPW.setFont(font);
		lb_from.setFont(fontBold);
		tf_from.setFont(font);
		lb_to.setFont(fontBold);
		tf_to.setFont(font);
		lb_cc.setFont(fontBold);
		tf_cc.setFont(font);
		lb_bcc.setFont(fontBold);
		tf_bcc.setFont(font);
		lb_subject.setFont(fontBold);
		tf_subject.setFont(font);
		ta_text.setFont(font);
		cb_attachXml.setFont(fontBold);
		cb_attachGif.setFont(fontBold);
		cb_attachPdf.setFont(fontBold);
		lnk_smtpInfo.setFont(fontSmallItalic);
		cb_smtp_auth.setFont(fontSmallItalic);
		cb_pwSave.setFont(fontSmallItalic);
	}

	public void closePanel() {
		storeConstants();
		Config.getInstance().setMail_split_position((int) this.getSize().getHeight());
		CurrentGui.getInstance().getGui().setMailPanelEnabled(false);
	}

	private class SendActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			sendMail();
		}
	}

	private class CancelActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			closePanel();
		}
	}

	private void checkVisibilityOfSmtpAuth() {
		boolean val = cb_smtp_auth.isSelected();
		lb_smtpUser.setVisible(val);
		tf_smtpUser.setVisible(val);
		lb_smtpPW.setVisible(val);
		pf_smtpPW.setVisible(val);
		cb_pwSave.setVisible(val);
		if (!val) {
			tf_smtpUser.setText("");
			pf_smtpPW.setText("");
			cb_pwSave.setSelected(false);
		}
		repaint();
	}

	private class AuthentificationActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			checkVisibilityOfSmtpAuth();
		}
	}


	public JTextField getFrom() {
		return tf_from;
	}

	public JTextField getSMTP() {
		return tf_smtp;
	}

	public JCheckBox getIsSMTPAuth() {
		return cb_smtp_auth;
	}

	public JTextField getSMTPUser() {
		return tf_smtpUser;
	}

	public JPasswordField getSMTPPassword() {
		return pf_smtpPW;
	}

	public JCheckBox getIsSavePassword() {
		return cb_pwSave;
	}

	public JTextField getTo() {
		return tf_to;
	}

	public JTextField getCC() {
		return tf_cc;
	}

	public JTextField getBCC() {
		return tf_bcc;
	}

	public JTextField getSubject() {
		return tf_subject;
	}

	public JTextArea getText() {
		return ta_text;
	}

	public JCheckBox getAttachXml() {
		return cb_attachXml;
	}

	public JCheckBox getAttachGif() {
		return cb_attachGif;
	}

	public JCheckBox getAttachPdf() {
		return cb_attachPdf;
	}
}
