package org.example.bank_rest.mapper;


import org.example.bank_rest.dto.CardCreateDto;
import org.example.bank_rest.dto.CardPatchDto;
import org.example.bank_rest.dto.CardAdminViewDto;
import org.example.bank_rest.dto.CardUserViewDto;
import org.example.bank_rest.persistence.model.entity.Card;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper
public interface CardMapper {

    Card toEntity(CardCreateDto dto);

    CardAdminViewDto toAdminDto(Card dto);

    CardUserViewDto toUserDto(Card dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchEntity(CardPatchDto dto, @MappingTarget Card entity);
}
