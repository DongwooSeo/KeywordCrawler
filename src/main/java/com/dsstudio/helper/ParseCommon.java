package com.dsstudio.helper;

import java.util.List;

import com.dsstudio.hibernate.model.Parser;

public class ParseCommon {
	public static String getTarget(String content, String cut, String start, String end) {
		if(!cut.isEmpty()){
			int startIndex = content.indexOf(cut);
			content = content.substring(startIndex);
		}
		return getTarget(content,start,end);
	}
	
	public static String getTarget(String content, String start, String end){
		//System.out.println(content.contains(start));
		if(!content.contains(start) || !content.contains(end)){
			return "";
		}
		int startIndex = content.indexOf(start) + start.length();
		content = content.substring(startIndex);
		String target = content.substring(0, content.indexOf(end));
		content = content.substring(content.indexOf(end) + end.length());
		return target.trim();
		
	}
}
