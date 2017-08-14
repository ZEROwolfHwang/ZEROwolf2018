package com.zero.wolf.greenroad.smartsearch;

import com.zero.wolf.greenroad.bean.SerializableGoods;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 *
 */
	public class PinyinComparator implements Comparator<SerializableGoods> {

	public int compare(SerializableGoods o1, SerializableGoods o2) {
		if (o1.sortLetters.equals("@") || o2.sortLetters.equals("#")) {
			return -1;
		} else if (o1.sortLetters.equals("#") || o2.sortLetters.equals("@")) {
			return 1;
		} else {
			return o1.sortLetters.compareTo(o2.sortLetters);
		}
	}

}
