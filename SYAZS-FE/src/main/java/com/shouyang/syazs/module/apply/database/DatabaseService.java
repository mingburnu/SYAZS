package com.shouyang.syazs.module.apply.database;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.dao.GenericDao;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.model.Pager;
import com.shouyang.syazs.core.service.GenericServiceFull;

@Service
public class DatabaseService extends GenericServiceFull<Database> {

	@Autowired
	private Database entity;

	@Autowired
	private DatabaseDao dao;

	@Override
	public DataSet<Database> getByRestrictions(DataSet<Database> ds)
			throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());

		DsRestrictions restrictions = getDsRestrictions();
		Database entity = ds.getEntity();
		String indexTerm = entity.getIndexTerm().trim();
		indexTerm = indexTerm.replaceAll(
				"[^0-9０-９\\p{Ll}\\p{Lm}\\p{Lo}\\p{Lt}\\p{Lu}\\u002d]", " ");
		String[] wordArray = indexTerm.split(" ");

		if (!ArrayUtils.isEmpty(wordArray)) {
			Junction andGroup = Restrictions.conjunction();
			for (int i = 0; i < wordArray.length; i++) {
				if (StringUtils.isBlank(wordArray[i])) {
					continue;
				} else {
					andGroup.add(Restrictions.ilike("dbTitle", wordArray[i],
							MatchMode.ANYWHERE));
				}
			}

			restrictions.customCriterion(andGroup);
		} else {
			Pager pager = ds.getPager();
			pager.setTotalRecord(0L);
			ds.setPager(pager);
			return ds;
		}

		return dao.findByRestrictions(restrictions, ds);
	}

	@Override
	protected GenericDao<Database> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	public List<Database> getAllDb() throws Exception {
		DsRestrictions restrictions = getDsRestrictions();
		return dao.findByRestrictions(restrictions);
	}

	public long countToatal() {
		return dao.countAll();
	}
}
