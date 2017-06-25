package com.baselet.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

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

	/**
	 * Some int and String
	 */

	private static final int paddingTop = 1;
	private static final int paddingBottom = 1;
	private static final int outerPaddingLeft = 15;
	private static final int outerPaddingRight = 15;
	private static final int halfHorizontalDividerSpace = 2;
	private static final int verticalDividerSpace = 10;

	/**
	 * Components
	 */

	private final GridBagLayout layout = new GridBagLayout();

	private final JLabel lbFrom = new JLabel("From:");
	private final JTextField tfFrom = new JTextField();
	private final JLink lnkSmtpInfo = new JLink(Program.getInstance().getWebsite() + "/smtp.htm", "What is SMTP?");

	private final JLabel lbSmtp = new JLabel("SMTP:");
	private final JTextField tfSmtp = new JTextField();
	private final JCheckBox cbSmtpAuth = new JCheckBox();

	private final JLabel lbSmtpUser = new JLabel("User:");
	private final JTextField tfSmtpUser = new JTextField();

	private final JLabel lbSmtpPW = new JLabel("PW:");
	private final JPasswordField pfSmtpPW = new JPasswordField();
	private final JCheckBox cbPwSave = new JCheckBox();

	private final JLabel lbTo = new JLabel("To:");
	private final JTextField tfTo = new JTextField();

	private final JLabel lbCC = new JLabel("CC:");
	private final JTextField tfCC = new JTextField();

	private final JLabel lbBCC = new JLabel("BCC:");
	private final JTextField tfBCC = new JTextField();

	private final JLabel lbSubject = new JLabel("Subject:");
	private final JTextField tfSubject = new JTextField();

	private final JTextArea taText = new JTextArea(5, 5);
	JScrollPane sp_text = new JScrollPane(taText);

	private final JCheckBox cbAttachXml = new JCheckBox();
	private final JCheckBox cbAttachGif = new JCheckBox();
	private final JCheckBox cbAttachPdf = new JCheckBox();

	private final JButton btSend = new JButton("Send");
	private final JButton btCancel = new JButton("Cancel");

	private final JPanel panelAttachments = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private final JPanel panelAttachmentsWithButton = new JPanel(layout);

	// the padding between lines is different for the labels and text components of the grid bag layout
	private final Insets paddingLeftLabel = new Insets(paddingTop, outerPaddingLeft, paddingBottom, halfHorizontalDividerSpace);
	private final Insets paddingMessagebox = new Insets(paddingTop, outerPaddingLeft, paddingBottom, outerPaddingRight);
	private final Insets paddingText = new Insets(paddingTop, halfHorizontalDividerSpace, paddingBottom, outerPaddingRight);
	private final Insets paddingCheckbox = new Insets(paddingTop - 2, halfHorizontalDividerSpace, paddingBottom - 2, outerPaddingRight);
	private final Insets paddingRightLabel = new Insets(paddingTop, halfHorizontalDividerSpace, paddingBottom, halfHorizontalDividerSpace);
	private final Insets noPadding = new Insets(0, 0, 0, 0);

	// the label doesn't get any additional space. it's always as short as possible
	private static final double noWeight = 0;
	private static final double fullWeight = 1;
	private static final double leftWeight = 0.75;
	private static final double rightWeight = 0.25;

	// the constraint int to fill the width
	private static final int fillWidth = GridBagConstraints.HORIZONTAL;
	private static final int fillBoth = GridBagConstraints.BOTH;

	public MailPanel() {

		initAndFillComponents();

		setLayout(layout);
		setSize(new Dimension(0, Config.getInstance().getMail_split_position()));

		int line = 0;
		addComponent(this, layout, Box.createRigidArea(new Dimension(0, verticalDividerSpace)), new Point(0, line), new Point(10, 1), fillWidth, fullWeight, 0, noPadding);
		line++;
		addComponent(this, layout, lbTo, new Point(0, line), new Point(1, 1), fillWidth, noWeight, 0, paddingLeftLabel);
		addComponent(this, layout, tfTo, new Point(1, line), new Point(1, 1), fillWidth, leftWeight, 0, paddingText);
		addComponent(this, layout, lbFrom, new Point(2, line), new Point(1, 1), fillWidth, noWeight, 0, paddingRightLabel);
		addComponent(this, layout, tfFrom, new Point(3, line), new Point(1, 1), fillWidth, rightWeight, 0, paddingRightLabel);
		addComponent(this, layout, lnkSmtpInfo, new Point(4, line), new Point(1, 1), fillWidth, noWeight, 0, paddingText);
		line++;
		addComponent(this, layout, lbCC, new Point(0, line), new Point(1, 1), fillWidth, noWeight, 0, paddingLeftLabel);
		addComponent(this, layout, tfCC, new Point(1, line), new Point(1, 1), fillWidth, leftWeight, 0, paddingText);
		addComponent(this, layout, lbSmtp, new Point(2, line), new Point(1, 1), fillWidth, noWeight, 0, paddingRightLabel);
		addComponent(this, layout, tfSmtp, new Point(3, line), new Point(1, 1), fillWidth, rightWeight, 0, paddingRightLabel);
		addComponent(this, layout, cbSmtpAuth, new Point(4, line), new Point(1, 1), fillWidth, noWeight, 0, paddingText);
		line++;
		addComponent(this, layout, lbBCC, new Point(0, line), new Point(1, 1), fillWidth, noWeight, 0, paddingLeftLabel);
		addComponent(this, layout, tfBCC, new Point(1, line), new Point(1, 1), fillWidth, leftWeight, 0, paddingText);
		addComponent(this, layout, lbSmtpUser, new Point(2, line), new Point(1, 1), fillWidth, noWeight, 0, paddingRightLabel);
		addComponent(this, layout, tfSmtpUser, new Point(3, line), new Point(1, 1), fillWidth, rightWeight, 0, paddingRightLabel);
		line++;
		addComponent(this, layout, lbSubject, new Point(0, line), new Point(1, 1), fillWidth, noWeight, 0, paddingLeftLabel);
		addComponent(this, layout, tfSubject, new Point(1, line), new Point(1, 1), fillWidth, leftWeight, 0, paddingText);
		addComponent(this, layout, lbSmtpPW, new Point(2, line), new Point(1, 1), fillWidth, noWeight, 0, paddingRightLabel);
		addComponent(this, layout, pfSmtpPW, new Point(3, line), new Point(1, 1), fillWidth, rightWeight, 0, paddingRightLabel);
		addComponent(this, layout, cbPwSave, new Point(4, line), new Point(1, 1), fillWidth, noWeight, 0, paddingCheckbox);
		line++;
		addComponent(this, layout, Box.createRigidArea(new Dimension(0, verticalDividerSpace)), new Point(0, line), new Point(10, 1), fillWidth, fullWeight, 0, noPadding);
		line++;
		addComponent(this, layout, sp_text, new Point(0, line), new Point(5, 1), fillBoth, leftWeight, 1, paddingMessagebox);
		line++;
		addComponent(this, layout, panelAttachmentsWithButton, new Point(1, line), new Point(5, 1), fillWidth, fullWeight, 0, noPadding);
		line++;
		addComponent(this, layout, Box.createRigidArea(new Dimension(0, verticalDividerSpace)), new Point(0, line), new Point(4, 1), fillWidth, fullWeight, 0, noPadding);

	}

	private void initAndFillComponents() {

		taText.setText(Constants.getDefaultMailtext());

		cbPwSave.setText("save in config");
		cbAttachXml.setText("attach " + Program.getInstance().getExtension().toUpperCase());
		cbAttachGif.setText("attach GIF");
		cbAttachPdf.setText("attach PDF");
		cbSmtpAuth.setText("authentication");

		btSend.addActionListener(new SendActionListener());
		btCancel.addActionListener(new CancelActionListener());
		cbSmtpAuth.addActionListener(new AuthentificationActionListener());

		// Set Tooltips
		String adressToolTip = "Separate multiple adresses with ','";
		cbPwSave.setToolTipText("WARNING: The password is stored as plain text in " + Program.getInstance().getConfigName());
		tfFrom.setToolTipText(adressToolTip);
		tfTo.setToolTipText(adressToolTip);
		tfCC.setToolTipText(adressToolTip);
		tfBCC.setToolTipText(adressToolTip);

		// Fill File Panel
		panelAttachments.add(cbAttachXml);
		panelAttachments.add(Box.createRigidArea(new Dimension(5, 0)));
		panelAttachments.add(cbAttachGif);
		panelAttachments.add(Box.createRigidArea(new Dimension(5, 0)));
		panelAttachments.add(cbAttachPdf);

		// Fill the superpanel which holds attachments and the send button
		addComponent(panelAttachmentsWithButton, layout, panelAttachments, new Point(0, 0), new Point(1, 1), fillWidth, fullWeight, 0, noPadding);
		addComponent(panelAttachmentsWithButton, layout, btSend, new Point(1, 0), new Point(1, 1), fillWidth, fullWeight, 0, paddingText);
		addComponent(panelAttachmentsWithButton, layout, btCancel, new Point(2, 0), new Point(1, 1), fillWidth, fullWeight, 0, paddingText);

		setAllFonts();
		readConstants();
		checkVisibilityOfSmtpAuth();
	}

	public List<File> getAttachments() throws IOException {
		List<File> attachments = new LinkedList<File>();
		final String diagramName = "diagram_" + new SimpleDateFormat("yyyyMMdd_hhmmss").format(new Date());
		DiagramFileHandler fileHandler = CurrentDiagram.getInstance().getDiagramHandler().getFileHandler();

		if (cbAttachXml.isSelected()) {
			attachments.add(
				fileHandler.doSaveTempDiagram(diagramName, Program.getInstance().getExtension())
			);
		}

		if (cbAttachGif.isSelected()) {
			attachments.add(
				fileHandler.doSaveTempDiagram(diagramName, "gif")
			);
		}

		if (cbAttachPdf.isSelected()) {
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

	public Properties getSMTPProperties(MailMessage message) {
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
		mailMessage.setHost(tfSmtp.getText());
		mailMessage.setUser(tfSmtpUser.getText());
		mailMessage.setPassword(String.valueOf(pfSmtpPW.getPassword()));

		mailMessage.setToRecipients(removeWhitespaceAndSplitAt(tfTo.getText()));
		mailMessage.setCCRecipients(removeWhitespaceAndSplitAt(tfCC.getText()));
		mailMessage.setBCCRecipients(removeWhitespaceAndSplitAt(tfBCC.getText()));

		mailMessage.setFrom(tfFrom.getText());
		mailMessage.setSubject(tfSubject.getText());
		mailMessage.setText(taText.getText());
		mailMessage.setUseAuthentication(cbSmtpAuth.isSelected());
		mailMessage.setAttachments(null);

		Properties props = getSMTPProperties(mailMessage);
		Session session = Session.getInstance(props);
		mailMessage.setMailSession(session);

		return mailMessage;
	}

	public void sendMail() {
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
			log.error("Diagram error", e);
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

	public String validateMail(MailMessage mail) {
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

	public void sendMail(MailMessage mail) throws MessagingException {
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

	public void setRecipients(MimeMessage mimeMessage, MailMessage mailMessage) throws MessagingException {
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

	public void setupMail(MailMessage mail) throws MessagingException, IOException {
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
	 * @param pos
	 *            The position on the grid where the component starts
	 * @param size
	 *            How many spaces of the grid's width and height will be used by the component
	 * @param fill
	 *            If the component's display area is larger than the component's requested size this param determines whether and how to resize the component
	 * @param weightx
	 *            Specifies how to distribute extra horizontal space.
	 * @param weighty
	 *            Specifies how to distribute extra vertical space.
	 * @param insets
	 *            Specifies the external padding of the component (= minimum amount of space between the component and the edges of its display area)
	 */
	private void addComponent(JPanel panel, GridBagLayout gbl, Component c, Point pos, Point size, int fill, double weightx, double weighty, Insets insets) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = (int)pos.getX();
		gbc.gridy = (int)pos.getY();
		gbc.gridwidth = (int)pos.getX();
		gbc.gridheight = (int)pos.getY();
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
		cfgMail.setMail_smtp(tfSmtp.getText());
		cfgMail.setMail_smtp_auth(cbSmtpAuth.isSelected());
		cfgMail.setMail_smtp_user(tfSmtpUser.getText());
		cfgMail.setMail_smtp_pw_store(cbPwSave.isSelected());
		if (cbPwSave.isSelected()) {
			cfgMail.setMail_smtp_pw(String.valueOf(pfSmtpPW.getPassword()));
		}
		else {
			cfgMail.setMail_smtp_pw("");
		}
		cfgMail.setMail_from(tfFrom.getText());
		cfgMail.setMail_to(tfTo.getText());
		cfgMail.setMail_cc(tfCC.getText());
		cfgMail.setMail_bcc(tfBCC.getText());
		cfgMail.setMail_xml(cbAttachXml.isSelected());
		cfgMail.setMail_gif(cbAttachGif.isSelected());
		cfgMail.setMail_pdf(cbAttachPdf.isSelected());
	}

	private void readConstants() {
		ConfigMail cfgMail = ConfigMail.getInstance();
		tfSmtp.setText(cfgMail.getMail_smtp());
		cbSmtpAuth.setSelected(cfgMail.isMail_smtp_auth());
		tfSmtpUser.setText(cfgMail.getMail_smtp_user());
		cbPwSave.setSelected(cfgMail.isMail_smtp_pw_store());
		pfSmtpPW.setText(cfgMail.getMail_smtp_pw());
		tfFrom.setText(cfgMail.getMail_from());
		tfTo.setText(cfgMail.getMail_to());
		tfCC.setText(cfgMail.getMail_cc());
		tfBCC.setText(cfgMail.getMail_bcc());
		cbAttachXml.setSelected(cfgMail.isMail_xml());
		cbAttachGif.setSelected(cfgMail.isMail_gif());
		cbAttachPdf.setSelected(cfgMail.isMail_pdf());
	}

	private void setAllFonts() {

		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
		Font fontBold = new Font(Font.SANS_SERIF, Font.BOLD, 12);
		Font fontSmallItalic = new Font(Font.SANS_SERIF, Font.ITALIC, 10);

		lbSmtp.setFont(fontBold);
		tfSmtp.setFont(font);
		lbSmtpUser.setFont(fontBold);
		tfSmtpUser.setFont(font);
		lbSmtpPW.setFont(fontBold);
		pfSmtpPW.setFont(font);
		lbFrom.setFont(fontBold);
		tfFrom.setFont(font);
		lbTo.setFont(fontBold);
		tfTo.setFont(font);
		lbCC.setFont(fontBold);
		tfCC.setFont(font);
		lbBCC.setFont(fontBold);
		tfBCC.setFont(font);
		lbSubject.setFont(fontBold);
		tfSubject.setFont(font);
		taText.setFont(font);
		cbAttachXml.setFont(fontBold);
		cbAttachGif.setFont(fontBold);
		cbAttachPdf.setFont(fontBold);
		lnkSmtpInfo.setFont(fontSmallItalic);
		cbSmtpAuth.setFont(fontSmallItalic);
		cbPwSave.setFont(fontSmallItalic);
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
		boolean val = cbSmtpAuth.isSelected();
		lbSmtpUser.setVisible(val);
		tfSmtpUser.setVisible(val);
		lbSmtpPW.setVisible(val);
		pfSmtpPW.setVisible(val);
		cbPwSave.setVisible(val);
		if (!val) {
			tfSmtpUser.setText("");
			pfSmtpPW.setText("");
			cbPwSave.setSelected(false);
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
		return tfFrom;
	}

	public JTextField getSMTP() {
		return tfSmtp;
	}

	public JCheckBox getIsSMTPAuth() {
		return cbSmtpAuth;
	}

	public JTextField getSMTPUser() {
		return tfSmtpUser;
	}

	public JPasswordField getSMTPPassword() {
		return pfSmtpPW;
	}

	public JCheckBox getIsSavePassword() {
		return cbPwSave;
	}

	public JTextField getTo() {
		return tfTo;
	}

	public JTextField getCC() {
		return tfCC;
	}

	public JTextField getBCC() {
		return tfBCC;
	}

	public JTextField getSubject() {
		return tfSubject;
	}

	public JTextArea getText() {
		return taText;
	}

	public JCheckBox getAttachXml() {
		return cbAttachXml;
	}

	public JCheckBox getAttachGif() {
		return cbAttachGif;
	}

	public JCheckBox getAttachPdf() {
		return cbAttachPdf;
	}
}
