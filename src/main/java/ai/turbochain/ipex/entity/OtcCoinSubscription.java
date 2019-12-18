package ai.turbochain.ipex.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * OTC币种订阅表
 *
 * @author 
 * @date 2019年12月18日
 */
@Entity
@Data
public class OtcCoinSubscription {
    @Excel(name = "id", orderNum = "1", width = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
  
    @ManyToOne
    @JoinColumn(name = "coin_id")  
    private OtcCoin otcCoin;
    
    @Excel(name = "排序", orderNum = "1", width = 20)
    private int sort;

    private int origin;
}