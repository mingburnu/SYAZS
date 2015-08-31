package com.shouyang.syazs.module.apply.database;

import java.util.List;
import java.util.UUID;

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

		return dao.findByRestrictions(restrictions, ds);
	}

	@Override
	protected GenericDao<Database> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getDbSerNosByTitle(String dbTitle) throws Exception {
		if (StringUtils.isNotBlank(dbTitle)) {
			DsQueryLanguage queryLanguage = getDsQueryLanguage();
			queryLanguage
					.setSql("SELECT serNo FROM Database WHERE LOWER(dbTitle) = :dbTitle");
			queryLanguage.addParameter("dbTitle", dbTitle.trim().toLowerCase());
			return (List<Long>) dao.findByQL(queryLanguage);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllDbTitles() throws Exception {
		DsQueryLanguage queryLanguage = getDsQueryLanguage();
		queryLanguage.setSql("SELECT LOWER(dbTitle) FROM Database");
		return (List<String>) dao.findByQL(queryLanguage);
	}

	public List<Database> getAllDbs() throws Exception {
		DsRestrictions restrictions = getDsRestrictions();
		return dao.findByRestrictions(restrictions);
	}

	@Override
	public Database save(Database entity, AccountNumber user) throws Exception {
		Assert.notNull(entity);

		entity.initInsert(user);
		entity.setUuIdentifier(UUID.randomUUID().toString());

		Database dbEntity = dao.save(entity);
		makeUserInfo(dbEntity);

		return dbEntity;
	}
}
