package ai.turbochain.ipex.dao;

import java.util.List;

import ai.turbochain.ipex.constant.CommonStatus;
import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.Coin;
import ai.turbochain.ipex.entity.TransferAddress;

/**
 * @author GS
 * @date 2018年02月27日
 */
public interface TransferAddressDao extends BaseDao<TransferAddress> {
    List<TransferAddress> findAllByStatusAndCoin(CommonStatus status, Coin coin);

    TransferAddress findByAddressAndCoin(String address, Coin coin);
}
