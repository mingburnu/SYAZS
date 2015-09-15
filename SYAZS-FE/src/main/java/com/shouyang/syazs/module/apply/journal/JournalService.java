package com.shouyang.syazs.module.apply.journal;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
public class JournalService extends GenericServiceFull<Journal> {

	@Autowired
	private Journal entity;

	@Autowired
	private JournalDao dao;

	@Override
	public DataSet<Journal> getByRestrictions(DataSet<Journal> ds)
			throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());

		DsRestrictions restrictions = getDsRestrictions();
		Journal entity = ds.getEntity();
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
				"[^-a-zA-Z0-9\u4e00-\u9fa5\u0391-\u03a9\u03b1-\u03c9]", " ");
		String[] wordArray = indexTerm.split(" ");

		if (!ArrayUtils.isEmpty(wordArray)) {
			Junction orGroup = Restrictions.disjunction();
			for (int i = 0; i < wordArray.length; i++) {
				if (wordArray[i].replace("-", "").length() == 8
						&& NumberUtils.isDigits(wordArray[i].replace("-", "")
								.substring(0, 6))
						&& (wordArray[i].replace("-", "").charAt(7) == 'x'
								|| wordArray[i].replace("-", "").charAt(7) == 'X' || CharUtils
									.isAsciiNumeric(wordArray[i].replace("-",
											"").charAt(7)))) {
					orGroup.add(Restrictions.ilike("issn",
							wordArray[i].replace("-", ""), MatchMode.EXACT));
				} else {
					String[] splitMinus = wordArray[i].split("-");

					for (int j = 0; j < splitMinus.length; j++) {
						orGroup.add(Restrictions.ilike("chineseTitle",
								splitMinus[j], MatchMode.ANYWHERE));
						orGroup.add(Restrictions.ilike("englishTitle",
								splitMinus[j], MatchMode.ANYWHERE));
						orGroup.add(Restrictions.ilike("abbreviationTitle",
								splitMinus[j], MatchMode.ANYWHERE));
						orGroup.add(Restrictions.ilike("publishName",
								splitMinus[j], MatchMode.ANYWHERE));
					}
				}
			}

			orGroup.add(Restrictions.eq("serNo", -1L));
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
	protected GenericDao<Journal> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	public DataSet<Journal> getByOption(DataSet<Journal> ds) throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());

		DsRestrictions restrictions = getDsRestrictions();
		Journal entity = ds.getEntity();
		String indexTerm = entity.getIndexTerm();
		String option = entity.getOption();

		if (option.equals("中文刊名")) {
			option = "chineseTitle";
		} else if (option.equals("英文刊名")) {
			option = "englishTitle";
		} else if (option.equals("英文縮寫")) {
			option = "abbreviationTitle";
		} else if (option.equals("出版商")) {
			option = "publishName";
		} else if (option.equals("ISSN")) {
			option = "issn";
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
				"[^-a-zA-Z0-9\u4e00-\u9fa5\u0391-\u03a9\u03b1-\u03c9]", " ");
		String[] wordArray = indexTerm.split(" ");

		if (!ArrayUtils.isEmpty(wordArray)) {
			Junction orGroup = Restrictions.disjunction();
			for (int i = 0; i < wordArray.length; i++) {
				if (option.equals("issn")) {
					if (wordArray[i].replace("-", "").length() == 8
							&& NumberUtils.isDigits(wordArray[i].replace("-",
									"").substring(0, 6))
							&& (wordArray[i].replace("-", "").charAt(7) == 'x'
									|| wordArray[i].replace("-", "").charAt(7) == 'X' || CharUtils
										.isAsciiNumeric(wordArray[i].replace(
												"-", "").charAt(7)))) {
						orGroup.add(Restrictions.ilike("issn",
								wordArray[i].replace("-", ""), MatchMode.EXACT));
					}
				} else {
					String[] splitMinus = wordArray[i].split("-");
					for (int j = 0; j < splitMinus.length; j++) {
						orGroup.add(Restrictions.ilike(option, splitMinus[j],
								MatchMode.ANYWHERE));
					}
				}
			}

			orGroup.add(Restrictions.eq("serNo", -1L));
			restrictions.customCriterion(orGroup);

		} else {
			Pager pager = ds.getPager();
			pager.setTotalRecord(0L);
			ds.setPager(pager);
			return ds;
		}

		return dao.findByRestrictions(restrictions, ds);
	}

	public DataSet<Journal> getByCusSerNo(DataSet<Journal> ds) throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());

		DsRestrictions restrictions = getDsRestrictions();
		entity = ds.getEntity();

		List<Journal> results = dao.findByRestrictions(restrictions);
		ds.setResults(results);
		return ds;
	}
}
