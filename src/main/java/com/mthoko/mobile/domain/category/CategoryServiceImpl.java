package com.mthoko.mobile.domain.category;

import com.mthoko.mobile.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl extends BaseServiceImpl<Category> implements CategoryService {

	public static final String ROAD_SIGNS_MARKINGS = "ROAD SIGNS & MARKINGS";
	public static final String RULES_OF_THE_ROAD = "RULES OF THE ROAD";
	public static final String HEAVY_MOTOR_VEHICLE_CONTROLS = "HEAVY MOTOR VEHICLE CONTROLS";
	public static final String LIGHT_MOTOR_VEHICLE_CONTROLS = "LIGHT MOTOR VEHICLE CONTROLS";

	@Autowired
	private CategoryRepo categoryRepo;

	@Override
	public JpaRepository<Category, Long> getRepo() {
		return categoryRepo;
	}

	@Override
	public List<Category> getCategories() {
		List<Category> categories = new ArrayList<>();
		categories.add(new Category(ROAD_SIGNS_MARKINGS, 1, 0));
		categories.add(new Category(RULES_OF_THE_ROAD, 2, 0));
		categories.add(new Category(LIGHT_MOTOR_VEHICLE_CONTROLS, 3, 0));
		categories.add(new Category(HEAVY_MOTOR_VEHICLE_CONTROLS, 4, 0));
		return categories;
	}

	@Override
	public List<String> retrieveAllNames() {
		return categoryRepo.retrieveAllNames();
	}

	@Override
	public List<Category> populateCategoryTable() {
		List<Category> categories = getCategories();
		List<Category> savedCategories = retrieveAll();
		categories.removeAll(savedCategories);
		if (!categories.isEmpty()) {
			saveAll(categories);
		}
		return categories;
	}

	@Override
	public Optional<Category> findByName(String name) {
		return categoryRepo.findByName(name);
	}

}
