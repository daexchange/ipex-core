package ai.turbochain.ipex.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ai.turbochain.ipex.constant.BooleanEnum;
import lombok.Data;

/**
 * @author
 * @description 会员法币钱包
 * @date 2019/7/1 14:00
 */
@Entity
@Data
@Table(uniqueConstraints ={@UniqueConstraint(columnNames={"memberId", "coin_id"})})
public class MemberLegalCurrencyWallet {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "coin_id")
    private Coin coin;
    /**
     * 可用余额
     */
    @Column(columnDefinition = "decimal(26,16) comment '可用余额'")
    private BigDecimal balance;
    /**
     * 冻结余额
     */
    @Column(columnDefinition = "decimal(26,16) comment '冻结余额'")
    private BigDecimal frozenBalance;

    /**
     * 待释放总量
     */
    @Column(columnDefinition = "decimal(18,8) comment '待释放总量'")
    private BigDecimal toReleased;

    @JsonIgnore
    @Version
    private int version;

    /**
     * 钱包是否锁定，0否，1是。锁定后
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "int default 0 comment '钱包不是锁定'")
    private BooleanEnum isLock = BooleanEnum.IS_FALSE;
}
