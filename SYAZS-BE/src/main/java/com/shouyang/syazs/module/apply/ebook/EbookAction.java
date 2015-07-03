package com.shouyang.syazs.module.apply.ebook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.apply.customer.CustomerService;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.model.Pager;
import com.shouyang.syazs.core.web.GenericWebActionFull;
import com.shouyang.syazs.module.apply.enums.Category;
import com.shouyang.syazs.module.apply.enums.Type;
import com.shouyang.syazs.module.apply.resourcesBuyers.ResourcesBuyers;
import com.shouyang.syazs.module.apply.resourcesBuyers.ResourcesBuyersService;
import com.shouyang.syazs.module.apply.resourcesUnion.ResourcesUnion;
import com.shouyang.syazs.module.apply.resourcesUnion.ResourcesUnionService;

@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EbookAction extends GenericWebActionFull<Ebook> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9166237220863961574L;

	private String[] checkItem;

	private String[] cusSerNo;

	private File[] file;

	private String[] fileFileName;

	private String[] fileContentType;

	@Autowired
	private Ebook ebook;

	@Autowired
	private EbookService ebookService;

	@Autowired
	private ResourcesUnion resourcesUnion;

	@Autowired
	private ResourcesUnionService resourcesUnionService;

	@Autowired
	private ResourcesBuyers resourcesBuyers;

	@Autowired
	private ResourcesBuyersService resourcesBuyersService;

	@Autowired
	private Customer customer;

	@Autowired
	private CustomerService customerService;

	private String[] importSerNos;

	@Override
	protected void validateSave() throws Exception {
		List<Category> categoryList = new ArrayList<Category>(
				Arrays.asList(Category.values()));
		categoryList.remove(categoryList.size() - 1);

		List<Type> typeList = new ArrayList<Type>(Arrays.asList(Type.values()));

		if (StringUtils.isBlank(getEntity().getBookName())) {
			errorMessages.add("書名不得空白");
		}

		String isbn = getRequest().getParameter("isbn");
		if (StringUtils.isBlank(isbn)
				|| !NumberUtils.isDigits(isbn.trim().replace("-", ""))) {
			errorMessages.add("ISBN必須填寫數字");
		} else {
			if (!isIsbn(Long.parseLong(isbn.trim().replace("-", "")))) {
				errorMessages.add("ISBN不正確");
			} else {
				if (ebookService.getEbkSerNoByIsbn(Long.parseLong(isbn.trim()
						.replace("-", ""))) != 0) {
					errorMessages.add("ISBN不可重複");
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

		if (ArrayUtils.isEmpty(cusSerNo)) {
			errorMessages.add("至少選擇一筆以上購買單位");
		} else {
			int i = 0;
			while (i < cusSerNo.length) {
				if (!NumberUtils.isDigits(String.valueOf(cusSerNo[i]))
						|| Long.parseLong(cusSerNo[i]) < 1
						|| customerService.getBySerNo(Long
								.parseLong(cusSerNo[i])) == null) {
					errorMessages.add(cusSerNo[i] + "為不可利用的流水號");
				}
				i++;
			}
		}

		boolean isLegalCategory = false;
		for (int i = 0; i < categoryList.size(); i++) {
			if (getRequest().getParameter("rCategory") != null
					&& getRequest().getParameter("rCategory").equals(
							categoryList.get(i).getCategory())) {
				isLegalCategory = true;
			}
		}

		if (isLegalCategory) {
			getRequest().setAttribute("rCategory",
					getRequest().getParameter("rCategory"));
		} else {
			errorMessages.add("資源類型錯誤");
		}

		boolean isLegalType = false;
		for (int i = 0; i < categoryList.size(); i++) {
			if (getRequest().getParameter("rType") != null
					&& getRequest().getParameter("rType").equals(
							typeList.get(i).getType())) {
				isLegalType = true;
			}
		}

		if (isLegalType) {
			getRequest().setAttribute("rType",
					getRequest().getParameter("rType"));
		} else {
			errorMessages.add("資源種類錯誤");
		}
	}

	@Override
	protected void validateUpdate() throws Exception {
		List<Category> categoryList = new ArrayList<Category>(
				Arrays.asList(Category.values()));
		categoryList.remove(categoryList.size() - 1);

		List<Type> typeList = new ArrayList<Type>(Arrays.asList(Type.values()));

		if (!hasEntity()) {
			errorMessages.add("Target must not be null");
		}

		if (StringUtils.isBlank(getEntity().getBookName())) {
			errorMessages.add("書名不得空白");
		}

		String isbn = getRequest().getParameter("isbn");
		if (StringUtils.isBlank(isbn)
				|| !NumberUtils.isDigits(isbn.trim().replace("-", ""))) {
			errorMessages.add("ISBN必須填寫數字");
		} else {
			if (!isIsbn(Long.parseLong(isbn.trim().replace("-", "")))) {
				errorMessages.add("ISBN不正確");
			} else {
				long ebkSerNo = ebookService.getEbkSerNoByIsbn(Long
						.parseLong(isbn.trim().replace("-", "")));
				if (ebkSerNo != 0 && ebkSerNo != getEntity().getSerNo()) {
					errorMessages.add("ISBN不可重複");
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

		if (ArrayUtils.isEmpty(cusSerNo)) {
			errorMessages.add("至少選擇一筆以上購買單位");
		} else {
			int i = 0;
			while (i < cusSerNo.length) {
				if (!NumberUtils.isDigits(String.valueOf(cusSerNo[i]))
						|| Long.parseLong(cusSerNo[i]) < 1
						|| customerService.getBySerNo(Long
								.parseLong(cusSerNo[i])) == null) {
					errorMessages.add(cusSerNo[i] + "為不可利用的流水號");
				}
				i++;
			}
		}

		boolean isLegalCategory = false;
		for (int i = 0; i < categoryList.size(); i++) {
			if (getRequest().getParameter("rCategory") != null
					&& getRequest().getParameter("rCategory").equals(
							categoryList.get(i).getCategory())) {
				isLegalCategory = true;
			}
		}

		if (isLegalCategory) {
			getRequest().setAttribute("rCategory",
					getRequest().getParameter("rCategory"));
		} else {
			errorMessages.add("資源類型錯誤");
		}

		boolean isLegalType = false;
		for (int i = 0; i < categoryList.size(); i++) {
			if (getRequest().getParameter("rType") != null
					&& getRequest().getParameter("rType").equals(
							typeList.get(i).getType())) {
				isLegalType = true;
			}
		}

		if (isLegalType) {
			getRequest().setAttribute("rType",
					getRequest().getParameter("rType"));
		} else {
			errorMessages.add("資源種類錯誤");
		}
	}

	@Override
	protected void validateDelete() throws Exception {
		if (ArrayUtils.isEmpty(checkItem)) {
			errorMessages.add("請選擇一筆或一筆以上的資料");
		} else {
			int i = 0;
			while (i < checkItem.length) {
				if (!NumberUtils.isDigits(String.valueOf(checkItem[i]))
						|| Long.parseLong(checkItem[i]) < 1
						|| ebookService
								.getBySerNo(Long.parseLong(checkItem[i])) == null) {
					errorMessages.add(checkItem[i] + "為不可利用的流水號");
				}
				i++;
			}
		}
	}

	@Override
	public String edit() throws Exception {
		List<Category> categoryList = new ArrayList<Category>(
				Arrays.asList(Category.values()));
		categoryList.remove(categoryList.size() - 1);
		getRequest().setAttribute("categoryList", categoryList);

		List<Type> typeList = new ArrayList<Type>(Arrays.asList(Type.values()));
		getRequest().setAttribute("typeList", typeList);

		getRequest().setAttribute("allCustomers",
				customerService.getAllCustomers());
		if (getEntity().getSerNo() != null) {
			ebook = ebookService.getBySerNo(getEntity().getSerNo());

			if (ebook != null) {
				Iterator<ResourcesUnion> iterator = resourcesUnionService
						.getResourcesUnionsByObj(getEntity(), Ebook.class)
						.iterator();

				List<Customer> customers = new ArrayList<Customer>();

				while (iterator.hasNext()) {
					resourcesUnion = iterator.next();
					customer = resourcesUnion.getCustomer();
					if (customer != null) {
						customers.add(customer);
					}
				}

				resourcesBuyers = resourcesUnion.getResourcesBuyers();
				getRequest().setAttribute("rCategory",
						resourcesBuyers.getrCategory().getCategory());
				getRequest().setAttribute("rType",
						resourcesBuyers.getrType().getType());
				ebook.setCustomers(customers);
			}

			setEntity(ebook);
		} else {
			List<Customer> customers = new ArrayList<Customer>();
			ebook.setCustomers(customers);
			setEntity(ebook);
		}
		return EDIT;
	}

	@Override
	public String list() throws Exception {
		if (StringUtils.isNotEmpty(getRequest().getParameter("option"))) {
			if (getRequest().getParameter("option").equals("entity.bookName")
					|| getRequest().getParameter("option").equals("isbn")) {
				getRequest().setAttribute("option",
						getRequest().getParameter("option"));

			} else {
				getRequest().setAttribute("option", "entity.bookName");
			}

		} else {
			getRequest().setAttribute("option", "entity.bookName");

		}

		if (StringUtils.isNotEmpty(getRequest().getParameter("isbn"))) {
			if (!NumberUtils.isDigits(getRequest().getParameter("isbn")
					.replace("-", ""))) {
				getEntity().setIsbn(0L);
			} else {
				getEntity().setIsbn(
						Long.parseLong(getRequest().getParameter("isbn")));
			}
		}

		DataSet<Ebook> ds = initDataSet();
		ds.setPager(Pager.getChangedPager(
				getRequest().getParameter("recordPerPage"), getRequest()
						.getParameter("recordPoint"), ds.getPager()));
		ds = ebookService.getByRestrictions(ds);

		List<Ebook> results = ds.getResults();

		int i = 0;
		while (i < results.size()) {
			results.get(i).setResourcesBuyers(
					resourcesUnionService.getByObjSerNo(
							results.get(i).getSerNo(), Ebook.class)
							.getResourcesBuyers());
			i++;
		}

		ds.setResults(results);
		setDs(ds);
		return LIST;
	}

	@Override
	public String save() throws Exception {
		List<Category> categoryList = new ArrayList<Category>(
				Arrays.asList(Category.values()));
		categoryList.remove(categoryList.size() - 1);

		List<Type> typeList = new ArrayList<Type>(Arrays.asList(Type.values()));

		validateSave();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			getEntity().setIsbn(
					Long.parseLong(getRequest().getParameter("isbn").trim()
							.replace("-", "")));
			ebook = ebookService.save(getEntity(), getLoginUser());

			resourcesBuyers = resourcesBuyersService.save(new ResourcesBuyers(
					getRequest().getParameter("resourcesBuyers.startDate"),
					getRequest().getParameter("resourcesBuyers.maturityDate"),
					Category.valueOf(getRequest().getParameter("rCategory")),
					Type.valueOf(getRequest().getParameter("rType")),
					getRequest().getParameter("resourcesBuyers.dbChtTitle"),
					getRequest().getParameter("resourcesBuyers.dbEngTitle")),
					getLoginUser());

			int i = 0;
			while (i < cusSerNo.length) {
				resourcesUnionService.save(
						new ResourcesUnion(customerService.getBySerNo(Long
								.parseLong(cusSerNo[i])), resourcesBuyers,
								ebook.getSerNo(), 0L, 0L), getLoginUser());

				i++;
			}

			resourcesUnion = resourcesUnionService.getByObjSerNo(
					ebook.getSerNo(), Ebook.class);
			ebook.setResourcesBuyers(resourcesUnion.getResourcesBuyers());

			List<ResourcesUnion> resourceUnions = resourcesUnionService
					.getResourcesUnionsByObj(ebook, Ebook.class);
			List<Customer> customers = new ArrayList<Customer>();

			Iterator<ResourcesUnion> iterator = resourceUnions.iterator();
			while (iterator.hasNext()) {
				resourcesUnion = iterator.next();
				customers.add(resourcesUnion.getCustomer());
			}

			ebook.setCustomers(customers);
			setEntity(ebook);
			return VIEW;
		} else {
			getRequest().setAttribute("categoryList", categoryList);
			getRequest().setAttribute("typeList", typeList);
			getRequest().setAttribute("allCustomers",
					customerService.getAllCustomers());

			getRequest()
					.setAttribute("isbn", getRequest().getParameter("isbn"));

			List<Customer> customers = new ArrayList<Customer>();
			if (cusSerNo != null && cusSerNo.length != 0) {
				int i = 0;
				while (i < cusSerNo.length) {
					customers.add(customerService.getBySerNo(Long
							.parseLong(cusSerNo[i])));
					i++;
				}
			}

			ebook = getEntity();
			ebook.setCustomers(customers);
			setEntity(ebook);
			return EDIT;
		}
	}

	@Override
	public String update() throws Exception {
		List<Category> categoryList = new ArrayList<Category>(
				Arrays.asList(Category.values()));
		categoryList.remove(categoryList.size() - 1);

		List<Type> typeList = new ArrayList<Type>(Arrays.asList(Type.values()));

		validateUpdate();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			getEntity().setIsbn(
					Long.parseLong(getRequest().getParameter("isbn").trim()
							.replace("-", "")));
			ebook = ebookService.update(getEntity(), getLoginUser());

			resourcesBuyers = resourcesUnionService.getByObjSerNo(
					ebook.getSerNo(), Ebook.class).getResourcesBuyers();
			resourcesBuyers.setStartDate(getRequest().getParameter(
					"resourcesBuyers.startDate"));
			resourcesBuyers.setMaturityDate(getRequest().getParameter(
					"resourcesBuyers.maturityDate"));
			resourcesBuyers.setrCategory(Category.valueOf(getRequest()
					.getParameter("rCategory")));
			resourcesBuyers.setrType(Type.valueOf(getRequest().getParameter(
					"rType")));
			resourcesBuyers.setDbChtTitle(getRequest().getParameter(
					"resourcesBuyers.dbChtTitle"));
			resourcesBuyers.setDbEngTitle(getRequest().getParameter(
					"resourcesBuyers.dbEngTitle"));
			resourcesBuyersService.update(resourcesBuyers, getLoginUser());

			List<ResourcesUnion> resourcesUnions = resourcesUnionService
					.getResourcesUnionsByObj(ebook, Ebook.class);

			for (int j = 0; j < cusSerNo.length; j++) {
				for (int i = 0; i < resourcesUnions.size(); i++) {
					resourcesUnion = resourcesUnions.get(i);
					if (resourcesUnion.getCustomer().getSerNo() == Long
							.parseLong(cusSerNo[j])) {
						resourcesUnions.remove(i);
					}
				}
			}

			Iterator<ResourcesUnion> iterator = resourcesUnions.iterator();
			while (iterator.hasNext()) {
				resourcesUnion = (ResourcesUnion) iterator.next();
				resourcesUnionService.deleteBySerNo(resourcesUnion.getSerNo());
			}

			int i = 0;
			while (i < cusSerNo.length) {
				if (!resourcesUnionService.isExist(ebook, Ebook.class,
						Long.parseLong(cusSerNo[i]))) {
					resourcesUnionService.save(
							new ResourcesUnion(customerService.getBySerNo(Long
									.parseLong(cusSerNo[i])), resourcesBuyers,
									ebook.getSerNo(), 0L, 0L), getLoginUser());
				}

				i++;
			}

			resourcesUnion = resourcesUnionService.getByObjSerNo(
					ebook.getSerNo(), Ebook.class);
			ebook.setResourcesBuyers(resourcesUnion.getResourcesBuyers());

			List<ResourcesUnion> resourceUnions = resourcesUnionService
					.getResourcesUnionsByObj(ebook, Ebook.class);
			List<Customer> customers = new ArrayList<Customer>();

			iterator = resourceUnions.iterator();
			while (iterator.hasNext()) {
				resourcesUnion = iterator.next();
				customers.add(resourcesUnion.getCustomer());
			}

			ebook.setCustomers(customers);
			setEntity(ebook);
			return VIEW;
		} else {
			getRequest().setAttribute("typeList", typeList);
			getRequest().setAttribute("categoryList", categoryList);
			getRequest().setAttribute("allCustomers",
					customerService.getAllCustomers());

			getRequest()
					.setAttribute("isbn", getRequest().getParameter("isbn"));

			List<Customer> customers = new ArrayList<Customer>();
			if (cusSerNo != null && cusSerNo.length != 0) {
				int i = 0;
				while (i < cusSerNo.length) {
					customers.add(customerService.getBySerNo(Long
							.parseLong(cusSerNo[i])));
					i++;
				}
			}

			ebook = getEntity();
			ebook.setCustomers(customers);
			setEntity(ebook);
			return EDIT;
		}

	}

	@Override
	public String delete() throws Exception {
		validateDelete();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			int j = 0;
			while (j < checkItem.length) {
				List<ResourcesUnion> resourcesUnions = resourcesUnionService
						.getResourcesUnionsByObj(ebookService.getBySerNo(Long
								.parseLong(checkItem[j])), Ebook.class);
				resourcesUnion = resourcesUnions.get(0);

				Iterator<ResourcesUnion> iterator = resourcesUnions.iterator();
				while (iterator.hasNext()) {
					resourcesUnion = iterator.next();
					resourcesUnionService.deleteBySerNo(resourcesUnion
							.getSerNo());
				}
				resourcesBuyersService.deleteBySerNo(resourcesUnion
						.getResourcesBuyers().getSerNo());
				ebookService.deleteBySerNo(Long.parseLong(checkItem[j]));

				j++;
			}

			DataSet<Ebook> ds = ebookService.getByRestrictions(initDataSet());
			List<Ebook> results = ds.getResults();

			int i = 0;
			while (i < results.size()) {
				results.get(i).setResourcesBuyers(
						resourcesUnionService.getByObjSerNo(
								results.get(i).getSerNo(), Ebook.class)
								.getResourcesBuyers());
				i++;
			}

			setDs(ds);
			addActionMessage("刪除成功");
			return LIST;
		} else {
			DataSet<Ebook> ds = ebookService.getByRestrictions(initDataSet());
			List<Ebook> results = ds.getResults();

			int i = 0;
			while (i < results.size()) {
				results.get(i).setResourcesBuyers(
						resourcesUnionService.getByObjSerNo(
								results.get(i).getSerNo(), Ebook.class)
								.getResourcesBuyers());
				i++;
			}

			setDs(ds);
			return LIST;
		}
	}

	public String view() throws NumberFormatException, Exception {
		getRequest().setAttribute("viewSerNo",
				getRequest().getParameter("viewSerNo"));

		if (StringUtils.isNotBlank(getRequest().getParameter("viewSerNo"))
				&& NumberUtils.isDigits(getRequest().getParameter("viewSerNo"))) {
			ebook = ebookService.getBySerNo(Long.parseLong(getRequest()
					.getParameter("viewSerNo")));
			if (ebook != null) {
				resourcesUnion = resourcesUnionService.getByObjSerNo(
						ebook.getSerNo(), Ebook.class);

				ebook.setResourcesBuyers(resourcesUnion.getResourcesBuyers());

				List<ResourcesUnion> resourceUnions = resourcesUnionService
						.getResourcesUnionsByObj(ebook, Ebook.class);
				List<Customer> customers = new ArrayList<Customer>();

				Iterator<ResourcesUnion> iterator = resourceUnions.iterator();
				while (iterator.hasNext()) {
					resourcesUnion = iterator.next();
					customers.add(resourcesUnion.getCustomer());
				}

				ebook.setCustomers(customers);
				setEntity(ebook);
			}
		}

		return VIEW;
	}

	public String imports() {
		return IMPORT;
	}

	public String queue() throws Exception {
		if (ArrayUtils.isEmpty(file) || !file[0].isFile()) {
			addActionError("請選擇檔案");
		} else {
			if (createWorkBook(new FileInputStream(file[0])) == null) {
				addActionError("檔案格式錯誤");
			}
		}

		if (!hasActionErrors()) {
			Workbook book = createWorkBook(new FileInputStream(file[0]));
			// book.getNumberOfSheets(); 判斷Excel文件有多少個sheet
			Sheet sheet = book.getSheetAt(0);

			Row firstRow = sheet.getRow(0);
			if (firstRow == null) {
				firstRow = sheet.createRow(0);
			}

			// 保存列名
			List<String> cellNames = new ArrayList<String>();
			String[] rowTitles = new String[19];
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

			LinkedHashSet<Ebook> originalData = new LinkedHashSet<Ebook>();
			Map<String, Ebook> checkRepeatRow = new LinkedHashMap<String, Ebook>();

			int normal = 0;

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null) {
					continue;
				}

				String[] rowValues = new String[19];
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

				List<Category> categoryList = new ArrayList<Category>(
						Arrays.asList(Category.values()));
				categoryList.remove(categoryList.size() - 1);

				String category = "";
				if (rowValues[13] == null || rowValues[13].trim().equals("")) {
					category = Category.未註明.getCategory();
				} else {
					boolean isLegalCategory = false;
					for (int j = 0; j < categoryList.size(); j++) {
						if (rowValues[13].trim().equals(
								categoryList.get(j).getCategory())) {
							category = categoryList.get(j).getCategory();
							isLegalCategory = true;
						}
					}

					if (!isLegalCategory) {
						category = Category.不明.getCategory();
					}
				}

				List<Type> typeList = new ArrayList<Type>(Arrays.asList(Type
						.values()));
				String type = "";
				if (rowValues[14] == null || rowValues[14].trim().equals("")) {
					type = Type.電子書.getType();
				} else {
					boolean isLegalType = false;
					for (int j = 0; j < typeList.size(); j++) {
						if (rowValues[14].trim().equals(
								typeList.get(j).getType())) {
							type = typeList.get(j).getType();
							isLegalType = true;
						}
					}

					if (!isLegalType) {
						type = Type.電子書.getType();
					}
				}

				resourcesBuyers = new ResourcesBuyers(rowValues[11],
						rowValues[12], Category.valueOf(category),
						Type.valueOf(type), rowValues[15], rowValues[16]);

				String isbn = rowValues[1].trim().replace("-", "");

				Integer version = null;
				if (NumberUtils.isNumber(rowValues[8])) {
					double d = Double.parseDouble(rowValues[8]);
					version = (int) d;
				}

				if (NumberUtils.isDigits(isbn)) {
					ebook = new Ebook(rowValues[0], Long.parseLong(isbn),
							rowValues[2], rowValues[3], rowValues[4],
							rowValues[5], rowValues[6], rowValues[7], version,
							rowValues[9], rowValues[10], "", "", "",
							resourcesBuyers, null, "");
				} else {
					ebook = new Ebook(rowValues[0], null, rowValues[2],
							rowValues[3], rowValues[4], rowValues[5],
							rowValues[6], rowValues[7], version, rowValues[9],
							rowValues[10], "", "", "", resourcesBuyers, null,
							"");
				}

				customer = new Customer();
				customer.setName(rowValues[17].trim());
				customer.setEngName(rowValues[18].trim());

				List<Customer> customers = new ArrayList<Customer>();
				customers.add(customer);
				ebook.setCustomers(customers);

				if (ebook.getIsbn() != null) {
					if (isIsbn(Long.parseLong(isbn))) {
						long ebkSerNo = ebookService.getEbkSerNoByIsbn(Long
								.parseLong(isbn));

						long cusSerNo = customerService
								.getCusSerNoByName(rowValues[17].trim());
						if (cusSerNo != 0) {
							if (ebkSerNo != 0) {
								if (resourcesUnionService.isExist(
										ebookService.getBySerNo(ebkSerNo),
										Ebook.class, cusSerNo)) {
									ebook.setExistStatus("已存在");
								}
							} else {
								if (ebook.getResourcesBuyers().getrCategory()
										.equals(Category.不明)) {
									ebook.setExistStatus("資源類型不明");
								}
							}
						} else {
							ebook.setExistStatus("無此客戶");
						}
					} else {
						ebook.setExistStatus("ISBN異常");
					}
				} else {
					ebook.setExistStatus("ISBN異常");
				}

				if (StringUtils.isNotEmpty(ebook.getCnClassBzStr())) {
					if (!NumberUtils.isDigits(ebook.getCnClassBzStr())
							|| ebook.getCnClassBzStr().length() != 3) {
						ebook.setCnClassBzStr(null);
					}
				}

				if (StringUtils.isNotEmpty(ebook.getBookInfoIntegral())) {
					if (!NumberUtils.isDigits(ebook.getBookInfoIntegral())
							|| ebook.getBookInfoIntegral().length() != 3) {
						ebook.setBookInfoIntegral(null);
					}
				}

				if (ebook.getExistStatus().equals("")) {
					ebook.setExistStatus("正常");
				}

				if (ebook.getExistStatus().equals("正常")
						&& !originalData.contains(ebook)) {

					if (checkRepeatRow.containsKey(ebook.getIsbn()
							+ customer.getName())) {
						ebook.setExistStatus("資料重複");

					} else {
						checkRepeatRow.put(
								ebook.getIsbn() + customer.getName(), ebook);
						++normal;
					}
				}

				originalData.add(ebook);
			}

			List<Ebook> excelData = new ArrayList<Ebook>(originalData);

			DataSet<Ebook> ds = initDataSet();
			List<Ebook> results = ds.getResults();

			ds.getPager().setTotalRecord((long) excelData.size());
			ds.getPager().setRecordPoint(0);

			if (excelData.size() < ds.getPager().getRecordPerPage()) {
				int i = 0;
				while (i < excelData.size()) {
					results.add(excelData.get(i));
					i++;
				}
			} else {
				int i = 0;
				while (i < ds.getPager().getRecordPerPage()) {
					results.add(excelData.get(i));
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
			return null;
		}

		clearCheckedItem();

		DataSet<Ebook> ds = initDataSet();
		ds.setPager(Pager.getChangedPager(
				getRequest().getParameter("recordPerPage"), getRequest()
						.getParameter("recordPoint"), ds.getPager()));
		ds.getPager().setTotalRecord((long) importList.size());

		int first = ds.getPager().getRecordPerPage()
				* (ds.getPager().getCurrentPage() - 1);
		int last = first + ds.getPager().getRecordPerPage();

		List<Ebook> results = new ArrayList<Ebook>();

		int i = 0;
		while (i < importList.size()) {
			if (i >= first && i < last) {
				results.add((Ebook) importList.get(i));
			}
			i++;
		}

		ds.setResults(results);
		setDs(ds);
		return QUEUE;
	}

	@SuppressWarnings("unchecked")
	public String getCheckedItem() {
		List<?> importList = (List<?>) getSession().get("importList");
		if (importList == null) {
			return null;
		}

		Set<Integer> checkItemSet = new TreeSet<Integer>();
		if (getSession().containsKey("checkItemSet")) {
			checkItemSet = (Set<Integer>) getSession().get("checkItemSet");
		}

		if (ArrayUtils.isNotEmpty(importSerNos)) {
			if (NumberUtils.isDigits(importSerNos[0])) {
				if (!checkItemSet.contains(Integer.parseInt(importSerNos[0]))) {
					if (((Ebook) importList.get(Integer
							.parseInt(importSerNos[0]))).getExistStatus()
							.equals("正常")) {
						checkItemSet.add(Integer.parseInt(importSerNos[0]));
					}
				} else {
					checkItemSet.remove(Integer.parseInt(importSerNos[0]));
				}
			}
		}

		getSession().put("checkItemSet", checkItemSet);

		return null;
	}

	public String allCheckedItem() {
		List<?> importList = (List<?>) getSession().get("importList");
		if (importList == null) {
			return null;
		}

		Set<Integer> checkItemSet = new TreeSet<Integer>();

		if (ArrayUtils.isNotEmpty(importSerNos)) {
			int i = 0;
			while (i < importSerNos.length) {
				if (NumberUtils.isDigits(importSerNos[i])) {
					if (Long.parseLong(importSerNos[i]) < importList.size()) {
						if (((Ebook) importList.get(Integer
								.parseInt(importSerNos[i]))).getExistStatus()
								.equals("正常")) {
							checkItemSet.add(Integer.parseInt(importSerNos[i]));
						}
					}

					if (checkItemSet.size() == importList.size()) {
						break;
					}
				}
				i++;
			}
		}

		getSession().put("checkItemSet", checkItemSet);
		return null;
	}

	public String clearCheckedItem() {
		if (getSession().get("importList") == null) {
			return null;
		}

		Set<Integer> checkItemSet = new TreeSet<Integer>();
		getSession().put("checkItemSet", checkItemSet);
		return null;
	}

	public String importData() throws Exception {
		List<?> importList = (List<?>) getSession().get("importList");

		if (importList == null) {
			return null;
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

				long ebkSerNo = ebookService.getEbkSerNoByIsbn(ebook.getIsbn());
				long cusSerNo = customerService.getCusSerNoByName(ebook
						.getCustomers().get(0).getName());

				if (ebkSerNo == 0) {
					resourcesBuyers = resourcesBuyersService.save(
							ebook.getResourcesBuyers(), getLoginUser());
					ebook = ebookService.save(ebook, getLoginUser());
					resourcesUnionService.save(new ResourcesUnion(
							customerService.getBySerNo(cusSerNo),
							resourcesBuyers, ebook.getSerNo(), 0L, 0L),
							getLoginUser());
				} else {
					resourcesUnion = resourcesUnionService.getByObjSerNo(
							ebkSerNo, Ebook.class);
					resourcesUnionService.save(new ResourcesUnion(
							customerService.getBySerNo(cusSerNo),
							resourcesUnion.getResourcesBuyers(), ebkSerNo, 0L,
							0L), getLoginUser());
				}

				++successCount;

			}

			getRequest().setAttribute("successCount", successCount);
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
		empinfo.put("1", new Object[] { "書名", "ISBN/13碼", "出版社", "作者",
				"authers/第二第三作者等", "uppername/系列叢書名", "電子書出版日期", "語文", "版本",
				"cnclassbzstr/中國圖書分類碼", "美國國家圖書館分類號", "startdate/起始日",
				"maturitydate/到期日", "Rcategory/資源類型", "Rtype/資源種類",
				"Dbchttitle/資料庫中文題名", "Dbengtitle/資料庫英文題名", "購買單位名稱",
				"購買單位英文名稱" });

		empinfo.put("2", new Object[] { "Ophthalmic Clinical Procedures",
				"9780080449784", "Elsevier(ClinicalKey)",
				"Frank Eperjesi & Hannah Bartlett & Mark Dunne", "", "",
				"2008/2/7", "eng", "", "001", "001", "", "", "", "電子書", "", "",
				"衛生福利部基隆醫院", "" });
		empinfo.put("3", new Object[] { "Ophthalmic Clinical Procedures",
				"9780080449784", "Elsevier(ClinicalKey)",
				"Frank Eperjesi & Hannah Bartlett & Mark Dunne", "", "",
				"2008/2/7", "eng", "", "011", "011", "", "", "", "電子書", "", "",
				"衛生福利部臺北醫院", "" });

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
		setInputStream(new ByteArrayInputStream(boas.toByteArray()));

		return XLSX;
	}

	public boolean isIsbn(long isbnNum) {
		if (isbnNum >= 9780000000000l && isbnNum < 9800000000000l) {
			String isbn = "" + isbnNum;
			int sum = Integer.parseInt(isbn.substring(0, 1)) * 1
					+ Integer.parseInt(isbn.substring(1, 2)) * 3
					+ Integer.parseInt(isbn.substring(2, 3)) * 1
					+ Integer.parseInt(isbn.substring(3, 4)) * 3
					+ Integer.parseInt(isbn.substring(4, 5)) * 1
					+ Integer.parseInt(isbn.substring(5, 6)) * 3
					+ Integer.parseInt(isbn.substring(6, 7)) * 1
					+ Integer.parseInt(isbn.substring(7, 8)) * 3
					+ Integer.parseInt(isbn.substring(8, 9)) * 1
					+ Integer.parseInt(isbn.substring(9, 10)) * 3
					+ Integer.parseInt(isbn.substring(10, 11)) * 1
					+ Integer.parseInt(isbn.substring(11, 12)) * 3;

			int remainder = sum % 10;
			int num = 10 - remainder;

			if (num == 10) {
				if (Integer.parseInt(isbn.substring(12)) != 0) {
					return false;
				}
			} else {
				if (Integer.parseInt(isbn.substring(12)) != num) {
					return false;
				}
			}

		} else {
			return false;
		}
		return true;
	}

	public boolean hasEntity() throws Exception {
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

	// 判斷文件類型
	public Workbook createWorkBook(InputStream is) throws IOException {
		try {
			if (fileFileName[0].toLowerCase().endsWith("xls")) {
				return new HSSFWorkbook(is);
			}

			if (fileFileName[0].toLowerCase().endsWith("xlsx")) {
				return new XSSFWorkbook(is);
			}
		} catch (InvalidOperationException e) {
			return null;
		}

		return null;
	}

	/**
	 * @return the checkItem
	 */
	public String[] getCheckItem() {
		return checkItem;
	}

	/**
	 * @param checkItem
	 *            the checkItem to set
	 */
	public void setCheckItem(String[] checkItem) {
		this.checkItem = checkItem;
	}

	/**
	 * @return the cusSerNo
	 */
	public String[] getCusSerNo() {
		return cusSerNo;
	}

	/**
	 * @param cusSerNo
	 *            the cusSerNo to set
	 */
	public void setCusSerNo(String[] cusSerNo) {
		this.cusSerNo = cusSerNo;
	}

	/**
	 * @return the file
	 */
	public File[] getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(File[] file) {
		this.file = file;
	}

	/**
	 * @return the fileFileName
	 */
	public String[] getFileFileName() {
		return fileFileName;
	}

	/**
	 * @param fileFileName
	 *            the fileFileName to set
	 */
	public void setFileFileName(String[] fileFileName) {
		this.fileFileName = fileFileName;
	}

	/**
	 * @return the fileContentType
	 */
	public String[] getFileContentType() {
		return fileContentType;
	}

	/**
	 * @param fileContentType
	 *            the fileContentType to set
	 */
	public void setFileContentType(String[] fileContentType) {
		this.fileContentType = fileContentType;
	}

	/**
	 * @return the importSerNos
	 */
	public String[] getImportSerNos() {
		return importSerNos;
	}

	/**
	 * @param importSerNos
	 *            the importSerNos to set
	 */
	public void setImportSerNos(String[] importSerNos) {
		this.importSerNos = importSerNos;
	}
}
