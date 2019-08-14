package ai.turbochain.ipex.service;

import static ai.turbochain.ipex.util.BigDecimalUtils.sub;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ai.turbochain.ipex.dao.MemberWalletDao;
import ai.turbochain.ipex.entity.Coin;
import ai.turbochain.ipex.entity.Member;
import ai.turbochain.ipex.entity.MemberWallet;
import ai.turbochain.ipex.entity.TransferOtherRecord;
import ai.turbochain.ipex.service.Base.BaseService;
import ai.turbochain.ipex.util.MessageResult;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class ExangeService extends BaseService {
   
	// 系统会员ID
	public static final Long system_member_id = 2l;
    
	@Autowired
    private MemberWalletDao memberWalletDao;
	@Autowired
    private WalletTransferOtherRecordService walletTransferOtherRecordService;
	
	
	/**
     * 币币资金转账到他人账户
     *
     * @param wallet
     * @param amount
     * @return
     * @throws Exception 
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public MessageResult transferToOther(String coinId,BigDecimal amount,BigDecimal fee,
    		Long memberIdFrom, Long memberIdTo) throws Exception { 
    		
    	// 悲观锁 将行数据锁定 select for udpate 所有update方法都需要加锁
    	MemberWallet memberWalletFrom = memberWalletDao.getLockMemberWalletByCoinAndMemberId(coinId, memberIdFrom);
    	MemberWallet memberWalletTo = memberWalletDao.getLockMemberWalletByCoinAndMemberId(coinId, memberIdTo);
    	MemberWallet systemMemberWallet = memberWalletDao.getLockMemberWalletByCoinAndMemberId(coinId, system_member_id);
    	
    	// 打款方币币账户扣减
        int result = memberWalletDao.transferDecreaseBalance(memberWalletFrom.getId(),memberIdFrom, amount,memberWalletFrom.getBalance());
       
        if (result > 0) {
        	BigDecimal arrivedAmount = sub(amount, fee);
        	
        	// 收款方他人币币账户增加
        	result = memberWalletDao.transferIncreaseBalance(memberWalletTo.getId(), memberIdTo, arrivedAmount, memberWalletTo.getBalance());
        	
        	if (result > 0) {
        		
        		if (fee.compareTo(BigDecimal.ZERO)==1) {
        			// 手续费划转到系统账户
                	result = memberWalletDao.transferIncreaseBalance(systemMemberWallet.getId(), systemMemberWallet.getMemberId(), fee, systemMemberWallet.getBalance());
        		}
        		
            	if (result > 0) {
            		TransferOtherRecord transferOtherRecord = new TransferOtherRecord();
                    
            		Coin coin = new Coin();
            		coin.setName(coinId);
            		Member memberTo = new Member();
            		memberTo.setId(memberIdTo);
            		
            		transferOtherRecord.setCoin(coin);
            		transferOtherRecord.setMemberIdFrom(memberIdFrom);
            		transferOtherRecord.setMemberTo(memberTo);
            		transferOtherRecord.setWalletIdFrom(memberWalletFrom.getId());
            		transferOtherRecord.setWalletIdTo(memberWalletTo.getId());
            		transferOtherRecord.setTotalAmount(amount);
            		transferOtherRecord.setFee(fee);
            		transferOtherRecord.setArrivedAmount(arrivedAmount);
            		transferOtherRecord.setStatus(1);
            		transferOtherRecord.setType(1);
                     
                    //增加记录
                    walletTransferOtherRecordService.save(transferOtherRecord);
                	
                    return new MessageResult(0, "success");
            	} else {
            		throw new Exception("划转失败！");
            	}
        	} else {
        		throw new Exception("划转失败！");
        	}
        } else {
            return new MessageResult(500, "recharge failed");
        }
    }
    
}
