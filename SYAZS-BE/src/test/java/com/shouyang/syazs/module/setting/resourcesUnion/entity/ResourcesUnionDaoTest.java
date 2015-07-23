package com.shouyang.syazs.module.setting.resourcesUnion.entity;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shouyang.syazs.core.GenericTest;
import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.service.ServiceFactory;
import com.shouyang.syazs.module.apply.resourcesUnion.ResourcesUnion;
import com.shouyang.syazs.module.apply.resourcesUnion.ResourcesUnionDao;

/**
 * ResourcesUnionDaoTest
 * 
 * @author Roderick
 * @version 2014/10/15
 */
public class ResourcesUnionDaoTest extends GenericTest {
	@Autowired
	private ResourcesUnionDao dao;

	@Test
	public void testCRUD() throws Exception {

		final Customer customer1 = new Customer();
		final Customer customer2 = new Customer();

		customer1.setSerNo(123L);
		customer1.setSerNo(456L);

		// Save dbResourcesUnion1
		ResourcesUnion resourcesUnion1 = new ResourcesUnion();
		resourcesUnion1.setCustomer(customer1);

		ResourcesUnion dbResourcesUnion1 = dao.save(resourcesUnion1);
		final Long resourcesUnionSerNo1 = dbResourcesUnion1.getSerNo();
		Assert.assertEquals(customer1, dbResourcesUnion1.getCustomer());

		// Save dbResourcesUnion2
		ResourcesUnion resourcesUnion2 = new ResourcesUnion();
		resourcesUnion2.setCustomer(customer2);

		ResourcesUnion dbResourcesUnion2 = dao.save(resourcesUnion2);
		final Long resourcesUnionSerNo2 = dbResourcesUnion2.getSerNo();
		Assert.assertEquals(customer2, dbResourcesUnion2.getCustomer());

		// Query by id
		dbResourcesUnion1 = dao.findBySerNo(resourcesUnionSerNo1);
		Assert.assertEquals(customer1, dbResourcesUnion1.getCustomer());

		// update
		final Customer dbResourcesUnion1UpdCus = new Customer();
		dbResourcesUnion1UpdCus.setSerNo(789L);
		dbResourcesUnion1.setCustomer(dbResourcesUnion1UpdCus);
		boolean updated = true;
		try {
			dao.update(dbResourcesUnion1);
		} catch (Exception e) {
			updated = false;
		}
		Assert.assertTrue(updated);

		// query by condition
		DsRestrictions restrictions = ServiceFactory.getDsRestrictions();
		restrictions.eq("customer.serNo", dbResourcesUnion1UpdCus);
		List<ResourcesUnion> resourcesUnions = dao
				.findByRestrictions(restrictions);
		Assert.assertEquals(1, resourcesUnions.size());
		Assert.assertEquals(dbResourcesUnion1UpdCus, resourcesUnions.get(0)
				.getCustomer());

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
