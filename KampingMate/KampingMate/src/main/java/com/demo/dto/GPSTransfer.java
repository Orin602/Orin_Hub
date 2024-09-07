package com.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GPSTransfer {
	private double lat;		// gps로 반환받은 위도
	private double lon;		// gps로 반환받은 경도
	
	private double xLat;	// x좌표로 변환된 위도
	private double yLon;	// y좌표로 변환된 경도
	
}
