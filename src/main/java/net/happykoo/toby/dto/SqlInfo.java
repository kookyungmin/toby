package net.happykoo.toby.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SqlInfo {
    private String id;
    private String sql;
}
