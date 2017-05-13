/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.suren.autotest.web.framework.selenium.locator;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.suren.autotest.web.framework.core.Locator;
import org.suren.autotest.web.framework.core.LocatorAware;

/**
 * 元素定位器的抽象父类，实现了部分通用方法
 * @author suren
 * @date 2016年7月25日 下午12:43:15
 */
public abstract class AbstractLocator<E> implements Locator, LocatorAware
{
	private String value;
	private long timeout;

	@Override
	public String getValue()
	{
		return value;
	}

	@Override
	public void setValue(String value)
	{
		this.value = value;
	}

	@Override
	public long getTimeout()
	{
		return timeout;
	}

	@Override
	public void setTimeout(long timeout)
	{
		this.timeout = timeout;
	}

	@Override
	public void setExtend(String extend){}
	
	@SuppressWarnings("unchecked")
	public E findElement(SearchContext driver)
	{
		By by = getBy();
		
		if(driver instanceof WebDriver)
		{
			elementWait((WebDriver) driver, getTimeout(), by);
		}
		
		return (E) driver.findElement(by);
	}
	
	@SuppressWarnings("unchecked")
	public List<E> findElements(SearchContext driver)
	{
		By by = getBy();
		
		if(driver instanceof WebDriver)
		{
			elementWait((WebDriver) driver, getTimeout(), by);
		}
		
		return (List<E>) driver.findElements(by);
	}
	
	protected abstract By getBy();

	/**
	 * 根据超时时间来等待元素
	 * @param locator
	 * @param by
	 */
	@SuppressWarnings("unchecked")
	protected boolean elementWait(WebDriver driver, long timeout, By by)
	{
		return eleWait(driver, timeout, ExpectedConditions.visibilityOfElementLocated(by));
	}

	@SuppressWarnings("unchecked")
	protected boolean iframeWait(WebDriver driver, long timeout, int index)
	{
		return eleWait(driver, timeout, ExpectedConditions.frameToBeAvailableAndSwitchToIt(index));
	}
	
	@SuppressWarnings("unchecked")
	protected boolean iframeWait(WebDriver driver, long timeout, String locator)
	{
		return eleWait(driver, timeout, ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
	}
	
	@SuppressWarnings("unchecked")
	protected boolean eleWait(WebDriver driver, long timeout, ExpectedCondition<? extends SearchContext> ...isTrueArray)
	{
		if(timeout > 0 && isTrueArray != null && isTrueArray.length > 0)
		{
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			for(ExpectedCondition<? extends SearchContext> isTrue : isTrueArray)
			{
				wait.until(isTrue);
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public String toString()
	{
		By by = getBy();
		String srtBy = (by == null ? "null locator" : by.toString());
		
		return srtBy;
	}
}
