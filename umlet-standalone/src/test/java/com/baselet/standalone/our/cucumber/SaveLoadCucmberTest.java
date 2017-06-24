package com.baselet.standalone.our.cucumber;

import org.junit.Rule;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features")
public class SaveLoadCucmberTest {
}
