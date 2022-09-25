package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(

                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),

                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(
            List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDayLimitArgument) {

        int caloriesPer1DayConsumed = 0;
        int counterAllMeals = 1;

        LocalDate lddCur = LocalDate.from(meals.get(0).getDateTime()).minusDays(1);
        List<UserMealWithExcess> listMealExcesInsideFilter = new ArrayList<>();
        int counterNotes1Day = 0;
        int counterListDayPos = 0;

        for (UserMeal usrM : meals) {
            if (!lddCur.equals(LocalDate.from(usrM.getDateTime()))) {
                if (caloriesPer1DayConsumed <= caloriesPerDayLimitArgument) {
                    counterListDayPos -= counterNotes1Day;
                }
                caloriesPer1DayConsumed = 0;
                counterNotes1Day = 0;
                lddCur = LocalDate.from(usrM.getDateTime());
            }
            caloriesPer1DayConsumed += usrM.getCalories();

            if (TimeUtil.isBetweenHalfOpen(LocalTime.from(usrM.getDateTime()), startTime, endTime)) {
                if (listMealExcesInsideFilter.size() < counterListDayPos + 1) {
                    listMealExcesInsideFilter.add(new UserMealWithExcess(usrM.getDateTime(), usrM.getDescription(), usrM.getCalories(), true));
                } else {
                    listMealExcesInsideFilter.set(counterListDayPos, new UserMealWithExcess(usrM.getDateTime(), usrM.getDescription(), usrM.getCalories(), true));
                    counterListDayPos++;
                    counterNotes1Day++;
                }
            }

            if (meals.size() == counterAllMeals) {
                if (caloriesPer1DayConsumed <= caloriesPerDayLimitArgument) {
                    if (counterListDayPos > counterListDayPos - counterNotes1Day) {
                        listMealExcesInsideFilter.subList(counterListDayPos - counterNotes1Day, counterListDayPos).clear();
                    }
                }
            }
            counterAllMeals++;
        }
        return listMealExcesInsideFilter;
    }
}
