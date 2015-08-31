package com.shouyang.syazs.module.apply.referenceOwner;

import java.util.Map;

import org.hibernate.Query;
import org.hibernate.QueryException;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.stereotype.Repository;

import com.shouyang.syazs.core.dao.ModuleDaoFull;

@Repository
public class ReferenceOwnerDao extends ModuleDaoFull<ReferenceOwner> {

	public boolean delRelatedObj(long refSerNo) {

		Map<String, ClassMetadata> map = (Map<String, ClassMetadata>) getSession()
				.getSessionFactory().getAllClassMetadata();

		for (String entityName : map.keySet()) {
			Query resourceQuery = getSession().createQuery(
					"SELECT COUNT(*) FROM " + entityName
							+ " WHERE serNo=?");

			if ((Long) resourceQuery.setLong(0, refSerNo).list().get(0) > 0) {
				return false;
			}
		}

		checkFK(false);

		for (String entityName : map.keySet()) {
			if (!entityName.startsWith("com.shouyang.syazs.module")) {
				continue;
			}

			Query query = getSession().createQuery("FROM " + entityName);
			query.setFirstResult(0);
			query.setMaxResults(1);
			log.info(query.list().toString());

			try {
				Query delQuery = getSession()
						.createQuery(
								"DELETE FROM " + entityName
										+ " WHERE referenceOwner.serNo=?");
				delQuery.setLong(0, refSerNo).executeUpdate();
			} catch (QueryException e) {
				continue;
			}
		}

		checkFK(false);
		return true;
	}
}
