package com.shouyang.syazs.core.security.accountNumber.entity;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shouyang.syazs.core.GenericTest;
import com.shouyang.syazs.core.apply.accountNumber.AccountNumber;
import com.shouyang.syazs.core.apply.accountNumber.AccountNumberDao;
import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.util.DsBeanFactory;

/**
 * SecUserDaoTest
 * 
 * @author Roderick
 * @version 2014/9/29
 */
public class SecUserDaoTest extends GenericTest {

	@Autowired
	private AccountNumberDao dao;

	@Test
	public void testCRUD() throws Exception {

		final String user1Code = "0123";
		final String user2Code = "0456";

		// Save user 1
		AccountNumber user1 = new AccountNumber();
		user1.setUserId(user1Code);
		user1.setUserName("Admin1");
		user1.setcUid(user1.getUserId());
		user1.setuUid(user1.getUserId());

		user1.initInsert(user1);

		AccountNumber dbUser1 = dao.save(user1);
		final Long user1SerNo = dbUser1.getSerNo();
		Assert.assertEquals(user1Code, dbUser1.getUserId());

		// Save user 2
		AccountNumber user2 = new AccountNumber();
		user2.setUserId(user2Code);
		user2.setUserName("Admin2");
		user2.setcUid(user1.getUserId());
		
		user2.setuUid(user1.getUserId());
		user2.initInsert(user2);

		AccountNumber dbUser2 = dao.save(user2);
		final Long user2SerNo = dbUser2.getSerNo();
		Assert.assertEquals(user2Code, dbUser2.getUserId());

		// Query by id
		dbUser1 = dao.findBySerNo(user1SerNo);
		Assert.assertEquals(user1Code, dbUser1.getUserId());

		// update
		final String user1UpdName = "Admin_test";
		dbUser1.setUserName("Admin_test");
		boolean updated = true;
		try {
			dao.update(dbUser1);
		} catch (Exception e) {
			updated = false;
		}
		Assert.assertTrue(updated);

		// query by condition
		DsRestrictions restrictions = DsBeanFactory.getDsRestrictions();
		restrictions.eq("userName", user1UpdName);
		List<AccountNumber> users = dao.findByRestrictions(restrictions);
		Assert.assertEquals(1, users.size());
		Assert.assertEquals(user1UpdName, users.get(0).getUserName());

		// delete by id
		boolean deleted = true;
		try {
			dao.deleteBySerNo(user1SerNo);
			dao.deleteBySerNo(user2SerNo);
		} catch (Exception e) {
			deleted = false;
		}
		Assert.assertTrue(deleted);
	}

}
