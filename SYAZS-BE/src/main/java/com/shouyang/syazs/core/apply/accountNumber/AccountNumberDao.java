package com.shouyang.syazs.core.apply.accountNumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.stereotype.Repository;

import com.shouyang.syazs.core.apply.enums.Role;
import com.shouyang.syazs.core.dao.ModuleDaoFull;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.model.Pager;

/**
 * 使用者 Dao
 * 
 * @author Roderick
 * @version 2014/9/29
 */
@Repository
public class AccountNumberDao extends ModuleDaoFull<AccountNumber> {

	public AccountNumber findByUserId(String userId) {
		Criteria criteria = getSession().createCriteria(AccountNumber.class);
		Restrictions.eq("userId", userId);

		return (AccountNumber) criteria.list().get(0);
	}

	@SuppressWarnings("unchecked")
	public DataSet<AccountNumber> findByRestrictions(AccountNumber loginUser,
			DataSet<AccountNumber> ds) {
		Criteria criteria = getSession().createCriteria(AccountNumber.class);
		criteria.createAlias("customer", "customer");
		Criteria countCri = getSession().createCriteria(AccountNumber.class);
		countCri.createAlias("customer", "customer");

		List<Role> roleList = new ArrayList<Role>(Arrays.asList(Role.values()));
		List<Role> tempList = new ArrayList<Role>();
		roleList.remove(roleList.size() - 1);

		int roleCode = roleList.indexOf(loginUser.getRole());

		for (int i = 0; i < roleCode; i++) {
			tempList.add(roleList.get(i));
		}

		for (int i = 0; i < tempList.size(); i++) {
			criteria.add(Restrictions.ne("role", tempList.get(i)));
			countCri.add(Restrictions.ne("role", tempList.get(i)));
		}

		if (loginUser.getRole() == Role.管理員) {
			criteria.add(Restrictions.eq("customer", loginUser.getCustomer()));
			countCri.add(Restrictions.eq("customer", loginUser.getCustomer()));
		} else {
			if (ds.getEntity().getCustomer() != null
					&& StringUtils.isNotBlank(ds.getEntity().getCustomer()
							.getName())) {
				criteria.add(Restrictions.ilike("customer.name", ds.getEntity()
						.getCustomer().getName().trim(), MatchMode.ANYWHERE));
				countCri.add(Restrictions.ilike("customer.name", ds.getEntity()
						.getCustomer().getName().trim(), MatchMode.ANYWHERE));

			}
		}

		if (StringUtils.isNotBlank(ds.getEntity().getUserId())) {
			criteria.add(Restrictions.ilike("userId", ds.getEntity()
					.getUserId().trim(), MatchMode.ANYWHERE));
			countCri.add(Restrictions.ilike("userId", ds.getEntity()
					.getUserId().trim(), MatchMode.ANYWHERE));
		}

		if (ds != null && ds.getPager() != null) { // 分頁
			Pager pager = ds.getPager();

			// count total records
			countCri.setProjection(Projections.rowCount());
			Long totalRecord = (Long) countCri.list().get(0);
			log.debug("totalRecord:" + totalRecord);
			pager.setTotalRecord(totalRecord);

			criteria.setFirstResult(pager.getOffset());
			criteria.setMaxResults(pager.getRecordPerPage());
		} else {
			ds = new DataSet<AccountNumber>();
		}

		ds.setResults(criteria.list());

		return ds;
	}

	public void updateCustomer(AccountNumber accountNumber) {
		Map<String, ClassMetadata> map = (Map<String, ClassMetadata>) getSession()
				.getSessionFactory().getAllClassMetadata();

		for (String entityName : map.keySet()) {
			Query query = getSession().createQuery("FROM " + entityName);
			query.setFirstResult(0);
			query.setMaxResults(1);

			if (query.list().toString().contains("customer=")
					&& query.list().toString().contains("accountNumber=")) {
				Query update = getSession().createQuery(
						"UPDATE " + entityName + " SET customer.serNo ="
								+ accountNumber.getCustomer().getSerNo()
								+ " WHERE accountNumber.serNo ="
								+ accountNumber.getSerNo());
				update.executeUpdate();
			}

		}
	}
}
