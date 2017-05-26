package com.dsstudio.parsers.stock;

import java.util.List;

import org.jsoup.nodes.Document;

import com.dsstudio.hibernate.model.Agent;
import com.dsstudio.hibernate.model.Parser;

public interface StockParser {
	void parseStock(Document document, int stockKeywordId, Agent agent, List<Parser> parsers);
}
