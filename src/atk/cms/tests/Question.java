package atk.cms.tests;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;

/**
 * Represents a SINGLE Question with: 
 * (1) Question number
 * (2) Question statement
 * (3) Question choices, with 4 possible answers
 * (4) Correct choice index
 */
@ManagedBean(name="question")
public class Question implements Serializable {

	private String answer;
	private boolean answered = false;
	private String question;
	private int questionNumber = 0;
	private String[] questionChoices;
	private int correctChoiceIndex;
	private static final long serialVersionUID = 1L;

	public void setAnswer(String answer) {
		this.answer = answer;
		answered = true;
	}

	public String getAnswer() { 
		return answer;   
	}

	public void setAnswered(boolean answered) { 
		this.answered = answered; 
	}

	public boolean isAnswered() { 
		return answered; 
	}

	public void setQuestion (String question) { 
		this.question = question; 
	}

	public String getQuestion() { 
		return question; 
	}
	
	// Set Question i when user clicks next or previous
	public void setQuestionNumber(int i) {
		questionNumber = i;
	}

	// Get Question i from XML file
	public int getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionChoices(String[] s) {
		questionChoices = s;
	}

	public String[] getQuestionChoices() { 
		return questionChoices;  
	}

	// Set Question i's correct answer from XML file
	public void setCorrectChoiceIndex(int i) {
		correctChoiceIndex = i;
	}
	
	// Get Question i's correct answer from XML file
	public int getCorrectChoiceIndex() {
		return correctChoiceIndex;
	}
}