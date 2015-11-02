package com.shouyang.syazs.module.apply.database;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.shouyang.syazs.module.apply.ebook.Ebook;
import com.shouyang.syazs.module.apply.enums.Type;
import com.shouyang.syazs.module.apply.feLogs.FeLogs;
import com.shouyang.syazs.module.apply.journal.Journal;
import com.shouyang.syazs.module.apply.referenceOwner.ReferenceOwner;
import com.shouyang.syazs.module.apply.resourcesBuyers.ResourcesBuyers;
import com.shouyang.syazs.module.entity.ModuleProperties;

@Entity
@Table(name = "db")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Database extends ModuleProperties {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1647915342720534484L;

	// 資料庫題名
	@Column(name = "DBtitle")
	private String dbTitle;

	// 語系
	@Column(name = "languages")
	private String languages;

	// 收錄種類
	@Column(name = "IncludedSpecies")
	private String includedSpecies;

	// 出版社
	@Column(name = "publishname")
	private String publishName;

	// 內容
	@Column(name = "Content")
	@org.hibernate.annotations.Type(type = "text")
	private String content;

	// 主題
	@Column(name = "topic")
	private String topic;

	// 分類
	@Column(name = "classification")
	private String classification;

	// 收錄年代
	@Column(name = "IndexedYears")
	private String indexedYears;

	// 出版時間差
	@Column(name = "embargo")
	private String embargo;

	// 資料庫資源種類
	@Column(name = "Rtype")
	@Enumerated(EnumType.STRING)
	private Type type;

	// URL
	@Column(name = "URL")
	private String url;

	// 公開存取
	@Column(name = "openAccess")
	private Boolean openAccess;

	// Universally Unique Identifier
	@Column(name = "uuIdentifier", updatable = false, unique = true)
	private String uuIdentifier;

	// ResourcesBuyers
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "res_serNo", nullable = false)
	@Autowired
	private ResourcesBuyers resourcesBuyers;

	// ReferenceOwner
	@ManyToMany
	@JoinTable(name = "ref_dat", joinColumns = @JoinColumn(name = "dat_SerNo"), inverseJoinColumns = @JoinColumn(name = "ref_SerNo"))
	private Set<ReferenceOwner> referenceOwners;

	@OneToMany(mappedBy = "database", orphanRemoval = true)
	private Set<Ebook> ebooks;

	@OneToMany(mappedBy = "database", orphanRemoval = true)
	private Set<Journal> journals;

	@OneToMany(mappedBy = "database", orphanRemoval = true)
	private Set<FeLogs> feLogses;

	/**
	 * @return the dbTitle
	 */
	public String getDbTitle() {
		return dbTitle;
	}

	/**
	 * @param dbTitle
	 *            the dbTitle to set
	 */
	public void setDbTitle(String dbTitle) {
		this.dbTitle = dbTitle;
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
	 * @return the includedSpecies
	 */
	public String getIncludedSpecies() {
		return includedSpecies;
	}

	/**
	 * @param includedSpecies
	 *            the includedSpecies to set
	 */
	public void setIncludedSpecies(String includedSpecies) {
		this.includedSpecies = includedSpecies;
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
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the topic
	 */
	public String getTopic() {
		return topic;
	}

	/**
	 * @param topic
	 *            the topic to set
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}

	/**
	 * @return the classification
	 */
	public String getClassification() {
		return classification;
	}

	/**
	 * @param classification
	 *            the classification to set
	 */
	public void setClassification(String classification) {
		this.classification = classification;
	}

	/**
	 * @return the indexedYears
	 */
	public String getIndexedYears() {
		return indexedYears;
	}

	/**
	 * @param indexedYears
	 *            the indexedYears to set
	 */
	public void setIndexedYears(String indexedYears) {
		this.indexedYears = indexedYears;
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
	 * @return the rType
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param rType
	 *            the rType to set
	 */
	public void setType(Type type) {
		this.type = type;
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

	/**
	 * @return the referenceOwners
	 */
	public Set<ReferenceOwner> getReferenceOwners() {
		return referenceOwners;
	}

	/**
	 * @param referenceOwners
	 *            the referenceOwners to set
	 */
	public void setReferenceOwners(Set<ReferenceOwner> referenceOwners) {
		this.referenceOwners = referenceOwners;
	}

	/**
	 * @return the ebooks
	 */
	public Set<Ebook> getEbooks() {
		return ebooks;
	}

	/**
	 * @return the journals
	 */
	public Set<Journal> getJournals() {
		return journals;
	}

	/**
	 * @return the feLogses
	 */
	public Set<FeLogs> getFeLogses() {
		return feLogses;
	}

	public Database() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Database(String dbTitle, String languages, String includedSpecies,
			String publishName, String content, String topic,
			String classification, String indexedYears, String embargo,
			Type type, String url, Boolean openAccess, String uuIdentifier,
			ResourcesBuyers resourcesBuyers, Set<ReferenceOwner> referenceOwners) {
		super();
		this.dbTitle = dbTitle;
		this.languages = languages;
		this.includedSpecies = includedSpecies;
		this.publishName = publishName;
		this.content = content;
		this.topic = topic;
		this.classification = classification;
		this.indexedYears = indexedYears;
		this.embargo = embargo;
		this.type = type;
		this.url = url;
		this.openAccess = openAccess;
		this.uuIdentifier = uuIdentifier;
		this.resourcesBuyers = resourcesBuyers;
		this.referenceOwners = referenceOwners;
	}
}
