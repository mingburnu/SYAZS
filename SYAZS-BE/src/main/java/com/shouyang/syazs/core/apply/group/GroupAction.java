package com.shouyang.syazs.core.apply.group;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.apply.customer.CustomerService;
import com.shouyang.syazs.core.apply.enums.Role;
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
	private Group parentGroup;

	@Autowired
	private Group repeatFirstGroup;

	@Autowired
	private Group repeatSecondGroup;

	@Autowired
	private GroupService groupService;

	@Autowired
	private GroupMapping groupMapping;

	@Autowired
	private GroupMappingService groupMappingService;

	@Autowired
	private Customer customer;

	@Autowired
	private CustomerService customerService;

	@Override
	protected void validateSave() throws Exception {
		if (hasCustomer()) {
			// if (getEntity().getCustomer().getSerNo() == 9) {TODO
			// if (!getLoginUser().getRole().equals(Role.系統管理員)) {
			// errorMessages.add("權限不符");
			// }
			// }

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
					if (StringUtils.isNotBlank(getEntity()
							.getSecondLevelOption())) {
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
							|| (!getEntity().getSecondLevelOption().equals(
									"new") && !getEntity()
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
								long firstSerNo = getEntity()
										.getFirstLevelSelect();
								long secondSerNo = getEntity()
										.getSecondLevelSelect();
								if (groupService.getBySerNo(secondSerNo) == null) {
									errorMessages.add("LEVEL 2流水號不正確");
								} else {
									if (getEntity().getCustomer().getSerNo() != groupService
											.getBySerNo(secondSerNo)
											.getCustomer().getSerNo()) {
										errorMessages.add("LEVEL 2用戶不正確");
									} else {
										if (groupService
												.getBySerNo(secondSerNo)
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
							if (!getEntity().getThirdLevelOption()
									.equals("new")) {
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
						if (StringUtils.isBlank(getEntity()
								.getThirdLevelOption())
								|| !getEntity().getThirdLevelOption().equals(
										"new")) {
							errorMessages.add("請選擇LEVEL 3");
						} else {
							if (StringUtils.isBlank(getEntity()
									.getThirdLevelName())) {
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
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	@Override
	protected void validateUpdate() throws Exception {
		if (hasCustomer()) {
			group = groupService.getTargetEntity(initDataSet());
			if (group == null) {
				getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
			} else {
				if (getEntity().getCustomer().getSerNo() == 9) {
					if (!getLoginUser().getRole().equals(Role.系統管理員)) {
						errorMessages.add("權限不符");
					}
				}

				if (group.getGroupMapping().getLevel() == 0) {
					getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
				} else {
					if (group.getGroupMapping().getLevel() == 1) {
						if (StringUtils
								.isBlank(getEntity().getFirstLevelName())) {
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
						if (StringUtils.isNotBlank(getEntity()
								.getSecondLevelName())
								&& getEntity().getFirstLevelSelect() != null
								&& groupService.getBySerNo(getEntity()
										.getFirstLevelSelect()) != null
								&& groupService
										.getBySerNo(
												getEntity()
														.getFirstLevelSelect())
										.getGroupMapping().getLevel() == 1
								&& groupService
										.getBySerNo(
												getEntity()
														.getFirstLevelSelect())
										.getCustomer().getSerNo() == customer
										.getSerNo()) {
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
						} else {
							if (StringUtils.isBlank(getEntity()
									.getSecondLevelName())) {
								errorMessages.add("請填寫LEVEL 2名稱");
							}

							if (getEntity().getFirstLevelSelect() == null
									|| groupService.getBySerNo(getEntity()
											.getFirstLevelSelect()) == null
									|| groupService
											.getBySerNo(
													getEntity()
															.getFirstLevelSelect())
											.getGroupMapping().getLevel() != 1
									|| groupService
											.getBySerNo(
													getEntity()
															.getFirstLevelSelect())
											.getCustomer().getSerNo() != customer
											.getSerNo()) {
								errorMessages.add("LEVEL 1異常");
								getEntity().setFirstLevelSelect(
										group.getGroupMapping()
												.getParentGroupMapping()
												.getGroup().getSerNo());
							}
						}

						getEntity().setFirstLevelGroups(
								groupService.getSubGroups(customer.getSerNo(),
										groupService.getRootGroup(getEntity()
												.getCustomer().getSerNo())));
						getEntity().setSecondLevelGroups(
								groupService.getSubGroups(customer.getSerNo(),
										groupService.getBySerNo(getEntity()
												.getFirstLevelSelect())));
					}

					if (group.getGroupMapping().getLevel() == 3) {
						if (StringUtils.isNotBlank(getEntity()
								.getThirdLevelName())
								&& getEntity().getSecondLevelSelect() != null
								&& groupService.getBySerNo(getEntity()
										.getSecondLevelSelect()) != null
								&& groupService
										.getBySerNo(
												getEntity()
														.getSecondLevelSelect())
										.getGroupMapping().getLevel() == 2
								&& groupService
										.getBySerNo(
												getEntity()
														.getSecondLevelSelect())
										.getGroupMapping().getGroup()
										.getSerNo() != group.getSerNo()) {
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
						} else {
							if (StringUtils.isBlank(getEntity()
									.getThirdLevelName())) {
								errorMessages.add("請填寫LEVEL 3名稱");
							}

							if (getEntity().getSecondLevelSelect() == null
									|| groupService.getBySerNo(getEntity()
											.getSecondLevelSelect()) == null
									|| groupService
											.getBySerNo(
													getEntity()
															.getSecondLevelSelect())
											.getGroupMapping().getLevel() != 2
									|| groupService
											.getBySerNo(
													getEntity()
															.getSecondLevelSelect())
											.getGroupMapping().getGroup()
											.getSerNo() != group.getSerNo()) {
								errorMessages.add("LEVEL 2異常");
								getEntity().setSecondLevelSelect(
										group.getGroupMapping()
												.getParentGroupMapping()
												.getGroup().getSerNo());
							}
						}

						List<Group> firstLevelGroups = new ArrayList<Group>();
						firstLevelGroups.add(group.getGroupMapping()
								.getParentGroupMapping()
								.getParentGroupMapping().getGroup());
						getEntity().setFirstLevelGroups(firstLevelGroups);

						getEntity().setSecondLevelGroups(
								groupService.getSubGroups(customer.getSerNo(),
										firstLevelGroups.get(0)));

						getEntity().setThirdLevelGroups(
								groupService.getSubGroups(customer.getSerNo(),
										groupService.getBySerNo(getEntity()
												.getSecondLevelSelect())));
					}
				}
			}
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	@Override
	protected void validateDelete() throws Exception {
		if (hasCustomer()) {
			group = groupService.getTargetEntity(initDataSet());

			if (group == null) {
				errorMessages.add("沒有這個物件");
			} else {
				if (getEntity().getCustomer().getSerNo() == 9) {
					if (!getLoginUser().getRole().equals(Role.系統管理員)) {
						errorMessages.add("權限不符");
					}
				}

				if (group.getGroupMapping().getLevel() == 0) {
					errorMessages.add("Can't delete Root Group");
				}
			}
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	@Override
	public String add() throws Exception {
		if (hasCustomer()) {
			getEntity().setCustomer(
					customerService.getBySerNo(getEntity().getCustomer()
							.getSerNo()));

			getEntity().setFirstLevelGroups(
					groupService.getSubGroups(getEntity().getCustomer()
							.getSerNo(), groupService.getRootGroup(getEntity()
							.getCustomer().getSerNo())));

			boolean isFirstGroupSerNo = getEntity().getFirstLevelSelect() != null
					&& getEntity().getFirstLevelSelect() > 0
					&& groupService.getBySerNo(getEntity()
							.getFirstLevelSelect()) != null
					&& groupService
							.getBySerNo(getEntity().getFirstLevelSelect())
							.getGroupMapping().getLevel() == 1
					&& groupService
							.getBySerNo(getEntity().getFirstLevelSelect())
							.getCustomer().getSerNo() == customer.getSerNo();

			boolean isSecondGroupSerNo = getEntity().getSecondLevelSelect() != null
					&& getEntity().getSecondLevelSelect() > 0
					&& groupService.getBySerNo(getEntity()
							.getSecondLevelSelect()) != null
					&& groupService
							.getBySerNo(getEntity().getSecondLevelSelect())
							.getGroupMapping().getLevel() == 2;

			if (isFirstGroupSerNo) {
				getEntity()
						.setSecondLevelGroups(
								groupService.getSubGroups(getEntity()
										.getCustomer().getSerNo(), groupService
										.getBySerNo(getEntity()
												.getFirstLevelSelect())));

			}

			if (isFirstGroupSerNo && isSecondGroupSerNo) {
				long firstSerNo = getEntity().getFirstLevelSelect();
				long secondSerNo = getEntity().getSecondLevelSelect();
				if (groupService.getBySerNo(secondSerNo).getGroupMapping()
						.getParentGroupMapping().getGroup().getSerNo() == firstSerNo) {
					getEntity().setThirdLevelGroups(
							groupService.getSubGroups(getEntity().getCustomer()
									.getSerNo(), groupService
									.getBySerNo(getEntity()
											.getSecondLevelSelect())));
				}
			}

			setEntity(getEntity());
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		return ADD;
	}

	@Override
	public String edit() throws Exception {
		if (hasCustomer()) {
			group = groupService.getTargetEntity(initDataSet());
			if (group != null) {
				if (group.getGroupMapping().getLevel() == 3) {
					getEntity().setSecondLevelGroups(
							groupService.getSubGroups(getEntity().getCustomer()
									.getSerNo(), group.getGroupMapping()
									.getParentGroupMapping()
									.getParentGroupMapping().getGroup()));
					getEntity().setThirdLevelGroups(
							groupService.getSubGroups(getEntity().getCustomer()
									.getSerNo(), group.getGroupMapping()
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

					group.setSecondLevelGroups(groupService.getSubGroups(
							getEntity().getCustomer().getSerNo(), group
									.getGroupMapping().getParentGroupMapping()
									.getParentGroupMapping().getGroup()));

					group.setThirdLevelGroups(groupService.getSubGroups(
							getEntity().getCustomer().getSerNo(), group
									.getGroupMapping().getParentGroupMapping()
									.getGroup()));

					if (getEntity().getThirdLevelName() != null) {
						group.setThirdLevelName(getEntity().getThirdLevelName());
					}
				}

				if (group.getGroupMapping().getLevel() == 2) {
					group.setFirstLevelOption("extend");
					group.setFirstLevelSelect(group.getGroupMapping()
							.getParentGroupMapping().getGroup().getSerNo());

					group.setSecondLevelOption("modify");
					group.setSecondLevelName(group.getGroupName());

					group.setFirstLevelGroups(groupService.getSubGroups(
							getEntity().getCustomer().getSerNo(), groupService
									.getRootGroup(getEntity().getCustomer()
											.getSerNo())));
					group.setSecondLevelGroups(groupService.getSubGroups(
							getEntity().getCustomer().getSerNo(), group
									.getGroupMapping().getParentGroupMapping()
									.getGroup()));

					if (getEntity().getSecondLevelName() != null) {
						group.setSecondLevelName(getEntity()
								.getSecondLevelName());
					}
				}

				if (group.getGroupMapping().getLevel() == 1) {
					group.setFirstLevelOption("modify");
					group.setFirstLevelName(group.getGroupName());

					group.setFirstLevelGroups(groupService.getSubGroups(
							getEntity().getCustomer().getSerNo(), groupService
									.getRootGroup(getEntity().getCustomer()
											.getSerNo())));

					if (getEntity().getFirstLevelName() != null) {
						group.setFirstLevelName(getEntity().getFirstLevelName());
					}
				}

				getEntity().setCustomer(customer);
				getEntity().setFirstLevelGroups(
						groupService.getSubGroups(getEntity().getCustomer()
								.getSerNo(), groupService
								.getRootGroup(getEntity().getCustomer()
										.getSerNo())));

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
					group.setSecondLevelGroups(groupService.getSubGroups(
							getEntity().getCustomer().getSerNo(), groupService
									.getBySerNo(getEntity()
											.getFirstLevelSelect())));
				}

				if (isFirstGroupSerNo && isSecondGroupSerNo) {
					long firstSerNo = getEntity().getFirstLevelSelect();
					long secondSerNo = getEntity().getSecondLevelSelect();
					group.setFirstLevelSelect(firstSerNo);
					group.setSecondLevelSelect(secondSerNo);

					if (groupService.getBySerNo(secondSerNo).getGroupMapping()
							.getParentGroupMapping().getGroup().getSerNo() == firstSerNo)
						group.setThirdLevelGroups(groupService.getSubGroups(
								getEntity().getCustomer().getSerNo(),
								groupService.getBySerNo(getEntity()
										.getSecondLevelSelect())));
				}

				setEntity(group);
			} else {
				getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		return EDIT;
	}

	@Override
	public String list() throws Exception {
		if (hasCustomer()) {
			DataSet<Group> ds = groupService.getByRestrictions(initDataSet());

			if (ds.getResults().size() == 0
					&& ds.getPager().getCurrentPage() > 1) {
				ds.getPager().setCurrentPage(
						(int) Math.ceil(ds.getPager().getTotalRecord()
								/ ds.getPager().getRecordPerPage()));
				ds = groupService.getByRestrictions(ds);
			}

			setDs(ds);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}
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

			addActionMessage("新增成功");
			return VIEW;
		} else {
			getEntity().setCustomer(
					customerService.getBySerNo(getEntity().getCustomer()
							.getSerNo()));

			getEntity().setFirstLevelGroups(
					groupService.getSubGroups(getEntity().getCustomer()
							.getSerNo(), groupService.getRootGroup(getEntity()
							.getCustomer().getSerNo())));

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
				getEntity()
						.setSecondLevelGroups(
								groupService.getSubGroups(getEntity()
										.getCustomer().getSerNo(), groupService
										.getBySerNo(getEntity()
												.getFirstLevelSelect())));
			}

			if (isFirstGroupSerNo && isSecondGroupSerNo) {
				long firstSerNo = getEntity().getFirstLevelSelect();
				long secondSerNo = getEntity().getSecondLevelSelect();
				if (groupService.getBySerNo(secondSerNo).getGroupMapping()
						.getParentGroupMapping().getGroup().getSerNo() == firstSerNo)
					getEntity().setThirdLevelGroups(
							groupService.getSubGroups(getEntity().getCustomer()
									.getSerNo(), groupService
									.getBySerNo(getEntity()
											.getSecondLevelSelect())));
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

				groupService.update(getEntity(), getLoginUser());
			}

			if (group.getGroupMapping().getLevel() == 3) {
				getEntity()
						.setGroupName(getEntity().getThirdLevelName().trim());

				getEntity().setGroupMapping(group.getGroupMapping());
				getEntity().getGroupMapping().setParentGroupMapping(
						groupService.getBySerNo(
								getEntity().getSecondLevelSelect())
								.getGroupMapping());

				groupService.update(getEntity(), getLoginUser());
			}

			addActionMessage("修改成功");
			return VIEW;
		} else {
			getEntity().setCustomer(
					customerService.getBySerNo(getEntity().getCustomer()
							.getSerNo()));

			getEntity().setFirstLevelGroups(
					groupService.getSubGroups(getEntity().getCustomer()
							.getSerNo(), groupService.getRootGroup(getEntity()
							.getCustomer().getSerNo())));

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
				getEntity()
						.setSecondLevelGroups(
								groupService.getSubGroups(getEntity()
										.getCustomer().getSerNo(), groupService
										.getBySerNo(getEntity()
												.getFirstLevelSelect())));
			}

			if (isFirstGroupSerNo && isSecondGroupSerNo) {
				long firstSerNo = getEntity().getFirstLevelSelect();
				long secondSerNo = getEntity().getSecondLevelSelect();
				if (groupService.getBySerNo(secondSerNo).getGroupMapping()
						.getParentGroupMapping().getGroup().getSerNo() == firstSerNo)
					getEntity().setThirdLevelGroups(
							groupService.getSubGroups(getEntity().getCustomer()
									.getSerNo(), groupService
									.getBySerNo(getEntity()
											.getSecondLevelSelect())));
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

	public String imports() {
		return IMPORT;
	}

	public String queue() throws Exception {
		if (ArrayUtils.isEmpty(getEntity().getFile())
				|| !getEntity().getFile()[0].isFile()) {
			addActionError("請選擇檔案");
		} else {
			if (createWorkBook(new FileInputStream(getEntity().getFile()[0])) == null) {
				addActionError("檔案格式錯誤");
			} else {
				if (!hasCustomer()) {
					addActionError("客戶錯誤");
				} else {
					if (getEntity().getCustomer().getSerNo() == 9) {
						if (!getLoginUser().getRole().equals(Role.系統管理員)) {
							addActionError("權限不符");
						}
					}
				}
			}
		}

		if (!hasActionErrors()) {
			Workbook book = createWorkBook(new FileInputStream(getEntity()
					.getFile()[0]));
			Sheet sheet = book.createSheet();
			if (book.getNumberOfSheets() != 0) {
				sheet = book.getSheetAt(0);
			}

			Row firstRow = sheet.getRow(0);
			if (firstRow == null) {
				firstRow = sheet.createRow(0);
			}

			// 保存列名
			List<String> cellNames = new ArrayList<String>();
			String[] rowTitles = new String[3];
			int n = 0;
			while (n < rowTitles.length) {
				if (firstRow.getCell(n) == null) {
					rowTitles[n] = "";
				} else {
					int typeInt = firstRow.getCell(n).getCellType();
					switch (typeInt) {
					case 0:
						String tempNumeric = "";
						tempNumeric = tempNumeric
								+ firstRow.getCell(n).getNumericCellValue();
						rowTitles[n] = tempNumeric;
						break;

					case 1:
						rowTitles[n] = firstRow.getCell(n).getStringCellValue();
						break;

					case 2:
						rowTitles[n] = firstRow.getCell(n).getCellFormula();
						break;

					case 3:
						rowTitles[n] = "";
						break;

					case 4:
						String tempBoolean = "";
						tempBoolean = ""
								+ firstRow.getCell(n).getBooleanCellValue();
						rowTitles[n] = tempBoolean;
						break;

					case 5:
						String tempByte = "";
						tempByte = tempByte
								+ firstRow.getCell(n).getErrorCellValue();
						rowTitles[n] = tempByte;
						break;
					}

				}

				cellNames.add(rowTitles[n]);
				n++;
			}

			LinkedHashSet<Group> originalData = new LinkedHashSet<Group>();
			getEntity().getCustomer().setName(
					customerService.getBySerNo(
							getEntity().getCustomer().getSerNo()).getName());

			int normal = 0;

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null) {
					continue;
				}

				String[] rowValues = new String[3];
				int k = 0;
				while (k < rowValues.length) {
					if (row.getCell(k) == null) {
						rowValues[k] = "";
					} else {
						int typeInt = row.getCell(k).getCellType();
						switch (typeInt) {
						case 0:
							String tempNumeric = "";
							tempNumeric = tempNumeric
									+ row.getCell(k).getNumericCellValue();
							rowValues[k] = tempNumeric;
							break;

						case 1:
							rowValues[k] = row.getCell(k).getStringCellValue();
							break;

						case 2:
							rowValues[k] = row.getCell(k).getCellFormula();
							break;

						case 3:
							rowValues[k] = "";
							break;

						case 4:
							String tempBoolean = "";
							tempBoolean = ""
									+ row.getCell(k).getBooleanCellValue();
							rowValues[k] = tempBoolean;
							break;

						case 5:
							String tempByte = "";
							tempByte = tempByte
									+ row.getCell(k).getErrorCellValue();
							rowValues[k] = tempByte;
							break;
						}

					}
					k++;
				}

				group = new Group();

				if (StringUtils.isNotBlank(rowValues[0].trim())) {
					group.setFirstLevelGroup(new Group(null, getEntity()
							.getCustomer(), rowValues[0].trim()));
				}

				if (StringUtils.isNotBlank(rowValues[1].trim())) {
					group.setSecondLevelGroup(new Group(null, getEntity()
							.getCustomer(), rowValues[1].trim()));
				}

				if (StringUtils.isNotBlank(rowValues[2].trim())) {
					group.setThirdLevelGroup(new Group(null, getEntity()
							.getCustomer(), rowValues[2].trim()));
				}

				if (group.getFirstLevelGroup() == null) {
					group.setDataStatus("Level 1 錯誤");
				}

				if (group.getFirstLevelGroup() != null
						&& group.getThirdLevelGroup() != null
						&& group.getSecondLevelGroup() == null) {
					group.setDataStatus("Level 2 錯誤");
				}

				if (group.getDataStatus() == null) {
					if (group.getSecondLevelGroup() == null
							&& group.getThirdLevelGroup() == null) {
						if (groupService.isRepeatMainGroup(group
								.getFirstLevelGroup().getGroupName(),
								getEntity().getCustomer().getSerNo())) {
							group.setDataStatus("已存在");
						}
					}

					if (group.getSecondLevelGroup() != null
							&& group.getThirdLevelGroup() == null) {
						parentGroup = groupService.getRepeatMainGroup(group
								.getFirstLevelGroup().getGroupName(),
								getEntity().getCustomer().getSerNo());
						if (parentGroup != null) {
							if (groupService.isRepeatSubGroup(group
									.getSecondLevelGroup().getGroupName(),
									getEntity().getCustomer().getSerNo(),
									parentGroup)) {
								group.setDataStatus("已存在");
							}
						}
					}

					if (group.getSecondLevelGroup() != null
							&& group.getThirdLevelGroup() != null) {
						parentGroup = groupService.getRepeatMainGroup(group
								.getFirstLevelGroup().getGroupName(),
								getEntity().getCustomer().getSerNo());
						if (parentGroup != null) {
							parentGroup = groupService.getRepeatSubGroup(group
									.getSecondLevelGroup().getGroupName(),
									getEntity().getCustomer().getSerNo(),
									parentGroup);
							if (parentGroup != null) {
								if (groupService.isRepeatSubGroup(group
										.getThirdLevelGroup().getGroupName(),
										getEntity().getCustomer().getSerNo(),
										parentGroup)) {
									group.setDataStatus("已存在");
								}
							}
						}
					}

				}

				if (group.getDataStatus() == null) {
					group.setDataStatus("正常");

					if (!originalData.contains(group)) {
						normal++;
					}
				}

				originalData.add(group);
			}

			List<Group> excelData = new ArrayList<Group>(originalData);

			DataSet<Group> ds = initDataSet();
			ds.getPager().setTotalRecord((long) excelData.size());

			if (excelData.size() < ds.getPager().getRecordPerPage()) {
				int i = 0;
				while (i < excelData.size()) {
					ds.getResults().add(excelData.get(i));
					i++;
				}
			} else {
				int i = 0;
				while (i < ds.getPager().getRecordPerPage()) {
					ds.getResults().add(excelData.get(i));
					i++;
				}
			}

			getSession().put("cellNames", cellNames);
			getSession().put("importList", excelData);
			getSession().put("total", excelData.size());
			getSession().put("normal", normal);

			setDs(ds);
			return QUEUE;
		} else {
			return IMPORT;
		}
	}

	public String paginate() throws Exception {
		List<?> importList = (List<?>) getSession().get("importList");
		if (importList == null) {
			return IMPORT;
		}

		clearCheckedItem();

		DataSet<Group> ds = initDataSet();
		ds.getPager().setTotalRecord((long) importList.size());

		int first = ds.getPager().getOffset();
		int last = first + ds.getPager().getRecordPerPage();

		int i = 0;
		while (i < importList.size()) {
			if (i >= first && i < last) {
				ds.getResults().add((Group) importList.get(i));
			}
			i++;
		}

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			ds.getPager().setCurrentPage(
					(int) Math.ceil(ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage()));
			first = ds.getPager().getOffset();
			last = first + ds.getPager().getRecordPerPage();

			int j = 0;
			while (j < importList.size()) {
				if (j >= first && j < last) {
					ds.getResults().add((Group) importList.get(j));
				}
				j++;
			}

		}

		setDs(ds);
		return QUEUE;
	}

	@SuppressWarnings("unchecked")
	public String getCheckedItem() {
		List<?> importList = (List<?>) getSession().get("importList");
		if (importList == null) {
			return IMPORT;
		}

		Set<Integer> checkItemSet = new TreeSet<Integer>();
		if (getSession().containsKey("checkItemSet")) {
			checkItemSet = (Set<Integer>) getSession().get("checkItemSet");
		}

		if (ArrayUtils.isNotEmpty(getEntity().getImportItem())) {
			if (getEntity().getImportItem()[0] != null
					&& getEntity().getImportItem()[0] >= 0
					&& getEntity().getImportItem()[0] < importList.size()) {
				if (!checkItemSet.contains(getEntity().getImportItem()[0])) {
					if (((Group) importList.get(getEntity().getImportItem()[0]))
							.getDataStatus().equals("正常")) {
						checkItemSet.add(getEntity().getImportItem()[0]);
					}
				} else {
					checkItemSet.remove(getEntity().getImportItem()[0]);
				}
			}
		}

		getSession().put("checkItemSet", checkItemSet);
		return QUEUE;
	}

	public String allCheckedItem() {
		List<?> importList = (List<?>) getSession().get("importList");
		if (importList == null) {
			return IMPORT;
		}

		Set<Integer> checkItemSet = new TreeSet<Integer>();

		if (ArrayUtils.isNotEmpty(getEntity().getImportItem())) {
			Set<Integer> deRepeatSet = new HashSet<Integer>(
					Arrays.asList(getEntity().getImportItem()));
			getEntity().setImportItem(
					deRepeatSet.toArray(new Integer[deRepeatSet.size()]));

			int i = 0;
			while (i < getEntity().getImportItem().length) {
				if (getEntity().getImportItem()[i] != null
						&& getEntity().getImportItem()[i] >= 0
						&& getEntity().getImportItem()[i] < importList.size()) {
					if (((Group) importList.get(getEntity().getImportItem()[i]))
							.getDataStatus().equals("正常")) {
						checkItemSet.add(getEntity().getImportItem()[i]);
					}

					if (checkItemSet.size() == importList.size()) {
						break;
					}
				}
				i++;
			}
		}

		getSession().put("checkItemSet", checkItemSet);
		return QUEUE;
	}

	public String clearCheckedItem() {
		if (getSession().get("importList") == null) {
			return IMPORT;
		}

		Set<Integer> checkItemSet = new TreeSet<Integer>();
		getSession().put("checkItemSet", checkItemSet);
		return null;
	}

	public String importData() throws Exception {
		List<?> importList = (List<?>) getSession().get("importList");

		if (importList == null) {
			return IMPORT;
		}

		Set<?> checkItemSet = (Set<?>) getSession().get("checkItemSet");

		if (CollectionUtils.isEmpty(checkItemSet)) {
			addActionError("請選擇一筆或一筆以上的資料");
		}

		if (!hasActionErrors()) {
			Iterator<?> iterator = checkItemSet.iterator();
			int successCount = 0;
			while (iterator.hasNext()) {
				int index = (Integer) iterator.next();
				group = (Group) importList.get(index);

				repeatFirstGroup = groupService.getRepeatMainGroup(group
						.getFirstLevelGroup().getGroupName(), group
						.getFirstLevelGroup().getCustomer().getSerNo());

				if (repeatFirstGroup == null) {
					parentGroup = groupService.save(
							new Group(new GroupMapping(groupMappingService
									.getRootMapping(group.getFirstLevelGroup()
											.getCustomer().getName()), group
									.getFirstLevelGroup().getCustomer()
									.getName(), 1), group.getFirstLevelGroup()
									.getCustomer(), group.getFirstLevelGroup()
									.getGroupName()), getLoginUser());

					if (group.getSecondLevelGroup() != null) {
						parentGroup = groupService.save(new Group(
								new GroupMapping(parentGroup.getGroupMapping(),
										group.getSecondLevelGroup()
												.getCustomer().getName(), 2),
								group.getSecondLevelGroup().getCustomer(),
								group.getSecondLevelGroup().getGroupName()),
								getLoginUser());
					}

					if (group.getThirdLevelGroup() != null) {
						groupService.save(new Group(new GroupMapping(
								parentGroup.getGroupMapping(), group
										.getThirdLevelGroup().getCustomer()
										.getName(), 3), group
								.getThirdLevelGroup().getCustomer(), group
								.getThirdLevelGroup().getGroupName()),
								getLoginUser());
					}

				} else {
					if (group.getSecondLevelGroup() != null) {
						repeatSecondGroup = groupService.getRepeatSubGroup(
								group.getSecondLevelGroup().getGroupName(),
								group.getSecondLevelGroup().getCustomer()
										.getSerNo(), repeatFirstGroup);
						if (repeatSecondGroup == null) {
							parentGroup = groupService.save(
									new Group(new GroupMapping(repeatFirstGroup
											.getGroupMapping(), group
											.getSecondLevelGroup()
											.getCustomer().getName(), 2), group
											.getSecondLevelGroup()
											.getCustomer(), group
											.getSecondLevelGroup()
											.getGroupName()), getLoginUser());

							if (group.getThirdLevelGroup() != null) {
								groupService.save(new Group(new GroupMapping(
										parentGroup.getGroupMapping(), group
												.getThirdLevelGroup()
												.getCustomer().getName(), 3),
										group.getThirdLevelGroup()
												.getCustomer(), group
												.getThirdLevelGroup()
												.getGroupName()),
										getLoginUser());
							}
						} else {
							if (group.getThirdLevelGroup() != null) {
								groupService.save(new Group(new GroupMapping(
										repeatSecondGroup.getGroupMapping(),
										group.getThirdLevelGroup()
												.getCustomer().getName(), 3),
										group.getThirdLevelGroup()
												.getCustomer(), group
												.getThirdLevelGroup()
												.getGroupName()),
										getLoginUser());
							}
						}
					}
				}
				group.setDataStatus("已存在");
				++successCount;
			}

			recheck();
			getRequest().setAttribute("successCount", successCount);
			return VIEW;
		} else {
			paginate();
			return QUEUE;
		}
	}

	public String example() throws Exception {
		// reportFile = "group_sample.xlsx";
		getEntity().setReportFile("group_sample.xlsx");
		// Create blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();
		// Create a blank sheet
		XSSFSheet spreadsheet = workbook.createSheet("customer");
		// Create row object
		XSSFRow row;
		// This data needs to be written (Object[])
		Map<String, Object[]> empinfo = new LinkedHashMap<String, Object[]>();
		empinfo.put("1", new Object[] { "Level 1", "Level 2", "Level 3" });

		empinfo.put("2", new Object[] { "工學院", "材料系", "系辦" });

		// Iterate over data and write to sheet
		Set<String> keyid = empinfo.keySet();
		int rowid = 0;
		for (String key : keyid) {
			row = spreadsheet.createRow(rowid++);
			Object[] objectArr = empinfo.get(key);
			int cellid = 0;
			for (Object obj : objectArr) {
				Cell cell = row.createCell(cellid++);
				cell.setCellValue((String) obj);
			}
		}

		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		workbook.write(boas);
		getEntity()
				.setInputStream(new ByteArrayInputStream(boas.toByteArray()));

		return XLSX;
	}

	protected boolean hasCustomer() throws Exception {
		if (getEntity().getCustomer() == null
				|| !getEntity().getCustomer().hasSerNo()
				|| getEntity().getCustomer().getSerNo() <= 0) {
			return false;
		} else {
			customer = customerService.getBySerNo(getEntity().getCustomer()
					.getSerNo());
			if (customer == null) {
				return false;
			} else {
				return true;
			}
		}
	}

	// 判斷文件類型
	protected Workbook createWorkBook(InputStream is) throws IOException {
		try {
			if (getEntity().getFileFileName()[0].toLowerCase().endsWith("xls")) {
				return new HSSFWorkbook(is);
			}

			if (getEntity().getFileFileName()[0].toLowerCase().endsWith("xlsx")) {
				return new XSSFWorkbook(is);
			}
		} catch (InvalidOperationException e) {
			return null;
		}

		return null;
	}

	protected void recheck() throws Exception {
		List<?> importList = (List<?>) getSession().get("importList");
		if (importList != null) {
			Iterator<?> iterator = importList.iterator();
			int normal = 0;
			while (iterator.hasNext()) {
				group = (Group) iterator.next();
				if (group.getDataStatus().equals("正常")) {
					repeatFirstGroup = groupService.getRepeatMainGroup(group
							.getFirstLevelGroup().getGroupName(), group
							.getFirstLevelGroup().getCustomer().getSerNo());
					if (repeatFirstGroup != null) {
						if (group.getSecondLevelGroup() != null) {
							repeatSecondGroup = groupService.getRepeatSubGroup(
									group.getSecondLevelGroup().getGroupName(),
									group.getSecondLevelGroup().getCustomer()
											.getSerNo(), repeatFirstGroup);
							if (repeatSecondGroup != null) {
								group.setDataStatus("已存在");
							} else {
								normal++;
							}
						} else {
							group.setDataStatus("已存在");
						}
					} else {
						normal++;
					}
				}
			}

			getSession().put("normal", normal);
		}
	}
}
