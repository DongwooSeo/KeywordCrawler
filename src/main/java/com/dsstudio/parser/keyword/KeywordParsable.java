package com.dsstudio.parser.keyword;

import com.dsstudio.hibernate.model.KeywordLinkQueue;

public interface KeywordParsable {
	void parse(KeywordLinkQueue keywordLinkQueue);
}
