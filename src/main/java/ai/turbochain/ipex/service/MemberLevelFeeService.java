package ai.turbochain.ipex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.turbochain.ipex.dao.MemberLevelFeeDao;
import ai.turbochain.ipex.entity.MemberLevelFee;
import ai.turbochain.ipex.service.Base.BaseService;

@Service
public class MemberLevelFeeService extends BaseService<MemberLevelFee> {
	  @Autowired
	  private MemberLevelFeeDao memberLevelFeeDao;
	  
	  public MemberLevelFee findOneBySymbolAndMemberLevelId(String symbol, Long memberLevelId) {
		  return memberLevelFeeDao.findOneBySymbolAndMemberLevelId(symbol,memberLevelId);
	  }

}
