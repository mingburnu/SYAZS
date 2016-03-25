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
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDateTime;
import org.owasp.esapi.ESAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.common.collect.Lists;
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

	@Override
	protected void validateSave() throws Exception {
		if (StringUtils.isBlank(getEntity().getBookName())) {
			errorMessages.add("書名不得空白");
		}

		if (StringUtils.isNotBlank(getRequest().getParameter("entity.isbn"))) {
			String isbn = getRequest().getParameter("entity.isbn");
			if (!ISBN_Validator.isIsbn13(isbn)
					&& !ISBN_Validator.isIsbn10(isbn)) {
				errorMessages.add("ISBN不正確");
			}
		}

		if (!isURL(getEntity().getUrl())) {
			errorMessages.add("URL必須填寫");
		}

		if (StringUtils.isNotEmpty(getRequest().getParameter("entity.pubDate"))) {
			if (getEntity().getPubDate() == null) {
				errorMessages.add("出版日不正確");
			}
		}

		if (!getEntity().getDatabase().hasSerNo()) {
			if (StringUtils.isNotEmpty(getRequest().getParameter(
					"entity.resourcesBuyers.startDate"))) {
				if (getEntity().getResourcesBuyers().getStartDate() == null) {
					errorMessages.add("起始日不正確");
				}
			}

			if (StringUtils.isNotEmpty(getRequest().getParameter(
					"entity.resourcesBuyers.maturityDate"))) {
				if (getEntity().getResourcesBuyers().getMaturityDate() == null) {
					errorMessages.add("到期日不正確");
				}
			}

			if (getEntity().getResourcesBuyers().getStartDate() != null
					&& getEntity().getResourcesBuyers().getMaturityDate() != null) {
				if (getEntity()
						.getResourcesBuyers()
						.getStartDate()
						.isAfter(
								getEntity().getResourcesBuyers()
										.getMaturityDate())) {
					errorMessages.add("到期日早於起始日");
				}
			}

			if (ArrayUtils.isEmpty(getEntity().getRefSerNo())) {
				errorMessages.add("至少選擇一筆以上訂閱單位");
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

			if (StringUtils
					.isNotBlank(getRequest().getParameter("entity.isbn"))) {
				String isbn = getRequest().getParameter("entity.isbn");
				if (!ISBN_Validator.isIsbn13(isbn)
						&& !ISBN_Validator.isIsbn10(isbn)) {
					errorMessages.add("ISBN不正確");
				}
			}

			if (!isURL(getEntity().getUrl())) {
				errorMessages.add("URL必須填寫");
			}

			if (StringUtils.isNotEmpty(getRequest().getParameter(
					"entity.pubDate"))) {
				if (getEntity().getPubDate() == null) {
					errorMessages.add("出版日不正確");
				}
			}

			if (!getEntity().getDatabase().hasSerNo()) {
				if (StringUtils.isNotEmpty(getRequest().getParameter(
						"entity.resourcesBuyers.startDate"))) {
					if (getEntity().getResourcesBuyers().getStartDate() == null) {
						errorMessages.add("起始日不正確");
					}
				}

				if (StringUtils.isNotEmpty(getRequest().getParameter(
						"entity.resourcesBuyers.maturityDate"))) {
					if (getEntity().getResourcesBuyers().getMaturityDate() == null) {
						errorMessages.add("到期日不正確");
					}
				}

				if (getEntity().getResourcesBuyers().getStartDate() != null
						&& getEntity().getResourcesBuyers().getMaturityDate() != null) {
					if (getEntity()
							.getResourcesBuyers()
							.getStartDate()
							.isAfter(
									getEntity().getResourcesBuyers()
											.getMaturityDate())) {
						errorMessages.add("到期日早於起始日");
					}
				}

				if (ArrayUtils.isEmpty(getEntity().getRefSerNo())) {
					errorMessages.add("至少選擇一筆以上訂閱單位");
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
			String isbn = getRequest().getParameter("entity.isbn");
			if (StringUtils.isNotEmpty(isbn)) {
				if (ISBN_Validator.isIsbn13(isbn.trim())) {
					getEntity().setIsbn(
							Long.parseLong(isbn.trim().replaceAll("-", "")));
				} else if (ISBN_Validator.isIsbn10(isbn.trim())) {
					getEntity()
							.setIsbn(
									Long.parseLong(ISBN_Validator.toIsbn13(isbn
											.trim())));
				} else {
					getEntity().setIsbn(Long.MIN_VALUE);
				}
			}
		}

		DataSet<Ebook> ds = ebookService.getByRestrictions(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			Double lastPage = Math.ceil(ds.getPager().getTotalRecord()
					.doubleValue()
					/ ds.getPager().getRecordPerPage().doubleValue());
			ds.getPager().setCurrentPage(lastPage.intValue());
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
			String isbn = getRequest().getParameter("entity.isbn");
			if (StringUtils.isNotBlank(isbn)) {
				if (ISBN_Validator.isIsbn10(isbn)) {
					isbn = ISBN_Validator.toIsbn13(isbn);
				}

				getEntity().setIsbn(
						Long.parseLong(isbn.trim().replace("-", "")));
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
			String isbn = getRequest().getParameter("entity.isbn");
			if (StringUtils.isNotBlank(isbn)) {
				if (ISBN_Validator.isIsbn10(isbn)) {
					isbn = ISBN_Validator.toIsbn13(isbn);
				}

				getEntity().setIsbn(
						Long.parseLong(isbn.trim().replace("-", "")));
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

		if (StringUtils.isNotBlank(isbn)) {
			if (ISBN_Validator.isIsbn13(isbn)) {
				if (hasRepeatIsbn(isbn, serNo)) {
					getRequest().setAttribute("tip", "已有相同ISBN");
				}

				if (dbHasRepeatIsbn(isbn, serNo, database)) {
					getRequest().setAttribute("repeat", "資料庫有重複資源");
				}
			}

			if (ISBN_Validator.isIsbn10(isbn)) {
				log.info(isbn);
				if (hasRepeatIsbn(ISBN_Validator.toIsbn13(isbn), serNo)) {
					getRequest().setAttribute("tip", "已有相同ISBN");
				}

				if (dbHasRepeatIsbn(ISBN_Validator.toIsbn13(isbn), serNo,
						database)) {
					getRequest().setAttribute("repeat", "資料庫有重複資源");
				}
			}
		} else {
			if (StringUtils.isNotBlank(getEntity().getBookName())) {
				if (hasRepeatName(getEntity().getBookName(), serNo)) {
					getRequest().setAttribute("tip", "相同名稱且無ISBN資源存在");
				}

				if (dbHasRepeatName(getEntity().getBookName(), serNo, database)) {
					getRequest().setAttribute("repeat", "資料庫有相同名稱且無ISBN資源存在");
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
					switch (firstRow.getCell(n).getCellType()) {
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
			int tip = 0;

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
						switch (row.getCell(k).getCellType()) {
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

				List<String> errorList = Lists.newArrayList();
				StringBuilder resStatus = new StringBuilder();

				boolean openAccess = false;
				if (StringUtils.isNotBlank(rowValues[14])) {
					if (rowValues[11].equals("1")
							|| rowValues[14].toLowerCase().equals("yes")
							|| rowValues[14].toLowerCase().equals("true")
							|| rowValues[14].equals("是")
							|| rowValues[14].equals("真")) {
						openAccess = true;
					}
				}

				Long isbn = (Long) toNumber(rowValues[1].trim(), Long.class);
				LocalDateTime pubDate = toLocalDateTime(rowValues[6].trim());

				if (length > 16) {
					Category category = null;
					if (NumberUtils.isDigits(rowValues[17])) {
						category = Category.getByToken(Integer
								.parseInt(rowValues[17]));
					} else {
						category = (Category) toEnum(rowValues[17].trim(),
								Category.class);
					}

					if (category == null) {
						category = Category.未註明;
					}

					resourcesBuyers = new ResourcesBuyers(
							toLocalDateTime(rowValues[15].trim()),
							toLocalDateTime(rowValues[16].trim()), category);

					if (StringUtils.isNotEmpty(rowValues[15])
							&& resourcesBuyers.getStartDate() == null) {
						errorList.add("起始日錯誤");
					}

					if (StringUtils.isNotEmpty(rowValues[16])
							&& resourcesBuyers.getMaturityDate() == null) {
						errorList.add("到期日錯誤");
					}

					if (resourcesBuyers.getStartDate() != null
							&& resourcesBuyers.getMaturityDate() != null) {
						if (resourcesBuyers.getStartDate().isAfter(
								resourcesBuyers.getMaturityDate())) {
							errorList.add("到期日早於起始日");
						}
					}

					Set<ReferenceOwner> owners = new HashSet<ReferenceOwner>();
					if (StringUtils.isNotBlank(rowValues[18].replace(",", ""))) {
						String[] names = rowValues[18].trim().split(",");

						int j = 0;
						while (j < names.length) {
							if (StringUtils.isNotBlank(names[j])) {
								referenceOwner = new ReferenceOwner();
								referenceOwner.setName(names[j].trim());
								referenceOwner.setSerNo(referenceOwnerService
										.getRefSerNoByName(referenceOwner
												.getName()));

								if (referenceOwner.getSerNo() == 0) {
									errorList.add(referenceOwner.getName()
											+ "不存在");
								}
								owners.add(referenceOwner);
							}
							j++;
						}
					} else {
						errorList.add("沒有訂閱單位");
					}

					ebook = new Ebook(rowValues[0], isbn, rowValues[2],
							rowValues[3], rowValues[4], rowValues[5], pubDate,
							rowValues[7], rowValues[8], rowValues[9],
							rowValues[10], rowValues[12], rowValues[13],
							rowValues[11], openAccess, null, null,
							resourcesBuyers, owners);
					ebook.setTempNotes(new String[] { rowValues[1],
							rowValues[6], rowValues[15], rowValues[16] });
					ebook.getResourcesBuyers().setDataStatus("");
				} else {
					ebook = new Ebook(rowValues[0], isbn, rowValues[2],
							rowValues[3], rowValues[4], rowValues[5], pubDate,
							rowValues[7], rowValues[8], rowValues[9],
							rowValues[10], rowValues[12], rowValues[13],
							rowValues[11], openAccess, null, null,
							new ResourcesBuyers(), null);
					ebook.setTempNotes(new String[] { rowValues[1],
							rowValues[6], null, null });
					ebook.getResourcesBuyers().setDataStatus("");

					if (StringUtils.isNotBlank(rowValues[15])) {
						database = databaseService.getByUUID(rowValues[15]
								.trim());
						if (database != null) {
							ebook.setDatabase(database);
						} else {
							errorList.add("資料庫代碼錯誤");
							database = new Database();
							database.setUuIdentifier(rowValues[15]);
							ebook.setDatabase(database);
						}
					} else {
						errorList.add("資料庫代碼空白");
						database = new Database();
						database.setUuIdentifier(rowValues[15]);
						ebook.setDatabase(database);
					}
				}

				if (StringUtils.isBlank(ebook.getBookName())) {
					errorList.add("書名必須填寫");
				}

				if (!isURL(ebook.getUrl())) {
					errorList.add("url不正確");
				}

				if (StringUtils.isNotEmpty(ebook.getTempNotes()[1])
						&& ebook.getPubDate() == null) {
					errorList.add("出版日錯誤");
				}

				if (StringUtils.isNotEmpty(ebook.getTempNotes()[0])) {
					if (ISBN_Validator.isIsbn13(ebook.getIsbn().toString())
							|| ISBN_Validator.isIsbn13(ebook.getTempNotes()[0])) {

						if (isbn != null) {
							if (hasRepeatIsbn(isbn.toString(), null)) {
								resStatus.append("已有相同ISBN<br>");
							}

							if (dbHasRepeatIsbn(isbn.toString(), null,
									ebook.getDatabase())) {
								resStatus.append("DB有相同ISBN<br>");
							}

							if (checkRepeatIsbn.containsKey(isbn)) {
								resStatus.append("清單有同ISBN資源<br>");

								String status = originalData
										.get(checkRepeatIsbn.get(isbn))
										.getResourcesBuyers().getDataStatus()
										+ "清單有同ISBN資源<br>";
								originalData
										.get(checkRepeatIsbn.get(isbn))
										.getResourcesBuyers()
										.setDataStatus(
												status.replace(
														"清單有同ISBN資源<br>清單有同ISBN資源<br>",
														"清單有同ISBN資源<br>"));
							}

							checkRepeatIsbn.put(isbn, originalData.size());

						} else {
							if (hasRepeatIsbn(ebook.getTempNotes()[0], null)) {
								resStatus.append("已有相同ISBN<br>");
							}

							if (dbHasRepeatIsbn(ebook.getTempNotes()[0], null,
									ebook.getDatabase())) {
								resStatus.append("DB有相同ISBN<br>");
							}

							if (checkRepeatIsbn.containsKey(Long
									.parseLong(ebook.getTempNotes()[0].replace(
											"-", "")))) {
								resStatus.append("清單有同ISBN資源<br>");

								String status = originalData
										.get(checkRepeatIsbn.get(Long
												.parseLong(ebook.getTempNotes()[0]
														.replace("-", ""))))
										.getResourcesBuyers().getDataStatus()
										+ "清單有同ISBN資源<br>";
								originalData
										.get(checkRepeatIsbn.get(Long
												.parseLong(ebook.getTempNotes()[0]
														.replace("-", ""))))
										.getResourcesBuyers()
										.setDataStatus(
												status.replace(
														"清單有同ISBN資源<br>清單有同ISBN資源<br>",
														"清單有同ISBN資源<br>"));
							}

							checkRepeatIsbn.put(Long.parseLong(ebook
									.getTempNotes()[0].replace("-", "")),
									originalData.size());
						}
					} else {
						errorList.add("ISBN不正確");
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

						if (checkRepeatName.containsKey(ebook.getBookName())) {
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

				ebook.getResourcesBuyers().setDataStatus(resStatus.toString());

				if (errorList.size() != 0) {
					ebook.setDataStatus(errorList.toString().replace("[", "")
							.replace("]", ""));
				} else {
					ebook.setDataStatus("正常");
					++normal;
				}

				originalData.add(ebook);
			}

			Iterator<Ebook> iterator = originalData.iterator();
			while (iterator.hasNext()) {
				ebook = iterator.next();
				if (StringUtils.isNotBlank(ebook.getResourcesBuyers()
						.getDataStatus()) && ebook.getDataStatus().equals("正常")) {
					++tip;
				}
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
			getSession().put("insert", 0);
			getSession().put("tip", tip);
			getSession().put("clazz", this.getClass());

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

		DataSet<Ebook> ds = initDataSet();
		ds.getPager().setTotalRecord((long) importList.size());

		int first = ds.getPager().getOffset();
		int last = first + ds.getPager().getRecordPerPage();

		int index = first;
		while (index >= first && index < last) {
			if (index < importList.size()) {
				ds.getResults().add((Ebook) importList.get(index));
			} else {
				break;
			}
			index++;
		}

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			Double lastPage = Math.ceil(ds.getPager().getTotalRecord()
					.doubleValue()
					/ ds.getPager().getRecordPerPage().doubleValue());
			ds.getPager().setCurrentPage(lastPage.intValue());
			first = ds.getPager().getOffset();
			last = first + ds.getPager().getRecordPerPage();

			index = first;
			while (index >= first && index < last) {
				if (index < importList.size()) {
					ds.getResults().add((Ebook) importList.get(index));
				} else {
					break;
				}
				index++;
			}
		}

		setDs(ds);
		return QUEUE;
	}

	@SuppressWarnings("unchecked")
	public String addAllItem() {
		List<?> importList = (List<?>) getSession().get("importList");
		if (importList == null) {
			return IMPORT;
		}

		Set<Integer> checkItemSet = new TreeSet<Integer>();
		if (getSession().containsKey("checkItemSet")) {
			checkItemSet = (Set<Integer>) getSession().get("checkItemSet");
		}

		Integer i = 0;
		while (i < importList.size()) {
			ebook = (Ebook) importList.get(i);
			if (ebook.getDataStatus().equals("正常")) {
				checkItemSet.add(i);
			}
			i++;
		}

		getSession().put("allChecked", true);
		getSession().put("checkItemSet", checkItemSet);
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

	public String allUncheckedItem() {
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
					checkItemSet.remove(getEntity().getImportItem()[i]);

					if (checkItemSet.size() == 0) {
						break;
					}
				}
				i++;
			}
		}

		getSession().put("checkItemSet", checkItemSet);
		return QUEUE;
	}

	public String removeAllItem() {
		if (getSession().get("importList") == null) {
			return IMPORT;
		}

		Set<Integer> checkItemSet = new TreeSet<Integer>();
		getSession().put("allChecked", false);
		getSession().put("checkItemSet", checkItemSet);
		return QUEUE;
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
						&& StringUtils.isNotBlank(ebook.getTempNotes()[0])) {
					ebook.setIsbn(Long.parseLong(ebook.getTempNotes()[0].trim()
							.replace("-", "")));
				}

				ebookService.save(ebook, getLoginUser());
				ebook.setDataStatus("已匯入");
				++successCount;
			}

			getRequest().setAttribute("successCount", successCount);
			int insert = (int) getSession().get("insert");
			getSession().put("insert", insert + successCount);
			getSession().remove("checkItemSet");
			getSession().remove("allChecked");
			return VIEW;
		} else {
			paginate();
			return QUEUE;
		}
	}

	public String backErrors() throws Exception {
		List<?> importList = (List<?>) getSession().get("importList");
		if (importList == null) {
			return IMPORT;
		}

		if (StringUtils.isBlank(getEntity().getOption())) {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		} else if (getEntity().getOption().equals("errors")) {
			getEntity().setReportFile("ebook_error.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet spreadsheet = workbook.createSheet("ebook_error");
			XSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setDataFormat(workbook.createDataFormat().getFormat("@"));

			XSSFRow row;

			Map<String, Object[]> empinfo = new LinkedHashMap<String, Object[]>();

			Integer mark = 1;
			List<?> cellNames = (List<?>) getSession().get("cellNames");

			if (cellNames.size() == 16) {
				empinfo.put("1", new Object[] { "書名", "ISBN/13碼", "出版社",
						"第一作者", "次要作者", "系列叢書名", "出版日期", "語文", "版本", "中國圖書分類碼",
						"杜威十進位分類號", "URL", "類型", "出版地", "開放近用", "資料庫UUID",
						"錯誤提示", "其他提示" });

				int i = 0;
				while (i < importList.size()) {
					ebook = (Ebook) importList.get(i);
					if (!ebook.getDataStatus().equals("正常")
							&& !ebook.getDataStatus().equals("已匯入")) {

						String tips = ebook.getResourcesBuyers()
								.getDataStatus().replace("<br>", ",");
						if (tips.length() > 0) {
							tips = tips.substring(0, tips.length() - 1);
						}

						mark = mark + 1;
						empinfo.put(
								mark.toString(),
								new Object[] { ebook.getBookName(),
										ebook.getTempNotes()[0],
										ebook.getPublishName(),
										ebook.getAutherName(),
										ebook.getAuthers(),
										ebook.getUppeName(),
										ebook.getTempNotes()[1],
										ebook.getLanguages(),
										ebook.getVersion(),
										ebook.getCnClassBzStr(),
										ebook.getBookInfoIntegral(),
										ebook.getUrl(), ebook.getStyle(),
										ebook.getPublication(),
										ebook.getOpenAccess().toString(),
										ebook.getDatabase().getUuIdentifier(),
										ebook.getDataStatus(), tips });
					}
					i++;
				}

			} else {
				empinfo.put("1", new Object[] { "書名", "ISBN/13碼", "出版社",
						"第一作者", "次要作者", "系列叢書名", "出版日期", "語文", "版本", "中國圖書分類碼",
						"杜威十進位分類號", "URL", "類型", "出版地", "開放近用", "起始日", "到期日",
						"資源類型", "訂閱單位", "錯誤提示", "其他提示" });

				int i = 0;
				while (i < importList.size()) {
					ebook = (Ebook) importList.get(i);
					if (!ebook.getDataStatus().equals("正常")
							&& !ebook.getDataStatus().equals("已匯入")) {
						List<String> ownerNames = Lists.newArrayList();
						for (ReferenceOwner owner : ebook.getReferenceOwners()) {
							ownerNames.add(owner.getName());
						}

						String tips = ebook.getResourcesBuyers()
								.getDataStatus().replace("<br>", ",");
						if (tips.length() > 0) {
							tips = tips.substring(0, tips.length() - 1);
						}

						mark = mark + 1;
						empinfo.put(
								mark.toString(),
								new Object[] {
										ebook.getBookName(),
										ebook.getTempNotes()[0],
										ebook.getPublishName(),
										ebook.getAutherName(),
										ebook.getAuthers(),
										ebook.getUppeName(),
										ebook.getTempNotes()[1],
										ebook.getLanguages(),
										ebook.getVersion(),
										ebook.getCnClassBzStr(),
										ebook.getBookInfoIntegral(),
										ebook.getUrl(),
										ebook.getStyle(),
										ebook.getPublication(),
										ebook.getOpenAccess().toString(),
										ebook.getTempNotes()[2],
										ebook.getTempNotes()[3],
										ebook.getResourcesBuyers()
												.getCategory().getCategory(),
										ownerNames.toString()
												.substring(
														1,
														ownerNames.toString()
																.length() - 1),
										ebook.getDataStatus(), tips });
					}
					i++;
				}
			}

			Set<String> keyid = empinfo.keySet();
			int rowid = 0;
			for (String key : keyid) {
				row = spreadsheet.createRow(rowid++);
				Object[] objectArr = empinfo.get(key);
				int cellid = 0;
				for (Object obj : objectArr) {
					Cell cell = row.createCell(cellid++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(obj.toString());
				}
			}

			ByteArrayOutputStream boas = new ByteArrayOutputStream();
			workbook.write(boas);
			getEntity().setInputStream(
					new ByteArrayInputStream(boas.toByteArray()));

		} else if (getEntity().getOption().equals("tips")) {
			getEntity().setReportFile("ebook_tip.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet spreadsheet = workbook.createSheet("ebook_tip");
			XSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setDataFormat(workbook.createDataFormat().getFormat("@"));

			for (int i = 0; i < 30; i++) {
				spreadsheet.setDefaultColumnStyle(i, cellStyle);
			}

			XSSFRow row;

			Map<String, Object[]> empinfo = new LinkedHashMap<String, Object[]>();

			Integer mark = 1;
			List<?> cellNames = (List<?>) getSession().get("cellNames");

			if (cellNames.size() == 16) {
				empinfo.put("1", new Object[] { "書名", "ISBN/13碼", "出版社",
						"第一作者", "次要作者", "系列叢書名", "出版日期", "語文", "版本", "中國圖書分類碼",
						"杜威十進位分類號", "URL", "類型", "出版地", "開放近用", "資料庫UUID",
						"物件狀態", "其他提示" });

				int i = 0;
				while (i < importList.size()) {
					ebook = (Ebook) importList.get(i);
					if (ebook.getDataStatus().equals("正常")
							|| ebook.getDataStatus().equals("已匯入")) {

						String tips = ebook.getResourcesBuyers()
								.getDataStatus().replace("<br>", ",");
						if (tips.length() > 0) {
							tips = tips.substring(0, tips.length() - 1);
						}

						mark = mark + 1;
						empinfo.put(
								mark.toString(),
								new Object[] { ebook.getBookName(),
										ebook.getTempNotes()[0],
										ebook.getPublishName(),
										ebook.getAutherName(),
										ebook.getAuthers(),
										ebook.getUppeName(),
										ebook.getTempNotes()[1],
										ebook.getLanguages(),
										ebook.getVersion(),
										ebook.getCnClassBzStr(),
										ebook.getBookInfoIntegral(),
										ebook.getUrl(), ebook.getStyle(),
										ebook.getPublication(),
										ebook.getOpenAccess().toString(),
										ebook.getDatabase().getUuIdentifier(),
										ebook.getDataStatus(), tips });
					}
					i++;
				}
			} else {
				empinfo.put("1", new Object[] { "書名", "ISBN/13碼", "出版社",
						"第一作者", "次要作者", "系列叢書名", "出版日期", "語文", "版本", "中國圖書分類碼",
						"杜威十進位分類號", "URL", "類型", "出版地", "開放近用", "起始日", "到期日",
						"資源類型", "訂閱單位", "物件狀態", "其他提示" });

				int i = 0;
				while (i < importList.size()) {
					ebook = (Ebook) importList.get(i);
					if (ebook.getDataStatus().equals("正常")
							|| ebook.getDataStatus().equals("已匯入")) {
						List<String> ownerNames = Lists.newArrayList();
						for (ReferenceOwner owner : ebook.getReferenceOwners()) {
							ownerNames.add(owner.getName());
						}

						String tips = ebook.getResourcesBuyers()
								.getDataStatus().replace("<br>", ",");
						if (tips.length() > 0) {
							tips = tips.substring(0, tips.length() - 1);
						}

						Object[] values = new Object[] {
								ebook.getBookName(),
								ebook.getTempNotes()[0],
								ebook.getPublishName(),
								ebook.getAutherName(),
								ebook.getAuthers(),
								ebook.getUppeName(),
								ebook.getTempNotes()[1],
								ebook.getLanguages(),
								ebook.getVersion(),
								ebook.getCnClassBzStr(),
								ebook.getBookInfoIntegral(),
								ebook.getUrl(),
								ebook.getStyle(),
								ebook.getPublication(),
								ebook.getOpenAccess().toString(),
								ebook.getTempNotes()[2],
								ebook.getTempNotes()[3],
								ebook.getResourcesBuyers().getCategory()
										.getCategory(),
								ownerNames.toString().substring(1,
										ownerNames.toString().length() - 1),
								ebook.getDataStatus(), tips };

						mark = mark + 1;
						empinfo.put(mark.toString(), values);
					}
					i++;
				}
			}

			Set<String> keyid = empinfo.keySet();
			int rowid = 0;
			for (String key : keyid) {
				row = spreadsheet.createRow(rowid++);
				Object[] objectArr = empinfo.get(key);
				int cellid = 0;
				for (Object obj : objectArr) {
					Cell cell = row.createCell(cellid++);
					cell.setCellValue(obj.toString());
				}
			}

			ByteArrayOutputStream boas = new ByteArrayOutputStream();
			workbook.write(boas);
			getEntity().setInputStream(
					new ByteArrayInputStream(boas.toByteArray()));

		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		return XLSX;
	}

	public String example() throws Exception {
		getEntity().setReportFile("ebook_sample.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet("ebook");
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setDataFormat(workbook.createDataFormat().getFormat("@"));

		for (int i = 0; i < 30; i++) {
			spreadsheet.setDefaultColumnStyle(i, cellStyle);
		}

		XSSFRow row;

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
					"URL", "類型", "出版地", "開放近用", "資料庫UUID" });
			empinfo.put(
					"2",
					new Object[] {
							"Ophthalmic clinical procedures：a multimedia guide",
							"978-0-08-044978-4", "Elsevier(ClinicalKey)",
							"Frank Eperjesi", "Hannah Bartlett & Mark Dunne",
							"N/A", "2008/2/7", "eng", "1", "410", "617",
							"https://lib3.cgmh.org.tw/cgi-bin/er4/browse.cgi",
							"醫學", "Netherlands", "1",
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
							"醫學", "Taiwan", "0",
							"7e123992-bc2d-4edd-a98a-0a290738bea8" });
			empinfo.put("4", new Object[] {
					"Edited by Faith C.S. Ho and P.C. Wu", "9789622093362",
					"Hong Kong University Press", "	Faith C.S. Ho", "P.C. Wu",
					"N/A", "1995-4-4", "eng", "N/A", "961", "752",
					"http://tpml.ebook.hyread.com.tw/bookDetail.jsp?id=40258",
					"醫學", "HK", "0", "7e123992-bc2d-4edd-a98a-0a290738bea8" });
		} else {
			empinfo.put("1", new Object[] { "書名", "ISBN/13碼", "出版社", "第一作者",
					"次要作者", "系列叢書名", "出版日期", "語文", "版本", "中國圖書分類碼", "杜威十進位分類號",
					"URL", "類型", "出版地", "開放近用", "起始日", "到期日", "資源類型", "訂閱單位" });

			empinfo.put("2", new Object[] {
					"Ophthalmic clinical procedures：a multimedia guide",
					"978-0-08-044978-4", "Elsevier(ClinicalKey)",
					"Frank Eperjesi", "Hannah Bartlett & Mark Dunne", "N/A",
					"2008-2-7", "eng", "1", "410", "617",
					"https://lib3.cgmh.org.tw/cgi-bin/er4/browse.cgi", "醫學",
					"Netherlands", "1", "2000/10/10", "", "0",
					"疾病管制署,高雄醫學院附設醫院" });
			empinfo.put("3", new Object[] { "C嘉魔法彩繪", "978-9-88-807293-4",
					"青森文化", "陳嘉慧", "N/A", "N/A", "2011-5-5", "cht", "N/A",
					"961", "752",
					"http://tpml.ebook.hyread.com.tw/bookDetail.jsp?id=40258",
					"醫學", "Taiwan", "0", "", "2015-10-14", "1", "高雄醫學院附設醫院" });
			empinfo.put("4", new Object[] {
					"Edited by Faith C.S. Ho and P.C. Wu", "9789622093362",
					"Hong Kong University Press", "	Faith C.S. Ho", "P.C. Wu",
					"N/A", "1995-4-4", "eng", "N/A", "961", "752",
					"http://tpml.ebook.hyread.com.tw/bookDetail.jsp?id=40258",
					"醫學", "HK", "0", "1999-5-23", "2015-10-14", "2",
					"高雄醫學院附設醫院" });
		}

		Set<String> keyid = empinfo.keySet();
		int rowid = 0;
		for (String key : keyid) {
			row = spreadsheet.createRow(rowid++);
			Object[] objectArr = empinfo.get(key);
			int cellid = 0;
			for (Object obj : objectArr) {
				Cell cell = row.createCell(cellid++);
				cell.setCellValue(obj.toString());
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
}
