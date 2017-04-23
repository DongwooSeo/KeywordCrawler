package com.dsstudio.updater;

import java.util.List;

public class Updater implements Runnable {

	private List<KeywordUpdatable> keywordUpdaters;

	public Updater(List<KeywordUpdatable> keywordUpdaters) {
		this.keywordUpdaters = keywordUpdaters;
	}

	public void run() {
		// TODO Auto-generated method stub
		// CommonKeywordUpdater commonKeywordUpdater = null;
		while (true) {
			System.out.println(Thread.currentThread() + " 작동중");
			for (KeywordUpdatable keywordUpdater : keywordUpdaters) {
				System.out.println("키워드 업데이트 시작!");
				keywordUpdater.updateKeyword();
			}
		}
	}
}
