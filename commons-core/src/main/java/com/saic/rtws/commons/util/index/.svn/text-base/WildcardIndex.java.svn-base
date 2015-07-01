package com.saic.rtws.commons.util.index;

import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Pattern;

import com.saic.rtws.commons.util.regex.PatternUtils;

/**
 * Index used to associate objects with wildcard expressions. Searching this index returns
 * entries who's indexed value is a wildcard match against a given literal value. For example,
 * indexing the value "user@*.com", allows you to later search for literal values such as
 * "user@gmail.com" and find whatever entry was associated with the pattern "user@*.com".
 * 
 * Note that searching a wildcard index is not the same wildcard searching an index. That is
 * to say that you cannot search for "user@*.com" and find entries like "user@gmail.com" and
 * "user@hotmail.com". The search string must be fixed literal.
 * 
 * This index offers two modes of operation, precise (default) and imprecise. Precise mode
 * provides strict matching against searched values, but has a higher performance cost.
 * Imprecise mode performs only a partial match against searched values, which can return
 * "spurious" hits. However, if you were going to "verify" the hit post search anyway, then
 * using imprecise mode improves performance.  
 */
public class WildcardIndex<E> implements Index<String, E>, Searchable<String, E> {

	/** The number of hash bins allocated for each level in the search tree. */
	private static final int ARITY = 128;
	
	/** The index of the hash bin that is dedicated to ONLY the wildcard character. */
	private static final int WILDCARD_INDEX = 0;
	
	/** The root of the tree. */
	private Sequence<E> root = new Sequence<E>();
	
	/** Flag indicating whether partial match can be returned. */
	private final boolean precise;
	
	/**
	 * Constructor.
	 */
	public WildcardIndex() {
		this(true);
	}
	
	/**
	 * Constructor.
	 */
	public WildcardIndex(boolean precise) {
		super();
		this.precise = precise;
	}
	
	/**
	 * Associates the given wildcard expression with the given entry.
	 */
	public void associate(String value, E entry) {
		
		// Proxy a meaningful value for null.
		value = nvl(value);
		
		Sequence<E> current = root;
		
		// Get the prefix of the wildcard pattern being indexed (the sequence of leading
		// characters up to the first occurrence of a wildcard character). 
		char character = '0';
		for(int i = 0; !isWildcard(character); i++) {
			// If we reach the end of the value without finding a wildcard, just pretend
			// we found one, otherwise the value won't get indexed at all.
			character = (i == value.length()) ? '*' : value.charAt(i);
			// Append each leading character into the index tree.
			current = current.element(character);
		}
		
		// Get the suffix of the wildcard pattern being indexed (the sequence of trailing
		// characters down to the last occurrence of a wildcard character). The suffix is
		// stored backwards to simplify later searching of the index; because a search
		// string could match suffixes of any length, it is not possible to know from what
		// position within the search string to begin making comparisons.
		character = '0';
		for(int i = value.length() - 1; !isWildcard(character); i--) {
			// If we reach the beginning of the value without finding a wildcard, just
			// pretend we found one, otherwise the value won't get indexed at all.
			character = (i == -1) ? '*' : value.charAt(i);
			// Append each trailing character into the index tree.
			current = current.element(character);
		}
		
		// Add the entry to the trailing wildcard node.
		current.addEntry(value, entry);
		
	}

	/**
	 * Finds any entries who's associated wildcard expression matches the given literal string.
	 * 
	 * In imprecise mode, results may also include spurious hits such that the leading and
	 * trailing characters of the pattern match, but characters in the middle of the pattern
	 * do not. For example, the pattern "user*.zone5@*.com" will match when searching both
	 * "user21.zone5@hotmail.com" (as expected) but also "user21.bogus.hot.mailcom". It can
	 * also return drastically unusual results that are a side effect of the internal hashing
	 * algorithm used to organize the index. You should only use imprecise mode if you have
	 * some way to filter the spurious hits post search.
	 */
	public Collection<E> find(String value) {
		LinkedList<E> buffer = new LinkedList<E>();
		findLeadingMatch(nvl(value), root, buffer);
		return buffer;
	}

