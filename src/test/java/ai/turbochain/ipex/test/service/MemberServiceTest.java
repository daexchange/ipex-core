package ai.turbochain.ipex.test.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ai.turbochain.ipex.test.BaseTest;

import ai.turbochain.ipex.entity.Member;
import ai.turbochain.ipex.service.MemberService;


public class MemberServiceTest extends BaseTest {

	@Autowired
	private MemberService memberService;
	
	@Test
	public void test() {
        Member member=memberService.findOne(25L);
        System.out.println(">>>>>>>>>>>>>>"+member);
        
	}

}
