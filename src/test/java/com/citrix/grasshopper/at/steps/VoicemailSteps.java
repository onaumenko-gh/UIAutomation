package com.citrix.grasshopper.at.steps;

import cucumber.api.java.en.Then;
import helper.NavigationTab;
import helper.SharedData;
import org.junit.Assert;

public class VoicemailSteps extends BaseSteps{

    @Then("^unread counter for Voicemails is updated$")
    public void unreadCounterForVoicemailsIsUpdated() throws Throwable {
        int unreadCounterBefore = SharedData.unreadCounterMap.get(NavigationTab.INBOX);

        Thread.sleep(30000);

        app.inboxPage().updateUnreadCounters();

        Assert.assertTrue("Verify unread counter was updated", unreadCounterBefore != SharedData.unreadCounterMap.get(NavigationTab.INBOX));
    }
}
