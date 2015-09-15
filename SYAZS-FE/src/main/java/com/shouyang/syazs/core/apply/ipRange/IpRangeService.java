package com.shouyang.syazs.core.apply.ipRange;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.dao.GenericDao;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.service.GenericServiceFull;

@Service
public class IpRangeService extends GenericServiceFull<IpRange> {

	@Autowired
	private IpRangeDao dao;

	@Override
	public DataSet<IpRange> getByRestrictions(DataSet<IpRange> ds)
			throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());
		DsRestrictions restrictions = getDsRestrictions();

		return dao.findByRestrictions(restrictions, ds);
	}

	@Override
	protected GenericDao<IpRange> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	public List<IpRange> getAllIpList() throws Exception {
		DsRestrictions restrictions = getDsRestrictions();
		return dao.findByRestrictions(restrictions);
	}

}
