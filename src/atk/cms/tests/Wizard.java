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

	private int questionNumber;
	private Question question = new Question();	
	private Questions questions = new Questions();
	public ArrayList<Question> questionsList = new ArrayList<Question>(10);
	private static final long serialVersionUID = 1L;
	
	/**
	 * Get Questions List from Questions bean
	 * @return questionsList
	 */
	public ArrayList<Question> getQuestionsList() { 
		return questionsList = questions.getQuestionsList();
	}
	
	public int getQuestionNumber() { 
		return questionNumber = question.getQuestionNumber();
	}
	
	public void nextButtonClicked(ActionEvent eventNext) throws SAXException, 
		ParserConfigurationException, IOException, URISyntaxException {
		
		if (question.getQuestionNumber() < questionsList.size()) {
			questions.setQuestion(questionNumber);
			//question.setQuestionNumber(++questionNumber);
		}
		
		else {
			getNextButtonEnabled();
			getFinishButtonEnabled();
		}
	}

	public void previousButtonClicked(ActionEvent eventPrevious) throws SAXException, 
		ParserConfigurationException, IOException, URISyntaxException {
		
		if (question.getQuestionNumber() > 1) {
			questions.setQuestion(question.getQuestionNumber());
			//question.setQuestionNumber(--questionNumber);
		}
		
		else {
			getNextButtonEnabled();
		}
	}

	public boolean getNextButtonEnabled() {
		return (question.getQuestionNumber() != questions.getQuestionsList().size() - 1
				&& questions.getQuestionsList().get(question.getQuestionNumber()).isAnswered());
	}

	public boolean getPreviousButtonEnabled() {
		return question.getQuestionNumber() > 0;
	}

	public boolean getFinishButtonEnabled() {
		return (question.getQuestionNumber() == questions.getQuestionsList().size() - 1 
				&& questions.getQuestionsList().get(question.getQuestionNumber()).isAnswered());
	}
	
	/**
	 * @throws IOException
	 * @throws SAXException 
	 * @throws URISyntaxException 
	 * @throws ParserConfigurationException
	 */
	public String start() throws SAXException, ParserConfigurationException, IOException, URISyntaxException {
		questions.setQuestion(questions.getCurrentQuestion());
		return "/Tests/StartPage.xhtml";
	}

	public String end() {
		return "/Tests/CompletePage.xhtml";
	}
}