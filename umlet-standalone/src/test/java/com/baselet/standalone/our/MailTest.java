package com.baselet.standalone.our;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import javax.swing.JPasswordField;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.baselet.control.config.handler.ConfigHandler;
import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;
import com.baselet.control.util.Utils;
import com.baselet.data.MailMessage;
import com.baselet.gui.MailPanel;

/**
 * © 2017, Gopper
 */
public class MailTest {
	private MailPanel mailPanel;
	@Before
	public void init() {
		Utils.BuildInfo buildInfo = Utils.readBuildInfo();
		Program.init(buildInfo.version, RuntimeType.BATCH);
		ConfigHandler.loadConfig();

		mailPanel = new MailPanel();

	}

	private void checkSplit(String input, int outputLength) {
		String[] output = mailPanel.removeWhitespaceAndSplitAt(input);
		assertEquals(outputLength, output.length);
	}

	@Test
	public void split() {
		checkSplit("to@to.com", 1);
		checkSplit("to@to.com,to@to.com",2);
		checkSplit("to@to.com, to@to.com", 2);
		checkSplit("", 0);
		checkSplit(" ", 1); // Should be 0
	}


	@Test
	public void correctSetup() {
		MailPanel panel = new MailPanel();
		panel.getFrom().setText("from@from.com");
		panel.getSMTP().setText("smtp.gmail.com");
		panel.getIsSMTPAuth().setSelected(true);
		panel.getSMTPUser().setText("gmail@gmail.com");
		panel.getSMTPPassword().setText("1234");
		panel.getIsSavePassword().setSelected(false); // <- if true check config
		panel.getTo().setText("to@to.com");
		panel.getCC().setText("cc@cc.com");
		panel.getBCC().setText("bcc@bcc.com");
		panel.getSubject().setText("Subject");
		panel.getText().setText("Super important text");
		panel.getAttachGif().setSelected(true);
		panel.getAttachPdf().setSelected(false);
		panel.getAttachXml().setSelected(false);


		MailMessage message = panel.getMailMessage();
		assertEquals(panel.getSMTP().getText(), message.getHost());;
		assertEquals(panel.getIsSMTPAuth().isSelected(), message.isUseAuthentication());
		assertEquals(panel.getSMTPUser().getText(), message.getUser());
		assertEquals(getPassword(panel.getSMTPPassword()), message.getPassword());
		assertEquals(panel.getFrom().getText(), message.getFrom());
		assertEquals(panel.getSubject().getText(), message.getSubject());
		assertEquals(panel.getText().getText(), message.getText());
	}

	@Test
	public void multipleAddresses() {
		MailPanel panel = new MailPanel();
		panel.getTo().setText("to@to.com,to@to.com");
		panel.getCC().setText("cc@cc.com,cc@cc.com");
		panel.getBCC().setText("bcc@bcc.com,bcc@bcc.com");

		String[] to = panel.removeWhitespaceAndSplitAt(panel.getTo().getText());
		String[] cc = panel.removeWhitespaceAndSplitAt(panel.getCC().getText());
		String[] bcc = panel.removeWhitespaceAndSplitAt(panel.getBCC().getText());


		MailMessage message = panel.getMailMessage();
		assertArrayEquals(to, message.getToRecipients());
		assertArrayEquals(cc, message.getCCRecipients());
		assertArrayEquals(bcc, message.getBCCRecipients());
	}

	@Test
	public void singleAddress() {
		MailPanel panel = new MailPanel();
		panel.getTo().setText("to@to.com");
		panel.getCC().setText("cc@cc.com");
		panel.getBCC().setText("bcc@bcc.com");

		String[] to = panel.removeWhitespaceAndSplitAt(panel.getTo().getText());
		String[] cc = panel.removeWhitespaceAndSplitAt(panel.getCC().getText());
		String[] bcc = panel.removeWhitespaceAndSplitAt(panel.getBCC().getText());


		MailMessage message = panel.getMailMessage();
		assertArrayEquals(to, message.getToRecipients());
		assertArrayEquals(cc, message.getCCRecipients());
		assertArrayEquals(bcc, message.getBCCRecipients());
	}
	@Test
	public void emptyAdresses() {
		MailPanel panel = new MailPanel();
		panel.getTo().setText("");
		panel.getCC().setText("");
		panel.getBCC().setText("");

		String[] to = panel.removeWhitespaceAndSplitAt(panel.getTo().getText());
		String[] cc = panel.removeWhitespaceAndSplitAt(panel.getCC().getText());
		String[] bcc = panel.removeWhitespaceAndSplitAt(panel.getBCC().getText());


		MailMessage message = panel.getMailMessage();
		assertArrayEquals(to, message.getToRecipients());
		assertArrayEquals(cc, message.getCCRecipients());
		assertArrayEquals(bcc, message.getBCCRecipients());
	}

	private String getPassword(JPasswordField field) {
		return String.valueOf(field.getPassword());
	}
}
