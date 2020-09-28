package engine;

import engine.DTO.UserDTO;
import engine.Model.*;
import engine.Service.CompletedQuizService;
import engine.Service.QuizService;
import engine.Service.UserService;
import engine.security.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class Controller {
    private final static String UNVALID_INPUT = "Unvalid input";

    private QuizService quizService;
    private UserService userService;
    private CompletedQuizService completedQuizService;
    private ArrayList<Quiz> quizzes = new ArrayList<>();


    public Controller(){}
    @Autowired
    public Controller(QuizService quizService, UserService userService, CompletedQuizService completedQuizService) {
        this.quizService = quizService;
        this.userService = userService;
        this.completedQuizService = completedQuizService;
        //quizService.deleteAll();
        //userService.deleteAll();
        //completedQuizService.deleteAll();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/register")
    public User register(@Valid @RequestBody UserDTO userDTO) {
        return userService.registerNewUser(userDTO);
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = "application/json",
            path="/quizzes")
    public Quiz createQuiz(@Valid @RequestBody Quiz newQuiz, Authentication authentication) {
        User user = userService.finUserByEmail(authentication.getName());
        newQuiz.setOwner(user);
        return quizService.saveQuiz(newQuiz);
    }

    @RequestMapping(path = "/quizzes/{id}")
    public Quiz getQuizById(@PathVariable("id") Integer id) {
        if(quizService.existsById(id)) {
            return quizService.getQuiz(id);
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
    }

    @RequestMapping(path = "/quizzes", method = RequestMethod.GET)
    public Page<Quiz> getAllQuizzes(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size,
                                    @RequestParam Optional<String> sort) {
        Page<Quiz> pagedResult = quizService.findAllWithPages(page.orElse(0),
                size.orElse(10), sort.orElse("id"));
        if(pagedResult.hasContent()) {
            return pagedResult;
        }
        else {
            return new PageImpl<Quiz>(new ArrayList<>());

        }
    }


    @RequestMapping(method = RequestMethod.POST,
            path="/quizzes/{id}/solve")
    public QuizResult getAnswer(@PathVariable("id") Integer id, @RequestBody AnswerToQuiz answerToQuiz, Authentication authentication) {

        if(!quizService.existsById(id))
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found");
        }
        Quiz q = quizService.getQuiz(id);

        QuizResult result = new QuizResult();


        if(q.getAnswer().size() != answerToQuiz.getAnswer().size()) {
            result.setSuccess(false);
            result.setFeedback("Wrong answer! Please, try again.");
            return result;
        }
        else if(answerToQuiz.getAnswer().containsAll(q.getAnswer()))
        {
            //Create a new completion record and save it to the database.
            User user = userService.finUserByEmail(authentication.getName());
            CompletedQuizRecord completed = new CompletedQuizRecord();
            completed.setId(q.getId());
            completed.setUserMail(user.getEmail());
            completed.setCompletedAt(LocalDateTime.now());
            completedQuizService.save(completed);

            //return a feedback
            result.setSuccess(true);
            result.setFeedback("Congratulations, you're right!");
            return result;
        }
        else {
            result.setSuccess(false);
            result.setFeedback("Wrong answer! Please, try again.");
            return result;
        }


    }


    @RequestMapping(method = RequestMethod.DELETE, path = "/quizzes/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteQuiz(@PathVariable Integer id, Authentication authentication) {
        String email = authentication.getName();
        Quiz quiz = quizService.getQuiz(id);
        if(quiz.getOwner().getEmail().equals(email))
        {
            quizService.deleteById(id);
            return;
        }
        throw new org.springframework.security.access.AccessDeniedException("403 returned");
    }

    @RequestMapping(method = RequestMethod.GET, path = "/quizzes/completed")
    public Page<CompletedQuizRecord> getAllCompletedQuizRecords(@RequestParam Optional<Integer> page,
                                                                @RequestParam Optional<Integer> size,
                                                                @RequestParam Optional<String> sortBy) {
        String currentUserMail = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<CompletedQuizRecord> pagedResult = completedQuizService.findAllByUserMailPaged(page.orElse(0),
                size.orElse(10),
                sortBy.orElse("id"), currentUserMail );
        if(pagedResult.hasContent()) {
            return pagedResult;
        }
        else {
            return new PageImpl<>(new ArrayList<>());
        }
    }

    @Deprecated
    @RequestMapping(method = RequestMethod.GET, path = "/quiz")
    public Quiz getQuiz() {
        Quiz quiz = new Quiz();
        quiz.setTitle("The Java Logo");
        quiz.setText("What is depicted on the Java logo?");

        ArrayList<String> options = new ArrayList<>();
        options.add("Robot");
        options.add("Tea Leaf");
        options.add("Cup of coffee");
        options.add("Bug");

        quiz.setOptions(options);

        return  quiz;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public HashMap<String, String> handleUnvalidInput(Exception e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", UNVALID_INPUT);
        response.put("error", e.getClass().getSimpleName());
        return response;
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public HashMap<String, String> handleUserAlreadyExists(Exception e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "User already exists");
        response.put("error", e.getClass().getSimpleName());
        return response;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public HashMap<String, String> handleUserNotFoundException(Exception e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "User not found.");
        response.put("error", e.getClass().getSimpleName());
        return response;
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public HashMap<String, String> handleDeletingNonExistingQuiz(Exception e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "There is no quiz with that id to delete");
        response.put("error", e.getClass().getSimpleName());
        return response;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public HashMap<String, String> handleEntityNotFoundException(Exception e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "Entity not found");
        response.put("error", e.getClass().getSimpleName());
        return response;
    }
}
