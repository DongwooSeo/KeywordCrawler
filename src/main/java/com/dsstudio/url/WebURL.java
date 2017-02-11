package com.dsstudio.url;

import java.io.Serializable;

public class WebURL implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String url;
	
	private int docid;
	private int parentDocid;
	private String parentUrl;
	private short depth;
	private String domain;
	private String subDomain;
	private String path;
	private String anchor;
	private byte priority;
	private String tag;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getDocid() {
		return docid;
	}
	public void setDocid(int docid) {
		this.docid = docid;
	}
	public int getParentDocid() {
		return parentDocid;
	}
	public void setParentDocid(int parentDocid) {
		this.parentDocid = parentDocid;
	}
	public String getParentUrl() {
		return parentUrl;
	}
	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
	}
	public short getDepth() {
		return depth;
	}
	public void setDepth(short depth) {
		this.depth = depth;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getSubDomain() {
		return subDomain;
	}
	public void setSubDomain(String subDomain) {
		this.subDomain = subDomain;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getAnchor() {
		return anchor;
	}
	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}
	public byte getPriority() {
		return priority;
	}
	public void setPriority(byte priority) {
		this.priority = priority;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
