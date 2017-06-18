package com.baselet.our.cucumber.step;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URL;

import org.slf4j.LoggerFactory;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.diagram.DiagramHandler;
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
		ufxTempFile = getFile("emptyWorkspace.ufx");
		diagramToSave = new DiagramHandler(ufxTempFile);

		Class classObject = new Class();
		classObject.setRectangle(new Rectangle(arg0, arg1, 1000, 1000));
		diagramToSave.getDrawPanel().addElement(classObject);
	}

	private File getFile(String name) {
		ClassLoader loader = getClass().getClassLoader();
		URL url = loader.getResource(name);
		return new File(url.getFile());
	}


	@When("^the Diagram has been saved$")
	public void theDiagramHasBeenSaved() throws Throwable {
		LoggerFactory.getLogger(getClass()).info("Saving"); //Komt nergens voor in de console output
		diagramToSave.doSaveAs("ufx");
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
