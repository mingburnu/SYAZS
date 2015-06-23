package com.shouyang.syazs.core.web;

import com.shouyang.syazs.core.entity.GenericEntitySerNo;

/**
 * GenericCRUDActionSerNo
 * 
 * @author David Hsu
 * @version 2014/10/15
 */
public abstract class GenericCRUDActionSerNo<T extends GenericEntitySerNo>
		extends GenericActionSerNo<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3138711928578726323L;

	/**
	 * 儲存檢核
	 */
	protected abstract void validateSave() throws Exception;

	/**
	 * 修改檢核
	 */
	protected abstract void validateUpdate() throws Exception;

	/**
	 * 刪除檢核
	 */
	protected abstract void validateDelete() throws Exception;

	/**
	 * 編輯頁
	 * 
	 * @return
	 */
	public abstract String edit() throws Exception;

	/**
	 * List頁
	 * 
	 * @return
	 */
	public abstract String list() throws Exception;

	/**
	 * 儲存
	 * 
	 * @return
	 */
	public abstract String save() throws Exception;

	/**
	 * 修改
	 * 
	 * @return
	 */
	public abstract String update() throws Exception;

	/**
	 * 刪除
	 * 
	 * @return
	 */
	public abstract String delete() throws Exception;

}
