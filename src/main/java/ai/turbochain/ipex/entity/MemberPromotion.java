package ai.turbochain.ipex.entity;

import lombok.Data;

import javax.persistence.*;

import ai.turbochain.ipex.constant.PromotionLevel;

/**
 * @author GS
 * @date 2018年03月08日
 */
@Entity
@Data
public class MemberPromotion {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    //邀请者Id
    private Long inviterId;
    //受邀者Id
    private Long inviteesId;

    @Enumerated(EnumType.ORDINAL)
    private PromotionLevel level;
}
