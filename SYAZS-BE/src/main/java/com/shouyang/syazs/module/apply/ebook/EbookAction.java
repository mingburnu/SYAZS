package com.shouyang.syazs.module.apply.ebook;

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
public class EbookAction extends GenericWebActionFull<Ebook> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9166237220863961574L;

	@Autowired
	private Ebook ebook;

	@Autowired
	private Database database;

	@Autowired
	private EbookService ebookService;

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
		if (StringUtils.isBlank(getEntity().getBookName())) {
			errorMessages.add("書名不得空白");
		}

		if (getEntity().getIsbn() != null) {
			if (!ISBN_Validator.isIsbn(getEntity().getIsbn())) {
				errorMessages.add("ISBN不正確");
			}
		} else {
			if (StringUtils
					.isNotBlank(getRequest().getParameter("entity.isbn"))) {
				if (!ISBN_Validator.isIsbn(getRequest().getParameter(
						"entity.isbn"))) {
					errorMessages.add("ISBN不正確");
				}
			}
		}

		if (StringUtils.isNotEmpty(getEntity().getCnClassBzStr())) {
			if (!NumberUtils.isDigits(getEntity().getCnClassBzStr())
					|| getEntity().getCnClassBzStr().length() != 3) {
				errorMessages.add("中國圖書分類碼不正確");
			}
		}

		if (StringUtils.isNotEmpty(getEntity().getBookInfoIntegral())) {
			if (!NumberUtils.isDigits(getEntity().getBookInfoIntegral())
					|| getEntity().getBookInfoIntegral().length() != 3) {
				errorMessages.add("美國國家圖書館類碼不正確");
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
			if (StringUtils.isBlank(getEntity().getBookName())) {
				errorMessages.add("書名不得空白");
			}

			if (getEntity().getIsbn() != null) {
				if (!ISBN_Validator.isIsbn(getEntity().getIsbn())) {
					errorMessages.add("ISBN不正確");
				}
			} else {
				if (StringUtils.isNotBlank(getRequest().getParameter(
						"entity.isbn"))) {
					if (!ISBN_Validator.isIsbn(getRequest().getParameter(
							"entity.isbn"))) {
						errorMessages.add("ISBN不正確");
					}
				}
			}

			if (StringUtils.isNotEmpty(getEntity().getCnClassBzStr())) {
				if (!NumberUtils.isDigits(getEntity().getCnClassBzStr())
						|| getEntity().getCnClassBzStr().length() != 3) {
					errorMessages.add("中國圖書分類碼不正確");
				}
			}

			if (StringUtils.isNotEmpty(getEntity().getBookInfoIntegral())) {
				if (!NumberUtils.isDigits(getEntity().getBookInfoIntegral())
						|| getEntity().getBookInfoIntegral().length() != 3) {
					errorMessages.add("美國國家圖書館類碼不正確");
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
						|| ebookService
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

		setEntity(ebook);

		return ADD;
	}

	@Override
	public String edit() throws Exception {
		if (hasEntity()) {
			List<ReferenceOwner> owners = ebookService.getcheckOwners(ebook
					.getSerNo());
			ebook.setOwners(owners);
			getRequest().setAttribute("uncheckReferenceOwners",
					referenceOwnerService.getUncheckOwners(owners));

			setEntity(ebook);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		return EDIT;
	}

	@Override
	public String list() throws Exception {
		if (StringUtils.isNotBlank(getEntity().getOption())) {
			if (!getEntity().getOption().equals("entity.bookName")
					&& !getEntity().getOption().equals("entity.isbn")) {
				getEntity().setOption("entity.bookName");
			}
		} else {
			getEntity().setOption("entity.bookName");
		}

		if (getEntity().getOption().equals("entity.isbn")) {
			if (getEntity().getIsbn() == null) {
				if (StringUtils.isNotEmpty(getRequest().getParameter(
						"entity.isbn"))) {
					if (ISBN_Validator.isIsbn(getRequest().getParameter(
							"entity.isbn").trim())) {
						getEntity().setIsbn(
								Long.parseLong(getRequest()
										.getParameter("entity.isbn").trim()
										.replaceAll("-", "")));
					} else {
						getEntity().setIsbn(Long.MIN_VALUE);
					}
				}
			}
		}

		DataSet<Ebook> ds = ebookService.getByRestrictions(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			ds.getPager().setCurrentPage(
					(int) Math.ceil(ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage()));
			ds = ebookService.getByRestrictions(ds);
		}

		setDs(ds);
		return LIST;
	}

	@Override
	public String save() throws Exception {
		validateSave();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			if (StringUtils
					.isNotBlank(getRequest().getParameter("entity.isbn"))) {
				getEntity().setIsbn(
						Long.parseLong(getRequest().getParameter("entity.isbn")
								.trim().replace("-", "")));
			}

			if (getEntity().getDatabase().getSerNo() != null) {
				getEntity().setResourcesBuyers(new ResourcesBuyers());
				getEntity().setReferenceOwners(null);
			} else {
				getEntity().setDatabase(null);
				getEntity().setReferenceOwners(
						new HashSet<ReferenceOwner>(getEntity().getOwners()));
			}

			ebook = ebookService.save(getEntity(), getLoginUser());
			setOwners();
			setEntity(ebook);
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
			if (StringUtils
					.isNotBlank(getRequest().getParameter("entity.isbn"))) {
				getEntity().setIsbn(
						Long.parseLong(getRequest().getParameter("entity.isbn")
								.trim().replace("-", "")));
			}

			if (getEntity().getDatabase().getSerNo() != null) {
				getEntity().setResourcesBuyers(new ResourcesBuyers());
				getEntity().setReferenceOwners(null);
			} else {
				getEntity().setDatabase(null);
				getEntity().setReferenceOwners(
						new HashSet<ReferenceOwner>(getEntity().getOwners()));
			}

			ebook = ebookService.update(getEntity(), getLoginUser());
			setOwners();
			setEntity(ebook);
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
				ebookService.deleteBySerNo(getEntity().getCheckItem()[i]);
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
			setEntity(ebook);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		return VIEW;
	}

	public String tip() throws Exception {
		Long serNo = getEntity().getSerNo();
		String isbn = getRequest().getParameter("entity.isbn");
		Long datserNo = getEntity().getDatabase().getSerNo();
		if (datserNo != null) {
			database = databaseService.getBySerNo(datserNo);
		}

		if (getEntity().getIsbn() != null) {
			if (ISBN_Validator.isIsbn(getEntity().getIsbn())) {
				if (hasRepeatIsbn(isbn, serNo)) {
					getRequest().setAttribute("tip", "已有相同ISBN");
				}

				if (dbHasRepeatIsbn(isbn, serNo, database)) {
					getRequest().setAttribute("repeat", "資料庫有重複資源");
				}
			}
		} else {
			if (StringUtils.isNotBlank(isbn)) {
				if (ISBN_Validator.isIsbn(isbn)) {
					if (hasRepeatIsbn(isbn, serNo)) {
						getRequest().setAttribute("tip", "已有相同ISBN");
					}

					if (dbHasRepeatIsbn(isbn, serNo, database)) {
						getRequest().setAttribute("repeat", "資料庫有重複資源");
					}
				}
			} else {
				if (StringUtils.isNotBlank(getEntity().getBookName())) {
					if (hasRepeatName(getEntity().getBookName(), serNo)) {
						getRequest().setAttribute("tip", "相同名稱且無ISBN資源存在");
					}

					if (dbHasRepeatName(getEntity().getBookName(), serNo,
							database)) {
						getRequest().setAttribute("repeat",
								"資料庫有相同名稱且無ISBN資源存在");
					}
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

			List<Ebook> originalData = new LinkedList<Ebook>();
			Map<Long, Integer> checkRepeatIsbn = new HashMap<Long, Integer>();
			Map<String, Integer> checkRepeatName = new HashMap<String, Integer>();

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

				StringBuilder objStatus = new StringBuilder();
				StringBuilder resStatus = new StringBuilder();

				boolean openAccess = false;
				if (rowValues[14].toLowerCase().equals("yes")
						|| rowValues[14].toLowerCase().equals("true")
						|| rowValues[14].equals("是")) {
					openAccess = true;
				}

				String isbn = rowValues[1].trim();

				Integer version = null;
				if (NumberUtils.isNumber(rowValues[8])) {
					double d = Double.parseDouble(rowValues[8]);
					version = (int) d;
				}

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

					Set<ReferenceOwner> owners = new HashSet<ReferenceOwner>();
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

					if (NumberUtils.isDigits(isbn)) {
						ebook = new Ebook(rowValues[0], Long.parseLong(isbn),
								rowValues[2], rowValues[3], rowValues[4],
								rowValues[5], rowValues[6], rowValues[7],
								version, rowValues[9], rowValues[10],
								rowValues[12], rowValues[13], rowValues[11],
								openAccess, null, null, resourcesBuyers, owners);
						ebook.getResourcesBuyers().setDataStatus("");
					} else {
						ebook = new Ebook(rowValues[0], null, rowValues[2],
								rowValues[3], rowValues[4], rowValues[5],
								rowValues[6], rowValues[7], version,
								rowValues[9], rowValues[10], rowValues[12],
								rowValues[13], rowValues[11], openAccess, null,
								null, resourcesBuyers, owners);
						ebook.setTempNote(isbn);
						ebook.getResourcesBuyers().setDataStatus("");
					}

					if (CollectionUtils.isEmpty(ebook.getReferenceOwners())) {
						objStatus.append("沒有擁有者<br>");
					}
				} else {
					database = databaseService.getByUUID(rowValues[15].trim());

					if (NumberUtils.isDigits(isbn)) {
						ebook = new Ebook(rowValues[0], Long.parseLong(isbn),
								rowValues[2], rowValues[3], rowValues[4],
								rowValues[5], rowValues[6], rowValues[7],
								version, rowValues[9], rowValues[10],
								rowValues[12], rowValues[13], rowValues[11],
								openAccess, database, null,
								new ResourcesBuyers(), null);
						ebook.getResourcesBuyers().setDataStatus("");
					} else {
						ebook = new Ebook(rowValues[0], null, rowValues[2],
								rowValues[3], rowValues[4], rowValues[5],
								rowValues[6], rowValues[7], version,
								rowValues[9], rowValues[10], rowValues[12],
								rowValues[13], rowValues[11], openAccess,
								database, null, new ResourcesBuyers(), null);
						ebook.setTempNote(isbn);
						ebook.getResourcesBuyers().setDataStatus("");
					}

					if (StringUtils.isNotBlank(rowValues[15])) {
						if (database == null) {
							objStatus.append("資料庫代碼錯誤<br>");
						}
					} else {
						objStatus.append("資料庫代碼空白<br>");
					}
				}

				if (StringUtils.isBlank(ebook.getBookName())) {
					objStatus.append("書名必須填寫<br>");
				}

				if (!isURL(ebook.getUrl())) {
					objStatus.append("url不正確<br>");
				}

				if (StringUtils.isNotEmpty(ebook.getCnClassBzStr())) {
					if (!NumberUtils.isDigits(ebook.getCnClassBzStr())
							|| ebook.getCnClassBzStr().length() != 3) {
						objStatus.append("中國圖書分類碼不正確<br>");
					}
				}

				if (StringUtils.isNotEmpty(ebook.getBookInfoIntegral())) {
					if (!NumberUtils.isDigits(ebook.getBookInfoIntegral())
							|| ebook.getBookInfoIntegral().length() != 3) {
						objStatus.append("杜威十進位分類號不正確<br>");
					}
				}

				if (ebook.getIsbn() != null) {
					if (ISBN_Validator.isIsbn(ebook.getIsbn())) {
						if (hasRepeatIsbn(isbn, null)) {
							resStatus.append("已有相同ISBN<br>");
						}

						if (dbHasRepeatIsbn(isbn, null, ebook.getDatabase())) {
							resStatus.append("DB有相同ISBN<br>");
						}

						if (checkRepeatIsbn.containsKey(ebook.getIsbn())) {
							resStatus.append("清單有同ISBN資源<br>");

							String status = originalData
									.get(checkRepeatIsbn.get(ebook.getIsbn()))
									.getResourcesBuyers().getDataStatus()
									+ "清單有同ISBN資源<br>";
							originalData
									.get(checkRepeatIsbn.get(ebook.getIsbn()))
									.getResourcesBuyers()
									.setDataStatus(
											status.replace(
													"清單有同ISBN資源<br>清單有同ISBN資源<br>",
													"清單有同ISBN資源<br>"));
						}

						checkRepeatIsbn.put(ebook.getIsbn(),
								originalData.size());
					} else {
						objStatus.append("ISBN不正確<br>");
					}
				} else {
					if (StringUtils.isNotEmpty(isbn)) {
						if (ISBN_Validator.isIsbn(isbn)) {
							if (hasRepeatIsbn(isbn, null)) {
								resStatus.append("已有相同ISBN<br>");
							}

							if (dbHasRepeatIsbn(isbn, null, ebook.getDatabase())) {
								resStatus.append("DB有相同ISBN<br>");
							}

							if (checkRepeatIsbn.containsKey(Long.parseLong(isbn
									.replace("-", "")))) {
								resStatus.append("清單有同ISBN資源<br>");

								String status = originalData
										.get(checkRepeatIsbn.get(Long
												.parseLong(isbn
														.replace("-", ""))))
										.getResourcesBuyers().getDataStatus()
										+ "清單有同ISBN資源<br>";
								originalData
										.get(checkRepeatIsbn.get(Long
												.parseLong(isbn
														.replace("-", ""))))
										.getResourcesBuyers()
										.setDataStatus(
												status.replace(
														"清單有同ISBN資源<br>清單有同ISBN資源<br>",
														"清單有同ISBN資源<br>"));
							}

							checkRepeatIsbn.put(
									Long.parseLong(isbn.replace("-", "")),
									originalData.size());
						} else {
							objStatus.append("ISBN不正確<br>");
						}
					} else {
						if (StringUtils.isNotBlank(ebook.getBookName())) {
							if (hasRepeatName(ebook.getBookName(), null)) {
								resStatus.append("已有無ISBN同名資源<br>");
							}

							if (dbHasRepeatName(ebook.getBookName(), null,
									ebook.getDatabase())) {
								resStatus.append("Db有無ISBN同名資源<br>");
							}

							if (checkRepeatName
									.containsKey(ebook.getBookName())) {
								resStatus.append("清單有無ISBN同名資源<br>");

								String status = originalData
										.get(checkRepeatName.get(ebook
												.getBookName()))
										.getResourcesBuyers().getDataStatus()
										+ "清單有無ISBN同名資源<br>";
								originalData
										.get(checkRepeatName.get(ebook
												.getBookName()))
										.getResourcesBuyers()
										.setDataStatus(
												status.replace(
														"清單有無ISBN同名資源<br>清單有無ISBN同名資源<br>",
														"清單有無ISBN同名資源<br>"));
							}

							checkRepeatName.put(ebook.getBookName(),
									originalData.size());
						}
					}
				}

				ebook.getResourcesBuyers().setDataStatus(resStatus.toString());
				ebook.setDataStatus(objStatus.toString());

				if (StringUtils.isEmpty(ebook.getDataStatus())) {
					ebook.setDataStatus("正常");
					++normal;
				}

				originalData.add(ebook);
			}

			List<Ebook> excelData = new ArrayList<Ebook>(originalData);

			DataSet<Ebook> ds = initDataSet();
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

		DataSet<Ebook> ds = initDataSet();
		ds.getPager().setTotalRecord((long) importList.size());

		int first = ds.getPager().getOffset();
		int last = first + ds.getPager().getRecordPerPage();

		int i = 0;
		while (i < importList.size()) {
			if (i >= first && i < last) {
				ds.getResults().add((Ebook) importList.get(i));
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
					ds.getResults().add((Ebook) importList.get(j));
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
					if (((Ebook) importList.get(getEntity().getImportItem()[0]))
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
					if (((Ebook) importList.get(getEntity().getImportItem()[i]))
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
				ebook = (Ebook) importList.get(index);

				if (ebook.getIsbn() == null
						&& StringUtils.isNotEmpty(ebook.getTempNote())) {
					ebook.setIsbn(Long.parseLong(ebook.getTempNote().replace(
							"-", "")));
				}

				ebookService.save(ebook, getLoginUser());
				ebook.setDataStatus("已匯入");
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
		getEntity().setReportFile("ebook_sample.xlsx");

		// Create blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();
		// Create a blank sheet
		XSSFSheet spreadsheet = workbook.createSheet("ebook");
		// Create row object
		XSSFRow row;
		// This data needs to be written (Object[])
		Map<String, Object[]> empinfo = new LinkedHashMap<String, Object[]>();

		if (StringUtils.isBlank(getEntity().getOption())) {
			getEntity().setOption("package");
		} else {
			if (!getEntity().getOption().equals("package")
					&& !getEntity().getOption().equals("individual")) {
				getEntity().setOption("package");
			}
		}

		if (getEntity().getOption().equals("package")) {
			empinfo.put("1", new Object[] { "書名", "ISBN/13碼", "出版社", "第一作者",
					"次要作者", "系列叢書名", "出版日期", "語文", "版本", "中國圖書分類碼", "杜威十進位分類號",
					"URL", "類型", "出版地", "公開資源", "資料庫UUID" });

			empinfo.put(
					"2",
					new Object[] {
							"Ophthalmic clinical procedures：a multimedia guide",
							"978-0-08-044978-4", "Elsevier(ClinicalKey)",
							"Frank Eperjesi", "Hannah Bartlett & Mark Dunne",
							"N/A", "2008/2/7", "eng", "1", "410", "617",
							"https://lib3.cgmh.org.tw/cgi-bin/er4/browse.cgi",
							"醫學", "Netherlands", "是",
							"7e123992-bc2d-4edd-a98a-0a290738bea8" });
			empinfo.put(
					"3",
					new Object[] {
							"C嘉魔法彩繪",
							"978-9-88-807293-4",
							"青森文化",
							"陳嘉慧",
							"N/A",
							"N/A",
							"2011",
							"cht",
							"N/A",
							"961",
							"752",
							"http://tpml.ebook.hyread.com.tw/bookDetail.jsp?id=40258",
							"醫學", "Taiwan", "是",
							"7e123992-bc2d-4edd-a98a-0a290738bea8" });
		} else {
			empinfo.put("1", new Object[] { "書名", "ISBN/13碼", "出版社", "第一作者",
					"次要作者", "系列叢書名", "出版日期", "語文", "版本", "中國圖書分類碼", "杜威十進位分類號",
					"URL", "類型", "出版地", "公開資源", "起始日", "到期日", "資源類型", "購買人名稱" });

			empinfo.put(
					"2",
					new Object[] {
							"Ophthalmic clinical procedures：a multimedia guide",
							"978-0-08-044978-4", "Elsevier(ClinicalKey)",
							"Frank Eperjesi", "Hannah Bartlett & Mark Dunne",
							"N/A", "2008/2/7", "eng", "1", "410", "617",
							"https://lib3.cgmh.org.tw/cgi-bin/er4/browse.cgi",
							"醫學", "Netherlands", "是", "2000", "2005", "租貸",
							"疾病管制署、高雄醫學院附設醫院" });
			empinfo.put("3", new Object[] { "C嘉魔法彩繪", "978-9-88-807293-4",
					"青森文化", "陳嘉慧", "N/A", "N/A", "2011", "cht", "N/A", "961",
					"752",
					"http://tpml.ebook.hyread.com.tw/bookDetail.jsp?id=40258",
					"醫學", "Taiwan", "是", "N/A", "N/A", "買斷", "高雄醫學院附設醫院" });
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

	protected boolean isURL(String url) {
		return ESAPI.validator().isValidInput("Ebook URL", url, "URL",
				Integer.MAX_VALUE, false);
	}

	protected boolean hasEntity() throws Exception {
		if (getEntity().getSerNo() == null) {
			getEntity().setSerNo(-1L);
			return false;
		}

		ebook = ebookService.getBySerNo(getEntity().getSerNo());
		if (ebook == null) {
			return false;
		}

		return true;
	}

	protected boolean hasRepeatIsbn(String isbn, Long serNo) throws Exception {
		List<Long> results = ebookService.getSerNosByIsbn(Long.parseLong(isbn
				.trim().replace("-", "")));
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

	protected boolean dbHasRepeatIsbn(String isbn, Long serNo, Database db)
			throws Exception {
		if (db != null && db.hasSerNo()) {
			List<Long> results = ebookService.getSerNosInDbByIsbn(
					Long.parseLong(isbn.trim().replace("-", "")), db);
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

	protected boolean hasRepeatName(String bookName, Long serNo)
			throws Exception {
		List<Long> results = ebookService.getSerNosByName(bookName.trim());
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

	protected boolean dbHasRepeatName(String bookName, Long serNo, Database db)
			throws Exception {
		if (db != null && db.hasSerNo()) {
			List<Long> results = ebookService.getSerNosInDbByName(
					bookName.trim(), db);
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

	protected void setOwners() {
		if (ebook.getDatabase() != null && ebook.getDatabase().hasSerNo()) {
			getRequest().setAttribute(
					"referenceOwners",
					databaseService
							.getResOwners(ebook.getDatabase().getSerNo()));
		} else {
			getRequest().setAttribute("referenceOwners",
					ebookService.getResOwners(ebook.getSerNo()));
		}
	}

	@SuppressWarnings("rawtypes")
	protected Object getEnum(String[] values, Class toClass) {
		return enumConverter.convertFromString(null, values, toClass);
	}
}
