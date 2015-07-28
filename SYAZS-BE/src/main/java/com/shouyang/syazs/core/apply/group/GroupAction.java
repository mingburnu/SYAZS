package com.shouyang.syazs.core.apply.group;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.apply.customer.CustomerService;
import com.shouyang.syazs.core.apply.groupMapping.GroupMapping;
import com.shouyang.syazs.core.apply.groupMapping.GroupMappingService;
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
	private GroupMapping groupMapping;

	@Autowired
	private GroupMappingService groupMappingService;

	@Autowired
	private List<Group> firstLevelGroups;

	@Autowired
	private List<Group> secondLevelGroups;

	@Autowired
	private List<Group> thirdLevelGroups;

	@Autowired
	Customer customer;

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
				if (getEntity().getFirstLevelSelect() != null) {
					long serNo = getEntity().getFirstLevelSelect();
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
									.getBySerNo(getEntity()
											.getFirstLevelSelect()))) {
								errorMessages.add("LEVEL 2名稱重複");
							}
						}
					}

					if (getEntity().getSecondLevelOption().equals("extend")) {
						if (getEntity().getSecondLevelSelect() != null) {
							long firstSerNo = getEntity().getFirstLevelSelect();
							long secondSerNo = getEntity()
									.getSecondLevelSelect();
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
									.getBySerNo(getEntity()
											.getSecondLevelSelect()))) {
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
		group = groupService.getTargetEntity(initDataSet());
		if (group == null) {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		} else {
			if (group.getGroupMapping().getLevel() == 0) {
				getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
			} else {
				if (group.getGroupMapping().getLevel() == 1) {
					if (StringUtils.isBlank(getEntity().getFirstLevelName())) {
						errorMessages.add("請填寫LEVEL 1名稱");
					} else {
						if (!group.getGroupName().equals(
								getEntity().getFirstLevelName().trim())) {

							if (groupService.isRepeatMainGroup(getEntity()
									.getFirstLevelName(), getEntity()
									.getCustomer().getSerNo())) {
								errorMessages.add("LEVEL 1名稱重複");
							}
						}
					}
				}

				if (group.getGroupMapping().getLevel() == 2) {
					if (StringUtils.isBlank(getEntity().getSecondLevelName())) {
						errorMessages.add("請填寫LEVEL 2名稱");
					} else {
						if (!group.getGroupName().equals(
								getEntity().getSecondLevelName().trim())) {
							if (groupService.isRepeatSubGroup(getEntity()
									.getSecondLevelName(), getEntity()
									.getCustomer().getSerNo(), groupService
									.getBySerNo(getEntity()
											.getFirstLevelSelect()))) {
								errorMessages.add("LEVEL 2名稱重複");
							}
						}
					}
				}

				if (group.getGroupMapping().getLevel() == 3) {
					if (StringUtils.isBlank(getEntity().getThirdLevelName())) {
						errorMessages.add("請填寫LEVEL 3名稱");
					} else {
						if (!group.getGroupName().equals(
								getEntity().getThirdLevelName().trim())) {
							if (groupService.isRepeatSubGroup(getEntity()
									.getThirdLevelName(), getEntity()
									.getCustomer().getSerNo(), groupService
									.getBySerNo(getEntity()
											.getSecondLevelSelect()))) {
								errorMessages.add("LEVEL 3名稱重複");
							}
						}
					}
				}
			}
		}
	}

	@Override
	protected void validateDelete() throws Exception {
		group = groupService.getTargetEntity(initDataSet());

		if (group == null) {
			errorMessages.add("沒有這個物件");
		} else {
			if (group.getGroupMapping().getLevel() == 0) {
				errorMessages.add("Can't delete Root Group");
			}
		}
	}

	@Override
	public String add() throws Exception {
		if (getEntity().getCustomer().getSerNo() != null
				&& customerService.getBySerNo(getEntity().getCustomer()
						.getSerNo()) != null) {
			getEntity().setCustomer(
					customerService.getBySerNo(getEntity().getCustomer()
							.getSerNo()));
			setFirstLevelGroups(groupService.getSubGroups(getEntity()
					.getCustomer().getSerNo(), groupService
					.getRootGroup(getEntity().getCustomer().getSerNo())));

			boolean isFirstGroupSerNo = getEntity().getFirstLevelSelect() != null
					&& getEntity().getFirstLevelSelect() > 0
					&& groupService.getBySerNo(getEntity()
							.getFirstLevelSelect()) != null
					&& groupService
							.getBySerNo(getEntity().getFirstLevelSelect())
							.getGroupMapping().getLevel() == 1;

			boolean isSecondGroupSerNo = getEntity().getSecondLevelSelect() != null
					&& getEntity().getSecondLevelSelect() > 0
					&& groupService.getBySerNo(getEntity()
							.getSecondLevelSelect()) != null
					&& groupService
							.getBySerNo(getEntity().getSecondLevelSelect())
							.getGroupMapping().getLevel() == 2;

			if (isFirstGroupSerNo) {
				setSecondLevelGroups(groupService.getSubGroups(getEntity()
						.getCustomer().getSerNo(), groupService
						.getBySerNo(getEntity().getFirstLevelSelect())));
			}

			if (isFirstGroupSerNo && isSecondGroupSerNo) {
				long firstSerNo = getEntity().getFirstLevelSelect();
				long secondSerNo = getEntity().getSecondLevelSelect();
				if (groupService.getBySerNo(secondSerNo).getGroupMapping()
						.getParentGroupMapping().getGroup().getSerNo() == firstSerNo)
					setThirdLevelGroups(groupService.getSubGroups(getEntity()
							.getCustomer().getSerNo(), groupService
							.getBySerNo(getEntity().getSecondLevelSelect())));
			}
		}

		setEntity(getEntity());
		return ADD;
	}

	@Override
	public String edit() throws Exception {
		group = groupService.getTargetEntity(initDataSet());
		if (group != null) {
			if (group.getGroupMapping().getLevel() == 3) {
				setSecondLevelGroups(groupService.getSubGroups(getEntity()
						.getCustomer().getSerNo(), group.getGroupMapping()
						.getParentGroupMapping().getParentGroupMapping()
						.getGroup()));

				setThirdLevelGroups(groupService.getSubGroups(getEntity()
						.getCustomer().getSerNo(), group.getGroupMapping()
						.getParentGroupMapping().getGroup()));

				group.setFirstLevelOption("extend");
				group.setFirstLevelSelect(group.getGroupMapping()
						.getParentGroupMapping().getParentGroupMapping()
						.getGroup().getSerNo());

				group.setSecondLevelOption("extend");
				group.setSecondLevelSelect(group.getGroupMapping()
						.getParentGroupMapping().getGroup().getSerNo());

				group.setThirdLevelOption("modify");
				group.setThirdLevelName(group.getGroupName());

				if (getEntity().getThirdLevelName() != null) {
					group.setThirdLevelName(getEntity().getThirdLevelName());
				}
			}

			if (group.getGroupMapping().getLevel() == 2) {
				setSecondLevelGroups(groupService.getSubGroups(getEntity()
						.getCustomer().getSerNo(), group.getGroupMapping()
						.getParentGroupMapping().getGroup()));
				group.setFirstLevelOption("extend");
				group.setFirstLevelSelect(group.getGroupMapping()
						.getParentGroupMapping().getGroup().getSerNo());

				group.setSecondLevelOption("modify");
				group.setSecondLevelName(group.getGroupName());

				if (getEntity().getSecondLevelName() != null) {
					group.setSecondLevelName(getEntity().getSecondLevelName());
				}
			}

			if (group.getGroupMapping().getLevel() == 1) {
				group.setFirstLevelOption("modify");
				group.setFirstLevelName(group.getGroupName());

				if (getEntity().getFirstLevelName() != null) {
					group.setFirstLevelName(getEntity().getFirstLevelName());
				}
			}

			if (getEntity().getCustomer().getSerNo() != null
					&& customerService.getBySerNo(getEntity().getCustomer()
							.getSerNo()) != null) {
				getEntity().setCustomer(
						customerService.getBySerNo(getEntity().getCustomer()
								.getSerNo()));
				setFirstLevelGroups(groupService.getSubGroups(getEntity()
						.getCustomer().getSerNo(), groupService
						.getRootGroup(getEntity().getCustomer().getSerNo())));

				boolean isFirstGroupSerNo = getEntity().getFirstLevelSelect() != null
						&& getEntity().getFirstLevelSelect() > 0
						&& groupService.getBySerNo(getEntity()
								.getFirstLevelSelect()) != null
						&& groupService
								.getBySerNo(getEntity().getFirstLevelSelect())
								.getGroupMapping().getLevel() == 1;

				boolean isSecondGroupSerNo = getEntity().getSecondLevelSelect() != null
						&& getEntity().getSecondLevelSelect() > 0
						&& groupService.getBySerNo(getEntity()
								.getSecondLevelSelect()) != null
						&& groupService
								.getBySerNo(getEntity().getSecondLevelSelect())
								.getGroupMapping().getLevel() == 2;

				if (isFirstGroupSerNo) {
					group.setFirstLevelSelect(getEntity().getFirstLevelSelect());
					setSecondLevelGroups(groupService.getSubGroups(getEntity()
							.getCustomer().getSerNo(), groupService
							.getBySerNo(getEntity().getFirstLevelSelect())));
				}

				if (isFirstGroupSerNo && isSecondGroupSerNo) {
					long firstSerNo = getEntity().getFirstLevelSelect();
					long secondSerNo = getEntity().getSecondLevelSelect();
					group.setFirstLevelSelect(firstSerNo);
					group.setSecondLevelSelect(secondSerNo);

					if (groupService.getBySerNo(secondSerNo).getGroupMapping()
							.getParentGroupMapping().getGroup().getSerNo() == firstSerNo)
						setThirdLevelGroups(groupService.getSubGroups(
								getEntity().getCustomer().getSerNo(),
								groupService.getBySerNo(getEntity()
										.getSecondLevelSelect())));
				}
			}

			setEntity(group);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		return EDIT;
	}

	@Override
	public String list() throws Exception {
		DataSet<Group> ds = groupService.getByRestrictions(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			ds.getPager().setCurrentPage(
					(int) Math.ceil(ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage()));
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
			customer = customerService.getBySerNo(getEntity().getCustomer()
					.getSerNo());
			if (getEntity().getFirstLevelOption().equals("new")) {
				getEntity().setGroupName(getEntity().getFirstLevelName());

				group = groupService.save(new Group(new GroupMapping(
						groupMappingService.getRootMapping(customer.getName()),
						customer.getName(), 1), customer, getEntity()
						.getFirstLevelName().trim()), getLoginUser());

			}

			if (StringUtils.isNotBlank(getEntity().getSecondLevelOption())
					&& getEntity().getSecondLevelOption().equals("new")) {
				getEntity().setGroupName(getEntity().getSecondLevelName());

				if (getEntity().getFirstLevelOption().equals("new")) {
					group = groupService.save(
							new Group(new GroupMapping(group.getGroupMapping(),
									customer.getName(), 2), customer,
									getEntity().getSecondLevelName().trim()),
							getLoginUser());
				}

				if (getEntity().getFirstLevelOption().equals("extend")) {
					group = groupService.save(
							new Group(new GroupMapping(groupService.getBySerNo(
									getEntity().getFirstLevelSelect())
									.getGroupMapping(), customer.getName(), 2),
									customer, getEntity().getSecondLevelName()
											.trim()), getLoginUser());
				}
			}

			if (StringUtils.isNotBlank(getEntity().getThirdLevelOption())
					&& getEntity().getThirdLevelOption().equals("new")) {
				if (getEntity().getSecondLevelOption().equals("new")) {
					group = groupService.save(
							new Group(new GroupMapping(group.getGroupMapping(),
									customer.getName(), 3), customer,
									getEntity().getThirdLevelName().trim()),
							getLoginUser());
				}

				if (getEntity().getSecondLevelOption().equals("extend")) {
					group = groupService.save(
							new Group(new GroupMapping(groupService.getBySerNo(
									getEntity().getSecondLevelSelect())
									.getGroupMapping(), customer.getName(), 3),
									customer, getEntity().getThirdLevelName()
											.trim()), getLoginUser());
				}
			}

			return VIEW;
		} else {
			getEntity().setCustomer(
					customerService.getBySerNo(getEntity().getCustomer()
							.getSerNo()));

			setFirstLevelGroups(groupService.getSubGroups(getEntity()
					.getCustomer().getSerNo(), groupService
					.getRootGroup(getEntity().getCustomer().getSerNo())));

			boolean isFirstGroupSerNo = getEntity().getFirstLevelSelect() != null
					&& getEntity().getFirstLevelSelect() > 0
					&& groupService.getBySerNo(getEntity()
							.getFirstLevelSelect()) != null
					&& groupService
							.getBySerNo(getEntity().getFirstLevelSelect())
							.getGroupMapping().getLevel() == 1;

			boolean isSecondGroupSerNo = getEntity().getSecondLevelSelect() != null
					&& getEntity().getSecondLevelSelect() > 0
					&& groupService.getBySerNo(getEntity()
							.getSecondLevelSelect()) != null
					&& groupService
							.getBySerNo(getEntity().getSecondLevelSelect())
							.getGroupMapping().getLevel() == 2;

			if (isFirstGroupSerNo) {
				setSecondLevelGroups(groupService.getSubGroups(getEntity()
						.getCustomer().getSerNo(), groupService
						.getBySerNo(getEntity().getFirstLevelSelect())));
			}

			if (isFirstGroupSerNo && isSecondGroupSerNo) {
				long firstSerNo = getEntity().getFirstLevelSelect();
				long secondSerNo = getEntity().getSecondLevelSelect();
				if (groupService.getBySerNo(secondSerNo).getGroupMapping()
						.getParentGroupMapping().getGroup().getSerNo() == firstSerNo)
					setThirdLevelGroups(groupService.getSubGroups(getEntity()
							.getCustomer().getSerNo(), groupService
							.getBySerNo(getEntity().getSecondLevelSelect())));
			}
			return ADD;
		}
	}

	@Override
	public String update() throws Exception {
		validateUpdate();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			if (group.getGroupMapping().getLevel() == 1) {
				getEntity()
						.setGroupName(getEntity().getFirstLevelName().trim());
				groupService
						.update(getEntity(), getLoginUser(), "groupMapping");
			}

			if (group.getGroupMapping().getLevel() == 2) {
				getEntity().setGroupName(
						getEntity().getSecondLevelName().trim());

				getEntity().setGroupMapping(group.getGroupMapping());
				getEntity().getGroupMapping().setParentGroupMapping(
						groupService.getBySerNo(
								getEntity().getFirstLevelSelect())
								.getGroupMapping());

				groupService.merge(getEntity(), getLoginUser());
			}

			if (group.getGroupMapping().getLevel() == 3) {
				getEntity()
						.setGroupName(getEntity().getThirdLevelName().trim());

				getEntity().setGroupMapping(group.getGroupMapping());
				getEntity().getGroupMapping().setParentGroupMapping(
						groupService.getBySerNo(
								getEntity().getSecondLevelSelect())
								.getGroupMapping());

				groupService.merge(getEntity(), getLoginUser());
			}

			return VIEW;
		} else {
			getEntity().setCustomer(
					customerService.getBySerNo(getEntity().getCustomer()
							.getSerNo()));

			setFirstLevelGroups(groupService.getSubGroups(getEntity()
					.getCustomer().getSerNo(), groupService
					.getRootGroup(getEntity().getCustomer().getSerNo())));

			getEntity().setGroupMapping(group.getGroupMapping());

			boolean isFirstGroupSerNo = getEntity().getFirstLevelSelect() != null
					&& getEntity().getFirstLevelSelect() > 0
					&& groupService.getBySerNo(getEntity()
							.getFirstLevelSelect()) != null
					&& groupService
							.getBySerNo(getEntity().getFirstLevelSelect())
							.getGroupMapping().getLevel() == 1;

			boolean isSecondGroupSerNo = getEntity().getSecondLevelSelect() != null
					&& getEntity().getSecondLevelSelect() > 0
					&& groupService.getBySerNo(getEntity()
							.getSecondLevelSelect()) != null
					&& groupService
							.getBySerNo(getEntity().getSecondLevelSelect())
							.getGroupMapping().getLevel() == 2;

			if (isFirstGroupSerNo) {
				setSecondLevelGroups(groupService.getSubGroups(getEntity()
						.getCustomer().getSerNo(), groupService
						.getBySerNo(getEntity().getFirstLevelSelect())));
			}

			if (isFirstGroupSerNo && isSecondGroupSerNo) {
				long firstSerNo = getEntity().getFirstLevelSelect();
				long secondSerNo = getEntity().getSecondLevelSelect();
				if (groupService.getBySerNo(secondSerNo).getGroupMapping()
						.getParentGroupMapping().getGroup().getSerNo() == firstSerNo)
					setThirdLevelGroups(groupService.getSubGroups(getEntity()
							.getCustomer().getSerNo(), groupService
							.getBySerNo(getEntity().getSecondLevelSelect())));

			}

			setEntity(getEntity());

			return EDIT;
		}
	}

	@Override
	public String delete() throws Exception {
		validateDelete();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			groupService.deleteBySerNo(getEntity().getSerNo());
			list();
			return LIST;
		} else {
			list();
			return LIST;
		}
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
