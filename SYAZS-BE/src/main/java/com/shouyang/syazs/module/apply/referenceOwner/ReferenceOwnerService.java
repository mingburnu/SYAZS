package com.shouyang.syazs.module.apply.referenceOwner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.dao.DsQueryLanguage;
import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.dao.GenericDao;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.service.GenericServiceFull;

@Service
public class ReferenceOwnerService extends GenericServiceFull<ReferenceOwner> {

	@Autowired
	private ReferenceOwner referenceOwner;

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

	public Object[] getOwnerBySerNo(long serNo) throws Exception {
		DsQueryLanguage queryLanguage = getDsQueryLanguage();
		queryLanguage
				.setHql("SELECT serNo, name FROM ReferenceOwner WHERE serNo=:serNo");
		queryLanguage.addParameter("serNo", serNo);

		List<?> result = dao.findByHQL(queryLanguage);

		if (CollectionUtils.isEmpty(result)) {
			return null;
		} else {
			return (Object[]) result.get(0);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getAllOwners() throws Exception {
		DsQueryLanguage queryLanguage = getDsQueryLanguage();
		queryLanguage.setHql("SELECT serNo, name FROM ReferenceOwner");
		return (List<Object[]>) dao.findByHQL(queryLanguage);
	}

	@SuppressWarnings("unchecked")
	public List<ReferenceOwner> getUncheckOwners(
			List<ReferenceOwner> checkedOwners) throws Exception {
		DsQueryLanguage queryLanguage = getDsQueryLanguage();
		queryLanguage
				.setHql("SELECT serNo, name FROM ReferenceOwner WHERE serNo NOT IN (:ckecks)");
		List<Long> ckecks = new ArrayList<Long>();
		if (CollectionUtils.isNotEmpty(checkedOwners)) {
			Iterator<ReferenceOwner> iterator = checkedOwners.iterator();
			while (iterator.hasNext()) {
				Long serNo = (Long) iterator.next().getSerNo();
				ckecks.add(serNo);
			}
		} else {
			ckecks.add(Long.MIN_VALUE);
		}

		queryLanguage.addParameterList("ckecks", ckecks);

		List<Object[]> unchecks = (List<Object[]>) dao.findByHQL(queryLanguage);
		List<ReferenceOwner> uncheckOwners = new ArrayList<ReferenceOwner>();
		for (int i = 0; i < unchecks.size(); i++) {
			referenceOwner = new ReferenceOwner();
			referenceOwner.setSerNo((Long) unchecks.get(i)[0]);
			referenceOwner.setName(unchecks.get(i)[1].toString());
			uncheckOwners.add(referenceOwner);
		}

		return uncheckOwners;
	}
}
