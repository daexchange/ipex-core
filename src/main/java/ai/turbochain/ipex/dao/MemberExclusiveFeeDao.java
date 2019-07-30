package ai.turbochain.ipex.dao;

import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.MemberExclusiveFee;

public interface MemberExclusiveFeeDao extends BaseDao<MemberExclusiveFee> {
	
	MemberExclusiveFee findOneBySymbolAndMemberId(String symbol, Long memberId);

}
