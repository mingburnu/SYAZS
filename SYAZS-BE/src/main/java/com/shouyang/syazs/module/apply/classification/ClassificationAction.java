package com.shouyang.syazs.module.apply.classification;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.web.GenericWebActionSerNo;

@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ClassificationAction extends GenericWebActionSerNo<Classification> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2472341597272275749L;

	@Autowired
	private Classification classification;

	@Autowired
	private ClassificationService classificationService;

	@Override
	protected void validateSave() throws Exception {
		if (StringUtils.isBlank(getEntity().getClassname())) {
			errorMessages.add("名稱必須填寫");
		} else {
			if (hasRepeatLCS(getEntity().getClassname(), getEntity().getSerNo())) {
				errorMessages.add("名稱重複");
			}
		}
	}

	@Override
	protected void validateUpdate() throws Exception {
		if (!hasEntity()) {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		} else {
			if (StringUtils.isBlank(getEntity().getClassname())) {
				errorMessages.add("名稱必須填寫");
			} else {
				if (hasRepeatLCS(getEntity().getClassname(), getEntity()
						.getSerNo())) {
					errorMessages.add("名稱重複");
				}
			}
		}
	}

	@Override
	protected void validateDelete() throws Exception {
		if (!hasEntity()) {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		} else {
			if (classification.getEbooks().size() > 0
					|| classification.getJournals().size() > 0) {
				errorMessages.add("此分類法已在使用, 無法刪除");
			}
		}
	}

	@Override
	public String add() throws Exception {
		return ADD;
	}

	@Override
	public String edit() throws Exception {
		if (hasEntity()) {
			setEntity(classification);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		return EDIT;
	}

	@Override
	public String list() throws Exception {
		DataSet<Classification> ds = initDataSet();
		ds.getPager().setRecordPerPage(Integer.MAX_VALUE);

		classificationService.getByRestrictions(ds);

		if (StringUtils.isBlank(getEntity().getDataStatus())) {
			getEntity().setDataStatus("done");
		}

		return LIST;
	}

	@Override
	public String save() throws Exception {
		validateSave();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			classification = classificationService.save(getEntity(),
					getLoginUser());
			setEntity(classification);
			addActionMessage("新增成功");
			return VIEW;
		} else {
			return ADD;
		}
	}

	@Override
	public String update() throws Exception {
		validateUpdate();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			classification = classificationService.update(getEntity(),
					getLoginUser());
			setEntity(classification);
			addActionMessage("修改成功");
			return VIEW;
		} else {
			return EDIT;
		}
	}

	@Override
	public String delete() throws Exception {
		validateDelete();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			classificationService.deleteBySerNo(getEntity().getSerNo());

			addActionMessage("刪除成功");

			list();
			return LIST;
		} else {
			list();
			return LIST;
		}
	}

	protected boolean hasRepeatLCS(String classname, Long serNo)
			throws Exception {
		List<Long> results = classificationService.getByName(classname);

		if (serNo != null) {
			if (CollectionUtils.isNotEmpty(results)) {
				results.remove(serNo);
				if (results.size() != 0) {
					return true;
				}
			}
		} else {
			if (CollectionUtils.isNotEmpty(results)) {
				return true;
			}
		}

		return false;
	}

	protected boolean hasEntity() throws Exception {
		if (!getEntity().hasSerNo()) {
			return false;
		}

		classification = classificationService.getBySerNo(getEntity()
				.getSerNo());
		if (classification == null) {
			return false;
		}

		return true;
	}
}
