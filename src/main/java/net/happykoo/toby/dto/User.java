package net.happykoo.toby.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.happykoo.toby.constant.Level;

@Getter
@Setter
@Builder
public class User {
    private String id;
    private String nickName;
    private Level level;
    private int loginCount;
    private int recommendCount;
}
