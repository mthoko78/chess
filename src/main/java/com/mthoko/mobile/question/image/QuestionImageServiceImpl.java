package com.mthoko.mobile.question.image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mthoko.mobile.category.Category;
import com.mthoko.mobile.category.CategoryServiceImpl;
import com.mthoko.mobile.common.BaseServiceImpl;
import com.mthoko.mobile.question.Question;

@Service
public class QuestionImageServiceImpl extends BaseServiceImpl<QuestionImage> implements QuestionImageService {

	private final QuestionImageRepo imageRepo;

	private QuestionImageRepoImpl imageRepoImpl = new QuestionImageRepoImpl();

	@Autowired
	public QuestionImageServiceImpl(QuestionImageRepo imageRepo, QuestionImageRepoImpl imageRepoImpl) {
		this.imageRepo = imageRepo;
		this.imageRepoImpl = imageRepoImpl;
	}

	@Override
	public JpaRepository<QuestionImage, Long> getRepo() {
		return imageRepo;
	}

	@Override
	public List<QuestionImage> findByQuestionNumberAndCategory(int questionNum, String category) {
		return imageRepo.findByQuestionNumberAndCategory(questionNum, category);
	}

	@Override
	public Map<Integer, QuestionImage> saveQuestionImages(Category category, List<Question> questions) {
		if (imageRepo.count() > 0) {
			return new HashMap<>();
		}
		questions = questions.stream().filter((question) -> category.equals(question.getCategory()))
				.collect(Collectors.toList());
		Map<Integer, QuestionImage> images = extractQuestionImages(category, questions);
		imageRepo.saveAll(new ArrayList<>(images.values()));
		return images;
	}

	public void allocateQuestionImagesToQuestions(Category category, List<Question> questions,
			Map<Integer, QuestionImage> images) {
		questions = questions.stream().filter(question -> category.equals(question.getCategory()))
				.collect(Collectors.toList());
		questions.forEach(question -> {
			if (images.containsKey(question.getNumber())) {
				question.setImage(images.get(question.getNumber()));
			}
		});
	}

	private Map<Integer, QuestionImage> extractQuestionImages(Category category, List<Question> questions) {
		String categoryName = category.getName();
		questions = filterByCategoryName(questions, categoryName);
		Map<Integer, QuestionImage> images = new HashMap<>();
		switch (categoryName) {
		case CategoryServiceImpl.ROAD_SIGNS_MARKINGS:
			Map<Integer, QuestionImage> choicesMap = imageRepoImpl.getQuestionSignImages();
			for (Entry<Integer, QuestionImage> entry : choicesMap.entrySet()) {
				Integer questionNum = entry.getKey();
				QuestionImage image = entry.getValue();
				images.put(questionNum, image);
			}
			break;
		case CategoryServiceImpl.RULES_OF_THE_ROAD:

			break;
		case CategoryServiceImpl.HEAVY_MOTOR_VEHICLE_CONTROLS:
			QuestionImage heavyImage = imageRepoImpl.heavyControlsImage();
			questions.forEach(question -> {
				if (question.getNumber() >= 6) {
					question.setImage(heavyImage);
					images.put(question.getNumber(), heavyImage);
				}
			});
			break;
		case CategoryServiceImpl.LIGHT_MOTOR_VEHICLE_CONTROLS:
			QuestionImage lightImage = imageRepoImpl.lightControlsImage();
			questions.forEach(question -> {
				question.setImage(lightImage);
				images.put(question.getNumber(), lightImage);
			});
			break;

		default:
			break;
		}
		return images;
	}

	private Optional<Question> filterOneByQuestionNum(List<Question> questions, Integer questionNum) {
		Optional<Question> optionalQuestion = questions.stream()
				.filter((question) -> question.getNumber() == questionNum).findFirst();
		return optionalQuestion;
	}

	private List<Question> filterByCategoryName(List<Question> questions, String categoryName) {
		return questions.stream().filter((question) -> categoryName.equals(question.getCategory().getName()))
				.collect(Collectors.toList());
	}

	@Override
	public List<QuestionImage> findByCategoryId(Long id) {
		return imageRepo.findByCategoryId(id);
	}

	@Override
	public void allocateImagesToQuestions(Category category, List<Question> questions,
			Map<Integer, QuestionImage> images) {
		List<Question> filtered = questions.stream().filter(q -> category.equals(q.getCategory()))
				.collect(Collectors.toList());
		images.entrySet().forEach(entry -> {
			Optional<Question> match = filtered.stream().filter(q -> q.getNumber() == entry.getKey()).findFirst();
			if (match.isPresent()) {
				match.get().setImage(entry.getValue());
				print("" + match.get());
			}
		});
	}
}
