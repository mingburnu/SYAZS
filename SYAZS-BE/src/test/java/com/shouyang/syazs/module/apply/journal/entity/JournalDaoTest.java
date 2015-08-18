package com.shouyang.syazs.module.apply.journal.entity;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shouyang.syazs.core.GenericTest;
import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.service.ServiceFactory;
import com.shouyang.syazs.module.apply.journal.Journal;
import com.shouyang.syazs.module.apply.journal.JournalDao;

/**
 * JournalDaoTest
 * 
 * @author Roderick
 * @version 2014/11/28
 */
public class JournalDaoTest extends GenericTest {
	@Autowired
	private JournalDao dao;

	@Test
	public void testCRUD() throws Exception {

		final String date1 = "health";
		final String date2 = "medicine";

		// Save dbJournal1
		Journal journal1 = new Journal();
		journal1.setTitle(date1);

		Journal dbJournal1 = dao.save(journal1);
		final Long resourcesUnionSerNo1 = dbJournal1.getSerNo();
		Assert.assertEquals(date1, dbJournal1.getTitle());

		// Save dbJournal2
		Journal journal2 = new Journal();
		journal2.setTitle(date2);

		Journal dbJournal2 = dao.save(journal2);
		final Long resourcesUnionSerNo2 = dbJournal2.getSerNo();
		Assert.assertEquals(date2, dbJournal2.getTitle());

		// Query by id
		dbJournal1 = dao.findBySerNo(resourcesUnionSerNo1);
		Assert.assertEquals(date1, dbJournal1.getTitle());

		// update
		final String dbJournal1UpdateValue = "Brain";
		dbJournal1.setTitle(dbJournal1UpdateValue);
		boolean updated = true;
		try {
			dao.update(dbJournal1);
		} catch (Exception e) {
			updated = false;
		}
		Assert.assertTrue(updated);

		// query by condition
		DsRestrictions restrictions = ServiceFactory.getDsRestrictions();
		restrictions.eq("englishTitle", dbJournal1UpdateValue);
		List<Journal> journal = dao
				.findByRestrictions(restrictions);
		Assert.assertEquals(1, journal.size());
		Assert.assertEquals(dbJournal1UpdateValue, journal.get(0)
				.getTitle());

		// delete by id
		boolean deleted = true;
		try {
			dao.deleteBySerNo(resourcesUnionSerNo1);
			dao.deleteBySerNo(resourcesUnionSerNo2);
		} catch (Exception e) {
			deleted = false;
		}
		Assert.assertTrue(deleted);
	}
}