	/**
	 * Scan the index tree for all sequences that prefix the given value.
	 * 
	 * @param value The value to search for.
	 * @param prefix The root of the tree.
	 * @param buffer A buffer to populate with matched entries.
	 */
	private void findLeadingMatch(String value, Sequence<E> prefix, Collection<E> buffer) {
		// Check for entries that match a zero width prefix (i.e. start with a leading wildcard).
		if(prefix.isTerminatedByThisSequence()) {
			findTrailingMatch(value, prefix.terminatedByThisSequence(), buffer);
		}
		// Iterate over each successive leading character.
		for(int i = 0; i < value.length(); i++) {
			char character = value.charAt(i);
			// Check whether adding another character to the current prefix would yield any potential matches.
			if(prefix.hasSucceedingCharacter(character)) {
				// Continue checking against longer prefixes.
				prefix = prefix.withSucceedingCharacter(character);
				// If the sequence checked thus far is a full match to an index prefix...
				if(prefix.isTerminatedByThisSequence()) {
					// Check whether the potential matches also match the suffix.
					findTrailingMatch(value, prefix.terminatedByThisSequence(), buffer);
				}
			} else {
				// If there are no potential matches with a longer prefix, stop looking. 
				break;
			}
		}
	}
	
	/**
	 * Scan index tree (from the given position) for all sequences that suffix the given value.
	 * 
	 * @param value The value to search for.
	 * @param suffix The wildcard node representing the transition point between the prefix, and the suffix.
	 * @param buffer A buffer to populate with matched entries.
	 */
	private void findTrailingMatch(String value, Sequence<E> suffix, Collection<E> buffer) {
		// Check for entries that match a zero width suffix (i.e. end with a trailing wildcard). 
		if(suffix.isTerminatedByThisSequence()) {
			collectEntries(value, suffix.terminatedByThisSequence(), buffer);
		}
		// Iterate over each successive trailing character.
		for(int i = value.length() - 1; i >= 0; i--) {
			char character = value.charAt(i);
			// Check whether adding another character to the current suffix would yield any potential matches.
			if(suffix.hasSucceedingCharacter(character)) {
				// Continue checking against longer suffixes.
				suffix = suffix.withSucceedingCharacter(character);
				// If the sequence checked thus far is a full match to an index suffix...
				if(suffix.isTerminatedByThisSequence()) {
					// Add the matching entries to the buffer.
					collectEntries(value, suffix.terminatedByThisSequence(), buffer);
				}
			} else {
				// If there are no potential matches with a longer suffixes, stop looking. 
				break;
			}
		}
	}
	
	/**
	 * Extract the entry object from the given sequence node and adds any that pass verification
	 * to the given buffer.
	 *  
	 * @param value The value being searched.
	 * @param match The sequence representing the "potentially" matching prefix/suffix.
	 * @param buffer The buffer to which actual matches should be appended.
	 */
	private void collectEntries(String value, Sequence<E> match, Collection<E> buffer) {
		for(Entry<E> entry : match.getEntries()) {
			if(!precise || entry.matches(value)) {
				buffer.add(entry.getEntry());
			}
		}
	}
	
	/**
	 * Determines whether the given character is a valid wildcard (either '*' or '?').
	 */
	protected static boolean isWildcard(char value) {
		return value == '?' || value == '*';
	}
	
	/**
	 * Computes a hash index for the given character. This hash index is used to identify
	 * which "bin" in an array is associated with a given character. The hashing algorithm
	 * insures that one bin is reserved for wildcard characters (all wildcard characters
	 * hash to the same bin, and all other characters has to other bins). 
	 */
	protected static int hash(char value) {
		if(isWildcard(value)) {
			return WILDCARD_INDEX;
		} else {
			return 1 + (value % (ARITY - 1));
		}
	}
	
	protected static String nvl(String value) {
		return (value == null) ? "" : value;
	}
	
