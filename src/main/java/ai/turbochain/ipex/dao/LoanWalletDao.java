package ai.turbochain.ipex.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.LoanWallet;

/**
 * @author 
 * @description 
 * @date
 */
public interface LoanWalletDao extends BaseDao<LoanWallet> {
	
	@Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select o from LoanWallet o where o.unit= :unit and o.memberId=:memberId ")
	LoanWallet getLockLoanWalletByCoinUnitAndMemberId(@Param("unit") String unit, @Param("memberId") long memberId);

	 /**
     * 增加钱包余额
     *
     * @param walletId
     * @param amount
     * @return
     */
    @Modifying
    @Query("update LoanWallet wallet set wallet.balance = wallet.balance + :amount where wallet.id = :walletId and wallet.balance = :balance and wallet.version = :version ")
    int transferIncreaseBalance(@Param("walletId") long walletId, @Param("amount") BigDecimal amount, @Param("balance") BigDecimal balance,@Param("version") Integer version);

    /**
     * 减少钱包余额
     *
     * @param walletId
     * @param amount
     * @return
     */
    @Modifying
    @Query("update LoanWallet wallet set wallet.balance = wallet.balance - :amount where wallet.id = :walletId and wallet.balance >= :amount and wallet.balance = :balance and wallet.version = :version ")
    int transferDecreaseBalance(@Param("walletId") long walletId, @Param("amount") BigDecimal amount, @Param("balance") BigDecimal balance,@Param("version") Integer version);

	List<LoanWallet> getByMemberId(@Param("memberId") long memberId);

}
