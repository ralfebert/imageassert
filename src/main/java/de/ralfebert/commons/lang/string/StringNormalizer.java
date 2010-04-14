package de.ralfebert.commons.lang.string;

import java.util.regex.Pattern;

public class StringNormalizer {

	private final static Pattern PATTERN_BLANK_LINES = Pattern.compile("^\\s*$", Pattern.MULTILINE);
	private final static Pattern PATTERN_DUPLICATE_EMPTY_LINES = Pattern.compile("\\n{3,}", Pattern.MULTILINE);

	private String string;

	public StringNormalizer(String string) {
		this.string = string;
	}


	public void removeBlankLines() {
		string = PATTERN_BLANK_LINES.matcher(string).replaceAll("");
	}

	public void removeDuplicateEmptyLines() {
		string = PATTERN_DUPLICATE_EMPTY_LINES.matcher(string).replaceAll("\n\n");
	}

	public String toString() {
		return string;
	}

}
