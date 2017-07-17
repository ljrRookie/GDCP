package com.example.ronny_xie.gdcp.util;

import com.example.ronny_xie.gdcp.bean.GotyeUserProxy;

import java.util.Comparator;


/**
 * 
 * @author xiaanming
 * 
 */
public class PinyinComparator implements Comparator<GotyeUserProxy> {

	public int compare(GotyeUserProxy o1, GotyeUserProxy o2) {
		if (o1.firstChar.equals("@") || o2.firstChar.equals("#")) {
			return -1;
		} else if (o1.firstChar.equals("#") || o2.firstChar.equals("@")) {
			return 1;
		} else {
			return o1.firstChar.compareTo(o2.firstChar);
		}
	}

}
