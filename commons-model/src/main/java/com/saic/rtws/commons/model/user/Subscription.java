package com.saic.rtws.commons.model.user;

public class Subscription
{
	private Long subscriberId;
	private String username;
	private Long filterId;
	private Boolean rtwsAppSubscriber;
	private Boolean emailSubscriber;
	private Long color;
	
	public Subscription()
	{
		super();
	}
	
	public Long getSubscriberId()
	{
		return subscriberId;
	}
	public void setSubscriberId(Long subscriberId)
	{
		this.subscriberId = subscriberId;
	}
	
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public Long getFilterId()
	{
		return filterId;
	}
	public void setFilterId(Long filterId)
	{
		this.filterId = filterId;
	}
	
	public Boolean isRtwsAppSubscriber()
	{
		return rtwsAppSubscriber;
	}
	public void setRtwsAppSubscriber(Boolean rtwsAppSubscriber)
	{
		this.rtwsAppSubscriber = rtwsAppSubscriber;
	}
	
	public Boolean isEmailSubscriber()
	{
		return emailSubscriber;
	}
	public void setEmailSubscriber(Boolean emailSubscriber)
	{
		this.emailSubscriber = emailSubscriber;
	}
	
	public Long getColor()
	{
		return color;
	}
	public void setColor(Long color)
	{
		this.color = color;
	}
}
