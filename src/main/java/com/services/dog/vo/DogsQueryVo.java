package com.services.dog.vo;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DogsQueryVo {
    private String q;
    public String getQ() {
        return Strings.isNullOrEmpty(q) ? "" : q;
    }
}
