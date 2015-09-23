package misc.common;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class FixedOrderBasedComparator<T extends FixedOrderBasedComparator.OrderSpecifier<U>, U> 
		implements Comparator<U> {
	
	public static interface OrderSpecifier<U> {
		String getOrderString();
		String getCode(U u);		
	}
	
	private final Map<String, Integer> orderMap;
	
	private final T t;
	
	public FixedOrderBasedComparator(T t) {
		this.t = t; 
		final String sortingOrder = t.getOrderString();
		final Map<String, Integer> aMap = new HashMap<String, Integer>();
		final String[] tokens = sortingOrder.split(",");
		for(int i = 0; i<tokens.length; i++) {
			aMap.put(tokens[i], i);
		}
		orderMap = Collections.unmodifiableMap(aMap);
	}	
	
	
	@Override
	public int compare(U o1, U o2) {
		
		final String code1 = t.getCode(o1);
		final String code2 = t.getCode(o2);		
		if (code1 == null || code2 == null) {
			return 0;
		}
		final Integer item1Wt = orderMap.get(code1);
		final Integer item2Wt = orderMap.get(code2);
	
		return item1Wt == null || item2Wt == null ? 0 : item1Wt - item2Wt;
	}
};


