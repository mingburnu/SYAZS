package com.shouyang.syazs.core.security.accountNumber.service;

import java.util.List;

//




import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shouyang.syazs.core.GenericTest;
import com.shouyang.syazs.core.apply.accountNumber.AccountNumber;
import com.shouyang.syazs.core.apply.accountNumber.AccountNumberService;
import com.shouyang.syazs.core.model.DataSet;

/**
 * SecUserServiceTest
 * 
 * @author David Hsu
 * @version 2014/3/11
 */
public class SecUserServiceTest extends GenericTest {

	@Autowired
	private AccountNumberService service;

	@Autowired
	private DataSet<AccountNumber> ds;

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
		user1.setUserPw("test");

		AccountNumber dbUser1 = service.save(user1, user1);
		final Long user1SerNo = dbUser1.getSerNo();
		Assert.assertEquals(user1Code, dbUser1.getUserId());

		// Save user 2
		AccountNumber user2 = new AccountNumber();
		user2.setUserId(user2Code);
		user2.setUserName("Admin2");
		user2.setcUid(user1.getUserId());
		user2.setuUid(user1.getUserId());
		user2.setUserPw("test");

		AccountNumber dbUser2 = service.save(user2, user1);
		final Long user2SerNo = dbUser2.getSerNo();
		Assert.assertEquals(user2Code, dbUser2.getUserId());

		// Query by id
		dbUser1 = service.getBySerNo(user1SerNo);
		Assert.assertEquals(user1Code, dbUser1.getUserId());

		// update
		final String user1UpdName = "Admin_test";
		dbUser1.setUserName("Admin_test");
		dbUser1 = service.update(dbUser1, user1);
		Assert.assertEquals(user1UpdName, dbUser1.getUserName());

		// query by condition
		AccountNumber queryUser = new AccountNumber();
		queryUser.setUserName("min_te");
		ds.setEntity(queryUser);
		ds = service.getByRestrictions(ds);
		List<AccountNumber> users = ds.getResults();
		Assert.assertEquals(5, users.size());
		Assert.assertEquals("administer", users.get(0).getUserName());

		// delete by id
		boolean deleted = true;
		try {
			service.deleteBySerNo(user1SerNo);
			service.deleteBySerNo(user2SerNo);
		} catch (Exception e) {
			deleted = false;
		}
		Assert.assertTrue(deleted);

		service.makeUserInfo(users.get(0));
	}

}
