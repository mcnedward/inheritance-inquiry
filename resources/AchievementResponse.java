package com.eatingcinci.app.response;

import java.util.ArrayList;
import java.util.List;

import com.eatingcinci.app.wrapper.AchievementWrapper;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 1, 2015
 *
 */
public abstract class AchievementResponse extends BaseResponse {

	protected List<AchievementWrapper> achievements;
	protected String achievementMessage = "";
	protected boolean hasAchievements = false;

	public AchievementResponse() {
		super();
		achievements = new ArrayList<AchievementWrapper>();
	}

	protected void setAchievements(List<AchievementWrapper> achievements) {
		this.achievements = achievements;
		for (AchievementWrapper achievement : achievements) {
			String message = achievement.achievementMessage;
			if (message != null) {
				// Break for every message after the first
				if (!achievementMessage.equals(""))
					achievementMessage += "<br>";
				achievementMessage += message;
			}
		}
	}

	public boolean hasAchievements() {
		return !achievements.isEmpty() && AchievementWrapper.hasFirst(achievements);
	}

}
