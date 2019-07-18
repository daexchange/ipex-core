package ai.turbochain.ipex.service;

import static ai.turbochain.ipex.util.BigDecimalUtils.sub;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    @Transactional(rollbackFor = Exception.class)
    public MessageResult transferToOther(MemberWallet memberWalletFrom,
    		String coinId,Long memberIdFrom, Long memberIdTo, 
    		BigDecimal amount,BigDecimal fee) throws Exception {
    	
    	MemberWallet memberWalletTo = memberWalletDao.getMemberWalletByCoinAndMemberId(coinId, memberIdTo);
 
    	// 当前币币账户扣减
        int result = memberWalletDao.transferDecreaseBalance(memberWalletFrom.getId(),memberIdFrom, amount,memberWalletFrom.getBalance());
       
        if (result > 0) {
        	BigDecimal arrivedAmount = sub(amount, fee);
        	// 他人币币账户增加
        	result = memberWalletDao.transferIncreaseBalance(memberWalletTo.getId(), memberIdTo, arrivedAmount, memberWalletTo.getBalance());
        	
        	if (result > 0) {
        		TransferOtherRecord walletTransferRecord = new TransferOtherRecord();
                 
        		Coin coin = new Coin();
        		coin.setName(coinId);
        		Member memberTo = new Member();
        		memberTo.setId(memberIdTo);
        		
        		walletTransferRecord.setCoin(coin);
        		walletTransferRecord.setMemberIdFrom(memberIdFrom);
        		walletTransferRecord.setMemberTo(memberTo);
        		walletTransferRecord.setWalletIdFrom(memberWalletFrom.getId());
        		walletTransferRecord.setWalletIdTo(memberWalletTo.getId());
        		walletTransferRecord.setTotalAmount(amount);
        		walletTransferRecord.setFee(fee);
        		walletTransferRecord.setArrivedAmount(arrivedAmount);
                walletTransferRecord.setStatus(1);
                 
                //增加记录
                walletTransferOtherRecordService.save(walletTransferRecord);
                 
               	return new MessageResult(0, "success");
        	} else {
        		throw new Exception("划转失败！");
        	}
        } else {
            return new MessageResult(500, "recharge failed");
        }
    }
}
