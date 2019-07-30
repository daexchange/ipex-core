package ai.turbochain.ipex.dao;

import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.MemberLevelFee;

public interface MemberLevelFeeDao extends BaseDao<MemberLevelFee> {
	
	MemberLevelFee findOneBySymbolAndMemberLevelId(String symbol, Long memberLevelId);

}
