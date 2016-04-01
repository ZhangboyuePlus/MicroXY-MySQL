package com.xFire.service.mealService;

import java.util.List;

import com.xFire.bean.meal.MealBean;
import com.xFire.bean.meal.RestaurantBean;

public interface MealService 
{
	public List<RestaurantBean> getRestaurantByLocation(String isInXRY);
	public List<MealBean> getMealByRestaurantId(String id);
}
