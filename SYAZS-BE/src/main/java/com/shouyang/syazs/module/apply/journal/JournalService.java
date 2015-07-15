package com.shouyang.syazs.module.apply.journal;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.dao.GenericDao;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.service.GenericServiceFull;
import com.shouyang.syazs.core.util.DsBeanFactory;

@Service
public class JournalService extends GenericServiceFull<Journal> {

	@Autowired
	private JournalDao dao;

	@Override
	public DataSet<Journal> getByRestrictions(DataSet<Journal> ds)
			throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());
		Journal entity = ds.getEntity();
		DsRestrictions restrictions = DsBeanFactory.getDsRestrictions();
		if (entity.getOption().equals("entity.chineseTitle")) {
			if (StringUtils.isNotBlank(entity.getChineseTitle())) {
				restrictions.likeIgnoreCase("chineseTitle",
						entity.getChineseTitle(), MatchMode.ANYWHERE);
			}
		}

		if (entity.getOption().equals("entity.englishTitle")) {
			if (StringUtils.isNotBlank(entity.getEnglishTitle())) {
				restrictions.likeIgnoreCase("englishTitle",
						entity.getEnglishTitle(), MatchMode.ANYWHERE);
			}
		}

		if (entity.getOption().equals("entity.issn")) {
			if (StringUtils.isNotBlank(entity.getIssn())) {
				String[] issnSpilt = entity.getIssn().split("-");
				StringBuilder issn = new StringBuilder("");

				int i = 0;
				while (i < issnSpilt.length) {
					issn.append(issnSpilt[i]);
					i++;
				}

				restrictions.likeIgnoreCase("issn", issn.toString());
			}
		}

		return dao.findByRestrictions(restrictions, ds);
	}

	@Override
	protected GenericDao<Journal> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	public long getJouSerNoByIssn(String issn) throws Exception {
		DsRestrictions restrictions = DsBeanFactory.getDsRestrictions();
		restrictions.eq("issn", issn);

		if (dao.findByRestrictions(restrictions).size() > 0) {
			return dao.findByRestrictions(restrictions).get(0).getSerNo();
		} else {
			return 0;
		}
	}

}
