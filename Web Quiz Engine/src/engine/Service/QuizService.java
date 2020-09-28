package engine.Service;

import engine.DAO.QuizRepository;
import engine.Model.Quiz;
import engine.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {
    private QuizRepository quizRepository;

    public QuizService() {}
    @Autowired
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Quiz getQuiz(Integer quizId) {
        return quizRepository.getOne(quizId);
    }

    public Quiz saveQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public Boolean existsById(Integer id) {
        return quizRepository.existsById(id);
    }

    public List<Quiz> findAll() {
        return quizRepository.findAll();
    }
    public void deleteAll(){
        quizRepository.deleteAll();
    }

    public void deleteById(Integer id) {
        quizRepository.deleteById(id);
    }

    public Page<Quiz> findAllWithPages(Integer page, Integer size, String sortBy) {
        Pageable pageReq = PageRequest.of(page, size, Sort.by(sortBy));

        Page<Quiz> pagedResult = quizRepository.findAll(pageReq);
        return pagedResult;
    }

}
