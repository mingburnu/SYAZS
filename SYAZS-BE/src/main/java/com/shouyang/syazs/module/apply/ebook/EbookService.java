package com.shouyang.syazs.module.apply.ebook;

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
public class EbookService extends GenericServiceFull<Ebook> {

	@Autowired
	private Ebook entity;

	@Autowired
	private EbookDao dao;

	@Override
	public DataSet<Ebook> getByRestrictions(DataSet<Ebook> ds) throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());
		Ebook entity = ds.getEntity();
		DsRestrictions restrictions = getDsRestrictions();

		if (entity.getOption().equals("entity.bookName")) {
			if (StringUtils.isNotBlank(entity.getBookName())) {
				restrictions.likeIgnoreCase("bookName", entity.getBookName(),
						MatchMode.ANYWHERE);
			}
		}

		if (entity.getOption().equals("entity.isbn")) {
			if (entity.getIsbn() != null) {
				restrictions.eq("isbn", entity.getIsbn());
			}
		}

		return dao.findByRestrictions(restrictions, ds);
	}

	@Override
	protected GenericDao<Ebook> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getSerNosByIsbn(long isbn) throws Exception {
		DsQueryLanguage queryLanguage = getDsQueryLanguage();
		queryLanguage.setSql("SELECT serNo FROM Ebook WHERE isbn = :isbn");
		queryLanguage.addParameter("isbn", isbn);
		return (List<Long>) dao.findByHQL(queryLanguage);
	}

	@SuppressWarnings("unchecked")
	public List<Long> getSerNosInDbByIsbn(long isbn, Database database)
			throws Exception {
		DsQueryLanguage queryLanguage = getDsQueryLanguage();
		queryLanguage
				.setSql("SELECT serNo FROM Ebook WHERE isbn = :isbn AND database.serNo = :datSerNo");
		queryLanguage.addParameter("isbn", isbn);
		queryLanguage.addParameter("datSerNo", database.getSerNo());
		return (List<Long>) dao.findByHQL(queryLanguage);
	}

	@SuppressWarnings("unchecked")
	public List<Long> getSerNosByName(String bookName) throws Exception {
		DsQueryLanguage queryLanguage = getDsQueryLanguage();
		queryLanguage
				.setSql("SELECT serNo FROM Ebook WHERE lower(bookName) = :bookName AND isbn = null");
		queryLanguage.addParameter("bookName", bookName.toLowerCase());
		return (List<Long>) dao.findByHQL(queryLanguage);
	}

	@SuppressWarnings("unchecked")
	public List<Long> getSerNosInDbByName(String bookName, Database database)
			throws Exception {
		DsQueryLanguage queryLanguage = getDsQueryLanguage();
		queryLanguage
				.setSql("SELECT serNo FROM Ebook WHERE lower(bookName) = :bookName AND database.serNo = :datSerNo AND isbn = null");
		queryLanguage.addParameter("bookName", bookName.toLowerCase());
		queryLanguage.addParameter("datSerNo", database.getSerNo());
		return (List<Long>) dao.findByHQL(queryLanguage);
	}

	@Override
	public Ebook save(Ebook entity, AccountNumber user) throws Exception {
		Assert.notNull(entity);

		String uuid = UUID.randomUUID().toString();
		while (!isUnusedUUID(uuid)) {
			uuid = UUID.randomUUID().toString();
		}

		entity.initInsert(user);
		entity.setUuIdentifier(uuid);

		Ebook dbEntity = dao.save(entity);
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
