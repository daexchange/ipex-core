package com.osp.blockchain.bean;


/**
 * 用户
 * 
 * @author zmc
 *
 */
public class User {

	private Integer id;
	private String name;
	private String icon;
	private Integer sex;
	private String realName;
	private String email;
	private String phone;
	private String inviteCode;
	private String qq;
	private String telegram;
	private String twitter;
	private String facebook;
	private String wechat;
	private String mnemonicWordHash;
	private Integer imUserid;
	private String imPassword;
	private String password;
	private Long ipexId;
	private Integer appId;
	
	public User() {
		super();
	}

	public User(Integer id, String name, String mnemonicWordHash, Integer imUserid, String imPassword,Long ipexId) {
		this.id = id;
		this.name = name;
		this.mnemonicWordHash = mnemonicWordHash;
		this.imUserid = imUserid;
		this.imPassword = imPassword;
		this.ipexId = ipexId;
	}

	public Long getIpexId() {
		return ipexId;
	}

	public void setIpexId(Long ipexId) {
		this.ipexId = ipexId;
	}
	
	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon == null ? null : icon.trim();
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public String getMnemonicWordHash() {
		return mnemonicWordHash;
	}

	public void setMnemonicWordHash(String mnemonicWordHash) {
		this.mnemonicWordHash = mnemonicWordHash == null ? null : mnemonicWordHash.trim();
	}

	public Integer getImUserid() {
		return imUserid;
	}

	public void setImUserid(Integer imUserid) {
		this.imUserid = imUserid;
	}

	public String getImPassword() {
		return imPassword;
	}

	public void setImPassword(String imPassword) {
		this.imPassword = imPassword == null ? null : imPassword.trim();
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName == null ? null : realName.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone == null ? null : phone.trim();
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode == null ? null : inviteCode.trim();
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getTelegram() {
		return telegram;
	}

	public void setTelegram(String telegram) {
		this.telegram = telegram;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}
}