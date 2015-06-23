package com.shouyang.syazs.module.apply.resourcesBuyers.entity;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shouyang.syazs.core.GenericTest;
import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.util.DsBeanFactory;
import com.shouyang.syazs.module.apply.resourcesBuyers.ResourcesBuyers;
import com.shouyang.syazs.module.apply.resourcesBuyers.ResourcesBuyersDao;

/**
 * ResourcesBuyersDaoTest
 * 
 * @author Roderick
 * @version 2014/11/28
 */
public class ResourcesBuyersDaoTest extends GenericTest {
	@Autowired
	private ResourcesBuyersDao dao;

	@Test
	public void testCRUD() throws Exception {

		final String date1 = "1985-02";
		final String date2 = "2200-02";

		// Save dbResourcesBuyers1
		ResourcesBuyers resourcesBuyers1 = new ResourcesBuyers();
		resourcesBuyers1.setStartDate(date1);

		ResourcesBuyers dbResourcesBuyers1 = dao.save(resourcesBuyers1);
		final Long resourcesUnionSerNo1 = dbResourcesBuyers1.getSerNo();
		Assert.assertEquals(date1, dbResourcesBuyers1.getStartDate());

		// Save dbResourcesBuyers2
		ResourcesBuyers resourcesBuyers2 = new ResourcesBuyers();
		resourcesBuyers2.setStartDate(date2);

		ResourcesBuyers dbResourcesBuyers2 = dao.save(resourcesBuyers2);
		final Long resourcesUnionSerNo2 = dbResourcesBuyers2.getSerNo();
		Assert.assertEquals(date2, dbResourcesBuyers2.getStartDate());

		// Query by id
		dbResourcesBuyers1 = dao.findBySerNo(resourcesUnionSerNo1);
		Assert.assertEquals(date1, dbResourcesBuyers1.getStartDate());

		// update
		final String dbResourcesBuyers1UpdateValue = "2300-02";
		dbResourcesBuyers1.setStartDate(dbResourcesBuyers1UpdateValue);
		boolean updated = true;
		try {
			dao.update(dbResourcesBuyers1);
		} catch (Exception e) {
			updated = false;
		}
		Assert.assertTrue(updated);

		// query by condition
		DsRestrictions restrictions = DsBeanFactory.getDsRestrictions();
		restrictions.eq("startDate", dbResourcesBuyers1UpdateValue);
		List<ResourcesBuyers> resourcesBuyers = dao
				.findByRestrictions(restrictions);
		Assert.assertEquals(1, resourcesBuyers.size());
		Assert.assertEquals(dbResourcesBuyers1UpdateValue, resourcesBuyers.get(0)
				.getStartDate());

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
