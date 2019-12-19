package ai.turbochain.ipex.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sparkframework.sql.model.Model;

import ai.turbochain.ipex.constant.BooleanEnum;
import ai.turbochain.ipex.constant.CommonStatus;
import ai.turbochain.ipex.dao.CoinDao;
import ai.turbochain.ipex.dao.MemberAddressDao;
import ai.turbochain.ipex.entity.Coin;
import ai.turbochain.ipex.entity.MemberAddress;
import ai.turbochain.ipex.pagination.Criteria;
import ai.turbochain.ipex.pagination.Restrictions;
import ai.turbochain.ipex.service.Base.BaseService;
import ai.turbochain.ipex.util.MessageResult;

/**
 * @author GS
 * @date 2018年01月26日
 */
@Service
public class MemberAddressService extends BaseService {
	@Autowired
	private MemberAddressDao memberAddressDao;
	@Autowired
	private CoinDao coinDao;

	public MessageResult addMemberAddress(Long memberId, String address, String unit, String remark) {
		Coin coin = coinDao.findByUnit(unit);
		if (coin == null || coin.getCanWithdraw().equals(BooleanEnum.IS_FALSE)) {
			return MessageResult.error(600, "The currency does not support withdrawals");
		}
		MemberAddress memberAddress = new MemberAddress();
		memberAddress.setAddress(address);
		memberAddress.setCoin(coin);
		memberAddress.setMemberId(memberId);
		memberAddress.setRemark(remark);
		MemberAddress memberAddress1 = memberAddressDao.saveAndFlush(memberAddress);
		if (memberAddress1 != null) {
			return MessageResult.success();
		} else {
			return MessageResult.error("failed");
		}
	}

	public MessageResult addOrUpdateMemberAddress(Long memberId, String address, String coinName, String remark)
			throws Exception {
		Coin coin = coinDao.findByName(coinName);
		if (coin == null || coin.getCanWithdraw().equals(BooleanEnum.IS_FALSE)) {
			return MessageResult.error(600, "The currency does not support withdrawals");
		}
		MemberAddress memberAddress = memberAddressDao.findByMemberIdAndCoinId(memberId, coin);
		if (memberAddress != null) {
			memberAddressDao.updateMemberAddressOfwithDraw(address, remark, memberAddress.getId(), memberId, coin);
			return MessageResult.success();
		} else {
			memberAddress = new MemberAddress();
			memberAddress.setAddress(address);
			memberAddress.setCoin(coin);
			memberAddress.setMemberId(memberId);
			memberAddress.setRemark(remark);
			MemberAddress memberAddress1 = memberAddressDao.saveAndFlush(memberAddress);
			if (memberAddress1 != null) {
				MessageResult messageResult = MessageResult.success();
				messageResult.setData(memberAddress1);
				return messageResult;
			} else {
				return MessageResult.error("failed");
			}
		}
	}

	public MessageResult findMemberAddressByCoinName(Long memberId, String coinName) throws Exception {
		Coin coin = coinDao.findByName(coinName);
		if (coin == null || coin.getCanWithdraw().equals(BooleanEnum.IS_FALSE)) {
			return MessageResult.error(600, "The currency does not support withdrawals");
		}
		MemberAddress memberAddress = memberAddressDao.findByMemberIdAndCoinId(memberId, coin);
		if (memberAddress != null) {
			MessageResult messageResult = MessageResult.success();
			messageResult.setData(memberAddress);
			return messageResult;
		} else {
			return MessageResult.error("用户：" + memberId + "未保存" + coinName + "提币地址");
		}
	}

	public MessageResult deleteMemberAddress(Long memberId, Long addressId) {
		int is = memberAddressDao.deleteMemberAddress(new Date(), addressId, memberId);
		if (is > 0) {
			return MessageResult.success();
		} else {
			return MessageResult.error("failed");
		}
	}

	public Page<MemberAddress> pageQuery(int pageNo, Integer pageSize, long id, String unit) {
		Sort orders = Criteria.sortStatic("id.desc");
		PageRequest pageRequest = new PageRequest(pageNo, pageSize, orders);
		Criteria<MemberAddress> specification = new Criteria<>();
		specification.add(Restrictions.eq("memberId", id, false));
		specification.add(Restrictions.eq("status", CommonStatus.NORMAL, false));
		specification.add(Restrictions.eq("coin.unit", unit, false));
		return memberAddressDao.findAll(specification, pageRequest);
	}

	public List<Map<String, String>> queryAddress(long userId, String coinId) {
		try {
			return new Model("member_address").field(" remark,address")
					.where("member_id=? and coin_id=? and status=?", userId, coinId, CommonStatus.NORMAL.ordinal())
					.select();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	public List<MemberAddress> findByMemberIdAndAddress(long userId, String address) {
		return memberAddressDao.findAllByMemberIdAndAddressAndStatus(userId, address, CommonStatus.NORMAL);
	}
}
