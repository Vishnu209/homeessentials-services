package com.home.essentials.payload;

import lombok.Data;

@Data
public class SuccessResponse {
		private String success;

		public SuccessResponse(String success) {
			super();
			this.success = success;
		}
		
	}
