package com.citrix.grasshopper.at.steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import helper.*;
import helper.Number;
import org.junit.Assert;
import org.openqa.selenium.By;

public class TextsSteps extends BaseSteps {

    @Then("^each item from Texts dropdown can be selected$")
    public void eachItemFromTextsDropdownCanBeSelected() throws Throwable {

        for (Number value : DefaultUser.numbers) {
            if (value.isSmsEnabled) {
                app.textsPage().setDropdownValue(value.number);

                Thread.sleep(Constants.Timeouts.defaultActionTimeout);
                Assert.assertTrue(app.recentPage().getSelectedDropdownValue().equalsIgnoreCase(value.number));
            }
        }
    }

    @When("^user opens (.*) tab on Texts$")
    public void userOpensDoneTabOnTexts(enums.TextTabs tab) throws Throwable {
        app.textsPage().openTab(tab);
    }

    @And("^checks there are messages marked as Done$")
    public void checksThereAreMessagesMarkedAsDone() throws Throwable {
        app.textsPage().refreshHistory();

        if (SharedData.textsMap.size() == 0) {
            app.textsPage().openTab(enums.TextTabs.ALL);
            app.textsPage().refreshHistory();
            app.textsPage().markMessageAsDone(SharedData.textsMap.get(0).mobileElement);
            app.textsPage().openTab(enums.TextTabs.DONE);
            app.textsPage().refreshHistory();
        }
    }

    @And("^sends new text message from (.*) to (.*)$")
    public void sendsNewTextMessageFromNumberFromToNumberTo(String numberFrom, String numberTo) throws Throwable {
        app.textsPage().refreshHistory();
        Thread.sleep(Constants.Timeouts.defaultActionTimeout);
        app.textsPage().updateUnreadDropdownCounters();

        TextsEntry entry = app.textsPage().findEntryByContactName(numberFrom);

        if (entry != null) {
            if (entry.isUnread) {
                // marking message as read.
                SharedData.textsMap.get(SharedData.textsMap.indexOf(entry)).mobileElement.click();
                app.newConversationScreen().clickOnBackButton();
                app.textsPage().refreshHistory();
                app.textsPage().updateUnreadCounters();
                app.textsPage().updateUnreadDropdownCounters();
            }
        }

        app.textsPage().setDropdownValue(numberFrom);
        app.textsPage().startNewConversation();
        app.newConversationScreen().enterContactNumber(numberTo);
        app.newConversationScreen().startConversation();
        app.newConversationScreen().sendNewRandomMessageWithTimestamp();
        app.newConversationScreen().clickOnBackButton();
    }

    @Then("^unread counter for Texts is updated$")
    public void unreadCounterForTextsIsUpdated() throws Throwable {
        Integer currentCounter = SharedData.unreadCounterMap.get(NavigationTab.TEXTS);

        Thread.sleep(Constants.Timeouts.longActionTimeout);

        app.textsPage().updateUnreadCounters();

        Assert.assertTrue("Verify bottom menu unread counter was updated", currentCounter != SharedData.unreadCounterMap.get(NavigationTab.TEXTS));
    }

    @Then("^unread counter for (.*) updates in the dropdown menu$")
    public void unreadCounterForNumberToUpdatesInTheDropdownMenu(String number) throws Throwable {
        Integer currentCounter = SharedData.textDropdownUnreadCounter.get(number);

        Thread.sleep(Constants.Timeouts.longActionTimeout);
        app.textsPage().updateUnreadDropdownCounters();

        Assert.assertTrue("Verify bottom menu unread counter was incremented", currentCounter != SharedData.textDropdownUnreadCounter.get(number));
    }

    public static void deleteContactMotoOnTextsPage(TextsEntry newEntry) {
        app.textsPage().editContactForEntry(newEntry);
        app.addNewContactMoto().deleteContact();
        // contact becomes selected here
        app.textsPage().navigateBack();
    }

    @And("^creates contact with (.*) for the dialog$")
    public void createsContactWithContactNameForTheNumberFromDialog(String contactName) throws Throwable {
        TextsEntry newEntry;
        String contactNumber;
        app.textsPage().refreshHistory();
        newEntry = app.textsPage().findEntryByContactName(contactName);

        if (newEntry != null) {
            // deleting the contact if it exists for the number.
            deleteContactMotoOnTextsPage(newEntry);
            //newEntry = app.textsPage().findEntryByContactName(contactName);
        }
        app.textsPage().refreshHistory();
        if (SharedData.textsMap.size() != 0) {
            for (TextsEntry text : SharedData.textsMap) {
                newEntry = text;
                break;
            }
        } else {
            // that means that we need to send new message to our number from the number specified in parameter.
            sendNewMessageToOurNumber();
            newEntry = app.textsPage().findEntryByContactName(SharedData.textsMap.get(0).contact);
        }
        contactNumber = newEntry.contact;
        app.textsPage().createContactForEntry(newEntry, contactName);
        app.addNewContactMoto().enterContactName(contactName);
        app.addNewContactMoto().saveContact();
        // contact becomes selected here
        app.textsPage().navigateBack();
        Thread.sleep(Constants.Timeouts.defaultActionTimeout);
        app.textsPage().refreshHistory();
        newEntry = app.textsPage().findEntryByContactName(contactNumber);
        Assert.assertTrue("Verify there are no entries with the number instead of contact name", newEntry == null);
    }

