package com.dsstudio.crawler;

public class CrawlConfig {
	
	private int threadMonitoringDelaySeconds = 10;
	
	private boolean shutdownOnEmptyQueue = true;
	
	private int threadShutdownDelaySeconds = 10;

	private int cleanupDelaySeconds = 10;

	private int maxPagesToFetch = -1;
	
	public int getThreadMonitoringDelaySeconds() {
		return threadMonitoringDelaySeconds;
	}

	public void setThreadMonitoringDelaySeconds(int threadMonitoringDelaySeconds) {
		this.threadMonitoringDelaySeconds = threadMonitoringDelaySeconds;
	}

	public boolean isShutdownOnEmptyQueue() {
		return shutdownOnEmptyQueue;
	}

	public void setShutdownOnEmptyQueue(boolean shutdownOnEmptyQueue) {
		this.shutdownOnEmptyQueue = shutdownOnEmptyQueue;
	}

	public int getThreadShutdownDelaySeconds() {
		return threadShutdownDelaySeconds;
	}

	public void setThreadShutdownDelaySeconds(int threadShutdownDelaySeconds) {
		this.threadShutdownDelaySeconds = threadShutdownDelaySeconds;
	}

	public int getCleanupDelaySeconds() {
		return cleanupDelaySeconds;
	}

	public void setCleanupDelaySeconds(int cleanupDelaySeconds) {
		this.cleanupDelaySeconds = cleanupDelaySeconds;
	}

	public int getMaxPagesToFetch() {
		// TODO Auto-generated method stub
		return maxPagesToFetch;
	}
	
	
}
