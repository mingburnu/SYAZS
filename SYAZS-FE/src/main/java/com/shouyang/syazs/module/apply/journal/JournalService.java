package com.shouyang.syazs.module.apply.journal;

import java.util.HashMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.criterion.Disjunction;
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
import com.shouyang.syazs.module.apply.referenceOwner.ReferenceOwner;

@Service
public class JournalService extends GenericServiceFull<Journal> {

	@Autowired
	private Journal entity;

	@Autowired
	private JournalDao dao;

	@Autowired
	private ReferenceOwner referenceOwner;

	@Autowired
	private HashMap<String, String> hanziMap;

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
						orGroup.add(Restrictions.ilike("title", splitMinus[j],
								MatchMode.ANYWHERE));
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
		if (option.equals("標題開頭為")) {
			restrictions.likeIgnoreCase("title", indexTerm, MatchMode.START);
		} else if (option.equals("標題等於")) {
			restrictions.likeIgnoreCase("title", indexTerm, MatchMode.EXACT);
		} else if (option.equals("標題包含文字")) {
			restrictions.likeIgnoreCase("title", indexTerm, MatchMode.ANYWHERE);
		} else if (option.equals("ISSN 等於")) {
			restrictions.eq("issn",
					Long.parseLong(indexTerm.trim().replace("-", "")));
		} else {
			restrictions.eq("serNo", Long.MIN_VALUE);
		}

		return dao.findByRestrictions(restrictions, ds);
	}

	public DataSet<Journal> getByPrefix(DataSet<Journal> ds) throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());

		DsRestrictions restrictions = getDsRestrictions();
		Journal entity = ds.getEntity();
		String option = entity.getOption();
		Disjunction orGroup = Restrictions.disjunction();
		int intCode = option.charAt(0);

		if (option.equals("0-9")) {
			int i = 0;
			while (i < 10) {
				orGroup.add(Restrictions.ilike("title",
						Character.toString((char) (i + 48)), MatchMode.START));
				orGroup.add(Restrictions.ilike("title",
						Character.toString((char) (i + 65296)), MatchMode.START));
				i++;
			}
		} else if ((intCode > 64 && intCode < 91)
				|| (intCode > 96 && intCode < 123)) {
			orGroup.add(Restrictions.ilike("title",
					Character.toString((char) intCode), MatchMode.START));
			orGroup.add(Restrictions.ilike("title",
					Character.toString((char) (intCode + 65248)),
					MatchMode.START));
		} else if ((intCode > 65312 && intCode < 65339)
				|| (intCode > 65345 && intCode < 65370)) {
			orGroup.add(Restrictions.ilike("title",
					Character.toString((char) intCode), MatchMode.START));
			orGroup.add(Restrictions.ilike("title",
					Character.toString((char) (intCode - 65248)),
					MatchMode.START));
		} else if (intCode > 12548 && intCode < 12577) {
			String words = hanziMap.get(Character.toString((char) intCode));
			int i = 0;
			while (i < words.length()) {
				orGroup.add(Restrictions.ilike("title",
						Character.toString(words.charAt(i)), MatchMode.START));
				i++;
			}
		} else {
			restrictions.eq("serNo", Long.MIN_VALUE);

		}

		restrictions.customCriterion(orGroup);

		return dao.findByRestrictions(restrictions, ds);
	}

	public long countByOwner(ReferenceOwner owner) throws Exception {
		return dao.count(owner);
	}

	public DataSet<Journal> getByRefSerNo(DataSet<Journal> ds) throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());

		entity = ds.getEntity();
		referenceOwner = new ReferenceOwner();
		referenceOwner.setSerNo(ds.getEntity().getRefSerNo());

		return dao.findByOwner(ds, referenceOwner);
	}
}
