package com.shouyang.syazs.core.apply.group;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.shouyang.syazs.core.apply.customer.CustomerService;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.web.GenericWebActionGroup;

@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GroupAction extends GenericWebActionGroup<Group> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3274723922938216387L;

	@Autowired
	private Group group;

	@Autowired
	private GroupService groupService;

	@Autowired
	private List<Group> firstLevelGroups;

	@Autowired
	private List<Group> secondLevelGroups;

	@Autowired
	private List<Group> thirdLevelGroups;

	@Autowired
	private CustomerService customerService;

	@Override
	protected void validateSave() throws Exception {
		if (StringUtils.isBlank(getEntity().getFirstLevelOption())
				|| (!getEntity().getFirstLevelOption().equals("new") && !getEntity()
						.getFirstLevelOption().equals("extend"))) {
			errorMessages.add("請選擇LEVEL 1");
		} else {
			if (getEntity().getFirstLevelOption().equals("new")) {
				if (StringUtils.isBlank(getEntity().getFirstLevelName())) {
					errorMessages.add("請填寫LEVEL 1名稱");
				} else {
					if (groupService.isRepeatMainGroup(getEntity()
							.getFirstLevelName(), getEntity().getCustomer()
							.getSerNo())) {
						errorMessages.add("LEVEL 1名稱重複");
					}
				}
			}

			if (getEntity().getFirstLevelOption().equals("extend")) {
				if (NumberUtils.isDigits(getEntity().getFirstLevelSelect())) {
					long serNo = Long.parseLong(getEntity()
							.getFirstLevelSelect());
					if (groupService.getBySerNo(serNo) == null) {
						errorMessages.add("LEVEL 1流水號不正確");
					} else {
						if (getEntity().getCustomer().getSerNo() != groupService
								.getBySerNo(serNo).getCustomer().getSerNo()) {
							errorMessages.add("LEVEL 1用戶不正確");
						} else {
							if (groupService.getBySerNo(serNo)
									.getGroupMapping().getLevel() != 1) {
								errorMessages.add("LEVEL 1層級不正確");
							}
						}
					}
				} else {
					errorMessages.add("LEVEL 1流水號不正確");
				}
			}
		}

		if (errorMessages.size() == 0) {
			if (getEntity().getFirstLevelOption().equals("new")) {
				if (StringUtils.isNotBlank(getEntity().getSecondLevelOption())) {
					if (!getEntity().getSecondLevelOption().equals("new")) {
						errorMessages.add("LEVEL 2必須新增");
					} else {
						if (StringUtils.isBlank(getEntity()
								.getSecondLevelName())) {
							errorMessages.add("請填寫LEVEL 2名稱");
						}
					}
				}
			}

			if (getEntity().getFirstLevelOption().equals("extend")) {
				if (StringUtils.isBlank(getEntity().getSecondLevelOption())
						|| (!getEntity().getSecondLevelOption().equals("new") && !getEntity()
								.getSecondLevelOption().equals("extend"))) {
					errorMessages.add("請選擇LEVEL 2");
				} else {
					if (getEntity().getSecondLevelOption().equals("new")) {
						if (StringUtils.isBlank(getEntity()
								.getSecondLevelName())) {
							errorMessages.add("請填寫LEVEL 2名稱");
						} else {
							if (groupService.isRepeatSubGroup(getEntity()
									.getSecondLevelName(), getEntity()
									.getCustomer().getSerNo(), groupService
									.getBySerNo(Long.parseLong(getEntity()
											.getFirstLevelSelect())))) {
								errorMessages.add("LEVEL 2名稱重複");
							}
						}
					}

					if (getEntity().getSecondLevelOption().equals("extend")) {
						if (NumberUtils.isDigits(getEntity()
								.getSecondLevelSelect())) {
							long firstSerNo = Long.parseLong(getEntity()
									.getFirstLevelSelect());
							long secondSerNo = Long.parseLong(getEntity()
									.getSecondLevelSelect());
							if (groupService.getBySerNo(secondSerNo) == null) {
								errorMessages.add("LEVEL 2流水號不正確");
							} else {
								if (getEntity().getCustomer().getSerNo() != groupService
										.getBySerNo(secondSerNo).getCustomer()
										.getSerNo()) {
									errorMessages.add("LEVEL 2用戶不正確");
								} else {
									if (groupService.getBySerNo(secondSerNo)
											.getGroupMapping().getLevel() != 2) {
										errorMessages.add("LEVEL 2層級不正確");
									} else {
										if (groupService
												.getBySerNo(secondSerNo)
												.getGroupMapping()
												.getParentGroupMapping()
												.getGroup().getSerNo() != firstSerNo) {
											errorMessages
													.add("LEVEL 2非LEVEL 1子群組");

										}
									}
								}
							}
						} else {
							errorMessages.add("LEVEL 2流水號不正確");
						}
					}
				}
			}
		}

		if (errorMessages.size() == 0) {
			if (StringUtils.isNotBlank(getEntity().getSecondLevelOption())) {
				if (getEntity().getSecondLevelOption().equals("new")) {
					if (getEntity().getThirdLevelOption() != null) {
						if (!getEntity().getThirdLevelOption().equals("new")) {
							errorMessages.add("請選擇LEVEL 3");
						} else {
							if (StringUtils.isBlank(getEntity()
									.getThirdLevelName())) {
								errorMessages.add("請填寫LEVEL 3名稱");
							}
						}
					}
				}

				if (getEntity().getSecondLevelOption().equals("extend")) {
					if (StringUtils.isBlank(getEntity().getThirdLevelOption())
							|| !getEntity().getThirdLevelOption().equals("new")) {
						errorMessages.add("請選擇LEVEL 3");
					} else {
						if (StringUtils
								.isBlank(getEntity().getThirdLevelName())) {
							errorMessages.add("請填寫LEVEL 3名稱");
						} else {
							if (groupService.isRepeatSubGroup(getEntity()
									.getThirdLevelName(), getEntity()
									.getCustomer().getSerNo(), groupService
									.getBySerNo(Long.parseLong(getEntity()
											.getSecondLevelSelect())))) {
								errorMessages.add("LEVEL 3名稱重複");
							}
						}
					}
				}
			}
		}

		if (errorMessages.size() == 0) {
			if (getEntity().getSecondLevelOption() == null
					&& getEntity().getThirdLevelOption() != null) {
				errorMessages.add("新增LEVEL 3必須有LEVEL 2");
			}
		}
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
		if (getEntity() != null
				&& getEntity().getCustomer() != null
				&& getEntity().getCustomer().getSerNo() != null
				&& customerService.getBySerNo(getEntity().getCustomer()
						.getSerNo()) != null) {
			getEntity().setCustomer(
					customerService.getBySerNo(getEntity().getCustomer()
							.getSerNo()));
			setFirstLevelGroups(groupService.getMainGroups(getEntity()
					.getCustomer().getSerNo()));

			boolean isFirstGroupSerNo = StringUtils.isNotEmpty(getEntity()
					.getFirstLevelSelect())
					&& NumberUtils.isDigits(getEntity().getFirstLevelSelect())
					&& Long.parseLong(getEntity().getFirstLevelSelect()) > 0
					&& groupService.getBySerNo(Long.parseLong(getEntity()
							.getFirstLevelSelect())) != null
					&& groupService
							.getBySerNo(
									Long.parseLong(getEntity()
											.getFirstLevelSelect()))
							.getGroupMapping().getLevel() == 1;

			boolean isSecondGroupSerNo = StringUtils.isNotEmpty(getEntity()
					.getSecondLevelSelect())
					&& NumberUtils.isDigits(getEntity().getSecondLevelSelect())
					&& Long.parseLong(getEntity().getSecondLevelSelect()) > 0
					&& groupService.getBySerNo(Long.parseLong(getEntity()
							.getSecondLevelSelect())) != null
					&& groupService
							.getBySerNo(
									Long.parseLong(getEntity()
											.getSecondLevelSelect()))
							.getGroupMapping().getLevel() == 2;

			if (isFirstGroupSerNo) {
				setSecondLevelGroups(groupService.getSubGroups(getEntity()
						.getCustomer().getSerNo(), groupService.getBySerNo(Long
						.parseLong(getEntity().getFirstLevelSelect()))));
			}

			if (isFirstGroupSerNo && isSecondGroupSerNo) {
				long firstSerNo = Long.parseLong(getEntity()
						.getFirstLevelSelect());
				long secondSerNo = Long.parseLong(getEntity()
						.getSecondLevelSelect());
				if (groupService.getBySerNo(secondSerNo).getGroupMapping()
						.getParentGroupMapping().getGroup().getSerNo() == firstSerNo)
					setThirdLevelGroups(groupService.getSubGroups(getEntity()
							.getCustomer().getSerNo(), groupService
							.getBySerNo(Long.parseLong(getEntity()
									.getSecondLevelSelect()))));
			}
		}

		setEntity(getEntity());
		return EDIT;
	}

	@Override
	public String list() throws Exception {
		DataSet<Group> ds = groupService.getByRestrictions(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			ds.getPager().setCurrentPage(
					(int) (ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage() + 1));
			ds = groupService.getByRestrictions(ds);
		}

		setDs(ds);
		return LIST;
	}

	@Override
	public String save() throws Exception {
		validateSave();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			if (getEntity().getFirstLevelOption().equals("new")) {

			}

			return EDIT;
		} else {
			getEntity().setCustomer(
					customerService.getBySerNo(getEntity().getCustomer()
							.getSerNo()));

			setFirstLevelGroups(groupService.getMainGroups(getEntity()
					.getCustomer().getSerNo()));

			boolean isFirstGroupSerNo = StringUtils.isNotEmpty(getEntity()
					.getFirstLevelSelect())
					&& NumberUtils.isDigits(getEntity().getFirstLevelSelect())
					&& Long.parseLong(getEntity().getFirstLevelSelect()) > 0
					&& groupService.getBySerNo(Long.parseLong(getEntity()
							.getFirstLevelSelect())) != null
					&& groupService
							.getBySerNo(
									Long.parseLong(getEntity()
											.getFirstLevelSelect()))
							.getGroupMapping().getLevel() == 1;

			boolean isSecondGroupSerNo = StringUtils.isNotEmpty(getEntity()
					.getSecondLevelSelect())
					&& NumberUtils.isDigits(getEntity().getSecondLevelSelect())
					&& Long.parseLong(getEntity().getSecondLevelSelect()) > 0
					&& groupService.getBySerNo(Long.parseLong(getEntity()
							.getSecondLevelSelect())) != null
					&& groupService
							.getBySerNo(
									Long.parseLong(getEntity()
											.getSecondLevelSelect()))
							.getGroupMapping().getLevel() == 2;

			if (isFirstGroupSerNo) {
				setSecondLevelGroups(groupService.getSubGroups(getEntity()
						.getCustomer().getSerNo(), groupService.getBySerNo(Long
						.parseLong(getEntity().getFirstLevelSelect()))));
			}

			if (isFirstGroupSerNo && isSecondGroupSerNo) {
				long firstSerNo = Long.parseLong(getEntity()
						.getFirstLevelSelect());
				long secondSerNo = Long.parseLong(getEntity()
						.getSecondLevelSelect());
				if (groupService.getBySerNo(secondSerNo).getGroupMapping()
						.getParentGroupMapping().getGroup().getSerNo() == firstSerNo)
					setThirdLevelGroups(groupService.getSubGroups(getEntity()
							.getCustomer().getSerNo(), groupService
							.getBySerNo(Long.parseLong(getEntity()
									.getSecondLevelSelect()))));
			}
			return EDIT;
		}
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

	/**
	 * @return the firstLevelGroups
	 */
	public List<Group> getFirstLevelGroups() {
		return firstLevelGroups;
	}

	/**
	 * @param firstLevelGroups
	 *            the firstLevelGroups to set
	 */
	public void setFirstLevelGroups(List<Group> firstLevelGroups) {
		this.firstLevelGroups = firstLevelGroups;
	}

	/**
	 * @return the secondLevelGroups
	 */
	public List<Group> getSecondLevelGroups() {
		return secondLevelGroups;
	}

	/**
	 * @param secondLevelGroups
	 *            the secondLevelGroups to set
	 */
	public void setSecondLevelGroups(List<Group> secondLevelGroups) {
		this.secondLevelGroups = secondLevelGroups;
	}

	/**
	 * @return the thirdLevelGroups
	 */
	public List<Group> getThirdLevelGroups() {
		return thirdLevelGroups;
	}

	/**
	 * @param thirdLevelGroups
	 *            the thirdLevelGroups to set
	 */
	public void setThirdLevelGroups(List<Group> thirdLevelGroups) {
		this.thirdLevelGroups = thirdLevelGroups;
	}
}
