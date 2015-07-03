package com.shouyang.syazs.core.apply.groupMapping;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.shouyang.syazs.core.dao.ModuleDaoSerNo;

@Repository
public class GroupMappingDao extends ModuleDaoSerNo<GroupMapping> {

	public void deleteByTitle(String title) {
		checkFK(false);

		Query delQuery = getSession().createQuery(
				"DELETE FROM GroupMapping WHERE title=?");
		delQuery.setString(0, title).executeUpdate();

		checkFK(true);
	}
}