    @Then("^all entries with the number are replaced with (.*) contact name$")
    public void allEntitiesWithNumberFromOnTextsScreenWillBeDisplayedWithTheContactName(String contactName) throws Throwable {
        TextsEntry newEntry;
        newEntry = app.textsPage().findEntryByContactName(contactName);
        Assert.assertTrue("Verify all entries with the number are replaced with contact name", newEntry != null);

        // Deleting contact here
        deleteContactMotoOnTextsPage(newEntry);
    }

    @And("^marks unread message from (.*) as Done$")
    public void marksUnreadMessageFromNumberFromAsDone(String number) throws Throwable {
        TextsEntry newEntry;

        app.textsPage().setDropdownValue(DefaultUser.numbers[0].number);
        app.textsPage().refreshHistory();
        newEntry = app.textsPage().findEntryByContactName(number);

        if (newEntry == null || !newEntry.isUnread) {
            // that means that we need to send new message to our number from the number specified in parameter.
            app.textsPage().setDropdownValue(number);

            app.textsPage().startNewConversation();
            app.newConversationScreen().enterContactNumber(DefaultUser.numbers[0].number);
            app.newConversationScreen().startConversation();
            app.newConversationScreen().sendNewRandomMessageWithTimestamp();
            app.newConversationScreen().clickOnBackButton();

            app.textsPage().setDropdownValue(DefaultUser.numbers[0].number);
        }

        app.textsPage().refreshHistory();
        newEntry = app.textsPage().findEntryByContactName(number);

        app.textsPage().markMessageAsDone(newEntry.mobileElement);
    }

    @And("^message from (.*) is moved to Done tab$")
    public void messageFromNumberFromIsMovedToDoneTab(String number) throws Throwable {
        TextsEntry newEntry;

        newEntry = app.textsPage().findEntryByContactName(number);

        Assert.assertTrue("Verify that entry was removed from All tab", newEntry == null);

        app.textsPage().openTab(enums.TextTabs.DONE);
        app.textsPage().refreshHistory();
        newEntry = app.textsPage().findEntryByContactName(number);

        Assert.assertTrue("Verify that entry was moved to Done tab", newEntry != null);
    }

    @And("^message from (.*) is displayed without New icon$")
    public void messageFromNumberFromIsDisplayedWitoutNewIcon(String number) throws Throwable {
        TextsEntry newEntry;

        newEntry = app.textsPage().findEntryByContactName(number);

        Assert.assertFalse("Verify that entry is displayed without New icon", newEntry.isUnread);
    }

    @And("^sends new MMS message with newly taken image from (.*) to (.*)$")
    public void sendsNewMMSMessageWithNewlyTakenImageFromNumberFromToNumberTo(String fromNumber, String toNumber) throws Throwable {
        app.textsPage().setDropdownValue(fromNumber);
        app.textsPage().startNewConversation();
        app.newConversationScreen().enterContactNumber(toNumber);
        app.newConversationScreen().startConversation();
        app.newConversationScreen().sendNewImage();
        app.newConversationScreen().clickOnBackButton();

        Thread.sleep(Constants.Timeouts.longActionTimeout * 2);
    }

    @Then("^message from (.*) is displayed with New icon on (.*) tab$")
    public void messageFromNumberFromIsDisplayedWithNewIconOnNumberFromTab(String numberFrom, String numberTo) throws Throwable {
        app.textsPage().setDropdownValue(numberTo);
        Thread.sleep(Constants.Timeouts.longActionTimeout * 2);
        TextsEntry newEntry;
        app.textsPage().refreshHistory();
        newEntry = app.textsPage().findEntryByContactName(numberFrom);
        Assert.assertTrue("Verify that entry is displayed with New icon", newEntry.isUnread);
        newEntry.openDialog();
        app.newConversationScreen().getAllMessages();
        Assert.assertTrue("Verify MMS message was delivered", SharedData.messagesList.get(SharedData.messagesList.size() - 1).isMMS);
        app.newConversationScreen().deleteMessage(SharedData.messagesList.get(SharedData.messagesList.size() - 1));
    }

