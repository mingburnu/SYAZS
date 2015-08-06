package com.shouyang.syazs.module.apply.journal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.shouyang.syazs.module.apply.resourcesBuyers.ResourcesBuyers;
import com.shouyang.syazs.module.entity.ModuleProperties;

@Entity
@Table(name = "journal")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Journal extends ModuleProperties {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8359789887877536098L;

	// 中文刊名
	@Column(name = "chinesetitle")
	private String chineseTitle;

	// 英文刊名
	@Column(name = "englishtitle")
	private String englishTitle;

	// 英文縮寫刊名
	@Column(name = "abbreviationtitle")
	private String abbreviationTitle;

	// 刊名演變
	@Column(name = "titleevolution")
	private String titleEvolution;

	// ISSN
	@Column(name = "ISSN")
	private String issn;

	// 語系
	@Column(name = "languages")
	private String languages;

	// 出版項
	@Column(name = "publishname")
	private String publishName;

	// 出版年
	@Column(name = "publishyear")
	private String publishYear;

	// 標題
	@Column(name = "caption")
	private String caption;

	// URL
	@Column(name = "URL")
	private String url;

	// 編號
	@Column(name = "numB")
	private String numB;

	// 刊別
	@Column(name = "Publication")
	private String publication;

	// 國會分類號
	@Column(name = "congressclassification")
	private String congressClassification;

	// 版本
	@Column(name = "version")
	private Integer version;

	// ResourcesBuyers
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "res_serNo", nullable = false)
	@Autowired
	private ResourcesBuyers resourcesBuyers;

	/**
	 * @return the chineseTitle
	 */
	public String getChineseTitle() {
		return chineseTitle;
	}

	/**
	 * @param chineseTitle
	 *            the chineseTitle to set
	 */
	public void setChineseTitle(String chineseTitle) {
		this.chineseTitle = chineseTitle;
	}

	/**
	 * @return the englishTitle
	 */
	public String getEnglishTitle() {
		return englishTitle;
	}

	/**
	 * @param englishTitle
	 *            the englishTitle to set
	 */
	public void setEnglishTitle(String englishTitle) {
		this.englishTitle = englishTitle;
	}

	/**
	 * @return the abbreviationTitle
	 */
	public String getAbbreviationTitle() {
		return abbreviationTitle;
	}

	/**
	 * @param abbreviationTitle
	 *            the abbreviationTitle to set
	 */
	public void setAbbreviationTitle(String abbreviationTitle) {
		this.abbreviationTitle = abbreviationTitle;
	}

	/**
	 * @return the titleEvolution
	 */
	public String getTitleEvolution() {
		return titleEvolution;
	}

	/**
	 * @param titleEvolution
	 *            the titleEvolution to set
	 */
	public void setTitleEvolution(String titleEvolution) {
		this.titleEvolution = titleEvolution;
	}

	/**
	 * @return the issn
	 */
	public String getIssn() {
		return issn;
	}

	/**
	 * @param issn
	 *            the issn to set
	 */
	public void setIssn(String issn) {
		this.issn = issn;
	}

	/**
	 * @return the languages
	 */
	public String getLanguages() {
		return languages;
	}

	/**
	 * @param languages
	 *            the languages to set
	 */
	public void setLanguages(String languages) {
		this.languages = languages;
	}

	/**
	 * @return the publishName
	 */
	public String getPublishName() {
		return publishName;
	}

	/**
	 * @param publishName
	 *            the publishName to set
	 */
	public void setPublishName(String publishName) {
		this.publishName = publishName;
	}

	/**
	 * @return the publishYear
	 */
	public String getPublishYear() {
		return publishYear;
	}

	/**
	 * @param publishYear
	 *            the publishYear to set
	 */
	public void setPublishYear(String publishYear) {
		this.publishYear = publishYear;
	}

	/**
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @param caption
	 *            the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the numB
	 */
	public String getNumB() {
		return numB;
	}

	/**
	 * @param numB
	 *            the numB to set
	 */
	public void setNumB(String numB) {
		this.numB = numB;
	}

	/**
	 * @return the publication
	 */
	public String getPublication() {
		return publication;
	}

	/**
	 * @param publication
	 *            the publication to set
	 */
	public void setPublication(String publication) {
		this.publication = publication;
	}

	/**
	 * @return the congressClassification
	 */
	public String getCongressClassification() {
		return congressClassification;
	}

	/**
	 * @param congressClassification
	 *            the congressClassification to set
	 */
	public void setCongressClassification(String congressClassification) {
		this.congressClassification = congressClassification;
	}

	/**
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * @return the resourcesBuyers
	 */
	public ResourcesBuyers getResourcesBuyers() {
		return resourcesBuyers;
	}

	/**
	 * @param resourcesBuyers
	 *            the resourcesBuyers to set
	 */
	public void setResourcesBuyers(ResourcesBuyers resourcesBuyers) {
		this.resourcesBuyers = resourcesBuyers;
	}

	public Journal() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Journal(String chineseTitle, String englishTitle,
			String abbreviationTitle, String titleEvolution, String issn,
			String languages, String publishName, String publishYear,
			String caption, String url, String numB, String publication,
			String congressClassification, Integer version,
			ResourcesBuyers resourcesBuyers) {
		super();
		this.chineseTitle = chineseTitle;
		this.englishTitle = englishTitle;
		this.abbreviationTitle = abbreviationTitle;
		this.titleEvolution = titleEvolution;
		this.issn = issn;
		this.languages = languages;
		this.publishName = publishName;
		this.publishYear = publishYear;
		this.caption = caption;
		this.url = url;
		this.numB = numB;
		this.publication = publication;
		this.congressClassification = congressClassification;
		this.version = version;
		this.resourcesBuyers = resourcesBuyers;
	}
}
