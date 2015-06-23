package com.shouyang.syazs.module.apply.journal.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shouyang.syazs.core.GenericTest;
import com.shouyang.syazs.core.apply.accountNumber.AccountNumber;
import com.shouyang.syazs.core.apply.accountNumber.AccountNumberService;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.module.apply.journal.Journal;
import com.shouyang.syazs.module.apply.journal.JournalService;


/**
 * JournalServiceTest
 * 
 * @author Roderick
 * @version 2014/10/15
 */
public class JournalServiceTest extends GenericTest {
	@Autowired
	private JournalService service;

	@Autowired
	private AccountNumberService userService;

	@Autowired
	private DataSet<Journal> ds;

	@Test
	public void testCRUD() throws Exception {

		final String date1 = "2011";
		final String date2 = "2054";
		AccountNumber user = userService.getBySerNo(1L);

		// Save journal1
		Journal journal1 = new Journal();
		journal1.setEnglishTitle(date1);

		Journal dbJournal1 = service.save(journal1, user);
		final Long dbJournal1SerNo = dbJournal1.getSerNo();
		Assert.assertEquals(date1, dbJournal1.getEnglishTitle());

		// Save dbJournal2
		Journal journal2 = new Journal();
		journal2.setEnglishTitle(date2);

		Journal dbJournal2 = service.save(journal2, user);
		final Long dbJournal2SerNo = dbJournal2.getSerNo();
		Assert.assertEquals(date2, dbJournal2.getEnglishTitle());

		// Query by id
		dbJournal1 = service.getBySerNo(dbJournal1SerNo);
		Assert.assertEquals(date1, dbJournal1.getEnglishTitle());

		// update
		final String dbJournal1UpdValue = "2087";
		dbJournal1.setEnglishTitle(dbJournal1UpdValue);
		dbJournal1 = service.update(dbJournal1, user);
		Assert.assertEquals(dbJournal1UpdValue,
				dbJournal1.getEnglishTitle());

		// query by condition
		Journal queryJournal = new Journal();
		// queryJournal.setStartDate(dbJournal1UpdValue);
		ds.setEntity(queryJournal);
		ds = service.getByRestrictions(ds);
		List<Journal> journals = ds.getResults();
		Assert.assertEquals(9, journals.size());
		Assert.assertEquals("New England", journals.get(0)
				.getEnglishTitle());

		// delete by id
		boolean deleted = true;
		try {
			service.deleteBySerNo(dbJournal1SerNo);
			service.deleteBySerNo(dbJournal2SerNo);
		} catch (Exception e) {
			deleted = false;
		}
		Assert.assertTrue(deleted);
	}
}
