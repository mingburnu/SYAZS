package com.shouyang.syazs.core.apply.customer;

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
public class CustomerService extends GenericServiceFull<Customer> {

	@Autowired
	private CustomerDao dao;

	@Override
	public DataSet<Customer> getByRestrictions(DataSet<Customer> ds)
			throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());

		DsRestrictions restrictions = getDsRestrictions();
		Customer entity = ds.getEntity();
		String itemTerm = entity.getIndexTerm();

		char[] cArray = itemTerm.toCharArray();
		StringBuilder itemTermBuilder = new StringBuilder();
		for (int i = 0; i < cArray.length; i++) {
			int charCode = (int) cArray[i];
			if (charCode > 65280 && charCode < 65375) {
				int halfChar = charCode - 65248;
				cArray[i] = (char) halfChar;
			}
			itemTermBuilder.append(cArray[i]);
		}

		itemTerm = itemTermBuilder.toString();
		itemTerm = itemTerm.replaceAll("[^a-zA-Z0-9\u4e00-\u9fa5]", " ");
		String[] wordArray = itemTerm.split(" ");

		if (!ArrayUtils.isEmpty(wordArray)) {
			Junction orGroup = Restrictions.disjunction();
			for (int i = 0; i < wordArray.length; i++) {
				orGroup.add(Restrictions.ilike("name", wordArray[i],
						MatchMode.ANYWHERE));
				orGroup.add(Restrictions.ilike("engName", wordArray[i],
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
	protected GenericDao<Customer> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}
}
