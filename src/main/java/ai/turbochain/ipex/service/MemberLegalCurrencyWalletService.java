package ai.turbochain.ipex.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ai.turbochain.ipex.dao.CoinDao;
import ai.turbochain.ipex.dao.MemberLegalCurrencyWalletDao;
import ai.turbochain.ipex.dao.MemberWalletDao;
import ai.turbochain.ipex.entity.Coin;
import ai.turbochain.ipex.entity.MemberLegalCurrencyWallet;
import ai.turbochain.ipex.entity.MemberWallet;
import ai.turbochain.ipex.entity.Order;
import ai.turbochain.ipex.entity.OtcCoin;
import ai.turbochain.ipex.entity.TransferSelfRecord;
import ai.turbochain.ipex.exception.InformationExpiredException;
import ai.turbochain.ipex.service.Base.BaseService;
import ai.turbochain.ipex.util.BigDecimalUtils;
import ai.turbochain.ipex.util.MessageResult;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberLegalCurrencyWalletService extends BaseService<MemberLegalCurrencyWallet> {
    @Autowired
    private MemberLegalCurrencyWalletDao memberLegalCurrencyWalletDao;
    @Autowired
    private MemberWalletDao memberWalletDao;
    @Autowired
    private TransferSelfRecordService transferSelfRecordService;
    @Autowired
    private CoinDao coinDao;

    /**
     * 获取钱包
     *
     * @param coin     otc币种
     * @param memberId
     * @return
     */
    public MemberLegalCurrencyWallet findByOtcCoinAndMemberId(OtcCoin coin, long memberId) {
        Coin coin1 = coinDao.findByUnit(coin.getUnit());
        return memberLegalCurrencyWalletDao.findByCoinAndMemberId(coin1, memberId);
    }

    public MemberLegalCurrencyWallet findByCoinUnitAndMemberId(String coinUnit, Long memberId) {
        Coin coin = coinDao.findByUnit(coinUnit);
        return memberLegalCurrencyWalletDao.findByCoinAndMemberId(coin, memberId);
    }

    public MemberLegalCurrencyWallet findByCoinAndMemberId(Coin coin, Long memberId) {
        return memberLegalCurrencyWalletDao.findByCoinAndMemberId(coin, memberId);
    }

    /**
     * 根据用户查找所有钱包
     *
     * @param member
     * @return
     */
    public List<MemberLegalCurrencyWallet> findAllByMemberId(Long memberId) {
        return memberLegalCurrencyWalletDao.findAllByMemberId(memberId);
    }

    /**
     * 冻结钱包
     *
     * @param memberWallet
     * @param amount
     * @return
     */
    public MessageResult freezeBalance(MemberLegalCurrencyWallet memberLegalCurrencyWallet, BigDecimal amount) {
        int ret = memberLegalCurrencyWalletDao.freezeBalance(memberLegalCurrencyWallet.getId(), amount);
        if (ret > 0) {
            return MessageResult.success();
        } else {
            return MessageResult.error("Information Expired");
        }
    }

    /**
     * 解冻钱包
     *
     * @param memberWallet
     * @param amount
     * @return
     */
    public MessageResult thawBalance(MemberLegalCurrencyWallet memberLegalCurrencyWallet, BigDecimal amount) {
        int ret = memberLegalCurrencyWalletDao.thawBalance(memberLegalCurrencyWallet.getId(), amount);
        if (ret > 0) {
            return MessageResult.success();
        } else {
            log.info("====order cancel=====订单取消异常（amount）："+amount);
            return MessageResult.error("Information Expired");
        }
    }

    /**
     * 放行更改双方钱包余额
     *
     * @param order
     * @param ret
     * @throws InformationExpiredException
     */
    public void transfer(Order order, int ret) throws InformationExpiredException {
        if (ret == 1) {
        	MemberLegalCurrencyWallet custoMemberLegalCurrencyWallet = findByOtcCoinAndMemberId(order.getCoin(), order.getCustomerId());
        	// 冻结余额
        	int is = memberLegalCurrencyWalletDao.decreaseFrozen(custoMemberLegalCurrencyWallet.getId(), order.getNumber());
            if (is > 0) {
            	MemberLegalCurrencyWallet memberLegalCurrencyWallet = findByOtcCoinAndMemberId(order.getCoin(), order.getMemberId());
                int a = memberLegalCurrencyWalletDao.increaseBalance(memberLegalCurrencyWallet.getId(), BigDecimalUtils.sub(order.getNumber(), order.getCommission()));
                if (a <= 0) {
                    throw new InformationExpiredException("Information Expired");
                }
            } else {
                throw new InformationExpiredException("Information Expired");
            }
        } else {
        	MemberLegalCurrencyWallet customerMemberLegalCurrencyWallet= findByOtcCoinAndMemberId(order.getCoin(), order.getMemberId());
            // 减少冻结余额
        	int is = memberLegalCurrencyWalletDao.decreaseFrozen(customerMemberLegalCurrencyWallet.getId(), BigDecimalUtils.add(order.getNumber(), order.getCommission()));
            if (is > 0) {
            	MemberLegalCurrencyWallet memberWallet = findByOtcCoinAndMemberId(order.getCoin(), order.getCustomerId());
                int a = memberLegalCurrencyWalletDao.increaseBalance(memberWallet.getId(), order.getNumber());
                if (a <= 0) {
                    throw new InformationExpiredException("Information Expired");
                }
            } else {
                throw new InformationExpiredException("Information Expired");
            }
        }

    }
    

    /**
     * 根据 coinId和会员ID查询会员账户信息
     * @param coinId
     * @param memberId
     * @return
     */
    public MemberLegalCurrencyWallet getMemberWalletByCoinAndMemberId(String coinId, long memberId) {
        return memberLegalCurrencyWalletDao.getMemberWalletByCoinAndMemberId(coinId,memberId);
    }
 
    
    /**
     * 币币到法币资金划转
     *
     * @param wallet
     * @param amount
     * @return
     * @throws Exception 
     */
    @Transactional(rollbackFor = Exception.class)
    public MessageResult transferIncreaseBalance(String coinId, Long memberId, BigDecimal amount) throws Exception {

    	MemberWallet memberWallet = memberWalletDao.getLockMemberWalletByCoinAndMemberId(coinId, memberId);
        
    	if (memberWallet.getBalance().compareTo(amount) < 0) {
            return new MessageResult(500, "可划转余额不足");
        }
    	
		MemberLegalCurrencyWallet memberLegalCurrencyWallet =  memberLegalCurrencyWalletDao.getLockMemberWalletByCoinAndMemberId(coinId, memberId);

    	if (memberLegalCurrencyWallet == null) {
            return new MessageResult(500, "wallet cannot be null");
        }
       
        int result = memberLegalCurrencyWalletDao.transferIncreaseBalance(memberLegalCurrencyWallet.getId(), amount,memberLegalCurrencyWallet.getBalance());
        
        if (result > 0) {
    	    result =  memberWalletDao.transferDecreaseBalance(memberWallet.getId(), memberId, amount, memberWallet.getBalance());
           
            if (result > 0) {
                 TransferSelfRecord transferSelfRecord = new TransferSelfRecord();
                 
                 Coin coin = new Coin();
         		coin.setName(coinId);
         		transferSelfRecord.setCoin(coin);
                 transferSelfRecord.setLegalcurrencyId(memberLegalCurrencyWallet.getId());
                 transferSelfRecord.setWalletId(memberWallet.getId());
                 transferSelfRecord.setMemberId(memberId);
                 transferSelfRecord.setType(0);
                 transferSelfRecord.setStatus(1);
                 transferSelfRecord.setTotalAmount(amount);
                 transferSelfRecord.setArrivedAmount(amount);
                 transferSelfRecord.setFee(BigDecimal.ZERO);
                 
                 transferSelfRecordService.save(transferSelfRecord);
                 
                //增加记录
                return new MessageResult(0, "success");
            } else {
            	throw new Exception("转账失败！");
            }
        } else {
            return new MessageResult(500, "recharge failed");
        }
    }
    
    
    /**
     * 法币到币币资金划转
     *
     * @param wallet
     * @param amount
     * @return
     * @throws Exception 
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public MessageResult transferDecreaseBalance(String coinId,Long memberId, BigDecimal amount) throws Exception {
    	
    	MemberLegalCurrencyWallet memberLegalCurrencyWallet = memberLegalCurrencyWalletDao.getLockMemberWalletByCoinAndMemberId(coinId, memberId);
    	
    	if (memberLegalCurrencyWallet.getBalance().compareTo(amount) < 0) {
            return new MessageResult(500, "可划转余额不足");
        }
    	
    	MemberWallet memberWallet = memberWalletDao.getLockMemberWalletByCoinAndMemberId(coinId, memberId);

    	// 法币账户扣减
        int result = memberLegalCurrencyWalletDao.transferDecreaseBalance(memberLegalCurrencyWallet.getId(), amount,memberLegalCurrencyWallet.getBalance());
       
        if (result > 0) {
        	 
        	// 币币账户增加
        	result = memberWalletDao.transferIncreaseBalance(memberWallet.getId(), memberId, amount, memberWallet.getBalance());
        	
        	if (result > 0) {
        		TransferSelfRecord transferSelfRecord = new TransferSelfRecord();
                 
        		 Coin coin = new Coin();
        		 coin.setName(coinId);
        		 transferSelfRecord.setCoin(coin);
                 transferSelfRecord.setLegalcurrencyId(memberLegalCurrencyWallet.getId());
                 transferSelfRecord.setWalletId(memberWallet.getId());
                 transferSelfRecord.setTotalAmount(amount);
                 transferSelfRecord.setMemberId(memberId);
                 transferSelfRecord.setType(1);
                 transferSelfRecord.setStatus(1);
                 transferSelfRecord.setTotalAmount(amount);
                 transferSelfRecord.setArrivedAmount(amount);
                 transferSelfRecord.setFee(BigDecimal.ZERO);
                 
               //增加记录
                transferSelfRecordService.save(transferSelfRecord);
                 
                return new MessageResult(0, "success");
        	} else {
        		throw new Exception("划转失败！");
        	}
        } else {
            return new MessageResult(500, "recharge failed");
        }
    }
  
}
