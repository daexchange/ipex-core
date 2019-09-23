package ai.turbochain.ipex.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Entity
@Data
public class TransferOtherRecord {
    @Excel(name = "交易记录编号", orderNum = "1", width = 25)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Excel(name = "币币from账户ID", orderNum = "2", width = 25)
    private Long walletIdFrom;
    @Excel(name = "币币to账户ID", orderNum = "2", width = 25)
    private Long walletIdTo;
    @Excel(name = "交易人员ID", orderNum = "2", width = 25)
    private Long memberIdFrom;
    //@ManyToOne
    //@JoinColumn(name = "member_id_from")
   // private Member memberFrom;
    
    @Excel(name = "交易人员ID", orderNum = "2", width = 25)
    @ManyToOne
    @JoinColumn(name = "member_id_to")
    private Member memberTo;
    
    @Excel(name = "币种ID", orderNum = "2", width = 25)
    @ManyToOne
    @JoinColumn(name = "coin_id")
    private Coin coin;
  
    @Excel(name = "数量", orderNum = "3", width = 25)
    @Column(columnDefinition = "decimal(18,8) comment 数量'")
    private BigDecimal totalAmount;
    /**
     * 交易手续费
     */
    @Column(precision = 18,scale = 8)
    private BigDecimal fee = BigDecimal.ZERO ;
    @Excel(name = "数量", orderNum = "3", width = 25)
    @Column(columnDefinition = "decimal(18,8) comment '预计到账数量'")
    private BigDecimal arrivedAmount;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", orderNum = "4", width = 25)
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 交易类型
     */
    @Excel(name = "交易类型", orderNum = "5", width = 25)
    private Integer status;
    
    /**
     * 交易类型
     */
    @Excel(name = "交易类型", orderNum = "5", width = 25)
    private Integer type;
  
}
