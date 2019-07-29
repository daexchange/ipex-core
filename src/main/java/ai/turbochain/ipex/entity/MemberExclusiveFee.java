package ai.turbochain.ipex.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 会员专属费率
 * @author 未央
 * @date 2019年7月29日
 */
@Entity
@Data
public class MemberExclusiveFee {
	/**
	 * 主键ID
	 */
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	/**
	 * 会员ID
	 */
	@NotNull(message = "默认不得为null")
	private Long memberId;
	/**
	 * 币对
	 */
	@NotNull(message = "默认不得为null")
	private String symbol;
	/**
	 * 费率
	 */
	@NotNull(message = "默认不得为null")
	private Double rate;
	/**
	 * 交易类型
	 */
	@NotNull(message = "默认不得为null")
	private Integer type;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
