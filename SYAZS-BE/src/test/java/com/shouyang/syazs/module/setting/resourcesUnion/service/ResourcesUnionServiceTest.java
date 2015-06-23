package com.shouyang.syazs.module.setting.resourcesUnion.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shouyang.syazs.core.GenericTest;
import com.shouyang.syazs.core.apply.accountNumber.AccountNumber;
import com.shouyang.syazs.core.apply.accountNumber.AccountNumberService;
import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.module.apply.resourcesBuyers.ResourcesBuyers;
import com.shouyang.syazs.module.apply.resourcesUnion.ResourcesUnion;
import com.shouyang.syazs.module.apply.resourcesUnion.ResourcesUnionService;

/**
 * ResourcesUnionServiceTest
 * 
 * @author Roderick
 * @version 2014/10/15
 */
public class ResourcesUnionServiceTest extends GenericTest {
	@Autowired
	private ResourcesUnionService service;

	@Autowired
	private AccountNumberService userService;

	@Autowired
	private DataSet<ResourcesUnion> ds;

	@Test
	public void testCRUD() throws Exception {

		final Customer customer1 = new Customer();
		final Customer customer2 = new Customer();
		customer1.setSerNo(123L);
		AccountNumber user = userService.getBySerNo(1L);

		// Save resourcesUnion1
		ResourcesUnion resourcesUnion1 = new ResourcesUnion();
		ResourcesBuyers resourcesBuyers =new ResourcesBuyers();
		resourcesBuyers.setSerNo(1L);
		resourcesUnion1.setCustomer(customer1);
		resourcesUnion1.setDatSerNo(1L);
		resourcesUnion1.setEbkSerNo(1L);
		resourcesUnion1.setJouSerNo(1L);
		resourcesUnion1.setResourcesBuyers(resourcesBuyers);

		ResourcesUnion dbResourcesUnion1 = service.save(resourcesUnion1, user);
		final Long dbResourcesUnion1SerNo = dbResourcesUnion1.getSerNo();
		Assert.assertEquals(customer1, dbResourcesUnion1.getCustomer());

		// Save dbResourcesUnion2
		ResourcesUnion resourcesUnion2 = new ResourcesUnion();
		resourcesUnion2.setCustomer(customer2);
		resourcesUnion2.setDatSerNo(1L);
		resourcesUnion2.setEbkSerNo(1L);
		resourcesUnion2.setJouSerNo(1L);
		resourcesUnion2.setResourcesBuyers(resourcesBuyers);

		ResourcesUnion dbResourcesUnion2 = service.save(resourcesUnion2, user);
		final Long dbResourcesUnion2SerNo = dbResourcesUnion2.getSerNo();
		Assert.assertEquals(customer2, dbResourcesUnion2.getCustomer());

		// Query by id
		dbResourcesUnion1 = service.getBySerNo(dbResourcesUnion1SerNo);
		Assert.assertEquals(customer1, dbResourcesUnion1.getCustomer());

		// update
		final Customer dbResourcesUnion1UpdCus = new Customer();
		dbResourcesUnion1UpdCus.setSerNo(789L);
		dbResourcesUnion1.setCustomer(dbResourcesUnion1UpdCus);
		dbResourcesUnion1 = service.update(dbResourcesUnion1, user);
		Assert.assertEquals(dbResourcesUnion1UpdCus,
				dbResourcesUnion1.getCustomer());

		// query by condition
		ResourcesUnion queryResourcesUnion = new ResourcesUnion();
		// queryResourcesUnion.setCusSerNo(dbResourcesUnion1UpdNum);
		ds.setEntity(queryResourcesUnion);
		ds = service.getByRestrictions(ds);
		List<ResourcesUnion> resourcesUnions = ds.getResults();
		Assert.assertEquals(33, resourcesUnions.size());

		Long assertNum = 3L;
		Assert.assertEquals(assertNum, resourcesUnions.get(0).getCustomer().getSerNo());

		// delete by id
		boolean deleted = true;
		try {
			service.deleteBySerNo(dbResourcesUnion1SerNo);
			service.deleteBySerNo(dbResourcesUnion2SerNo);
		} catch (Exception e) {
			deleted = false;
		}
		Assert.assertTrue(deleted);
	}
}
