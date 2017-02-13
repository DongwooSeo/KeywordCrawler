package com.dsstudio.parser;

import com.dsstudio.hibernate.model.KeywordLinkQueue;

public class BasicKeywordParser extends CommonKeywordParser{
	Object lock = new Object();
	
	@Override
	protected int saveKeywordLinkQueue(String link) {
		// TODO Auto-generated method stub
		return 0;
	}

	protected KeywordLinkQueue DequeueLink(){
		KeywordLinkQueue link = null;
		
		return null;
		
	}
	@Override
	protected void parseKeyword() {
		// TODO Auto-generated method stub
		synchronized(lock){
			
		}
	}
	
	private boolean isCrawl(KeywordLinkQueue keywordLinkQueue){
		
		return false;
	}

}
