package com.shouyang.syazs.core.apply.customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.dao.GenericDao;
import com.shouyang.syazs.core.model.DataSet;
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

		return dao.findByRestrictions(restrictions, ds);
	}

	@Override
	protected GenericDao<Customer> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	public boolean nameIsExist(Customer entity) throws Exception {
		Assert.notNull(entity);

		DsRestrictions restrictions = getDsRestrictions();
		restrictions.eq("name", entity.getName().trim());

		List<Customer> customers = dao.findByRestrictions(restrictions);
		if (customers == null || customers.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public long getCusSerNoByName(String name) throws Exception {
		DsRestrictions restrictions = getDsRestrictions();
		restrictions.eq("name", name);

		if (dao.findByRestrictions(restrictions).size() > 0) {
			return dao.findByRestrictions(restrictions).get(0).getSerNo();
		} else {
			return 0;
		}
	}

	public List<Customer> getAllCustomers() throws Exception {
		DsRestrictions restrictions = getDsRestrictions();

		return dao.findByRestrictions(restrictions);
	}

	public List<Customer> getUncheckCustomers(List<Customer> checkedCustomers)
			throws Exception {
		DsRestrictions restrictions = getDsRestrictions();
		if (checkedCustomers != null) {
			int i = 0;
			while (i < checkedCustomers.size()) {
				restrictions.ne("serNo", checkedCustomers.get(i).getSerNo());
				i++;
			}
		}

		return dao.findByRestrictions(restrictions);
	}

	public Map<String, Object> getCusDatas() {
		return dao.getMap(new HashMap<String, Object>());
	}
}
