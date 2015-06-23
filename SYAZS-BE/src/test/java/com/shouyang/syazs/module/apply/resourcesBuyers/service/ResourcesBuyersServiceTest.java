package com.shouyang.syazs.module.apply.resourcesBuyers.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shouyang.syazs.core.GenericTest;
import com.shouyang.syazs.core.apply.accountNumber.AccountNumber;
import com.shouyang.syazs.core.apply.accountNumber.AccountNumberService;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.module.apply.resourcesBuyers.ResourcesBuyers;
import com.shouyang.syazs.module.apply.resourcesBuyers.ResourcesBuyersService;

/**
 * ResourcesBuyersServiceTest
 * 
 * @author Roderick
 * @version 2014/10/15
 */
public class ResourcesBuyersServiceTest extends GenericTest {
	@Autowired
	private ResourcesBuyersService service;

	@Autowired
	private AccountNumberService userService;

	@Autowired
	private DataSet<ResourcesBuyers> ds;

	@Test
	public void testCRUD() throws Exception {

		final String date1 = "2011";
		final String date2 = "2054";
		AccountNumber user = userService.getBySerNo(1L);

		// Save resourcesBuyers1
		ResourcesBuyers resourcesBuyers1 = new ResourcesBuyers();
		resourcesBuyers1.setStartDate(date1);

		ResourcesBuyers dbResourcesBuyers1 = service.save(resourcesBuyers1, user);
		final Long dbResourcesBuyers1SerNo = dbResourcesBuyers1.getSerNo();
		Assert.assertEquals(date1, dbResourcesBuyers1.getStartDate());

		// Save dbResourcesBuyers2
		ResourcesBuyers resourcesBuyers2 = new ResourcesBuyers();
		resourcesBuyers2.setStartDate(date2);

		ResourcesBuyers dbResourcesBuyers2 = service.save(resourcesBuyers2, user);
		final Long dbResourcesBuyers2SerNo = dbResourcesBuyers2.getSerNo();
		Assert.assertEquals(date2, dbResourcesBuyers2.getStartDate());

		// Query by id
		dbResourcesBuyers1 = service.getBySerNo(dbResourcesBuyers1SerNo);
		Assert.assertEquals(date1, dbResourcesBuyers1.getStartDate());

		// update
		final String dbResourcesBuyers1UpdValue = "2087";
		dbResourcesBuyers1.setStartDate(dbResourcesBuyers1UpdValue);
		dbResourcesBuyers1 = service.update(dbResourcesBuyers1, user);
		Assert.assertEquals(dbResourcesBuyers1UpdValue,
				dbResourcesBuyers1.getStartDate());

		// query by condition
		ResourcesBuyers queryResourcesBuyers = new ResourcesBuyers();
		// queryResourcesBuyers.setStartDate(dbResourcesBuyers1UpdValue);
		ds.setEntity(queryResourcesBuyers);
		ds = service.getByRestrictions(ds);
		List<ResourcesBuyers> resourcesBuyerss = ds.getResults();
		Assert.assertEquals(5, resourcesBuyerss.size());
		Assert.assertEquals("1980", resourcesBuyerss.get(0)
				.getStartDate());

		// delete by id
		boolean deleted = true;
		try {
			service.deleteBySerNo(dbResourcesBuyers1SerNo);
			service.deleteBySerNo(dbResourcesBuyers2SerNo);
		} catch (Exception e) {
			deleted = false;
		}
		Assert.assertTrue(deleted);
	}
}
