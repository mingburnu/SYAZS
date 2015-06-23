package com.shouyang.syazs.module.apply.feLogs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shouyang.syazs.core.dao.GenericDaoLog;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.service.GenericServiceLog;

@Service
public class FeLogsService extends GenericServiceLog<FeLogs> {

	@Autowired
	private FeLogsDao dao;

	@Override
	public DataSet<FeLogs> getByRestrictions(DataSet<FeLogs> ds)
			throws Exception {
		return dao.getRanks(ds);
	}

	@Override
	protected GenericDaoLog<FeLogs> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

}
