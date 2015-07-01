package com.saic.rtws.commons.util.index;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.saic.rtws.commons.util.regex.PatternUtils;

/**
 * Index implementing range and wildcard searching for string values.
 * 
 * This class optimizes wildcard searching by computing the prefix (up to the first occurrence of a wildcard)
 * of a search expression and using that value to narrow which index entries need to evaluated against the
 * search expression. In order to accommodate search expression that begin with a leading wildcard, the
 * index is stored twice; once as a normal TreeMap and again, but with the key values lexically reversed.
 * This allows expression with a leading wildcard to be seached using their suffix instead of a preffix. In
 * the event that a search expression has both a leading and trailing wildcard, no optimization is possible.
 */
public class TextIndex<E> implements Index<String, E>, RangeSearchable<String, E>, WildcardSearchable<E> {
	
	/** The index. */
	private NavigableMap<String, Collection<E>> forwardIndex = new TreeMap<String, Collection<E>>();
	
	/** A copy of the index such that all the keys are reversed. */
	private NavigableMap<String, Collection<E>> reverseIndex = new TreeMap<String, Collection<E>>();
	
	/**
	 * Constructor.
	 */
	public TextIndex() {
		super();
	}
	
	/**
	 * Finds all entries associated with the given value.
	 */
	public Collection<E> find(String value) {
		if(forwardIndex.containsKey(value)) {
			return forwardIndex.get(value);
		} else {
			return Collections.emptyList();
		}
	}
	
	/**
	 * Finds all entries associated with values that are less than or equal to the given value.
	 */
	public Collection<E> findLessThan(String value, boolean inclusive) {
		LinkedList<E> buffer = new LinkedList<E>();
		for(Collection<E> match : forwardIndex.headMap(value, inclusive).values()) {
			buffer.addAll(match);
		}
		return buffer;
	}
	
	/**
	 * Finds all entries associated with values that are greater than or equal to the given value.
	 */
	public Collection<E> findGreaterThan(String value, boolean inclusive) {
		LinkedList<E> buffer = new LinkedList<E>();
		for(Collection<E> match : forwardIndex.tailMap(value, inclusive).values()) {
			buffer.addAll(match);
		}
		return buffer;
	}
	
	/**
	 * Finds all entries associated with values that are within the given range.
	 */
	public Collection<E> findBetween(String from, String to, boolean inclusive) {
		LinkedList<E> buffer = new LinkedList<E>();
		for(Collection<E> match : forwardIndex.subMap(from, inclusive, to, inclusive).values()) {
			buffer.addAll(match);
		}
		return buffer;
	}
	
	/**
	 * Returns all the entries in the index.
	 */
	public Collection<E> findAll() {
		LinkedList<E> buffer = new LinkedList<E>();
		for(Collection<E> list : forwardIndex.values()) {
			buffer.addAll(list);
		}
		return buffer;
	}
	
	/**
	 * Finds all entries associated with values that match the given wildcard expression.
	 */
	public Collection<E> findLike(String pattern) {
		
		if(pattern == null) {
			return Collections.emptySet();
		} else if(pattern.equals("*")) {
			return findAll();
		}
		
		String forward = pattern;
		String reverse = reverse(pattern);
		
		String prefix = prefix(forward);
		String suffix = prefix(reverse);
		
		if(prefix.length() == pattern.length()) {
			return find(pattern);
		} else if(prefix.length() > suffix.length()) {
			return findLike(forwardIndex, forward, prefix);
		} else {
			return findLike(reverseIndex, reverse, suffix);
		}
		
	}
	
	/**
	 * Searches the given map for key's that start with the given prefix, and returns entries for
	 * those who's keys match the given pattern.
	 *  
	 * @param index The map to search through.
	 * @param pattern The pattern that determines which keys to find.
	 * @param prefix The non-wildcard prefix of the pattern. 
	 */
	private Collection<E> findLike(NavigableMap<String, Collection<E>> index, String pattern, String prefix) {
		
		String limit = increment(prefix);
		NavigableMap<String, Collection<E>> range = index.tailMap(prefix, true);
		range = (limit == null) ? range :range.headMap(limit, false);
		
		Matcher matcher = Pattern.compile(PatternUtils.convertUnixWildcardToRegex(pattern)).matcher("");
		LinkedList<E> buffer = new LinkedList<E>();
		for(String value : range.keySet()) {
			if(matcher.reset(value).matches()) {
				buffer.addAll(range.get(value));
			}
		}
		
		return buffer;
		
	}
	
	/**
	 * Associates the given entry with the given value.
	 */
	public void associate(String value, E entry) {
		
		if(forwardIndex.containsKey(value)) {
			forwardIndex.get(value).add(entry);
		} else {
			forwardIndex.put(value, new LinkedList<E>(Collections.singleton(entry)));
		}
		
		value = reverse(value);
		if(reverseIndex.containsKey(value)) {
			reverseIndex.get(value).add(entry);
		} else {
			reverseIndex.put(value, new LinkedList<E>(Collections.singleton(entry)));
		}
		
	}

	/**
	 * Reverses the given string.
	 */
	private static String reverse(String value) {
		return new StringBuilder(value).reverse().toString();
	}
	
	/**
	 * Returns the leading portion of the given string up to the first wildcard character.
	 */
	private static String prefix(String value) {
		for(int i = 0; i < value.length(); i++) {
			char character = value.charAt(i);
			if(character == '*' || character == '?') {
				return value.substring(0, i);
			}
		}
		return "";
	}
	
	/**
	 * Sets the last character in the given string to the next alphabetical value. If that
	 * character is already the last character in the unicode character space, the truncate
	 * the last character off and repeat for the shorted string.
	 * 
	 * This logically produces the lowest lexical value that is greater than the given value
	 * and all string that are prefixed by that value. 
	 */
	private static String increment(String value) {
		if(value == null) {
			return null;
		} else if(value.length() == 0) {
			return null;
		} else {
			char tail = value.charAt(value.length() - 1);
			if(++tail == '\0') {
				return increment(value.substring(0, value.length() - 1));
			} else {
				return value.substring(0, value.length() - 1) + tail;
			}
		}
	}
	
}
