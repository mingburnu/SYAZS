package com.shouyang.syazs.module.apply.referenceOwner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
public class ReferenceOwnerService extends GenericServiceFull<ReferenceOwner> {

	@Autowired
	private ReferenceOwnerDao dao;

	@Override
	public DataSet<ReferenceOwner> getByRestrictions(DataSet<ReferenceOwner> ds)
			throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());

		DsRestrictions restrictions = getDsRestrictions();
		ReferenceOwner entity = ds.getEntity();

		String indexTerm = StringUtils.replaceChars(entity.getIndexTerm()
				.trim(), "０１２３４５６７８９", "0123456789");
		indexTerm = indexTerm.replaceAll(
				"[^0-9\\p{Ll}\\p{Lm}\\p{Lo}\\p{Lt}\\p{Lu}]", " ");
		Set<String> keywordSet = new HashSet<String>(Arrays.asList(indexTerm
				.split(" ")));
		String[] wordArray = keywordSet.toArray(new String[keywordSet.size()]);

		if (!ArrayUtils.isEmpty(wordArray)) {
			Junction or = Restrictions.disjunction();
			Junction nameAnd = Restrictions.conjunction();
			Junction engNameAnd = Restrictions.conjunction();
			for (int i = 0; i < wordArray.length; i++) {
				nameAnd.add(Restrictions.ilike("name", wordArray[i],
						MatchMode.ANYWHERE));
				engNameAnd.add(Restrictions.ilike("engName", wordArray[i],
						MatchMode.ANYWHERE));
			}

			or.add(nameAnd).add(engNameAnd);
			restrictions.customCriterion(or);
		} else {
			Pager pager = ds.getPager();
			pager.setTotalRecord(0L);
			ds.setPager(pager);
			return ds;
		}

		return dao.findByRestrictions(restrictions, ds);
	}

	@Override
	protected GenericDao<ReferenceOwner> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}
}