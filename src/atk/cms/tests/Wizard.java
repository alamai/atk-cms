package atk.cms.tests;

import java.util.ArrayList;
import java.io.IOException;
import java.io.Serializable;
import org.xml.sax.SAXException;
import java.net.URISyntaxException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Controller for the tests wizard
 * Methods herein control enabled state of tests wizard's buttons
 */
@ManagedBean(name="wizard")
@SessionScoped
public class Wizard implements Serializable {
	
	private String button_id;
	
	public String getButton_id() {
		return button_id;
	}

	private int questionNumber;
	private Question question = new Question();
	private Questions questions = new Questions();
	private ArrayList<Question> questionsList = new ArrayList<Question>(10);
	private static final long serialVersionUID = 1L;
	
	public int getQuestionNumber() { 
		return questionNumber = question.getQuestionNumber();
	}
	
	public ArrayList<Question> getQuestionsList() {
		return questionsList = questions.getQuestionsList();
	}
	
	public void nextButtonClicked(ActionEvent eventNext) throws SAXException, 
		ParserConfigurationException, IOException, URISyntaxException {
		
		button_id = eventNext.getComponent().getId();
		chooseAnswers();
		chooseQuestions();
	}

	public void previousButtonClicked(ActionEvent eventPrevious) throws SAXException, 
		ParserConfigurationException, IOException, URISyntaxException {
		
		button_id = eventPrevious.getComponent().getId();
		chooseAnswers();
		chooseQuestions();
	}

	public boolean getNextButtonEnabled() {
		return questionNumber != (questionsList.size() - 1) && questionsList.get(questionNumber).isAnswered();
	}

	public boolean getPreviousButtonEnabled() {
		return questionNumber > 0 && questionNumber < 10;
	}

	public boolean getFinishButtonEnabled() {
		return questionNumber == (questionsList.size() - 1) && questionsList.get(questionNumber).isAnswered();
	}

	/**
	 * @throws IOException
	 * @throws SAXException 
	 * @throws URISyntaxException 
	 * @throws ParserConfigurationException
	 */
	public String startTest() throws SAXException, 
		ParserConfigurationException, IOException, URISyntaxException {
		
		questions.resetCurrentQuestion();
		questions.setQuestion(questions.currentQuestion);
		question = questionsList.get(questions.currentQuestion);
		return "/Tests/TestPage.xhtml";
	}
	
	public void chooseAnswers() {
		
		int selectedRadio = -1;
		ArrayList<String> answerRadio = question.getAnswers();
		
		if ("1".equals(answerRadio)) {
			selectedRadio = 1;
			questions.getSelections().put(questions.currentQuestion, selectedRadio);
			System.out.println("You selected " + selectedRadio);
		}
		
		else if ("2".equals(answerRadio)) {
			selectedRadio = 2;
			questions.getSelections().put(questions.currentQuestion, selectedRadio);
			System.out.println("You selected " + selectedRadio);
		}
		
		else if ("3".equals(answerRadio)) {
			selectedRadio = 3;
			questions.getSelections().put(questions.currentQuestion, selectedRadio);
			System.out.println("You selected " + selectedRadio);
		}
		
		else if ("4".equals(answerRadio)) {
			selectedRadio = 4;
			questions.getSelections().put(questions.currentQuestion, selectedRadio);
			System.out.println("You selected " + selectedRadio);
		}
	}
	
	public void chooseQuestions() throws SAXException, 
		ParserConfigurationException, IOException, URISyntaxException {
		
		if (button_id.equals("next")) {
			questions.currentQuestion++;
			questions.setQuestion(questions.currentQuestion);
			question = questionsList.get(questions.currentQuestion);	
		}
		
		else if (button_id.equals("previous")) {   
			questions.currentQuestion--;
			questions.setQuestion(questions.currentQuestion);
			question = questionsList.get(questions.currentQuestion);	
		}
		
		else if (button_id.equals("finish")) {   
			@SuppressWarnings("unused")
			int testScore = questions.calculateResult(questions);
		}
	}

	public String endTest() {
		return "/Tests/CompletedPage.xhtml";
	}
}