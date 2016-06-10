package rox.util;

import java.util.HashMap;

public class DoubleKeyMap<K1, K2, V>
{
	private HashMap<K1, HashMap<K2, V>> doubleKeyMap;
	
	public DoubleKeyMap() 
	{
		doubleKeyMap = new HashMap<K1, HashMap<K2, V>>();
	}

	public void put(K1 firstKey, K2 secondKey, V value)
	{
		HashMap<K2, V> innerMap = null;
		
		if (doubleKeyMap.containsKey(firstKey))
		{
			doubleKeyMap.get(firstKey);
		}
		else
		{	
			innerMap = new HashMap<K2, V>();
		}
		
		innerMap.put(secondKey, value);
	}
	
	public V get(K1 firstKey, K2 secondKey)
	{
		HashMap<K2, V> innerMap = doubleKeyMap.get(firstKey);
		return innerMap.get(secondKey);
	}
}
