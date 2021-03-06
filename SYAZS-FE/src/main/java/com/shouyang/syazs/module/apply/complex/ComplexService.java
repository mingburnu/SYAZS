package com.shouyang.syazs.module.apply.complex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shouyang.syazs.core.model.DataSet;

@Service
public class ComplexService {

	@Autowired
	private ComplexDao dao;

	public DataSet<Complex> getByRestrictions(
			DataSet<Complex> ds) throws Exception {
		return dao.query(ds);
	}

	protected ComplexDao getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

}
