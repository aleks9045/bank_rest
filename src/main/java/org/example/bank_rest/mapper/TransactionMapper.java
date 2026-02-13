package org.example.bank_rest.mapper;


import org.example.bank_rest.dto.TransactionCreateDto;
import org.example.bank_rest.dto.TransactionViewDto;
import org.example.bank_rest.persistence.model.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(uses = {CardIdMapper.class})
public interface TransactionMapper {

    @Mapping(target = "fromCard", source = "fromCardId")
    @Mapping(target = "toCard", source = "toCardId")
    Transaction toEntity(TransactionCreateDto dto);

    @Mapping(target = "fromCardId", source = "fromCard")
    @Mapping(target = "toCardId", source = "toCard")
    TransactionViewDto toDto(Transaction dto);
}
