package com.shouyang.syazs.module.apply.referenceOwner;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.dao.GenericDao;
import com.shouyang.syazs.core.model.DataSet;
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
		ReferenceOwner entity = ds.getEntity();
		DsRestrictions restrictions = getDsRestrictions();

		if (entity.getOption().equals("entity.engName")) {
			if (StringUtils.isNotBlank(entity.getEngName())) {
				restrictions.likeIgnoreCase("engName", entity.getEngName(),
						MatchMode.ANYWHERE);
			}
		}

		if (entity.getOption().equals("entity.name")) {
			if (StringUtils.isNotBlank(entity.getName())) {
				restrictions.likeIgnoreCase("name", entity.getName(),
						MatchMode.ANYWHERE);
			}
		}

		restrictions.addOrderAsc("serNo");
		restrictions.addOrderAsc("engName");

		return dao.findByRestrictions(restrictions, ds);
	}

	@Override
	protected GenericDao<ReferenceOwner> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	public boolean nameIsExist(ReferenceOwner entity) throws Exception {
		Assert.notNull(entity);

		DsRestrictions restrictions = getDsRestrictions();
		restrictions.eq("name", entity.getName().trim());

		List<ReferenceOwner> customers = dao.findByRestrictions(restrictions);
		if (customers == null || customers.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public long getRefSerNoByName(String name) throws Exception {
		DsRestrictions restrictions = getDsRestrictions();
		restrictions.eq("name", name);
		List<ReferenceOwner> results = dao.findByRestrictions(restrictions);

		if (results.size() > 0) {
			return results.get(0).getSerNo();
		} else {
			return 0;
		}
	}

	public List<ReferenceOwner> getAllOwners() throws Exception {
		DsRestrictions restrictions = getDsRestrictions();

		return dao.findByRestrictions(restrictions);
	}

	public List<ReferenceOwner> getUncheckOwners(
			List<ReferenceOwner> checkedOwners) throws Exception {
		DsRestrictions restrictions = getDsRestrictions();
		if (checkedOwners != null) {
			Iterator<ReferenceOwner> iterator = checkedOwners.iterator();
			while (iterator.hasNext()) {
				restrictions.ne("serNo", iterator.next().getSerNo());
			}
		}

		return dao.findByRestrictions(restrictions);
	}
}
