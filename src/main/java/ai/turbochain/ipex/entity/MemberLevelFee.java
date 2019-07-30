package ai.turbochain.ipex.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 会员等级费率
 * @author 未央
 * @date 2019年7月29日
 */
@Entity
@Data
public class MemberLevelFee {
	/**
	 * 主键ID
	 */
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	/**
	 * 币对
	 */
	@NotNull(message = "默认不得为null")
	private String symbol;
	/**
	 * 会员等级ID
	 */
	@NotNull(message = "默认不得为null")
	private Long memberLevelId;
	/**
	 * 手续费
	 */
    @Column(columnDefinition = "decimal(18,8) default 0 comment '手续费'")
    private BigDecimal fee;
    
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
