package com.lizi.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserEasyVo {
    private Long id;
    private String userName;
    @JsonFormat(timezone = "GMT8",pattern = "yyyy-MM-dd")
    private Date createTime;
    private String sex;
    private String nickName;
    private String avatar;
}
