package ai.turbochain.ipex.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;

import ai.turbochain.ipex.constant.BooleanEnum;
import ai.turbochain.ipex.constant.PageModel;
import ai.turbochain.ipex.dao.CoinDao;
import ai.turbochain.ipex.dao.MemberDepositDao;
import ai.turbochain.ipex.dao.MemberLegalCurrencyWalletDao;
import ai.turbochain.ipex.dao.MemberWalletDao;
import ai.turbochain.ipex.dto.MemberWalletDTO;
import ai.turbochain.ipex.entity.Coin;
import ai.turbochain.ipex.entity.Member;
import ai.turbochain.ipex.entity.MemberDeposit;
import ai.turbochain.ipex.entity.MemberLegalCurrencyWallet;
import ai.turbochain.ipex.entity.MemberWallet;
import ai.turbochain.ipex.entity.Order;
import ai.turbochain.ipex.entity.OtcCoin;
import ai.turbochain.ipex.entity.QMember;
import ai.turbochain.ipex.entity.QMemberWallet;
import ai.turbochain.ipex.entity.TransferSelfRecord;
import ai.turbochain.ipex.es.ESUtils;
import ai.turbochain.ipex.exception.InformationExpiredException;
import ai.turbochain.ipex.service.Base.BaseService;
import ai.turbochain.ipex.util.BigDecimalUtils;
import ai.turbochain.ipex.util.MessageResult;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberLegalCurrencyWalletService extends BaseService {
    @Autowired
    private MemberLegalCurrencyWalletDao memberLegalCurrencyWalletDao;
    @Autowired
    private MemberWalletDao memberWalletDao;
    @Autowired
    private TransferSelfRecordService transferSelfRecordService;
    @Autowired
    private CoinDao coinDao;
    @Autowired
    private MemberDepositDao depositDao;
    @Autowired(required=false)
    private ESUtils esUtils;
    
    public MemberLegalCurrencyWallet save(MemberLegalCurrencyWallet memberLegalCurrencyWallet) {
        return memberLegalCurrencyWalletDao.saveAndFlush(memberLegalCurrencyWallet);
    }

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


    /**
     * 根据币种和钱包地址获取钱包
     *
     * @param coin
     * @param address
     * @return
     */
   // public MemberLegalCurrencyWallet findByCoinAndAddress(Coin coin, String address) {
   //     return memberLegalCurrencyWalletDao.findByCoinAndAddress(coin, address);
   // }

    /**
     * 根据币种和用户ID获取钱包
     *
     * @param coin
     * @param member
     * @return
     */
    public MemberLegalCurrencyWallet findByCoinAndMember(Coin coin, Member member) {
        return memberLegalCurrencyWalletDao.findByCoinAndMemberId(coin, member.getId());
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
    public List<MemberLegalCurrencyWallet> findAllByMemberId(Member member) {
        return memberLegalCurrencyWalletDao.findAllByMemberId(member.getId());
    }

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



    /* */

    /**
     * 放行更改双方钱包余额
     *
     * @param order
     * @param ret
     * @throws InformationExpiredException
     */
    public void transferAdmin(Order order, int ret) throws InformationExpiredException {
        if (ret == 1 || ret == 4) {
            trancerDetail(order, order.getCustomerId(), order.getMemberId());
        } else {
            trancerDetail(order, order.getMemberId(), order.getCustomerId());

        }

    }


    private void trancerDetail(Order order, long sellerId, long buyerId) throws InformationExpiredException {
    	MemberLegalCurrencyWallet customerWallet = findByOtcCoinAndMemberId(order.getCoin(), sellerId);
        //卖币者，买币者要处理的金额
        BigDecimal sellerAmount, buyerAmount;
        if (order.getMemberId() == sellerId) {
            sellerAmount = BigDecimalUtils.add(order.getNumber(), order.getCommission());
            buyerAmount = order.getNumber();
        } else {
            sellerAmount = order.getNumber();
            buyerAmount = order.getNumber().subtract(order.getCommission());
        }
        int is = memberLegalCurrencyWalletDao.decreaseFrozen(customerWallet.getId(), sellerAmount);
        if (is > 0) {
        	MemberLegalCurrencyWallet memberWallet = findByOtcCoinAndMemberId(order.getCoin(), buyerId);
            int a = memberLegalCurrencyWalletDao.increaseBalance(memberWallet.getId(), buyerAmount);
            if (a <= 0) {
                throw new InformationExpiredException("Information Expired");
            }
        } else {
            throw new InformationExpiredException("Information Expired");
        }
    }

    public int deductBalance(MemberWallet memberWallet, BigDecimal amount) {
        return memberLegalCurrencyWalletDao.decreaseBalance(memberWallet.getId(), amount);
    }

    @Override
    public List<MemberLegalCurrencyWallet> findAll() {
        return memberLegalCurrencyWalletDao.findAll();
    }

    public List<MemberLegalCurrencyWallet> findAllByCoin(Coin coin) {
        return memberLegalCurrencyWalletDao.findAllByCoin(coin);
    }

    /**
     * 锁定钱包
     *
     * @param uid
     * @param unit
     * @return
     */
    @Transactional
    public boolean lockWallet(Long uid, String unit) {
    	MemberLegalCurrencyWallet wallet = findByCoinUnitAndMemberId(unit, uid);
        if (wallet != null && wallet.getIsLock() == BooleanEnum.IS_FALSE) {
            wallet.setIsLock(BooleanEnum.IS_TRUE);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 解锁钱包
     *
     * @param uid
     * @param unit
     * @return
     */
    @Transactional
    public boolean unlockWallet(Long uid, String unit) {
    	MemberLegalCurrencyWallet wallet = findByCoinUnitAndMemberId(unit, uid);
        if (wallet != null && wallet.getIsLock() == BooleanEnum.IS_TRUE) {
            wallet.setIsLock(BooleanEnum.IS_FALSE);
            return true;
        } else {
            return false;
        }
    }

    public MemberLegalCurrencyWallet findOneByCoinNameAndMemberId(String coinName, long memberId) {
        BooleanExpression and = QMemberWallet.memberWallet.coin.name.eq(coinName)
                .and(QMemberWallet.memberWallet.memberId.eq(memberId));
        return memberLegalCurrencyWalletDao.findOne(and);
    }

    public Page<MemberWalletDTO> joinFind(List<Predicate> predicates,QMember qMember ,QMemberWallet qMemberWallet,PageModel pageModel) {
        List<OrderSpecifier> orderSpecifiers = pageModel.getOrderSpecifiers();
        predicates.add(qMember.id.eq(qMemberWallet.memberId));
        JPAQuery<MemberWalletDTO> query = queryFactory.select(
                        Projections.fields(MemberWalletDTO.class, qMemberWallet.id.as("id"),qMemberWallet.memberId.as("memberId") ,qMember.username,qMember.realName.as("realName"),
                        qMember.email,qMember.mobilePhone.as("mobilePhone"),qMemberWallet.balance,qMemberWallet.address,qMemberWallet.coin.unit
                        ,qMemberWallet.frozenBalance.as("frozenBalance"),qMemberWallet.balance.add(qMemberWallet.frozenBalance).as("allBalance"))).from(QMember.member,QMemberWallet.memberWallet).where(predicates.toArray(new Predicate[predicates.size()]))
                        .orderBy(orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]));
        List<MemberWalletDTO> content = query.offset((pageModel.getPageNo()-1)*pageModel.getPageSize()).limit(pageModel.getPageSize()).fetch();
        long total = query.fetchCount();
        return new PageImpl<>(content, pageModel.getPageable(), total);
    }

    public BigDecimal getAllBalance(String coinName){
        return memberLegalCurrencyWalletDao.getWalletAllBalance(coinName);
    }

    public MemberDeposit findDeposit(String address,String txid){
        return depositDao.findByAddressAndTxid(address,txid);
    }

    public void decreaseFrozen(Long walletId,BigDecimal amount){
        memberLegalCurrencyWalletDao.decreaseFrozen(walletId,amount);
    }

    public void increaseBalance(Long walletId,BigDecimal amount){
        memberLegalCurrencyWalletDao.increaseBalance(walletId,amount);
    }
    
    
    public int unfreezeLess(){
        return memberLegalCurrencyWalletDao.unfreezeLess();
    }
    
    public int unfreezeMore(){
        return memberLegalCurrencyWalletDao.unfreezeMore();
    }
    
    
//    public int initSuperPaterner(long memberId){
//        return memberLegalCurrencyWalletDao.initSuperPaterner(memberId);
//    }
    
    public int dropWeekTable(int weekDay){
        return memberLegalCurrencyWalletDao.dropWeekTable(weekDay);
    }
    
    public int createWeekTable(int weekDay){
        return memberLegalCurrencyWalletDao.createWeekTable(weekDay);
    }

    public BigDecimal getWalletBalanceAmount(String coinId,int week){
        return memberLegalCurrencyWalletDao.getWalletBalanceAmount(coinId,week);
    }

    public List<MemberLegalCurrencyWallet> geteveryBHB(String coinName,int week){
        return memberLegalCurrencyWalletDao.geteveryBHB(coinName,week);
    }


    //更新团队钱包
    public int updateTeamWallet(BigDecimal teamBalance,long teamId){
        return memberLegalCurrencyWalletDao.updateTeamWallet(teamBalance,teamId);
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
     * 根据用户ID和coinID更新用户钱包
     *
     */
    @Transactional
    public int updateByMemberIdAndCoinId(long memberId,String coinId,BigDecimal balance){
        return memberLegalCurrencyWalletDao.updateByMemberIdAndCoinId(memberId,coinId,balance);
    }

    /**
     * 增加用户BHB钱包余额
     * @param mineAmount
     * @param memberId
     * @return
     */
    public int increaseBalanceForBHB(BigDecimal mineAmount,Long memberId) {
       return  memberLegalCurrencyWalletDao.increaseBalanceForBHB(mineAmount,memberId);
    }


    /**
     * 查询待释放BHB大于500的
     */
    public List<MemberLegalCurrencyWallet> findUnfreezeGTE(){
        return  memberLegalCurrencyWalletDao.findUnfreezeGTE();
    }
    /**
     * 查询待释放BHB大于500的
     */
    public List<MemberLegalCurrencyWallet> findUnfreezeLTE(){
        return  memberLegalCurrencyWalletDao.findUnfreezeLTE();
    }


    public int updateBalanceByIdAndAmount (long id,double amount){
        return memberLegalCurrencyWalletDao.updateBalanceByIdAndAmount(id,amount);
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
    public MessageResult transferIncreaseBalance(MemberWallet memberWallet,String coinId, Long memberId, BigDecimal amount) throws Exception {

		MemberLegalCurrencyWallet memberLegalCurrencyWallet =  memberLegalCurrencyWalletDao.getMemberWalletByCoinAndMemberId(coinId, memberId);

    	if (memberWallet == null) {
            return new MessageResult(500, "wallet cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new MessageResult(500, "amount must large then 0");
        }
       
        int result = memberLegalCurrencyWalletDao.transferIncreaseBalance(memberLegalCurrencyWallet.getId(), amount,memberLegalCurrencyWallet.getBalance());
        
        if (result > 0) {
    	    result =  memberWalletDao.transferDecreaseBalance(memberWallet.getId(), memberId, amount, memberWallet.getBalance());
           
            if (result > 0) {
                 TransferSelfRecord walletTransferRecord = new TransferSelfRecord();
                 
                 Coin coin = new Coin();
         		coin.setName(coinId);
         		walletTransferRecord.setCoin(coin);
                 walletTransferRecord.setLegalcurrencyId(memberLegalCurrencyWallet.getId());
                 walletTransferRecord.setWalletId(memberWallet.getId());
                 walletTransferRecord.setMemberId(memberId);
                 walletTransferRecord.setType(0);
                 walletTransferRecord.setStatus(1);
                 walletTransferRecord.setTotalAmount(amount);
                 walletTransferRecord.setArrivedAmount(amount);
                 walletTransferRecord.setFee(BigDecimal.ZERO);
                 
                 transferSelfRecordService.save(walletTransferRecord);
                 
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
    @Transactional(rollbackFor = Exception.class)
    public MessageResult transferDecreaseBalance(MemberLegalCurrencyWallet memberLegalCurrencyWallet,String coinId,Long memberId, BigDecimal amount) throws Exception {
    	
    	MemberWallet memberWallet = memberWalletDao.getMemberWalletByCoinAndMemberId(coinId, memberId);
 
    	// 法币账户扣减
        int result = memberLegalCurrencyWalletDao.transferDecreaseBalance(memberLegalCurrencyWallet.getId(), amount,memberLegalCurrencyWallet.getBalance());
       
        if (result > 0) {
        	 
        	// 币币账户增加
        	result = memberWalletDao.transferIncreaseBalance(memberWallet.getId(), memberId, amount, memberWallet.getBalance());
        	
        	if (result > 0) {
        		TransferSelfRecord walletTransferRecord = new TransferSelfRecord();
                 
        		Coin coin = new Coin();
        		coin.setName(coinId);
        		walletTransferRecord.setCoin(coin);
                 walletTransferRecord.setLegalcurrencyId(memberLegalCurrencyWallet.getId());
                 walletTransferRecord.setWalletId(memberWallet.getId());
                 walletTransferRecord.setTotalAmount(amount);
                 walletTransferRecord.setMemberId(memberId);
                 walletTransferRecord.setType(1);
                 walletTransferRecord.setStatus(1);
                 walletTransferRecord.setTotalAmount(amount);
                 walletTransferRecord.setArrivedAmount(amount);
                 walletTransferRecord.setFee(BigDecimal.ZERO);
                 
               //增加记录
                 transferSelfRecordService.save(walletTransferRecord);
                 
                 return new MessageResult(0, "success");
        	} else {
        		throw new Exception("划转失败！");
        	}
        } else {
            return new MessageResult(500, "recharge failed");
        }
    }
  
}