    @And("^sends new MMS message with existent image from (.*) to (.*)$")
    public void sendsNewMMSMessageWithExistentImageFromNumberFromToNumberTo(String fromNumber, String toNumber) throws Throwable {
        app.textsPage().setDropdownValue(fromNumber);
        app.textsPage().startNewConversation();
        app.newConversationScreen().enterContactNumber(toNumber);
        app.newConversationScreen().startConversation();
        app.newConversationScreen().sendRandomExistingImage();
        app.newConversationScreen().clickOnBackButton();
        Thread.sleep(Constants.Timeouts.longActionTimeout * 2);
    }

    @And("^sends new MMS message with existent video from (.*) to (.*)$")
    public void sendsNewMMSMessageWithExistentVideoFromNumberFromToNumberTo(String fromNumber, String toNumber) throws Throwable {
        app.textsPage().setDropdownValue(fromNumber);
        app.textsPage().startNewConversation();
        app.newConversationScreen().enterContactNumber(toNumber);
        app.newConversationScreen().startConversation();

    }

    public static void sendNewMessageToOurNumber() throws InterruptedException {
        app.textsPage().setDropdownValue(DefaultUser.numbers[0].number);
        Thread.sleep(Constants.Timeouts.defaultActionTimeout);
        app.textsPage().startNewConversation();
        app.newConversationScreen().enterContactNumber(DefaultUser.numbers[1].number);
        app.newConversationScreen().startConversation();
        app.newConversationScreen().sendNewRandomMessageWithTimestamp();
        app.newConversationScreen().clickOnBackButton();
        app.textsPage().setDropdownValue(DefaultUser.numbers[1].number);
        app.textsPage().refreshHistory();
    }

    public static void sendNewMessageToNumber(String fromNumber, String toNumber) {
        app.textsPage().setDropdownValue(fromNumber);
        app.textsPage().startNewConversation();
        app.newConversationScreen().enterContactNumber(toNumber);
        app.newConversationScreen().startConversation();
        app.newConversationScreen().sendNewRandomMessageWithTimestamp();
        app.newConversationScreen().clickOnBackButton();
        app.textsPage().setDropdownValue(toNumber);
        app.textsPage().refreshHistory();
    }

    public TextsEntry selectUnreadNewEntry() {
        TextsEntry newEntry = null;
        for (int index = 0; index < DefaultUser.numbers.length; index++) {
            app.textsPage().setDropdownValue(DefaultUser.numbers[index].number);
            app.textsPage().refreshHistory();
            if (SharedData.textsMap.size() != 0) {
                for (TextsEntry text : SharedData.textsMap) {
                    if (text.isUnread) {
                        newEntry = text;
                        break;
                    }
                }
            }
            if (newEntry != null) break;
        }
        return newEntry;
    }

    @And("^marks unread message as Done$")
    public void marksUnreadMessageAsDone() throws Throwable {
        TextsEntry newEntry = selectUnreadNewEntry();
        if (newEntry == null) {
            // that means that we need to send new message to our number from the number specified in parameter.
            sendNewMessageToOurNumber();
            newEntry = selectUnreadNewEntry();
        }

        app.textsPage().markMessageAsDone(newEntry.mobileElement);
        SharedData.messageMarkedAsDone = newEntry;
    }

    @And("^message marked as Done is moved to Done tab$")
    public void messageMarkedAsDoneIsMovedToDoneTab() throws Throwable {
        app.textsPage().refreshHistory();
        String numberOfDoneMessage = SharedData.messageMarkedAsDone.contact;
        Assert.assertTrue("Verify that entry was removed from All tab", !app.textsPage().isConversationPresent(numberOfDoneMessage));
        app.textsPage().openTab(enums.TextTabs.DONE);
        app.textsPage().refreshHistory();
        // movedMessage = app.textsPage().findEntryByContactName(numberOfDoneMessage);
        Assert.assertTrue("Verify that entry was moved to Done tab", app.textsPage().isConversationPresent(numberOfDoneMessage));
    }

    @And("^moved message is displayed without New icon$")
    public void movedMessageIsDisplayedWithoutNewIcon() throws Throwable {
        String numberOfDoneMessage;
        numberOfDoneMessage = SharedData.messageMarkedAsDone.contact;
        TextsEntry movedMessage;
        movedMessage = app.textsPage().findEntryByContactName(numberOfDoneMessage);
        Assert.assertFalse("Verify that entry is displayed without New icon", movedMessage.isUnread);


    }
}