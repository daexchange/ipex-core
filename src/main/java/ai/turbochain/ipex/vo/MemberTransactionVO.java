package ai.turbochain.ipex.vo;

import ai.turbochain.ipex.entity.MemberTransaction;
import lombok.Data;

@Data
public class MemberTransactionVO extends MemberTransaction{

    private String memberUsername ;

    private String memberRealName ;

    private String phone ;

    private String email ;
    
}
