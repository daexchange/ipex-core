package ai.turbochain.ipex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.turbochain.ipex.dao.MemberExclusiveFeeDao;
import ai.turbochain.ipex.entity.MemberExclusiveFee;
import ai.turbochain.ipex.service.Base.BaseService;

@Service
public class MemberExclusiveFeeService extends BaseService<MemberExclusiveFee> {
	  @Autowired
	  private MemberExclusiveFeeDao memberExclusiveFeeDao;
	  
	  public MemberExclusiveFee findOneBySymbolAndMemberId(String symbol, Long memberId) {
		  return memberExclusiveFeeDao.findOneBySymbolAndMemberId(symbol,memberId);
	  }

}
