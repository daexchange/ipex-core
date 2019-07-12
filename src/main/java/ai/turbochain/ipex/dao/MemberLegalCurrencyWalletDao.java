package ai.turbochain.ipex.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.Coin;
import ai.turbochain.ipex.entity.MemberLegalCurrencyWallet;

public interface MemberLegalCurrencyWalletDao extends BaseDao<MemberLegalCurrencyWallet> {

    /**
     * 增加钱包余额
     *
     * @param walletId
     * @param amount
     * @return
     */
    @Modifying
    @Query("update MemberLegalCurrencyWallet wallet set wallet.balance = wallet.balance + :amount where wallet.id = :walletId")
    int increaseBalance(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);

    /**
     * 减少钱包余额
     *
     * @param walletId
     * @param amount
     * @return
     */
    @Modifying
    @Query("update MemberLegalCurrencyWallet wallet set wallet.balance = wallet.balance - :amount where wallet.id = :walletId and wallet.balance >= :amount")
    int decreaseBalance(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);

    /**
     * 冻结钱包余额
     *
     * @param walletId
     * @param amount
     * @return
     */
    @Modifying
    @Query("update MemberLegalCurrencyWallet wallet set wallet.balance = wallet.balance - :amount,wallet.frozenBalance=wallet.frozenBalance + :amount where wallet.id = :walletId and wallet.balance >= :amount")
    int freezeBalance(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);

    /**
     * 解冻钱包余额
     *
     * @param walletId
     * @param amount
     * @return
     */
    @Modifying
    @Query("update MemberLegalCurrencyWallet wallet set wallet.balance = wallet.balance + :amount,wallet.frozenBalance=wallet.frozenBalance - :amount where wallet.id = :walletId and wallet.frozenBalance >= :amount")
    int thawBalance(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);

    /**
     * 减少冻结余额
     *
     * @param walletId
     * @param amount
     * @return
     */
    @Modifying
    @Query("update MemberLegalCurrencyWallet wallet set wallet.frozenBalance=wallet.frozenBalance - :amount where wallet.id = :walletId and wallet.frozenBalance >= :amount")
    int decreaseFrozen(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);


   // MemberLegalCurrencyWallet findByCoinAndAddress(Coin coin, String address);

    MemberLegalCurrencyWallet findByCoinAndMemberId(Coin coin, Long memberId);

    List<MemberLegalCurrencyWallet> findAllByMemberId(Long memberId);

    List<MemberLegalCurrencyWallet> findAllByCoin(Coin coin);

    @Query(value="select sum(a.balance)+sum(a.frozen_balance) as allBalance from member_legal_currency_wallet a where a.coin_id = :coinName",nativeQuery = true)
    BigDecimal getWalletAllBalance(@Param("coinName")String coinName);

    
    //查询快照表BHB总数
    @Query(value="select sum(a.balance) as allBalance from member_legal_currency_wallet_:weekDay a where a.coin_id = :coinName AND balance >=10000 AND member_id NOT IN (66946,65859,13029,55)",nativeQuery = true)
    BigDecimal getWalletBalanceAmount(@Param("coinName")String coinName,@Param("weekDay")int weekDay);
    
    
    //定时任务，筛选小于500的直接累加
    @Transactional
    @Modifying
    @Query(value = "UPDATE member_legal_currency_wallet SET balance=balance+to_released,to_released=0 WHERE to_released<=500 AND to_released<>0",nativeQuery = true)
    int unfreezeLess();

    /**
     * 查询待释放BHB小于500的
     */
    @Query(value = "select * from member_legal_currency_wallet WHERE to_released<=500 AND to_released>0",nativeQuery = true)
    List<MemberLegalCurrencyWallet> findUnfreezeLTE();

    /**
     * 查询待释放BHB大于500的
     */
    @Query(value = "select * from member_legal_currency_wallet WHERE to_released>500",nativeQuery = true)
    List<MemberLegalCurrencyWallet> findUnfreezeGTE();
    
    //定时任务，筛选大于500的逐步释放
    @Transactional
    @Modifying
    @Query(value = "UPDATE member_legal_currency_wallet SET balance=balance+500,to_released=to_released-500 WHERE to_released>500",nativeQuery = true)
    int unfreezeMore();


    //删除快照表
    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE member_legal_currency_wallet_:weekDay",nativeQuery = true)
    int dropWeekTable(@Param("weekDay")int weekDay);


