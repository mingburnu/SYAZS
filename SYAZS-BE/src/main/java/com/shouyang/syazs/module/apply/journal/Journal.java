package com.shouyang.syazs.module.apply.journal;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.shouyang.syazs.module.apply.database.Database;
import com.shouyang.syazs.module.apply.feLogs.FeLogs;
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

	// 刊名
	@Column(name = "title")
	private String title;

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

	// 出版社
	@Column(name = "publishname")
	private String publishName;

	// 出版年
	@Column(name = "publishyear")
	private Short publishYear;

	// 標題
	@Column(name = "caption")
	private String caption;

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
	private String version;

	// 全文取得授權刊期
	@Column(name = "embargo")
	private String embargo;

	// URL
	@Column(name = "URL")
	private String url;

	// 公開存取
	@Column(name = "openAccess")
	private Boolean openAccess;

	@Column(name = "startdate")
	@org.hibernate.annotations.Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime startDate;

	@Column(name = "maturitydate")
	@org.hibernate.annotations.Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime maturityDate;

	// 來源資料庫
	@ManyToOne
	@JoinColumn(name = "dat_serNo")
	@Autowired
	private Database database;

	// Universally Unique Identifier
	@Column(name = "uuIdentifier", updatable = false, unique = true)
	private String uuIdentifier;

	// ResourcesBuyers
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "res_serNo", nullable = false)
	@Autowired
	private ResourcesBuyers resourcesBuyers;

	@OneToMany(mappedBy = "journal", orphanRemoval = true)
	private Set<FeLogs> feLogses;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
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
	public Short getPublishYear() {
		return publishYear;
	}

	/**
	 * @param publishYear
	 *            the publishYear to set
	 */
	public void setPublishYear(Short publishYear) {
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
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the embargo
	 */
	public String getEmbargo() {
		return embargo;
	}

	/**
	 * @param embargo
	 *            the embargo to set
	 */
	public void setEmbargo(String embargo) {
		this.embargo = embargo;
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
	 * @return the openAccess
	 */
	public Boolean getOpenAccess() {
		return openAccess;
	}

	/**
	 * @param openAccess
	 *            the openAccess to set
	 */
	public void setOpenAccess(Boolean openAccess) {
		this.openAccess = openAccess;
	}

	/**
	 * @return the startDate
	 */
	public LocalDateTime getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the maturityDate
	 */
	public LocalDateTime getMaturityDate() {
		return maturityDate;
	}

	/**
	 * @param maturityDate
	 *            the maturityDate to set
	 */
	public void setMaturityDate(LocalDateTime maturityDate) {
		this.maturityDate = maturityDate;
	}

	/**
	 * @return the database
	 */
	public Database getDatabase() {
		return database;
	}

	/**
	 * @param database
	 *            the database to set
	 */
	public void setDatabase(Database database) {
		this.database = database;
	}

	/**
	 * @return the uuIdentifier
	 */
	public String getUuIdentifier() {
		return uuIdentifier;
	}

	/**
	 * @param uuIdentifier
	 *            the uuIdentifier to set
	 */
	public void setUuIdentifier(String uuIdentifier) {
		this.uuIdentifier = uuIdentifier;
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

	public Journal(String title, String abbreviationTitle,
			String titleEvolution, String issn, String languages,
			String publishName, Short publishYear, String caption, String numB,
			String publication, String congressClassification, String version,
			String embargo, String url, Boolean openAccess,
			LocalDateTime startDate, LocalDateTime maturityDate,
			Database database, String uuIdentifier,
			ResourcesBuyers resourcesBuyers) {
		super();
		this.title = title;
		this.abbreviationTitle = abbreviationTitle;
		this.titleEvolution = titleEvolution;
		this.issn = issn;
		this.languages = languages;
		this.publishName = publishName;
		this.publishYear = publishYear;
		this.caption = caption;
		this.numB = numB;
		this.publication = publication;
		this.congressClassification = congressClassification;
		this.version = version;
		this.embargo = embargo;
		this.url = url;
		this.openAccess = openAccess;
		this.startDate = startDate;
		this.maturityDate = maturityDate;
		this.database = database;
		this.uuIdentifier = uuIdentifier;
		this.resourcesBuyers = resourcesBuyers;
	}
}
