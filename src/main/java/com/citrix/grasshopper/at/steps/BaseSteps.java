package com.citrix.grasshopper.at.steps;

import PageObjects.GrasshopperApp;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import helper.NavigationTab;
import org.junit.Assert;

public class BaseSteps {

    public static final GrasshopperApp app = GrasshopperApp.getInstance();

//    protected static WebDriver driver;
//    public static AppiumDriver<MobileElement> driver = app.setup();
//    public BaseSteps(AndroidDriver driver){
//        this.driver = driver;
//    }
//
//        @Before
//    public void beforeScenario() {
//              app = GrasshopperApp.getInstance();
//            driver = GrasshopperApp.setup();
//        System.out.println("this will run before the actual scenario");
//    }

}
