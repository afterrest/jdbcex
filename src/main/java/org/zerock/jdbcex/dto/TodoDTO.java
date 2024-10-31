package org.zerock.jdbcex.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Builder
@Data //getter, setter, toSTring, equal, hashCode 등 전부 포함
@NoArgsConstructor
@AllArgsConstructor
public class TodoDTO {

    private Long tno;

    private String title;

    private LocalDate dueDate;

    private boolean finished;
}
