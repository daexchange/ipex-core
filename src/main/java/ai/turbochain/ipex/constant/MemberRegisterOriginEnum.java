package ai.turbochain.ipex.constant;

import com.fasterxml.jackson.annotation.JsonValue;

import ai.turbochain.ipex.core.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author
 * @description 会员注册来源
 * @date 2019/12/11 14:50
 */
@AllArgsConstructor
@Getter
public enum MemberRegisterOriginEnum implements BaseEnum{
    DELIVER(1),
    HARDID(2);

    @Setter
    private Integer sourceType;

    @Override
    @JsonValue
    public int getOrdinal(){
        return this.ordinal();
    }
}
