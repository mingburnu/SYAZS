package com.shouyang.syazs.module.apply.database;

import org.apache.commons.lang3.ArrayUtils;
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
		String indexTerm = entity.getIndexTerm();

		char[] cArray = indexTerm.toCharArray();
		StringBuilder indexTermBuilder = new StringBuilder();
		for (int i = 0; i < cArray.length; i++) {
			int charCode = (int) cArray[i];
			if (charCode > 65280 && charCode < 65375) {
				int halfChar = charCode - 65248;
				cArray[i] = (char) halfChar;
			}
			indexTermBuilder.append(cArray[i]);
		}

		indexTerm = indexTermBuilder.toString();
		indexTerm = indexTerm.replaceAll(
				"[^a-zA-Z0-9\u4e00-\u9fa5\u0391-\u03a9\u03b1-\u03c9]", " ");
		String[] wordArray = indexTerm.split(" ");

		if (!ArrayUtils.isEmpty(wordArray)) {
			Junction orGroup = Restrictions.disjunction();
			for (int i = 0; i < wordArray.length; i++) {
				orGroup.add(Restrictions.ilike("dbTitle", wordArray[i],
						MatchMode.ANYWHERE));
			}

			restrictions.customCriterion(orGroup);

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

	public DataSet<Database> getByOption(DataSet<Database> ds) throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());

		DsRestrictions restrictions = getDsRestrictions();
		Database entity = ds.getEntity();
		String indexTerm = entity.getIndexTerm();
		String option = entity.getOption();

		if (option.equals("中文題名")) {
			option = "dbChtTitle";
		} else if (option.equals("出版社")) {
			option = "publishName";
		} else if (option.equals("內容描述")) {
			option = "content";
		}

		char[] cArray = indexTerm.toCharArray();
		StringBuilder indexTermBuilder = new StringBuilder();
		for (int i = 0; i < cArray.length; i++) {
			int charCode = (int) cArray[i];
			if (charCode > 65280 && charCode < 65375) {
				int halfChar = charCode - 65248;
				cArray[i] = (char) halfChar;
			}
			indexTermBuilder.append(cArray[i]);
		}

		indexTerm = indexTermBuilder.toString();
		indexTerm = indexTerm.replaceAll(
				"[^a-zA-Z0-9\u4e00-\u9fa5\u0391-\u03a9\u03b1-\u03c9]", " ");
		String[] wordArray = indexTerm.split(" ");

		if (!ArrayUtils.isEmpty(wordArray)) {
			Junction orGroup = Restrictions.disjunction();
			for (int i = 0; i < wordArray.length; i++) {
				orGroup.add(Restrictions.ilike(option, wordArray[i],
						MatchMode.ANYWHERE));
			}

			restrictions.customCriterion(orGroup);

		} else {
			Pager pager = ds.getPager();
			pager.setTotalRecord(0L);
			ds.setPager(pager);
			return ds;
		}

		return dao.findByRestrictions(restrictions, ds);
	}

	public DataSet<Database> getAllDb(DataSet<Database> ds) throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());

		DsRestrictions restrictions = getDsRestrictions();
		return dao.findByRestrictions(restrictions, ds);
	}

	public long countToatal() {
		return dao.countAll();
	}
}
