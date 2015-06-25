package com.shouyang.syazs.core.web;

import com.shouyang.syazs.core.entity.Entity;

public abstract class GenericCRUDAction<T extends Entity> extends
		GenericAction<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8456347034602219914L;

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
