package com.shouyang.syazs.module.apply.database;

import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.apply.accountNumber.AccountNumber;
import com.shouyang.syazs.core.dao.DsQueryLanguage;
import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.dao.GenericDao;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.service.GenericServiceFull;

@Service
public class DatabaseService extends GenericServiceFull<Database> {

	@Autowired
	private Database entity;

	@Autowired
	private DatabaseDao dao;

	@Override
	public DataSet<Database> getByRestrictions(DataSet<Database> ds)
			throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());
		entity = ds.getEntity();
		DsRestrictions restrictions = getDsRestrictions();

		if (entity.getOption().equals("entity.dbTitle")) {
			if (StringUtils.isNotBlank(entity.getDbTitle())) {
				restrictions.likeIgnoreCase("dbTitle", entity.getDbTitle(),
						MatchMode.ANYWHERE);
			}
		}

		if (entity.getOption().equals("entity.uuIdentifier")) {
			if (StringUtils.isNotBlank(entity.getUuIdentifier())) {
				restrictions.eq("uuIdentifier", entity.getUuIdentifier());
			}
		}

		return dao.findByRestrictions(restrictions, ds);
	}

	@Override
	protected GenericDao<Database> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getSerNosByTitle(String dbTitle) throws Exception {
		if (StringUtils.isNotBlank(dbTitle)) {
			DsQueryLanguage queryLanguage = getDsQueryLanguage();
			queryLanguage
					.setHql("SELECT serNo FROM Database WHERE LOWER(dbTitle) = :dbTitle");
			queryLanguage.addParameter("dbTitle", dbTitle.trim().toLowerCase());
			return (List<Long>) dao.findByHQL(queryLanguage);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllDbTitles() throws Exception {
		DsQueryLanguage queryLanguage = getDsQueryLanguage();
		queryLanguage.setHql("SELECT LOWER(dbTitle) FROM Database");
		return (List<String>) dao.findByHQL(queryLanguage);
	}

	public List<Database> getAllDbs() throws Exception {
		DsRestrictions restrictions = getDsRestrictions();
		return dao.findByRestrictions(restrictions);
	}

	public Database getByUUID(String uuid) throws Exception {
		DsRestrictions restrictions = getDsRestrictions();
		restrictions.eq("uuIdentifier", uuid);

		List<Database> results = dao.findByRestrictions(restrictions);
		if (CollectionUtils.isNotEmpty(results)) {
			return results.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Database save(Database entity, AccountNumber user) throws Exception {
		Assert.notNull(entity);

		String uuid = UUID.randomUUID().toString();
		while (!isUnusedUUID(uuid)) {
			uuid = UUID.randomUUID().toString();
		}

		entity.initInsert(user);
		entity.setUuIdentifier(uuid);

		Database dbEntity = dao.save(entity);
		makeUserInfo(dbEntity);

		return dbEntity;
	}

	public boolean isUnusedUUID(String uuid) throws Exception {
		DsRestrictions restrictions = getDsRestrictions();
		restrictions.eq("uuIdentifier", uuid);
		if (CollectionUtils.isEmpty(dao.findByRestrictions(restrictions))) {
			return true;
		} else {
			return false;
		}
	}
}
