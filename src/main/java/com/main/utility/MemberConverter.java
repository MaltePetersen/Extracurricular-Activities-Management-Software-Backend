package com.main.utility;

import org.springframework.stereotype.Component;

import com.main.dto.MemberDTO;
import com.main.model.Member;

@Component
public class MemberConverter extends DTOConverter {

	public MemberConverter() {
		super(Member.class, MemberDTO.class);
	}

	@Override
	public Object convert(Object object) {
		Object convertedObject = super.convert(object);
		if (convertedObject.getClass().equals(getDtoClass())) {
			Member member = (Member) object;
			((MemberDTO) convertedObject).setAdmin(member.isAdmin());
		}
		
		return convertedObject;
	}
}
