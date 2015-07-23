package com.shouyang.syazs.module.apply.database;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.dao.GenericDao;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.service.GenericServiceFull;

@Service
public class DatabaseService extends GenericServiceFull<Database> {

	@Autowired
	private DatabaseDao dao;

	@Override
	public DataSet<Database> getByRestrictions(DataSet<Database> ds)
			throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());
		Database entity = ds.getEntity();
		DsRestrictions restrictions = getDsRestrictions();

		if (entity.getOption().equals("entity.dbChtTitle")) {
			if (StringUtils.isNotBlank(entity.getDbChtTitle())) {
				restrictions.likeIgnoreCase("dbChtTitle",
						entity.getDbChtTitle(), MatchMode.ANYWHERE);
			}
		}
		
		if (entity.getOption().equals("entity.dbEngTitle")) {
			if (StringUtils.isNotBlank(entity.getDbEngTitle())) {
				restrictions.likeIgnoreCase("dbEngTitle",
						entity.getDbEngTitle(), MatchMode.ANYWHERE);
			}
		}

		return dao.findByRestrictions(restrictions, ds);
	}

	@Override
	protected GenericDao<Database> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	public long getDatSerNoByBothName(String dbChtTitle, String dbEngTitle)
			throws Exception {
		DsRestrictions restrictions = getDsRestrictions();
		restrictions.customCriterion(Restrictions.and(
				Restrictions.ilike("dbChtTitle", dbChtTitle, MatchMode.EXACT),
				Restrictions.ilike("dbEngTitle", dbEngTitle, MatchMode.EXACT)));

		if (dao.findByRestrictions(restrictions).size() > 0) {
			return (dao.findByRestrictions(restrictions).get(0)).getSerNo();
		} else {
			return 0;
		}
	}

	public long getDatSerNoByChtName(String dbChtTitle) throws Exception {
		DsRestrictions restrictions = getDsRestrictions();

		if (StringUtils.isNotBlank(dbChtTitle)) {
			restrictions.likeIgnoreCase("dbChtTitle", dbChtTitle.trim(),
					MatchMode.EXACT);
		} else {
			return 0;
		}

		if (dao.findByRestrictions(restrictions).size() > 0) {
			return (dao.findByRestrictions(restrictions).get(0)).getSerNo();
		} else {
			return 0;
		}
	}

	public long getDatSerNoByEngName(String dbEngTitle) throws Exception {
		DsRestrictions restrictions = getDsRestrictions();

		if (StringUtils.isNotBlank(dbEngTitle)) {
			restrictions.likeIgnoreCase("dbEngTitle", dbEngTitle.trim(),
					MatchMode.EXACT);
		} else {
			return 0;
		}

		if (dao.findByRestrictions(restrictions).size() > 0) {
			return (dao.findByRestrictions(restrictions).get(0)).getSerNo();
		} else {
			return 0;
		}
	}
}
