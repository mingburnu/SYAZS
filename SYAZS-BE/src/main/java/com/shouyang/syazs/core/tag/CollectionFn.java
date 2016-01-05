package com.shouyang.syazs.core.tag;

import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;

public class CollectionFn {
	public static boolean containsInt(Collection<?> coll, Object o) {
		if (CollectionUtils.isEmpty(coll)) {
			return false;
		}

		return coll.contains(Integer.parseInt(o.toString()));
	}
}
