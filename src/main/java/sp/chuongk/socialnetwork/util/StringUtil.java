package sp.chuongk.socialnetwork.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtil {
	private static final String regex = "^(.+)@(.+)$";
	 
	private static final Pattern pattern = Pattern.compile(regex);
	
	private StringUtil() {
	}
	
	public static Set<String> findMentionEmails(String text){
		String[] words = text.split("\\s+");
		Set<String> mentionList = new HashSet<>();
		for (String word : words) {
			if (isEmailFormat(word)) {
				mentionList.add(word);
			}
		}
		return mentionList;
	}
	
	public static boolean isEmailFormat(String email) {
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
}