    //新增快照表
    @Transactional
    @Modifying
    @Query(value = "insert INTO member_legal_currency_wallet_:weekDay SELECT * FROM member_legal_currency_wallet",nativeQuery = true)
    int createWeekTable(@Param("weekDay")int weekDay);


    //根据快照表查询每个人拥有的BHB
    @Query(value="select * from member_legal_currency_wallet_:weekDay a where a.coin_id = :coinName  AND balance>=10000 AND member_id NOT IN (66946,65859,13029,55)",nativeQuery = true)
    List<MemberLegalCurrencyWallet> geteveryBHB(@Param("coinName")String coinName,@Param("weekDay")int weekDay);

    @Query(value = "select * from member_legal_currency_wallet where  coin_id=:coinId and member_id=:memberId ",nativeQuery =true)
    MemberLegalCurrencyWallet getMemberWalletByCoinAndMemberId(@Param("coinId") String coinId, @Param("memberId") long memberId);


    @Transactional
    @Modifying
    @Query(value="UPDATE member_legal_currency_wallet SET balance=balance+:teamBalance where coin_id = 'BHB' AND member_id=:teamId",nativeQuery = true)
    int updateTeamWallet(@Param("teamBalance")BigDecimal teamBalance,@Param("teamId")long teamId);

    @Transactional
    @Modifying
    @Query(value="UPDATE member_legal_currency_wallet SET balance=balance-:normalBalance,frozen_balance=frozen_balance+:normalBalance where coin_id=:coinId AND member_id=:memberId",nativeQuery = true)
    int updateMemberWalletByMemberIdAndCoinId(@Param("normalBalance")BigDecimal normalBalance,@Param("coinId")String coinId,@Param("memberId")long memberId);

    @Transactional
    @Modifying
    @Query(value="UPDATE member_legal_currency_wallet SET balance=balance+:allBalance,frozen_balance=frozen_balance-:forzenBalance where coin_id=:coinId AND member_id=:memberId",nativeQuery = true)
    int updateMemberWalletByMemberIdAndCoinId(@Param("allBalance")BigDecimal allBalance,@Param("forzenBalance")BigDecimal forzenBalance,@Param("coinId")String coinId,@Param("memberId")long memberId);

    
    /**
     * 根据用户Id和币种ID更新用户钱包
     * @param memberId
     * @param coinId
     * @param balance
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "UPDATE member_legal_currency_wallet SET balance=balance+:balance WHERE coin_id=:coinId AND member_id=:memberId",nativeQuery = true)
    int updateByMemberIdAndCoinId(@Param("memberId")long memberId,@Param("coinId")String coinId,@Param("balance")BigDecimal balance);

    /**
     * 增加用户BHB余额
     * @param memberId
     * @return
     */

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "UPDATE member_legal_currency_wallet SET balance=balance+:balance WHERE coin_id='BHB' AND member_id=:memberId",nativeQuery = true)
    int increaseBalanceForBHB(@Param("balance")BigDecimal mineAmount,@Param("memberId") Long memberId);

//    //初始化超级合伙人BHB的数量
//    @Modifying
//    @Transactional
//    @Query(value="update member_legal_currency_wallet  set balance = '20' where member_id = :memberId and coin_id = 'BHB' ",nativeQuery = true)
//    int initSuperPaterner(@Param("memberId") long memberId);

    /**
     * 币竞猜扣件用户余额
     * @param id
     * @param amount
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "UPDATE member_legal_currency_wallet SET balance=balance-:amount WHERE id=:id and balance>=:amount",nativeQuery = true)
    int updateBalanceByIdAndAmount (@Param("id") long id,@Param("amount") double amount);

    /**
     * 增加钱包余额
     *
     * @param walletId
     * @param amount
     * @return
     */
    @Modifying
    @Query("update MemberLegalCurrencyWallet wallet set wallet.balance = wallet.balance + :amount where wallet.id = :walletId and wallet.balance = :balance")
    int transferIncreaseBalance(@Param("walletId") long walletId, @Param("amount") BigDecimal amount, @Param("balance") BigDecimal balance);

    /**
     * 减少钱包余额
     *
     * @param walletId
     * @param amount
     * @return
     */
    @Modifying
    @Query("update MemberLegalCurrencyWallet wallet set wallet.balance = wallet.balance - :amount where wallet.id = :walletId and wallet.balance >= :amount and wallet.balance = :balance ")
    int transferDecreaseBalance(@Param("walletId") long walletId, @Param("amount") BigDecimal amount, @Param("balance") BigDecimal balance);

}
