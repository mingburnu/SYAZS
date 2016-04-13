package com.shouyang.syazs.module.apply.classification;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.shouyang.syazs.core.dao.ModuleDaoSerNo;

@Repository
public class ClassificationDao extends ModuleDaoSerNo<Classification> {
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMap(Map<String, Object> datas) {
		Query query = getSession().createQuery(
				"SELECT c.classname, c.serNo FROM Classification c");
		List<Object[]> rows = query.list();

		for (Object[] row : rows) {
			String classname = (String) row[0];
			Long serNo = (Long) row[1];
			datas.put(classname, serNo);
		}

		return datas;
	}
}
