package com.shouyang.syazs.module.apply.ebook;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.shouyang.syazs.core.apply.feLogs.FeLogs;
import com.shouyang.syazs.module.apply.classification.Classification;
import com.shouyang.syazs.module.apply.database.Database;
import com.shouyang.syazs.module.apply.resourcesBuyers.ResourcesBuyers;
import com.shouyang.syazs.module.entity.ModuleProperties;

@Entity
@Table(name = "ebook")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Ebook extends ModuleProperties {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5381064634722129515L;

	// 書名
	@Column(name = "bookname")
	private String bookName;

	// ISBN
	@Column(name = "ISBN")
	private Long isbn;

	// 出版社
	@Column(name = "publishname")
	private String publishName;

	// 作者
	@Column(name = "authername")
	private String autherName;

	// 第二第三作者等
	@Column(name = "authers")
	private String authers;

	// 系列叢書名
	@Column(name = "uppename")
	private String uppeName;

	// 電子書出版日期
	@Column(name = "pubdate")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime pubDate;

	// 語系
	@Column(name = "languages")
	private String languages;

	// 版本
	@Column(name = "Version")
	private String version;

	// 類型
	@Column(name = "style")
	private String style;

	// 出版地
	@Column(name = "publication")
	private String publication;

	// URL
	@Column(name = "URL")
	private String url;

	// 公開存取
	@Column(name = "openAccess")
	private Boolean openAccess;

	// 來源資料庫
	@ManyToOne
	@JoinColumn(name = "dat_serNo")
	@Autowired
	private Database database;

	// 分類法
	@ManyToOne
	@JoinColumn(name = "lcs_serNo")
	@Autowired
	private Classification classification;

	// LCS code
	@Column(name = "lcs_code")
	private String lcsCode;

	// Universally Unique Identifier
	@Column(name = "uuIdentifier", updatable = false, unique = true)
	private String uuIdentifier;

	// ResourcesBuyers
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "res_serNo", nullable = false)
	@Autowired
	private ResourcesBuyers resourcesBuyers;

	@OneToMany(mappedBy = "ebook", orphanRemoval = true)
	private Set<FeLogs> feLogses;

	/**
	 * @return the bookName
	 */
	public String getBookName() {
		return bookName;
	}

	/**
	 * @param bookName
	 *            the bookName to set
	 */
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	/**
	 * @return the isbn
	 */
	public Long getIsbn() {
		return isbn;
	}

	/**
	 * @param issn
	 *            the issn to set
	 */
	public void setIsbn(Long isbn) {
		this.isbn = isbn;
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
	 * @return the autherName
	 */
	public String getAutherName() {
		return autherName;
	}

	/**
	 * @param autherName
	 *            the autherName to set
	 */
	public void setAutherName(String autherName) {
		this.autherName = autherName;
	}

	/**
	 * @return the authers
	 */
	public String getAuthers() {
		return authers;
	}

	/**
	 * @param authers
	 *            the authers to set
	 */
	public void setAuthers(String authers) {
		this.authers = authers;
	}

	/**
	 * @return the uppeName
	 */
	public String getUppeName() {
		return uppeName;
	}

	/**
	 * @param uppeName
	 *            the uppeName to set
	 */
	public void setUppeName(String uppeName) {
		this.uppeName = uppeName;
	}

	/**
	 * @return the pubDate
	 */
	public LocalDateTime getPubDate() {
		return pubDate;
	}

	/**
	 * @param pubDate
	 *            the pubDate to set
	 */
	public void setPubDate(LocalDateTime pubDate) {
		this.pubDate = pubDate;
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
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @param style
	 *            the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
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
	 * @return the classification
	 */
	public Classification getClassification() {
		return classification;
	}

	/**
	 * @param classification
	 *            the classification to set
	 */
	public void setClassification(Classification classification) {
		this.classification = classification;
	}

	/**
	 * @return the lcsCode
	 */
	public String getLcsCode() {
		return lcsCode;
	}

	/**
	 * @param lcsCode
	 *            the lcsCode to set
	 */
	public void setLcsCode(String lcsCode) {
		this.lcsCode = lcsCode;
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

	public Ebook() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Ebook(String bookName, Long isbn, String publishName,
			String autherName, String authers, String uppeName,
			LocalDateTime pubDate, String languages, String version,
			String style, String publication, String url, Boolean openAccess,
			Database database, Classification classification, String lcsCode,
			String uuIdentifier, ResourcesBuyers resourcesBuyers) {
		super();
		this.bookName = bookName;
		this.isbn = isbn;
		this.publishName = publishName;
		this.autherName = autherName;
		this.authers = authers;
		this.uppeName = uppeName;
		this.pubDate = pubDate;
		this.languages = languages;
		this.version = version;
		this.style = style;
		this.publication = publication;
		this.url = url;
		this.openAccess = openAccess;
		this.database = database;
		this.classification = classification;
		this.lcsCode = lcsCode;
		this.uuIdentifier = uuIdentifier;
		this.resourcesBuyers = resourcesBuyers;
	}
}
