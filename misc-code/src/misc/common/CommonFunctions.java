package misc.common;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class CommonFunctions {
	private static final Pattern NUMERIC_PATTERN = Pattern.compile("\\d*");
	private static final String DEFAULT_SEPARATOR = "|";

	private CommonFunctions() {
	}

	public static String notBlank(String value) {
		if (value == null || "".equals(value.trim()))
			return null;
		return value.trim();
	}

	public static boolean equalOrBothNull(Object value1, Object value2) {
		return (value1 == null ? value2 == null : value1.equals(value2));
	}

	public static Object findByName(Collection<?> objects, String name) {
		for (final Object o : objects) {
			try {

				final Method meth = o.getClass().getMethod("getName",
						(Class[]) null);
				if (name.equals(meth.invoke(o, (Object[]) null))) {
					return o;
				}
			} catch (final Exception e) {
				throw new RuntimeException(
						"Unable to locate getName method on object " + o, e);
			}
		}
		return null;
	}

	public static boolean isNullOrBlank(Object o) {
		if (o == null)
			return true;

		if (o instanceof String) {
			return "".equals(((String) o).trim());
		}

		return false;
	}

	public static boolean isNumeric(String s) {
		if (s == null)
			return false;

		return NUMERIC_PATTERN.matcher(s).matches();
	}

	public static List<?> toOrderedList(Collection<?> col,
			Comparator<Object> comparator) {
		if (col == null || col.isEmpty())
			return Collections.EMPTY_LIST;
		if (comparator == null)
			throw new NullPointerException(
					"toOrderedList requires a non-null comparator");

		final List<?> list = new ArrayList<Object>(col);
		Collections.sort(list, comparator);
		return list;
	}

	public static String makeEmptyStringNull(String s) {
		if (s == null)
			return null;

		final String trimmed = s.trim();
		return ("".equals(trimmed) ? null : trimmed);
	}

	public static String toPrintableMapKey(Map<?, ?> map) {
		final StringBuffer buffer = new StringBuffer();
		for (final Object next : map.keySet()) {
			buffer.append(next);
			buffer.append("|");
		}
		return buffer.toString();
	}

	public static String toPrintableMapValue(Map<?, ?> map) {
		final StringBuffer buffer = new StringBuffer();
		for (final Object next : map.values()) {
			buffer.append(next);
			buffer.append("|");
		}
		return buffer.toString();
	}

	public static String toPrintableListValue(List<?> list, String separator) {
		final StringBuffer buffer = new StringBuffer();
		for (final Object next : list) {
			buffer.append(next);
			buffer.append(separator);
		}
		return buffer.toString();
	}

	public static String toPrintableListValue(List<?> list) {
		return toPrintableListValue(list, DEFAULT_SEPARATOR);
	}

	public static <T> List<T> copyMapValueToList(HashMap<?, T> map) {
		return new ArrayList<T>(map.values());
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] copyIterToArray(Iterator<T> iter, int size,
			Class<T> klass) {
		final T[] objArray = (T[]) Array.newInstance(klass, size);
		for (int i = 0; i < size; i++) {
			objArray[i] = iter.next();
		}
		return objArray;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] copyIterToArrayReverse(Iterator<T> iter, int size,
			Class<T> clazz) {
		final T[] objArray = (T[]) Array.newInstance(clazz, size);
		for (int i = objArray.length - 1; i >= 0; i--) {
			objArray[i] = iter.next();
		}
		return objArray;
	}

	public static void removeNulls(HashMap<String, ?> map) {
		final Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			final String key = iter.next();
			final Object val = map.get(key);
			if (val == null)
				iter.remove();
		}

	}

	public static String escapeRegex(String aRegexFragment) {
		final StringBuffer result = new StringBuffer();

		final StringCharacterIterator iterator = new StringCharacterIterator(
				aRegexFragment);
		char character = iterator.current();
		while (character != CharacterIterator.DONE) {
			/*
			 * All literals need to have backslashes doubled.
			 */
			if (".\\?*+&:{}[]()^$".indexOf(character) >= 0) {
				result.append('\\');
			}

			result.append(character);

			character = iterator.next();
		}
		return result.toString();
	}

	public static boolean NullSafeEquals(Object s1, Object s2) {
		if (s1 == null && s2 != null)
			return false;
		if (s2 == null && s1 != null)
			return false;
		if (s2 == null && s1 == null)
			return true;
		return s1.equals(s2);
	}

	public static Set<Object> toSet(Object[] arr) {
		final Set<Object> st = new TreeSet<Object>();
		for (int i = 0; i < arr.length; i++) {
			st.add(arr[i]);
		}
		return st;
	}

	public static Long toLong(String longString) {
		return longString == null ? null : new Long(longString);
	}

	public static void close(Closeable closeable) {
		if (closeable == null)
			return; // nothing to close

		try {
			closeable.close();
		} catch (final IOException e) {
			throw new RuntimeException("Exception while closing", e);
		}
	}

	public static Class<?> getClassFromName(String className) {
		if (className == null)
			return null;
		Class<?> javaTypeClass = null;
		try {
			javaTypeClass = Class.forName(className);
			return javaTypeClass;
		} catch (final ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static Set<String> getStringAsCollection(String tokenizedString) {
		return Collections.unmodifiableSet(new HashSet<String>(Arrays
				.asList(tokenizedString.split(","))));
	}

	public static List<String> getStringAsList(String tokenizedString) {
		return Collections.unmodifiableList(Arrays.asList(tokenizedString
				.split(",")));
	}

	public static String getSingleQuotedString(String[] strings) {
		assert strings != null;
		final StringBuilder sb = new StringBuilder();

		for (int i = 0; i < strings.length; i++) {
			sb.append("'" + strings[i]
					+ ((i == strings.length - 1) ? "'," : "'"));
		}
		return sb.toString();
	}

	public static void stream2Stream(InputStream in, OutputStream out)
			throws IOException {
		final byte[] buf = new byte[8192];
		int count;

		while ((count = in.read(buf)) >= 0) {
			out.write(buf, 0, count);
		}
	}

	public static List<String> getListFromDelimitedString(String str,
			String delimiter) {
		List<String> l = null;
		if (str != null) {
			final StringTokenizer st = new StringTokenizer(str, delimiter);
			l = new ArrayList<String>();
			while (st.hasMoreTokens()) {
				l.add(st.nextToken().trim());
			}
		}
		return l;
	}

	// Adds the value only if it is true
	public static void addValue(Map<Long, Boolean> map, Long key, Boolean value) {

		if (!map.containsKey(key)) {
			map.put(key, value);
		} else {
			if (map.get(key).equals(Boolean.TRUE)) {
				map.put(key, value);
			}
		}
	}
}
