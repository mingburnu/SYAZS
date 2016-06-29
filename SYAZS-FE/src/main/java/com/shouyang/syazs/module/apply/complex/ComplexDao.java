package com.shouyang.syazs.module.apply.complex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.shouyang.syazs.core.dao.ModuleDaoModel;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.module.apply.database.Database;
import com.shouyang.syazs.module.apply.ebook.Ebook;
import com.shouyang.syazs.module.apply.journal.Journal;
import com.shouyang.syazs.module.entity.ModuleProperties;

@Repository
public class ComplexDao extends ModuleDaoModel<ModuleProperties> {

	@SuppressWarnings("unchecked")
	public DataSet<ModuleProperties> query(DataSet<ModuleProperties> ds) {
		List<ModuleProperties> results = ds.getResults();
		List<ModuleProperties> list = new ArrayList<ModuleProperties>();

		if (StringUtils.isBlank(ds.getEntity().getIndexTerm())) {
			Criteria criteriaAll = getSession().createCriteria(
					ModuleProperties.class);

			list = criteriaAll.list();

		} else {
			ModuleProperties entity = ds.getEntity();
			String indexTerm = StringUtils.replaceChars(entity.getIndexTerm()
					.trim(), "－０１２３４５６７８９", "-0123456789");

			indexTerm = indexTerm.replaceAll(
					"[^0-9\\p{Ll}\\p{Lm}\\p{Lo}\\p{Lt}\\p{Lu}]", " ");

			Criteria criteriaDat = getSession().createCriteria(Database.class);
			Criteria criteriaEbk = getSession().createCriteria(Ebook.class);
			Criteria criteriaJou = getSession().createCriteria(Journal.class);

			Set<String> keywordSet = new HashSet<String>(
					Arrays.asList(indexTerm.split(" ")));
			String[] wordArray = keywordSet.toArray(new String[keywordSet
					.size()]);

			Conjunction datAnd = Restrictions.and();
			Conjunction ebkAnd = Restrictions.and();
			Conjunction jouAnd = Restrictions.and();

			for (int i = 0; i < wordArray.length; i++) {
				datAnd.add(Restrictions.ilike("dbTitle", wordArray[i],
						MatchMode.ANYWHERE));
				ebkAnd.add(Restrictions.ilike("bookName", wordArray[i],
						MatchMode.ANYWHERE));
				jouAnd.add(Restrictions.ilike("title", wordArray[i],
						MatchMode.ANYWHERE));
			}

			criteriaDat.add(datAnd);
			criteriaEbk.add(ebkAnd);
			criteriaJou.add(jouAnd);

			list.addAll(criteriaDat.list());
			list.addAll(criteriaEbk.list());
			list.addAll(criteriaJou.list());

		}

		long totalRecord = list.size() + 0L;
		ds.getPager().setTotalRecord(totalRecord);

		Double lastPage = Math.ceil(ds.getPager().getTotalRecord()
				.doubleValue()
				/ ds.getPager().getRecordPerPage().doubleValue());

		if (ds.getPager().getCurrentPage() > lastPage) {
			ds.getPager().setCurrentPage(lastPage.intValue());
		}

		int fromIndex = ds.getPager().getOffset();
		int toIndex = fromIndex + ds.getPager().getRecordPerPage();

		if (toIndex > totalRecord) {
			toIndex = list.size();
		}

		results.addAll(list.subList(fromIndex, toIndex));
		return ds;
	}
}
