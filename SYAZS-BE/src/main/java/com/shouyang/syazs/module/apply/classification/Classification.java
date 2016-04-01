package com.shouyang.syazs.module.apply.classification;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.shouyang.syazs.core.entity.GenericEntitySerNo;
import com.shouyang.syazs.module.apply.ebook.Ebook;

@Entity
@Table(name = "classification")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Classification extends GenericEntitySerNo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7452420166786794621L;

	@Column(name = "classname")
	private String classname;

	@OneToMany(mappedBy = "classification", orphanRemoval = true)
	private Set<Ebook> ebooks;

	/**
	 * @return the classname
	 */
	public String getClassname() {
		return classname;
	}

	/**
	 * @param classname
	 *            the classname to set
	 */
	public void setClassname(String classname) {
		this.classname = classname;
	}

	/**
	 * @return the ebooks
	 */
	public Set<Ebook> getEbooks() {
		return ebooks;
	}

	public Classification() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Classification(String classname) {
		super();
		this.classname = classname;
	}
}
