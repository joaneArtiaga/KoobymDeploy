package com.koobym.service;

import com.koobym.model.BookOwner;

public interface BookOwnerService extends BaseService<BookOwner, Long> {

	public BookOwner setBookOwner(long bookOwnerId, long userId);
}
