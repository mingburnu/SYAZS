package com.shouyang.syazs.core.web;

import com.shouyang.syazs.core.entity.Entity;

/**
 * Action
 * 
 * @author Roderick
 * @version 2014/12/11
 */
public interface Action<T extends Entity> {

	public final static String INDEX = "index";

	public final static String ADD = "add";

	public final static String EDIT = "edit";

	public final static String LIST = "list";

	public final static String VIEW = "view";

	public final static String BOX = "box";

	public final static String JSON = "json";

	public final static String QUEUE = "queue";

	public final static String XLSX = "xlsx";

	public final static String IMPORT = "import";

	public final static String PREFIX = "prefix";
}