	/**
	 * Inner class representing a node in a polyary tree.
	 * 
	 * The indexes built here are a tree such that each element in the tree is a character
	 * from either a prefix or suffix of an indexed string. To find an entry for a given value,
	 * one starts at the root and, with each successive character in the value, bifurcates down
	 * the next path in the tree (each child path represent a different next character in the
	 * sequence).
	 * 
	 * Because the intent is to index "wildcard" expressions, one cannot index the "wild"
	 * portions of a string. Instead, a partial index is generated using only the prefix
	 * up to the leading wildcard, and the suffix back to the last trailing wildcard. (Both
	 * the prefix and suffix are index to insure that values that may start immediately with
	 * a wildcard do not defeat the index, limiting "worst case" values to only those that
	 * both start and end with a wildcard; which are hopefully few).
	 * 
	 * To illustrate... Indexing the expression "john*@mail.*.com" yields the following tree.
	 * Notice that the ".com" suffix is indexed backwards (to simplify searching), and the
	 * "@mail." portion is lift out altogether. The star that terminates the prefix is
	 * retained as part of the index as a flag to indicate that the following characters are
	 * part of the suffix rather than a continuation of the prefix. And the last star is
	 * retained to indicate the end of a suffix (so that partial matches are not returned).  
	 * <pre><code>
	 * ->j->o->h->n->*->m->o->c->.->*
	 * </code></pre>
	 * 
	 * If we add another value "josh*@mail.*.com", the tree will then look as follows.
	 * <pre><code>
	 *        /->h->n->*->m->o->c->.->*
	 * ->j->o-
	 *        \->s->h->*->m->o->c->.->*
	 * </code></pre>
	 *
	 * And if we add a value with a shared suffix, such as "john*@gmail.com", we get the following.
	 * <pre><code>
	 *                               /->l->i->a->m->g->@->*
	 *        /->h->n->*->m->o->c->.-
	 * ->j->o-                       \->*
	 *        \->s->h->*->m->o->c->.->*
	 * </code></pre>
	 */
	private static class Sequence<T> {

		/** Hash table representing all possible continuations of the sequence up to this node. */
		@SuppressWarnings("unchecked")
		private Sequence<T>[] next = new Sequence[ARITY];
		
		/** List of entries associated with the sequence terminated by this node. */
		private LinkedList<Entry<T>> entries = new LinkedList<Entry<T>>();
		
		/**
		 * Constructor.
		 */
		public Sequence() {
			super();
		}
		
		/**
		 * Returns the child element for the given character. In other words, the
		 * node that would be obtained by adding the given character to the sequence
		 * already represent by this node. If the specified child does not exist,
		 * one is created.
		 */
		public Sequence<T> element(char character) {
			int index = hash(character);
			if(next[index] == null) {
				next[index] = new Sequence<T>();
			}
			return next[index];
		}
		
		/**
		 * Adds the given object to the list of entries associated with the sequence
		 * of characters terminated by this node.
		 */
		public void addEntry(String pattern, T object) {
			entries.add(new Entry<T>(pattern, object));
		}
		
		/**
		 * Determines whether a child entry exist for the sequence generated by adding the
		 * given character to the sequence represented by this node.
		 */
		public boolean hasSucceedingCharacter(char character) {
			return next[hash(character)] != null;
		}
		
		/**
		 * Returns the child entry for the sequence generated by adding the given character
		 * to the sequence represented by this node.
		 */
		public Sequence<T> withSucceedingCharacter(char character) {
			return next[hash(character)];
		}
		
		/**
		 * Determines whether this node represents the end of an indexed sequence.
		 */
		public boolean isTerminatedByThisSequence() {
			return next[WILDCARD_INDEX] != null;
		}
		
		/**
		 * Returns the place holder node containing the entry objects associated with
		 * the sequence terminated at this node.
		 */
		public Sequence<T> terminatedByThisSequence() {
			return next[WILDCARD_INDEX];
		}
		
		/**
		 * Returns the entries associated with the sequence terminated by this node.
		 */
		public Collection<Entry<T>> getEntries() {
			return entries;
		}
		
	}

	/**
	 * Inner class used to associate an index value with it's entry.
	 */
	private static class Entry<T> {
		private Pattern pattern;
		private T entry;
		public Entry(String pattern, T entry) {
			this.pattern = Pattern.compile(PatternUtils.convertUnixWildcardToRegex(pattern));
			this.entry = entry;
		}
		public boolean matches(String literal) {
			return pattern.matcher(literal).matches();
		}
		public T getEntry() {
			return entry;
		}
	}
	
}
