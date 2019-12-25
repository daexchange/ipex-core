package ai.turbochain.ipex.entity;

import lombok.Data;

@Data
public class WechatPushMessage {
	private String androidView;
	private String title;
	private String content;
	private String iOSView;
	private String msgTypeName;
	private String otcOrderBean;
}
