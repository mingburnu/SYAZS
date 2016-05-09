package com.shouyang.syazs.module.apply.ebook;

import java.util.HashMap;
import org.hibernate.criterion.Junction;
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
public class EbookService extends GenericServiceFull<Ebook> {

	@Autowired
	private Ebook entity;

	@Autowired
	private EbookDao dao;

	@Autowired
	private HashMap<String, String> hanziMap;

	@Override
	public DataSet<Ebook> getByRestrictions(DataSet<Ebook> ds) throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());

		DsRestrictions restrictions = getDsRestrictions();
		Ebook entity = ds.getEntity();
		String option = entity.getOption();
		String indexTerm = entity.getIndexTerm().trim();

		if (option.equals("標題開頭為")) {
			restrictions.likeIgnoreCase("bookName", indexTerm, MatchMode.START);
		} else if (option.equals("標題包含文字")) {
			restrictions.likeIgnoreCase("bookName", indexTerm,
					MatchMode.ANYWHERE);
		} else if (option.equals("出版社")) {
			restrictions.likeIgnoreCase("publishName", indexTerm,
					MatchMode.ANYWHERE);
		} else if (option.equals("ISBN 等於")) {
			restrictions.eq("isbn", indexTerm.replace("-", "").toUpperCase());
		} else if (option.equals("第一作者")) {
			restrictions.likeIgnoreCase("autherName", indexTerm,
					MatchMode.ANYWHERE);
		} else if (option.equals("次要作者")) {
			restrictions.likeIgnoreCase("authers", indexTerm,
					MatchMode.ANYWHERE);
		} else if (option.equals("分類號")) {
			restrictions.likeIgnoreCase("lcsCode", indexTerm,
					MatchMode.ANYWHERE);
		}

		return dao.findByRestrictions(restrictions, ds);
	}

	@Override
	protected GenericDao<Ebook> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	public DataSet<Ebook> getByPrefix(DataSet<Ebook> ds) throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());

		DsRestrictions restrictions = getDsRestrictions();
		Ebook entity = ds.getEntity();
		String option = entity.getOption();

		Junction or = Restrictions.disjunction();
		int intCode = option.charAt(0);

		if (option.equals("0-9")) {
			int i = 0;
			while (i < 10) {
				or.add(Restrictions.ilike("bookName",
						Character.toString((char) (i + 48)), MatchMode.START));
				i++;
			}
		} else if ((intCode > 64 && intCode < 91)
				|| (intCode > 96 && intCode < 123)) {
			or.add(Restrictions.ilike("bookName",
					Character.toString((char) intCode), MatchMode.START));
		} else if (intCode > 12548 && intCode < 12577) {
			String words = hanziMap.get(Character.toString((char) intCode));
			int i = 0;
			while (i < words.length()) {
				or.add(Restrictions.ilike("bookName",
						Character.toString(words.charAt(i)), MatchMode.START));
				i++;
			}
		} else {
			or.add(Restrictions
					.sqlRestriction("bookName REGEXP '^[^0-9a-zA-Z０-９ａ-ｚＡ-Ｚ\u4e00-\u9fa5]'"));
		}

		restrictions.customCriterion(or);

		return dao.findByRestrictions(restrictions, ds);
	}

	public long countToatal() {
		return dao.countAll();
	}
}
