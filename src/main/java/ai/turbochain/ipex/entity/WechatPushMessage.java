package ai.turbochain.ipex.entity;

public class WechatPushMessage {
	private String androidView;
	private String title;
	private String content;
	private String iOSView;
	private String msgTypeName;
	private String otcOrderBean;
	private String loanOrderBean;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAndroidView() {
		return androidView;
	}

	public void setAndroidView(String androidView) {
		this.androidView = androidView;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getiOSView() {
		return iOSView;
	}

	public void setiOSView(String iOSView) {
		this.iOSView = iOSView;
	}

	public String getOtcOrderBean() {
		return otcOrderBean;
	}

	public void setOtcOrderBean(String otcOrderBean) {
		this.otcOrderBean = otcOrderBean;
	}

	public String getMsgTypeName() {
		return msgTypeName;
	}

	public void setMsgTypeName(String msgTypeName) {
		this.msgTypeName = msgTypeName;
	}

	public String getLoanOrderBean() {
		return loanOrderBean;
	}

	public void setLoanOrderBean(String loanOrderBean) {
		this.loanOrderBean = loanOrderBean;
	}
}
