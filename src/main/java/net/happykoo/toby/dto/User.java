package net.happykoo.toby.dto;

import lombok.*;
import net.happykoo.toby.constant.Level;

import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String nickName;
    private Level level;
    private int loginCount;
    private int recommendCount;

    public void upgradeLevel() {
        Level nextLevel = this.level.getNextLevel();
        if (Objects.isNull(nextLevel)) {
            throw new IllegalStateException(this + "can not upgrade");
        }
        this.level = nextLevel;
    }
}
