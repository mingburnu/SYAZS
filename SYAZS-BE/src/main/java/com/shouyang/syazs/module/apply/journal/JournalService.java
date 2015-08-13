package com.shouyang.syazs.module.apply.journal;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.dao.GenericDao;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.service.GenericServiceFull;

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
				String issn = entity.getIssn().trim();
				Pattern pattern = Pattern
						.compile("(\\d{4})(\\-{1})(\\d{3})[\\dX]");
				Matcher matcher = pattern.matcher(issn.toUpperCase());
				if (matcher.matches()) {
					issn = issn.replace("-", "");
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
		DsRestrictions restrictions = getDsRestrictions();
		restrictions.eq("issn", issn);

		List<Journal> result = dao.findByRestrictions(restrictions);
		if (result.size() > 0) {
			return result.get(0).getSerNo();
		} else {
			return 0;
		}
	}

	public Journal getJouByIssn(String issn) throws Exception {
		DsRestrictions restrictions = getDsRestrictions();
		restrictions.eq("issn", issn);

		List<Journal> result = dao.findByRestrictions(restrictions);
		if (dao.findByRestrictions(restrictions).size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public boolean isExist(long jouSerNo, long refSerNo) {
		entity = dao.findByJouSerNoRefSeNo(jouSerNo, refSerNo);

		if (entity != null) {
			return true;
		}

		return false;
	}
}
