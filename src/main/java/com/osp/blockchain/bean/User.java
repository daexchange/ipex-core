package com.osp.blockchain.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户
 * 
 * @author zmc
 *
 */
@ApiModel(description = "用户")
public class User {

	/**
	 * 用户账号
	 */
	@ApiModelProperty(value = "用户编号", name = "id", required = true)
	private Integer id;
	@ApiModelProperty(value = "用户名", name = "name")
	private String name;
	@ApiModelProperty(value = "用户头像url", name = "icon")
	private String icon;
	@ApiModelProperty(value = "性别", name = "sex")
	private Integer sex;
	@ApiModelProperty(value = "真实姓名", name = "realName")
	private String realName;
	@ApiModelProperty(value = "邮箱", name = "email")
	private String email;
	@ApiModelProperty(value = "手机号码", name = "phone")
	private String phone;
	@ApiModelProperty(value = "邀请码", name = "inviteCode")
	private String inviteCode;
	@ApiModelProperty(value = "QQ", name = "qq")
	private String qq;
	@ApiModelProperty(value = "电报", name = "telegram")
	private String telegram;
	@ApiModelProperty(value = "推特", name = "twitter")
	private String twitter;
	@ApiModelProperty(value = "脸书", name = "facebook")
	private String facebook;
	@ApiModelProperty(value = "微信", name = "wechat")
	private String wechat;
	@ApiModelProperty(value = "助记词哈希，此字段不支持修改", name = "mnemonicWordHash")
	private String mnemonicWordHash;
	@ApiModelProperty(value = "IM用户编号，此字段不支持修改", name = "imUserid")
	private Integer imUserid;
	@ApiModelProperty(value = "IM用户密码，此字段不支持修改", name = "imPassword")
	private String imPassword;
	@ApiModelProperty(value = "用户密码，此字段不支持修改", name = "password")
	private String password;
	@ApiModelProperty(value = "IPEX用户编号，此字段不支持修改", name = "ipexId")
	private Long ipexId;

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
}