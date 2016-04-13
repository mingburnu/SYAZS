package com.shouyang.syazs.core.apply.customer;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.shouyang.syazs.core.web.GenericWebActionFull;

@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CustomerAction extends GenericWebActionFull<Customer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4530353636126561614L;

	@Autowired
	private Customer customer;

	@Autowired
	private CustomerService customerService;

	@Override
	protected void validateSave() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validateUpdate() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validateDelete() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String add() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String edit() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String show() throws Exception {
		if (hasEntity()) {
			if (StringUtils.isNotBlank(customer.getLogo())) {
				FileInputStream fis = new FileInputStream(customer.getLogo());
				String mime = getFileMime(new File(customer.getLogo()), "pic");
				getEntity().setDataStatus(mime);
				getEntity().setInputStream(fis);
			} else {
				getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		return PIC;
	}

	protected boolean hasEntity() throws Exception {
		if (!getEntity().hasSerNo()) {
			return false;
		}

		customer = customerService.getBySerNo(getEntity().getSerNo());
		if (customer == null) {
			return false;
		}

		return true;
	}
}
