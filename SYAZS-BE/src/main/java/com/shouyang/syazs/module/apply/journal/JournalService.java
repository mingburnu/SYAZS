package com.shouyang.syazs.module.apply.journal;

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
import com.shouyang.syazs.module.apply.database.Database;

@Service
public class JournalService extends GenericServiceFull<Journal> {

	@Autowired
	private Journal entity;

	@Autowired
	private JournalDao dao;

	@Override
	public DataSet<Journal> getByRestrictions(DataSet<Journal> ds)
			throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());
		Journal entity = ds.getEntity();
		DsRestrictions restrictions = getDsRestrictions();
		if (entity.getOption().equals("entity.title")) {
			if (StringUtils.isNotBlank(entity.getTitle())) {
				restrictions.likeIgnoreCase("title", entity.getTitle(),
						MatchMode.ANYWHERE);
			}
		}

		if (entity.getOption().equals("entity.issn")) {
			if (StringUtils.isNotBlank(entity.getIssn())) {
				String issn = entity.getIssn().trim().replace("-", "");

				restrictions.likeIgnoreCase("issn", issn);
			}
		}

		return dao.findByRestrictions(restrictions, ds);
	}

	@Override
	protected GenericDao<Journal> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getSerNosByIssn(String issn) throws Exception {
		DsQueryLanguage queryLanguage = getDsQueryLanguage();
		queryLanguage.setHql("SELECT serNo FROM Journal WHERE issn = :issn");
		queryLanguage.addParameter("issn", issn);
		return (List<Long>) dao.findByHQL(queryLanguage);
	}

	@SuppressWarnings("unchecked")
	public List<Long> getSerNosInDbByIssn(String issn, Database database)
			throws Exception {
		DsQueryLanguage queryLanguage = getDsQueryLanguage();
		queryLanguage
				.setHql("SELECT serNo FROM Journal WHERE issn = :issn AND database.serNo = :datSerNo");
		queryLanguage.addParameter("issn", issn);
		queryLanguage.addParameter("datSerNo", database.getSerNo());
		return (List<Long>) dao.findByHQL(queryLanguage);
	}

	@SuppressWarnings("unchecked")
	public List<Long> getSerNosByTitle(String title) throws Exception {
		DsQueryLanguage queryLanguage = getDsQueryLanguage();
		queryLanguage
				.setHql("SELECT serNo FROM Journal WHERE lower(title) = :title AND (issn = null OR issn ='')");
		queryLanguage.addParameter("title", title.toLowerCase());
		return (List<Long>) dao.findByHQL(queryLanguage);
	}

	@SuppressWarnings("unchecked")
	public List<Long> getSerNosInDbByTitle(String title, Database database)
			throws Exception {
		DsQueryLanguage queryLanguage = getDsQueryLanguage();
		queryLanguage
				.setHql("SELECT serNo FROM Journal WHERE lower(title) = :title AND database.serNo = :datSerNo AND (issn = null OR issn ='')");
		queryLanguage.addParameter("title", title.toLowerCase());
		queryLanguage.addParameter("datSerNo", database.getSerNo());
		return (List<Long>) dao.findByHQL(queryLanguage);
	}

	@Override
	public Journal save(Journal entity, AccountNumber user) throws Exception {
		Assert.notNull(entity);

		String uuid = UUID.randomUUID().toString();
		while (!isUnusedUUID(uuid)) {
			uuid = UUID.randomUUID().toString();
		}

		entity.initInsert(user);
		entity.setUuIdentifier(uuid);

		Journal dbEntity = dao.save(entity);
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
