package com.dsstudio.parser;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import com.dsstudio.hibernate.model.Agent;

public abstract class CommonKeywordParser {
	protected int agentId;
	
	protected void setAgentId(int agentId) {
		this.agentId = agentId;
	}
	protected int getAgentId() {
		return agentId;
	}
	
	protected boolean isCrawl(Agent agent){
		boolean isCrawl = false;
		
		if(agent!=null){
			if(agent.getDateFinished()==null){
				isCrawl = true;
			}else{
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.HOUR, -1 * agent.getHours());
				if(new Timestamp(cal.getTime().getTime()).after(agent.getDateFinished())){
					isCrawl = true;
				}
			}
		}
		return isCrawl;
	}
	protected abstract void parseKeyword();

}
