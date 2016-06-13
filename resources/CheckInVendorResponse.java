/**
 * 
 */
package com.eatingcinci.app.response;

import java.util.List;

import com.eatingcinci.app.wrapper.AchievementWrapper;
import com.eatingcinci.app.wrapper.VendorWrapper;

/**
 * @author Edward
 *
 */
public class CheckInVendorResponse {

	public VendorWrapper vendor;
	public List<AchievementWrapper> achievements;

	public CheckInVendorResponse() {

	}

	public CheckInVendorResponse(VendorWrapper vendor, List<AchievementWrapper> achievements) {
		this.vendor = vendor;
		this.achievements = achievements;
	}

}
