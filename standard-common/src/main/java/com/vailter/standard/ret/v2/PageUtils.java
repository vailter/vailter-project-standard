package com.vailter.standard.ret.v2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageUtils implements Serializable {
    private static final long serialVersionUID = 1L;
    private int total;
    private List<?> rows;
}
