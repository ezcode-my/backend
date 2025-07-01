package org.ezcode.codetest.infrastructure.notification.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationPageResponse<T> implements Serializable {

	private List<T> content;

	private int page;

	private int size;

	private long totalElements;

}
