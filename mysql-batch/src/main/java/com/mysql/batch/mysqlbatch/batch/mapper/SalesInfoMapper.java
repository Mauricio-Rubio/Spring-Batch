package com.mysql.batch.mysqlbatch.batch.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.mysql.batch.mysqlbatch.Domain.SalesInfo;
import com.mysql.batch.mysqlbatch.batch.dto.SalesInfoDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SalesInfoMapper {

    SalesInfo mapToEntity(SalesInfoDTO salesInfoDTO);
}