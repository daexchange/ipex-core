package ai.turbochain.ipex.dto;

import lombok.Data;

import java.util.List;

import ai.turbochain.ipex.entity.Member;
import ai.turbochain.ipex.entity.MemberWallet;

@Data
public class MemberDTO {

    private Member member ;

    private List<MemberWallet> list ;

}
