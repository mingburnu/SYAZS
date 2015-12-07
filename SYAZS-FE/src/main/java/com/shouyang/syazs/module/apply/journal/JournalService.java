package com.shouyang.syazs.module.apply.journal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.dao.DsQueryLanguage;
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

		String indexTerm = StringUtils.replaceChars(entity.getIndexTerm()
				.trim(), "－０１２３４５６７８９", "-0123456789");

		if (ISSN_Validator.isIssn(indexTerm)) {
			restrictions.eq("issn", indexTerm.replace("-", "").toUpperCase());
		} else {
			indexTerm = indexTerm.replaceAll(
					"[^0-9\\p{Ll}\\p{Lm}\\p{Lo}\\p{Lt}\\p{Lu}]", " ");
			Set<String> keywordSet = new HashSet<String>(
					Arrays.asList(indexTerm.split(" ")));
			String[] wordArray = keywordSet.toArray(new String[keywordSet
					.size()]);

			if (!ArrayUtils.isEmpty(wordArray)) {
				Junction or = Restrictions.disjunction();
				Junction titleAnd = Restrictions.conjunction();
				Junction abbreviationTitleAnd = Restrictions.conjunction();
				Junction publishNameAnd = Restrictions.conjunction();
				for (int i = 0; i < wordArray.length; i++) {
					titleAnd.add(Restrictions.ilike("title", wordArray[i],
							MatchMode.ANYWHERE));
					abbreviationTitleAnd.add(Restrictions.ilike(
							"abbreviationTitle", wordArray[i],
							MatchMode.ANYWHERE));
					publishNameAnd.add(Restrictions.ilike("publishName",
							wordArray[i], MatchMode.ANYWHERE));
				}

				or.add(titleAnd).add(abbreviationTitleAnd).add(publishNameAnd);
				restrictions.customCriterion(or);

			} else {
				Pager pager = ds.getPager();
				pager.setTotalRecord(0L);
				ds.setPager(pager);
				return ds;
			}
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
		String option = entity.getOption();
		String indexTerm = StringUtils.replaceChars(entity.getIndexTerm()
				.trim(), "－０１２３４５６７８９", "-0123456789");

		if (option.equals("標題開頭為")) {
			restrictions.likeIgnoreCase("title", indexTerm, MatchMode.START);
		} else if (option.equals("標題等於")) {
			restrictions.likeIgnoreCase("title", indexTerm, MatchMode.EXACT);
		} else if (option.equals("標題包含文字")) {
			indexTerm = indexTerm.replaceAll(
					"[^0-9\\p{Ll}\\p{Lm}\\p{Lo}\\p{Lt}\\p{Lu}]", " ");
			Set<String> keywordSet = new HashSet<String>(
					Arrays.asList(indexTerm.split(" ")));
			String[] wordArray = keywordSet.toArray(new String[keywordSet
					.size()]);

			if (!ArrayUtils.isEmpty(wordArray)) {
				for (int i = 0; i < wordArray.length; i++) {
					restrictions.likeIgnoreCase("title", indexTerm,
							MatchMode.ANYWHERE);
				}
			} else {
				Pager pager = ds.getPager();
				pager.setTotalRecord(0L);
				ds.setPager(pager);
				return ds;
			}
		} else if (option.equals("ISSN 等於")) {
			if (ISSN_Validator.isIssn(indexTerm)) {
				restrictions.eq("issn", indexTerm.toUpperCase()
						.replace("-", ""));
			} else {
				Pager pager = ds.getPager();
				pager.setTotalRecord(0L);
				ds.setPager(pager);
				return ds;
			}
		}

		return dao.findByRestrictions(restrictions, ds);
	}

	public DataSet<Journal> getByPrefix(DataSet<Journal> ds) throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());

		DsRestrictions restrictions = getDsRestrictions();
		Journal entity = ds.getEntity();
		String option = entity.getOption();

		Junction or = Restrictions.disjunction();
		int intCode = option.trim().charAt(0);

		if (option.equals("0-9")) {
			int i = 0;
			while (i < 10) {
				or.add(Restrictions.ilike("title",
						Character.toString((char) (i + 48)), MatchMode.START));
				i++;
			}
		} else if ((intCode > 64 && intCode < 91)
				|| (intCode > 96 && intCode < 123)) {
			or.add(Restrictions.ilike("title",
					Character.toString((char) intCode), MatchMode.START));
		} else if (intCode > 12548 && intCode < 12577) {
			String words = hanziMap.get(Character.toString((char) intCode));
			int i = 0;
			while (i < words.length()) {
				or.add(Restrictions.ilike("title",
						Character.toString(words.charAt(i)), MatchMode.START));
				i++;
			}
		} else {
			or.add(Restrictions
					.sqlRestriction("title REGEXP '^[^0-9a-zA-Z０-９ａ-ｚＡ-Ｚ\u4e00-\u9fa5]'"));
		}

		restrictions.customCriterion(or);

		return dao.findByRestrictions(restrictions, ds);
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getResOwners(long serNo) {
		DsQueryLanguage queryLanguage = getDsQueryLanguage();
		queryLanguage
				.setHql("SELECT jr.serNo, jr.name FROM Journal j JOIN j.referenceOwners jr WHERE j.serNo = :serNo");
		queryLanguage.addParameter("serNo", serNo);
		return (List<Object[]>) dao.findByHQL(queryLanguage);
	}

	public long countToatal() {
		return dao.countAll();
	}

	public long countByOwner(long ownerSerNo) {
		return dao.countByOwner(ownerSerNo);
	}

	public DataSet<Journal> getByOwner(DataSet<Journal> ds) throws Exception {
		DsQueryLanguage queryLanguage = getDsQueryLanguage();
		String hql = "SELECT j FROM Journal j JOIN j.referenceOwners jr WHERE jr.serNo=:ownerSerNo OR j.serNo IN (SELECT j.serNo FROM Journal j JOIN j.database jd JOIN jd.referenceOwners jdr WHERE jdr.serNo=:ownerSerNo)";
		queryLanguage.setHql(hql);
		queryLanguage.addParameter("ownerSerNo", ds.getEntity().getRefSerNo());
		return dao.findByHQL(queryLanguage, ds);
	}
}
