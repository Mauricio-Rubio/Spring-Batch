package com.mysql.batch.mysqlbatch.batch.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.mysql.batch.mysqlbatch.Domain.SalesInfo;
import com.mysql.batch.mysqlbatch.batch.dto.SalesInfoDTO;
import com.mysql.batch.mysqlbatch.batch.mapper.SalesInfoMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class SalesInfoItemProcessor implements ItemProcessor<SalesInfoDTO, SalesInfo> {
    private final SalesInfoMapper salesInfoMapper;

    @Override
    public SalesInfo process(SalesInfoDTO item) throws Exception {
        log.info("processing the item: {}", item.toString());
        return salesInfoMapper.mapToEntity(item);
    }
}