package com.shouyang.syazs.module.apply.journal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.owasp.esapi.ESAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.shouyang.syazs.core.converter.EnumConverter;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.web.GenericWebActionFull;
import com.shouyang.syazs.module.apply.database.Database;
import com.shouyang.syazs.module.apply.database.DatabaseService;
import com.shouyang.syazs.module.apply.enums.Category;
import com.shouyang.syazs.module.apply.referenceOwner.ReferenceOwner;
import com.shouyang.syazs.module.apply.referenceOwner.ReferenceOwnerService;
import com.shouyang.syazs.module.apply.resourcesBuyers.ResourcesBuyers;

@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JournalAction extends GenericWebActionFull<Journal> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4383738517930055495L;

	@Autowired
	private Journal journal;

	@Autowired
	private Database database;

	@Autowired
	private JournalService journalService;

	@Autowired
	private DatabaseService databaseService;

	@Autowired
	private ResourcesBuyers resourcesBuyers;

	@Autowired
	private ReferenceOwner referenceOwner;

	@Autowired
	private ReferenceOwnerService referenceOwnerService;

	@Autowired
	private EnumConverter enumConverter;

	@Override
	protected void validateSave() throws Exception {
		if (StringUtils.isBlank(getEntity().getTitle())) {
			errorMessages.add("刊名不得空白");
		}

		if (StringUtils.isNotEmpty(getEntity().getIssn())) {
			if (!ISSN_Validator.isIssn(getEntity().getIssn())) {
				errorMessages.add("ISSN不正確");
			}
		}

		if (StringUtils.isNotEmpty(getEntity().getCongressClassification())) {
			if (!isLCC(getEntity().getCongressClassification())) {
				errorMessages.add("國會分類號格式不正確");
			}
		}

		if (!isURL(getEntity().getUrl())) {
			errorMessages.add("URL必須填寫");
		}

		if (!getEntity().getDatabase().hasSerNo()) {
			if (ArrayUtils.isEmpty(getEntity().getRefSerNo())) {
				errorMessages.add("至少選擇一筆以上擁有人");
			} else {
				Set<Long> deRepeatSet = new HashSet<Long>(
						Arrays.asList(getEntity().getRefSerNo()));
				getEntity().setRefSerNo(
						deRepeatSet.toArray(new Long[deRepeatSet.size()]));
				getEntity().setOwners(new LinkedList<ReferenceOwner>());

				int i = 0;
				while (i < getEntity().getRefSerNo().length) {
					if (getEntity().getRefSerNo()[i] == null) {
						errorMessages.add("null為不可利用的流水號");
					} else {
						if (getEntity().getRefSerNo()[i] < 1) {
							errorMessages.add(getEntity().getRefSerNo()[i]
									+ "為不可利用的流水號");
						} else {
							Object[] ownerValue = referenceOwnerService
									.getOwnerBySerNo(getEntity().getRefSerNo()[i]);
							if (ownerValue == null) {
								errorMessages.add(getEntity().getRefSerNo()[i]
										+ "為不可利用的流水號");
							} else {
								referenceOwner = new ReferenceOwner();
								referenceOwner.setSerNo(getEntity()
										.getRefSerNo()[i]);
								referenceOwner
										.setName(ownerValue[1].toString());

								getEntity().getOwners().add(referenceOwner);
							}
						}
					}

					i++;
				}
			}
		} else {
			database = databaseService.getBySerNo(getEntity().getDatabase()
					.getSerNo());
			if (database == null) {
				errorMessages.add("不可利用的流水號");
			} else {
				getEntity().setDatabase(database);
			}

			if (ArrayUtils.isNotEmpty(getEntity().getRefSerNo())) {
				Set<Long> deRepeatSet = new HashSet<Long>(
						Arrays.asList(getEntity().getRefSerNo()));
				getEntity().setRefSerNo(
						deRepeatSet.toArray(new Long[deRepeatSet.size()]));
				getEntity().setOwners(new LinkedList<ReferenceOwner>());

				int i = 0;
				while (i < getEntity().getRefSerNo().length) {
					if (getEntity().getRefSerNo()[i] == null) {
						continue;
					} else {
						if (getEntity().getRefSerNo()[i] < 1) {
							continue;
						} else {
							Object[] ownerValue = referenceOwnerService
									.getOwnerBySerNo(getEntity().getRefSerNo()[i]);
							if (ownerValue == null) {
								continue;
							} else {
								referenceOwner = new ReferenceOwner();
								referenceOwner.setSerNo(getEntity()
										.getRefSerNo()[i]);
								referenceOwner
										.setName(ownerValue[1].toString());
								getEntity().getOwners().add(referenceOwner);
							}
						}
					}

					i++;
				}
			}
		}

		if (getEntity().getResourcesBuyers().getCategory() == null) {
			getEntity().getResourcesBuyers().setCategory(Category.未註明);
		}

		if (getEntity().getOpenAccess() == null) {
			getEntity().setOpenAccess(false);
		}
	}

	@Override
	protected void validateUpdate() throws Exception {
		if (!hasEntity()) {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		} else {
			if (StringUtils.isBlank(getEntity().getTitle())) {
				errorMessages.add("刊名不得空白");
			}

			if (StringUtils.isNotEmpty(getEntity().getIssn())) {
				if (!ISSN_Validator.isIssn(getEntity().getIssn())) {
					errorMessages.add("ISSN不正確");
				}
			}

			if (StringUtils.isNotEmpty(getEntity().getCongressClassification())) {
				if (!isLCC(getEntity().getCongressClassification())) {
					errorMessages.add("國會分類號格式不正確");
				}
			}

			if (!isURL(getEntity().getUrl())) {
				errorMessages.add("URL必須填寫");
			}

			if (!getEntity().getDatabase().hasSerNo()) {
				if (ArrayUtils.isEmpty(getEntity().getRefSerNo())) {
					errorMessages.add("至少選擇一筆以上擁有人");
				} else {
					Set<Long> deRepeatSet = new HashSet<Long>(
							Arrays.asList(getEntity().getRefSerNo()));
					getEntity().setRefSerNo(
							deRepeatSet.toArray(new Long[deRepeatSet.size()]));
					getEntity().setOwners(new LinkedList<ReferenceOwner>());

					int i = 0;
					while (i < getEntity().getRefSerNo().length) {
						if (getEntity().getRefSerNo()[i] == null) {
							errorMessages.add("null為不可利用的流水號");
						} else {
							if (getEntity().getRefSerNo()[i] < 1) {
								errorMessages.add(getEntity().getRefSerNo()[i]
										+ "為不可利用的流水號");
							} else {
								Object[] ownerValue = referenceOwnerService
										.getOwnerBySerNo(getEntity()
												.getRefSerNo()[i]);
								if (ownerValue == null) {
									errorMessages
											.add(getEntity().getRefSerNo()[i]
													+ "為不可利用的流水號");
								} else {
									referenceOwner = new ReferenceOwner();
									referenceOwner.setSerNo(getEntity()
											.getRefSerNo()[i]);
									referenceOwner.setName(ownerValue[1]
											.toString());

									getEntity().getOwners().add(referenceOwner);
								}
							}
						}

						i++;
					}
				}
			} else {
				database = databaseService.getBySerNo(getEntity().getDatabase()
						.getSerNo());
				if (database == null) {
					errorMessages.add("不可利用的流水號");
				} else {
					getEntity().setDatabase(database);
				}

				if (ArrayUtils.isNotEmpty(getEntity().getRefSerNo())) {
					Set<Long> deRepeatSet = new HashSet<Long>(
							Arrays.asList(getEntity().getRefSerNo()));
					getEntity().setRefSerNo(
							deRepeatSet.toArray(new Long[deRepeatSet.size()]));
					getEntity().setOwners(new LinkedList<ReferenceOwner>());

					int i = 0;
					while (i < getEntity().getRefSerNo().length) {
						if (getEntity().getRefSerNo()[i] == null) {
							continue;
						} else {
							if (getEntity().getRefSerNo()[i] < 1) {
								continue;
							} else {
								Object[] ownerValue = referenceOwnerService
										.getOwnerBySerNo(getEntity()
												.getRefSerNo()[i]);
								if (ownerValue == null) {
									continue;
								} else {
									referenceOwner = new ReferenceOwner();
									referenceOwner.setSerNo(getEntity()
											.getRefSerNo()[i]);
									referenceOwner.setName(ownerValue[1]
											.toString());
									getEntity().getOwners().add(referenceOwner);
								}
							}
						}

						i++;
					}
				}
			}

			if (getEntity().getResourcesBuyers().getCategory() == null) {
				getEntity().getResourcesBuyers().setCategory(Category.未註明);
			}

			if (getEntity().getOpenAccess() == null) {
				getEntity().setOpenAccess(false);
			}
		}
	}

	@Override
	protected void validateDelete() throws Exception {
		if (ArrayUtils.isEmpty(getEntity().getCheckItem())) {
			errorMessages.add("請選擇一筆或一筆以上的資料");
		} else {
			Set<Long> deRepeatSet = new HashSet<Long>(Arrays.asList(getEntity()
					.getCheckItem()));
			getEntity().setCheckItem(
					deRepeatSet.toArray(new Long[deRepeatSet.size()]));

			int i = 0;
			while (i < getEntity().getCheckItem().length) {
				if (getEntity().getCheckItem()[i] == null
						|| getEntity().getCheckItem()[i] < 1
						|| journalService
								.getBySerNo(getEntity().getCheckItem()[i]) == null) {
					addActionError("有錯誤流水號");
					break;
				}
				i++;
			}
		}
	}

	@Override
	public String add() throws Exception {
		getRequest().setAttribute(
				"uncheckReferenceOwners",
				referenceOwnerService
						.getUncheckOwners(new ArrayList<ReferenceOwner>()));

		setEntity(journal);
		return ADD;
	}

	@Override
	public String edit() throws Exception {
		if (hasEntity()) {
			List<ReferenceOwner> owners = journalService.getcheckOwners(journal
					.getSerNo());
			journal.setOwners(owners);
			getRequest().setAttribute("uncheckReferenceOwners",
					referenceOwnerService.getUncheckOwners(owners));

			setEntity(journal);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		return EDIT;
	}

	@Override
	public String list() throws Exception {
		if (StringUtils.isNotEmpty(getEntity().getOption())) {
			if (!getEntity().getOption().equals("entity.title")
					&& !getEntity().getOption().equals("entity.issn")) {
				getEntity().setOption("entity.title");
			}
		} else {
			getEntity().setOption("entity.title");
		}

		if (getEntity().getOption().equals("entity.title")) {
			getEntity().setIssn(null);
		} else {
			getEntity().setTitle(null);

			if (StringUtils.isNotBlank(getEntity().getIssn())) {
				if (!ISSN_Validator.isIssn(getEntity().getIssn())) {
					return LIST;
				}
			}
		}

		DataSet<Journal> ds = journalService.getByRestrictions(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			ds.getPager().setCurrentPage(
					(int) Math.ceil(ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage()));
			ds = journalService.getByRestrictions(ds);
		}

		setDs(ds);
		return LIST;
	}

	@Override
	public String save() throws Exception {
		validateSave();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			getEntity().setTitle(getEntity().getTitle().trim());
			getEntity()
					.setIssn(
							getEntity().getIssn().toUpperCase()
									.replace("-", "").trim());

			if (getEntity().getDatabase().getSerNo() != null) {
				getEntity().setResourcesBuyers(new ResourcesBuyers());
				getEntity().setReferenceOwners(null);
			} else {
				getEntity().setDatabase(null);
				getEntity().setReferenceOwners(
						new HashSet<ReferenceOwner>(getEntity().getOwners()));
			}

			journal = journalService.save(getEntity(), getLoginUser());
			setOwners();
			setEntity(journal);
			addActionMessage("新增成功");
			return VIEW;
		} else {
			getRequest().setAttribute(
					"uncheckReferenceOwners",
					referenceOwnerService.getUncheckOwners(getEntity()
							.getOwners()));

			setEntity(getEntity());
			return ADD;
		}
	}

	@Override
	public String update() throws Exception {
		validateUpdate();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			getEntity().setTitle(getEntity().getTitle().trim());
			getEntity()
					.setIssn(
							getEntity().getIssn().toUpperCase()
									.replace("-", "").trim());

			if (getEntity().getDatabase().getSerNo() != null) {
				getEntity().setResourcesBuyers(new ResourcesBuyers());
				getEntity().setReferenceOwners(null);
			} else {
				getEntity().setDatabase(null);
				getEntity().setReferenceOwners(
						new HashSet<ReferenceOwner>(getEntity().getOwners()));
			}

			journal = journalService.update(getEntity(), getLoginUser());
			setOwners();
			setEntity(journal);
			addActionMessage("修改成功");
			return VIEW;
		} else {
			getRequest().setAttribute(
					"uncheckReferenceOwners",
					referenceOwnerService.getUncheckOwners(getEntity()
							.getOwners()));

			setEntity(getEntity());
			return EDIT;
		}
	}

	@Override
	public String delete() throws Exception {
		validateDelete();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			int i = 0;
			while (i < getEntity().getCheckItem().length) {
				journalService.deleteBySerNo(getEntity().getCheckItem()[i]);
				i++;
			}

			list();
			addActionMessage("刪除成功");
			return LIST;
		} else {
			list();
			return LIST;
		}
	}

	public String view() throws NumberFormatException, Exception {
		if (hasEntity()) {
			getRequest().setAttribute("viewSerNo", getEntity().getSerNo());
			setOwners();
			setEntity(journal);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		return VIEW;
	}

	public String tip() throws Exception {
		Long serNo = getEntity().getSerNo();
		Long datserNo = getEntity().getDatabase().getSerNo();
		if (datserNo != null) {
			database = databaseService.getBySerNo(datserNo);
		}

		if (StringUtils.isNotBlank(getEntity().getIssn())) {
			if (ISSN_Validator.isIssn(getEntity().getIssn())) {
				if (hasRepeatIssn(getEntity().getIssn(), serNo)) {
					getRequest().setAttribute("tip", "已有相同ISBN");
				}

				if (dbHasRepeatIssn(getEntity().getIssn(), serNo, database)) {
					getRequest().setAttribute("repeat", "資料庫有重複資源");
				}
			}
		} else {
			if (StringUtils.isNotBlank(getEntity().getTitle())) {
				if (hasRepeatTitle(getEntity().getTitle(), serNo)) {
					getRequest().setAttribute("tip", "相同名稱且無ISSN資源存在");
				}

				if (dbHasRepeatTitle(getEntity().getTitle(), serNo, database)) {
					getRequest().setAttribute("repeat", "資料庫有相同名稱且無ISSN資源存在");
				}
			}
		}
		return TIP;
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
			}
		}

		if (!hasActionErrors()) {
			if (StringUtils.isNotBlank(getEntity().getOption())) {
				if (!getEntity().getOption().equals("package")
						&& !getEntity().getOption().equals("individual")) {
					getEntity().setOption("package");
				}
			} else {
				getEntity().setOption("package");
			}

			int length = 0;
			if (getEntity().getOption().equals("package")) {
				length = 16;
			} else {
				length = 19;
			}

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
			String[] rowTitles = new String[length];
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

			List<Journal> originalData = new ArrayList<Journal>();
			Map<String, Integer> checkRepeatIssn = new HashMap<String, Integer>();
			Map<String, Integer> checkRepeatTitle = new HashMap<String, Integer>();

			int normal = 0;

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null) {
					continue;
				}

				String[] rowValues = new String[length];
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
							rowValues[k] = row.getCell(k).getStringCellValue()
									.trim();
							break;

						case 2:
							rowValues[k] = row.getCell(k).getCellFormula()
									.trim();
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

				StringBuilder objStatus = new StringBuilder();
				StringBuilder resStatus = new StringBuilder();

				boolean openAccess = false;
				if (rowValues[14].toLowerCase().equals("yes")
						|| rowValues[14].toLowerCase().equals("true")
						|| rowValues[14].equals("是")) {
					openAccess = true;
				}

				Integer version = null;
				if (NumberUtils.isNumber(rowValues[11])) {
					double d = Double.parseDouble(rowValues[11]);
					version = (int) d;
				}

				Set<ReferenceOwner> owners = new HashSet<ReferenceOwner>();

				if (length > 16) {
					Category category = null;
					Object objCategory = getEnum(
							new String[] { rowValues[17].trim() },
							Category.class);
					if (objCategory != null) {
						category = (Category) objCategory;
					} else {
						category = Category.未註明;
					}

					resourcesBuyers = new ResourcesBuyers(rowValues[15],
							rowValues[16], category);

					if (StringUtils.isNotBlank(rowValues[18].replace("、", ""))) {
						String[] names = rowValues[18].trim().split("、");

						int j = 0;
						while (j < names.length) {
							if (StringUtils.isNotBlank(names[j])) {
								referenceOwner = new ReferenceOwner();
								referenceOwner.setName(names[j].trim());
								referenceOwner.setSerNo(referenceOwnerService
										.getRefSerNoByName(referenceOwner
												.getName()));

								if (referenceOwner.getSerNo() == 0) {
									objStatus.append(referenceOwner.getName()
											+ "不存在<br>");
								}
								owners.add(referenceOwner);
							}
							j++;
						}
					}

					journal = new Journal(rowValues[0], rowValues[1],
							rowValues[2], rowValues[3].trim(), rowValues[4],
							rowValues[5], rowValues[6], rowValues[7],
							rowValues[8], rowValues[9], rowValues[10], version,
							rowValues[12], rowValues[13], openAccess, null,
							null, resourcesBuyers, owners);
					journal.getResourcesBuyers().setDataStatus("");

					if (CollectionUtils.isEmpty(journal.getReferenceOwners())) {
						objStatus.append("沒有擁有者<br>");
					}
				} else {
					database = databaseService.getByUUID(rowValues[15].trim());

					journal = new Journal(rowValues[0], rowValues[1],
							rowValues[2], rowValues[3].trim(), rowValues[4],
							rowValues[5], rowValues[6], rowValues[7],
							rowValues[8], rowValues[9], rowValues[10], version,
							rowValues[12], rowValues[13], openAccess, database,
							null, new ResourcesBuyers(), null);
					journal.getResourcesBuyers().setDataStatus("");

					if (StringUtils.isNotBlank(rowValues[15])) {
						if (database == null) {
							objStatus.append("資料庫代碼錯誤<br>");
						}
					} else {
						objStatus.append("資料庫代碼空白<br>");
					}
				}

				if (StringUtils.isBlank(journal.getTitle())) {
					objStatus.append("標題空白<br>");
				}

				if (!isURL(journal.getUrl())) {
					objStatus.append("url不正確<br>");
				}

				if (StringUtils.isNotEmpty(journal.getCongressClassification())) {
					if (!isLCC(journal.getCongressClassification())) {
						objStatus.append("LCC不正確<br>");
					}
				}

				if (StringUtils.isNotEmpty(journal.getIssn())) {
					if (ISSN_Validator.isIssn(journal.getIssn())) {
						String issn = journal.getIssn().replace("-", "")
								.toUpperCase();
						if (hasRepeatIssn(issn, null)) {
							resStatus.append("已有相同ISSN<br>");
						}

						if (dbHasRepeatIssn(issn, null, journal.getDatabase())) {
							resStatus.append("DB有相同ISSN<br>");
						}

						if (checkRepeatIssn.containsKey(issn)) {
							resStatus.append("清單有同ISSN資源<br>");

							String status = originalData
									.get(checkRepeatIssn.get(issn))
									.getResourcesBuyers().getDataStatus()
									+ "清單有同ISSN資源<br>";
							originalData
									.get(checkRepeatIssn.get(issn))
									.getResourcesBuyers()
									.setDataStatus(
											status.replace(
													"清單有同ISSN資源<br>清單有同ISSN資源<br>",
													"清單有同ISSN資源<br>"));
						}

						checkRepeatIssn.put(issn, originalData.size());
					} else {
						objStatus.append("ISSN不正確<br>");
					}
				} else {
					if (StringUtils.isNotBlank(journal.getTitle())) {
						if (hasRepeatTitle(journal.getTitle(), null)) {
							resStatus.append("已有無ISSN同名資源<br>");
						}

						if (dbHasRepeatTitle(journal.getTitle(), null,
								journal.getDatabase())) {
							resStatus.append("Db有無ISSN同名資源<br>");
						}

						if (checkRepeatTitle.containsKey(journal.getTitle())) {
							resStatus.append("清單有無ISSN同名資源<br>");

							String status = originalData
									.get(checkRepeatTitle.get(journal
											.getTitle())).getResourcesBuyers()
									.getDataStatus()
									+ "清單有無ISSN同名資源<br>";
							originalData
									.get(checkRepeatTitle.get(journal
											.getTitle()))
									.getResourcesBuyers()
									.setDataStatus(
											status.replace(
													"清單有無ISSN同名資源<br>清單有無ISSN同名資源<br>",
													"清單有無ISSN同名資源<br>"));
						}

						checkRepeatTitle.put(journal.getTitle(),
								originalData.size());
					}
				}

				journal.getResourcesBuyers()
						.setDataStatus(resStatus.toString());
				journal.setDataStatus(objStatus.toString());

				if (StringUtils.isEmpty(journal.getDataStatus())) {
					journal.setDataStatus("正常");
					++normal;
				}

				originalData.add(journal);
			}

			List<Journal> excelData = new ArrayList<Journal>(originalData);

			DataSet<Journal> ds = initDataSet();
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

		DataSet<Journal> ds = initDataSet();
		ds.getPager().setTotalRecord((long) importList.size());

		int first = ds.getPager().getOffset();
		int last = first + ds.getPager().getRecordPerPage();

		int i = 0;
		while (i < importList.size()) {
			if (i >= first && i < last) {
				ds.getResults().add((Journal) importList.get(i));
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
					ds.getResults().add((Journal) importList.get(j));
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
					if (((Journal) importList
							.get(getEntity().getImportItem()[0]))
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
					if (((Journal) importList
							.get(getEntity().getImportItem()[i]))
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
				journal = (Journal) importList.get(index);

				journal.setIssn(journal.getIssn().replace("-", "")
						.toUpperCase());

				journalService.save(journal, getLoginUser());
				journal.setDataStatus("已匯入");
				++successCount;
			}

			getRequest().setAttribute("successCount", successCount);
			int normal = (int) getSession().get("normal");
			getSession().put("normal", normal - successCount);
			return VIEW;
		} else {
			paginate();
			return QUEUE;
		}
	}

	public String example() throws Exception {
		getEntity().setReportFile("journal_sample.xlsx");

		// Create blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();
		// Create a blank sheet
		XSSFSheet spreadsheet = workbook.createSheet("journal");
		// Create row object
		XSSFRow row;
		// This data needs to be written (Object[])
		Map<String, Object[]> empinfo = new LinkedHashMap<String, Object[]>();

		if (StringUtils.isNotBlank(getEntity().getOption())) {
			if (!getEntity().getOption().equals("package")
					&& !getEntity().getOption().equals("individual")) {
				getEntity().setOption("package");
			}
		} else {
			getEntity().setOption("package");
		}

		if (getEntity().getOption().equals("package")) {
			empinfo.put("1", new Object[] { "刊名", "英文縮寫刊名", "刊名演變", "ISSN",
					"語文", "出版項", "出版年", "標題", "編號", "刊別", "國會分類號", "版本",
					"出版時間差", "URL", "公開資源", "資料庫UUID" });

			empinfo.put(
					"2",
					new Object[] {
							"The New England Journal of Medicine",
							"N. Engl. j. med.",
							"＜Boston medical and surgical journal 0096-6762",
							"15334406",
							"eng",
							"Boston, Massachusetts Medical Society.",
							"1928",
							"Medicine--Periodicals ; Surgery--Periodicals ; Medicine--periodicals",
							"000955", "Weekly", "R11", "N/A", "N/A",
							"http://www.nejm.org/", "是",
							"afa7bfd4-7571-4e71-a5d4-c93bbe671e06" });

			empinfo.put("3", new Object[] { "Cell", "Cell", "", "00928674",
					"eng", "Cambridge, Mass. : MIT Press.", "1974",
					"Cytology--Periodicals ; Virology--Periodicals", "002064",
					"Biweekly", "QH573", "N/A", "N/A", "http://www.cell.com/",
					"否", "afa7bfd4-7571-4e71-a5d4-c93bbe671e06" });
		} else {
			empinfo.put("1", new Object[] { "刊名", "英文縮寫刊名", "刊名演變", "ISSN",
					"語文", "出版項", "出版年", "標題", "編號", "刊別", "國會分類號", "版本",
					"出版時間差", "URL", "公開資源", "起始日", "到期日", "資源類型", "購買人名稱" });

			empinfo.put(
					"2",
					new Object[] {
							"The New England Journal of Medicine",
							"N. Engl. j. med.",
							"＜Boston medical and surgical journal 0096-6762",
							"15334406",
							"eng",
							"Boston, Massachusetts Medical Society.",
							"1928",
							"Medicine--Periodicals ; Surgery--Periodicals ; Medicine--periodicals",
							"000955", "Weekly", "R11", "N/A", "N/A",
							"http://www.nejm.org/", "是", "2000", "2005", "租貸",
							"高雄醫學院附設醫院、疾病管制署" });

			empinfo.put("3", new Object[] { "Cell", "Cell", "", "00928674",
					"eng", "Cambridge, Mass. : MIT Press.", "1974",
					"Cytology--Periodicals ; Virology--Periodicals", "002064",
					"Biweekly", "QH573", "N/A", "N/A", "http://www.cell.com/",
					"否", "1998", "2020", "買斷", "高雄醫學院附設醫院" });
		}

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

	protected boolean isLCC(String LCC) {
		String LCCPattern = "([A-Z]{1,3})((\\d+)(\\.?)(\\d+))";

		return Pattern.compile(LCCPattern).matcher(LCC).matches();
	}

	protected boolean isURL(String url) {
		return ESAPI.validator().isValidInput("Journal URL", url, "URL",
				Integer.MAX_VALUE, false);
	}

	protected boolean hasRepeatIssn(String issn, Long serNo) throws Exception {
		List<Long> results = journalService.getSerNosByIssn(issn.trim()
				.replace("-", ""));
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

	protected boolean dbHasRepeatIssn(String issn, Long serNo, Database db)
			throws Exception {
		if (db != null && db.hasSerNo()) {
			List<Long> results = journalService.getSerNosInDbByIssn(issn.trim()
					.replace("-", ""), db);
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
		}

		return false;
	}

	protected boolean hasRepeatTitle(String title, Long serNo) throws Exception {
		List<Long> results = journalService.getSerNosByTitle(title.trim());
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

	protected boolean dbHasRepeatTitle(String title, Long serNo, Database db)
			throws Exception {
		if (db != null && db.hasSerNo()) {
			List<Long> results = journalService.getSerNosInDbByTitle(
					title.trim(), db);
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
		}

		return false;
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

	protected boolean hasEntity() throws Exception {
		if (!getEntity().hasSerNo()) {
			return false;
		}

		journal = journalService.getBySerNo(getEntity().getSerNo());
		if (journal == null) {
			return false;
		}

		return true;
	}

	protected void setOwners() {
		if (journal.getDatabase() != null && journal.getDatabase().hasSerNo()) {
			getRequest().setAttribute(
					"referenceOwners",
					databaseService.getResOwners(journal.getDatabase()
							.getSerNo()));
		} else {
			getRequest().setAttribute("referenceOwners",
					journalService.getResOwners(journal.getSerNo()));
		}
	}

	@SuppressWarnings("rawtypes")
	protected Object getEnum(String[] values, Class toClass) {
		return enumConverter.convertFromString(null, values, toClass);
	}
}
