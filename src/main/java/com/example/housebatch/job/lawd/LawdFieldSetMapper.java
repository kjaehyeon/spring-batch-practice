package com.example.housebatch.job.lawd;

import com.example.housebatch.core.entity.Lawd;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class LawdFieldSetMapper implements FieldSetMapper<Lawd> {

    public static final String LAWD_CD = "lawdCd";
    public static final String LAWD_DONG = "lawdDong";
    public static final String EXIST = "exist";

    private static final String EXIST_TRUE = "존재";

    @Override
    public Lawd mapFieldSet(FieldSet fieldSet) throws BindException {
        return Lawd.builder()
                .lawdCd(fieldSet.readString(LAWD_CD))
                .lawdDong(fieldSet.readString(LAWD_DONG))
                .exist(fieldSet.readBoolean(EXIST, EXIST_TRUE))
                .build();
    }
}
