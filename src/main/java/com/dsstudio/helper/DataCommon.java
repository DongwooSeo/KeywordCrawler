package com.dsstudio.helper;

import java.util.List;

import com.dsstudio.hibernate.model.Parser;

public class DataCommon {
	public static String getValueBy(String key, List<Parser> parsers) {
		for (Parser parser : parsers) {
			if (parser.getKey().equals(key)) {
				return parser.getValue();
			}
		}
		return null;
	}

	public static boolean containValue(String target, String value) {
		if (target.contains(",")) {
			for (String str : target.split(",")) {
				if (value.contains(str))
					return true;
			}
		} else {
			if (value.contains(target))
				return true;
		}
		return false;
	}
}
