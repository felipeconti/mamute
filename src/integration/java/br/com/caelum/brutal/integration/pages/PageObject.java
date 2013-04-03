package br.com.caelum.brutal.integration.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class PageObject {

    protected final WebDriver driver;
    private static Logger LOG = Logger.getLogger(PageObject.class);

	public PageObject(WebDriver driver) {
		this.driver = driver;
	}
	
	protected WebElement byId(String id) {
		return driver.findElement(By.id(id));
	}

	protected WebElement byName(String name) {
		return driver.findElement(By.name(name));
	}

	protected List<WebElement> allByClassName(String name) {
		return driver.findElements(By.className(name));
	}
	
	protected WebElement byClassName(String name) {
        return driver.findElement(By.className(name));
	}
	
	protected WebElement byCSS(String selector) {
	    try {
	        return driver.findElement(By.cssSelector(selector));
	    } catch (NoSuchElementException e) {
	        throw new NoSuchElementException("could not find element for selector: " + selector);
        }
	}
	
	protected List<WebElement> allByCSS(String selector) {
	    return driver.findElements(By.cssSelector(selector));
	}
	
	protected void waitForElement(final By by, int time) {
	    try {
	        new WebDriverWait(driver, time).until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (TimeoutException e) {
            LOG.warn("waited for element " + by + " but it didn't show up");
        }
	}
	
	protected void waitForTextIn(final By by, String text, int time) {
	    new WebDriverWait(driver, time).until(ExpectedConditions.textToBePresentInElement(by, text));
	}

    public List<String> confirmationMessages() {
        return elementsTexts("confirmation");
    }
    
    private List<String> errorMessages() {
    	return elementsTexts("error");
    }

	public boolean containsErrorMessageLike(String message) {
		List<String> errorMessages = errorMessages();
		return containsMessageLike(message, errorMessages);
	}

	public boolean containsConfirmationMessageLike(String message) {
		List<String> confirmationMessages = confirmationMessages();
		return containsMessageLike(message, confirmationMessages);
	}

	
	private boolean containsMessageLike(String message, List<String> messages) {
		for (String errorMessage : messages) {
			if(errorMessage.contains(message))
				return true;
		}
		return false;
	}

	private List<String> elementsTexts(String className) {
		List<WebElement> elements = allByClassName(className);
		List<String> elementsTexts = new ArrayList<>();
		for (WebElement element : elements) {
    		elementsTexts.add(element.getText());
    	}
    	return elementsTexts;
	}
}
