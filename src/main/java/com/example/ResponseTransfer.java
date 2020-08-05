package com.example;

import java.util.Date;

public class ResponseTransfer {
	
	private String text;
	
	private Date date;

	public ResponseTransfer(String text, Date date) {
		this.text = text;
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
