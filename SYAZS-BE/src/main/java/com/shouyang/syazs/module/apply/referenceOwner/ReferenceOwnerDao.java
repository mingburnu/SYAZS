package com.shouyang.syazs.module.apply.referenceOwner;

import java.util.HashSet;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.dao.ModuleDaoFull;
import com.shouyang.syazs.module.apply.database.Database;
import com.shouyang.syazs.module.apply.ebook.Ebook;
import com.shouyang.syazs.module.apply.journal.Journal;

@Repository
public class ReferenceOwnerDao extends ModuleDaoFull<ReferenceOwner> {

	@Autowired
	private ReferenceOwner referenceOwner;

	@Autowired
	private Database database;

	@Autowired
	private Ebook ebook;

	@Autowired
	private Journal journal;

	@Override
	public void deleteBySerNo(Long serNo) throws Exception {
		Assert.notNull(serNo);
		ReferenceOwner t = findBySerNo(serNo);
		Iterator<Database> databases = t.getDatabases().iterator();
		Iterator<Ebook> ebooks = t.getEbooks().iterator();
		Iterator<Journal> journals = t.getJournals().iterator();

		while (databases.hasNext()) {
			database = databases.next();
			if (database.getReferenceOwners().size() == 1) {
				getSession().delete(database);
			} else {
				Iterator<ReferenceOwner> rSet = database.getReferenceOwners()
						.iterator();
				database.setReferenceOwners(new HashSet<ReferenceOwner>());
				while (rSet.hasNext()) {
					referenceOwner = rSet.next();
					if (referenceOwner.getSerNo() != t.getSerNo()) {
						database.getReferenceOwners().add(referenceOwner);
					}
				}
			}
		}

		while (ebooks.hasNext()) {
			ebook = ebooks.next();
			if (ebook.getReferenceOwners().size() == 1) {
				getSession().delete(ebook);
			} else {
				Iterator<ReferenceOwner> rSet = ebook.getReferenceOwners()
						.iterator();
				ebook.setReferenceOwners(new HashSet<ReferenceOwner>());
				while (rSet.hasNext()) {
					referenceOwner = rSet.next();
					if (referenceOwner.getSerNo() != t.getSerNo()) {
						ebook.getReferenceOwners().add(referenceOwner);
					}
				}
			}
		}

		while (journals.hasNext()) {
			journal = journals.next();
			if (journal.getReferenceOwners().size() == 1) {
				getSession().delete(journal);
			} else {
				Iterator<ReferenceOwner> rSet = journal.getReferenceOwners()
						.iterator();
				journal.setReferenceOwners(new HashSet<ReferenceOwner>());
				while (rSet.hasNext()) {
					referenceOwner = rSet.next();
					if (referenceOwner.getSerNo() != t.getSerNo()) {
						journal.getReferenceOwners().add(referenceOwner);
					}
				}
			}
		}

		getSession().delete(t);
	}
}
