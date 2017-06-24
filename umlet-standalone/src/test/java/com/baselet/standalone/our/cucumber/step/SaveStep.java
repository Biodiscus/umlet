package com.baselet.standalone.our.cucumber.step;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.net.URL;

import org.slf4j.LoggerFactory;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.config.handler.ConfigHandler;
import com.baselet.control.enums.ElementId;
import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;
import com.baselet.control.util.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.Notifier;
import com.baselet.diagram.io.DiagramFileHandler;
import com.baselet.element.ElementFactorySwing;
import com.baselet.element.NewGridElement;
import com.baselet.element.elementnew.uml.Class;
import com.baselet.element.interfaces.GridElement;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SaveStep {

	private DiagramHandler diagramToSave;
	private DiagramHandler diagramLoaded;
	private File ufxTempFile;

	@Given("^a Diagram with a Element positioned at '(\\d+),(\\d+)'$")
	public void aDiagramWithAElementPositionedAt(int arg0, int arg1) throws Throwable {
		Utils.BuildInfo buildInfo = Utils.readBuildInfo();
		Program.init(buildInfo.version, RuntimeType.BATCH);
		ConfigHandler.loadConfig();

		ufxTempFile = new File("src/test/resources/emptyWorkspace.uxf");
		diagramToSave = new DiagramHandler(ufxTempFile);

		NewGridElement element = ElementFactorySwing.create(ElementId.UMLClass, new Rectangle(arg0, arg1, 1000, 1000), "", "", diagramToSave);
		diagramToSave.getDrawPanel().addElement(element);
	}

	private File getFile(String name) {
		ClassLoader loader = getClass().getClassLoader();
		URL url = loader.getResource(name);
		return new File(url.getFile());
	}


	@When("^the Diagram has been saved$")
	public void theDiagramHasBeenSaved() throws Throwable {
		Notifier.getInstance();
		LoggerFactory.getLogger(getClass()).info("Saving"); //Komt nergens voor in de console output
		ufxTempFile = File.createTempFile("temp", ".ufx");
		DiagramFileHandler diagramFileHandler = DiagramFileHandler.createInstance(diagramToSave, ufxTempFile);
		diagramFileHandler.doSave();
	}

	@Then("^load the Diagram again$")
	public void loadTheDiagramAgain() throws Throwable {
		diagramLoaded = new DiagramHandler(ufxTempFile);
	}

	@Then("^verify that Element's positioned is '(\\d+),(\\d+)' again$")
	public void verifyThatElementSPositionedIsAgain(int arg0, int arg1) throws Throwable {
		GridElement ge = diagramLoaded.getDrawPanel().getGridElements().get(0);
		assertThat(ge.getRectangle().getX() == arg0);
		assertThat(ge.getRectangle().getY() == arg1);
	}
}
