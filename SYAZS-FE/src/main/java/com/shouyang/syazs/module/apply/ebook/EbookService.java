package com.shouyang.syazs.module.apply.ebook;

import java.util.HashMap;
import org.apache.commons.lang3.ArrayUtils;
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
public class EbookService extends GenericServiceFull<Ebook> {

	@Autowired
	private Ebook entity;

	@Autowired
	private EbookDao dao;

	@Autowired
	private ReferenceOwner referenceOwner;

	@Autowired
	private HashMap<String, String> hanziMap;

	@Override
	public DataSet<Ebook> getByRestrictions(DataSet<Ebook> ds) throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());

		DsRestrictions restrictions = getDsRestrictions();
		Ebook entity = ds.getEntity();
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

				if (NumberUtils.isDigits(wordArray[i].replace("-", ""))) {
					orGroup.add(Restrictions.eq("isbn",
							Long.parseLong(wordArray[i].replace("-", ""))));
				} else {
					String[] splitMinus = wordArray[i].split("-");

					for (int j = 0; j < splitMinus.length; j++) {
						orGroup.add(Restrictions.ilike("bookName",
								splitMinus[j], MatchMode.ANYWHERE));
						orGroup.add(Restrictions.ilike("publishName",
								splitMinus[j], MatchMode.ANYWHERE));
						orGroup.add(Restrictions.ilike("autherName",
								splitMinus[j], MatchMode.ANYWHERE));
					}
				}
			}

			orGroup.add(Restrictions.eq("isbn", -1L));
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
	protected GenericDao<Ebook> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	public DataSet<Ebook> getByOption(DataSet<Ebook> ds) throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());

		DsRestrictions restrictions = getDsRestrictions();
		Ebook entity = ds.getEntity();
		String indexTerm = entity.getIndexTerm();
		String option = entity.getOption();
		if (option.equals("標題開頭為")) {
			restrictions.likeIgnoreCase("bookName", indexTerm, MatchMode.START);
		} else if (option.equals("標題等於")) {
			restrictions.likeIgnoreCase("bookName", indexTerm, MatchMode.EXACT);
		} else if (option.equals("標題包含文字")) {
			restrictions.likeIgnoreCase("bookName", indexTerm,
					MatchMode.ANYWHERE);
		} else if (option.equals("ISBN 等於")) {
			restrictions.eq("isbn",
					Long.parseLong(indexTerm.trim().replace("-", "")));
		} else {
			restrictions.eq("serNo", Long.MIN_VALUE);
		}

		return dao.findByRestrictions(restrictions, ds);
	}

	public DataSet<Ebook> getByPrefix(DataSet<Ebook> ds) throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());

		DsRestrictions restrictions = getDsRestrictions();
		Ebook entity = ds.getEntity();
		String option = entity.getOption();
		Disjunction orGroup = Restrictions.disjunction();
		int intCode = option.charAt(0);

		if (option.equals("0-9")) {
			int i = 0;
			while (i < 10) {
				orGroup.add(Restrictions.ilike("bookName",
						Character.toString((char) (i + 48)), MatchMode.START));
				orGroup.add(Restrictions.ilike("bookName",
						Character.toString((char) (i + 65296)), MatchMode.START));
				i++;
			}
		} else if ((intCode > 64 && intCode < 91)
				|| (intCode > 96 && intCode < 123)) {
			orGroup.add(Restrictions.ilike("bookName",
					Character.toString((char) intCode), MatchMode.START));
			orGroup.add(Restrictions.ilike("bookName",
					Character.toString((char) (intCode + 65248)),
					MatchMode.START));
		} else if ((intCode > 65312 && intCode < 65339)
				|| (intCode > 65345 && intCode < 65370)) {
			orGroup.add(Restrictions.ilike("bookName",
					Character.toString((char) intCode), MatchMode.START));
			orGroup.add(Restrictions.ilike("bookName",
					Character.toString((char) (intCode - 65248)),
					MatchMode.START));
		} else if (intCode > 12548 && intCode < 12577) {
			String words = hanziMap.get(Character.toString((char) intCode));
			int i = 0;
			while (i < words.length()) {
				orGroup.add(Restrictions.ilike("bookName",
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

	public DataSet<Ebook> getByRefSerNo(DataSet<Ebook> ds) throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());

		entity = ds.getEntity();
		referenceOwner = new ReferenceOwner();
		referenceOwner.setSerNo(ds.getEntity().getRefSerNo());

		return dao.findByOwner(ds, referenceOwner);
	}
}
